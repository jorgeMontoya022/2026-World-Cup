package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.HelloApplication;
import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.model.Usuario;
import co.edu.uniquindio.cup.world_app.service.AuthService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador de la ventana principal.
 * Gestiona el sidebar de navegación y carga las vistas en el área de contenido.
 */
public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private Label lblUserName;
    @FXML private Label lblUserRole;
    @FXML private Label lblUserAvatar;
    @FXML private StackPane contentArea;

    // Botones de navegación
    @FXML private Button btnDashboard;
    @FXML private Button btnEquipos;
    @FXML private Button btnJugadores;
    @FXML private Button btnTecnicos;
    @FXML private Button btnEstadios;
    @FXML private Button btnGrupos;
    @FXML private Button btnPartidos;
    @FXML private Button btnConsultas;
    @FXML private Button btnReportes;
    @FXML private Button btnUsuarios;
    @FXML private Button btnBitacora;
    private Button botonActivo;
    private final AuthService authService = new AuthService();

    @FXML
    void initialize() {
        Usuario usuario = SessionManager.getInstancia().getUsuarioActual();
        if (usuario != null) {
            lblUserName.setText(usuario.getNombreCompleto());
            lblUserRole.setText(usuario.getTipo().getEtiqueta());
            // Avatar: primera letra del nombre
            lblUserAvatar.setText(String.valueOf(usuario.getNombreCompleto().charAt(0)).toUpperCase());
        }

        // Ocultar opciones según el rol
        configurarPermisos();

        // Cargar vista inicial
        navegarA("dashboard-view.fxml", btnDashboard);
    }

    private void configurarPermisos() {
        boolean esAdmin = SessionManager.getInstancia().isAdmin();
        boolean puedeEscribir = SessionManager.getInstancia().puedeEscribir();

        // Solo admin ve usuarios y bitácora
        if (btnUsuarios != null) btnUsuarios.setVisible(esAdmin);
        if (btnBitacora != null) btnBitacora.setVisible(esAdmin);

        // Esporádico no puede crear/editar (se controla en cada vista)
    }

    // ── Navegación ─────────────────────────────────────────────────────────────

    @FXML void irDashboard(ActionEvent e)  { navegarA("dashboard-view.fxml", btnDashboard); }
    @FXML void irEquipos(ActionEvent e)    { navegarA("equipos-view.fxml",   btnEquipos);   }
    @FXML void irJugadores(ActionEvent e)  { navegarA("jugadores-view.fxml", btnJugadores); }
    @FXML void irTecnicos(ActionEvent e)   { navegarA("tecnicos-view.fxml",  btnTecnicos);  }
    @FXML void irEstadios(ActionEvent e)   { navegarA("estadios-view.fxml",  btnEstadios);  }
    @FXML void irGrupos(ActionEvent e)     { navegarA("grupos-view.fxml",    btnGrupos);    }
    @FXML void irPartidos(ActionEvent e)   { navegarA("partidos-view.fxml",  btnPartidos);  }
    @FXML void irConsultas(ActionEvent e)  { navegarA("consultas-view.fxml", btnConsultas); }
    @FXML void irReportes(ActionEvent e)   { navegarA("reportes-view.fxml",  btnReportes);  }
    @FXML void irUsuarios(ActionEvent e)   { navegarA("usuarios-view.fxml",  btnUsuarios);  }
    @FXML void irBitacora(ActionEvent e)   { navegarA("bitacora-view.fxml",  btnBitacora);  }

    @FXML
    void cerrarSesion(ActionEvent event) {
        if (!AlertaUtil.confirmar("Cerrar sesión", "¿Desea cerrar la sesión actual?")) return;
        try {
            authService.logout();
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(
                            "/co/edu/uniquindio/cup/world_app/view/login-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("Mundial 2026 — Login");
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setWidth(480);
            stage.setHeight(640);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception ex) {
            AlertaUtil.error("Error", "No se pudo cerrar la sesión: " + ex.getMessage());
        }
    }

    // ── Helper de navegación ──────────────────────────────────────────────────

    private void navegarA(String fxmlFile, Button boton) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(
                            "/co/edu/uniquindio/cup/world_app/view/" + fxmlFile));
            Node vista = loader.load();
            contentArea.getChildren().setAll(vista);

            // Actualizar botón activo
            if (botonActivo != null) botonActivo.getStyleClass().remove("active");
            if (boton != null) {
                boton.getStyleClass().add("active");
                botonActivo = boton;
            }
        } catch (IOException ex) {
            AlertaUtil.error("Error de navegación", "No se pudo cargar la vista: " + ex.getMessage());
        }
    }
}
