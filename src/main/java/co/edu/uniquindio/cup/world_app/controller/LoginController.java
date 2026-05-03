package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.HelloApplication;
import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de login.
 * Gestiona la autenticación y la selección del tipo de usuario.
 */
public class LoginController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button btnAdmin;
    @FXML private Button btnEsporadico;
    @FXML private Button btnTradicional;
    @FXML private Label lblError;
    @FXML private PasswordField txfPassword;
    @FXML private TextField txfUsuario;

    private TipoUsuario tipoSeleccionado = TipoUsuario.ADMINISTRADOR;
    private final AuthService authService = new AuthService();

    @FXML
    void initialize() {
        lblError.setVisible(false);
        lblError.setManaged(false);
        marcarBotonActivo(btnAdmin);
    }

    @FXML
    void selectAdmin(ActionEvent event) {
        tipoSeleccionado = TipoUsuario.ADMINISTRADOR;
        marcarBotonActivo(btnAdmin);
    }

    @FXML
    void selectTradicional(ActionEvent event) {
        tipoSeleccionado = TipoUsuario.TRADICIONAL;
        marcarBotonActivo(btnTradicional);
    }

    @FXML
    void selectEsporadico(ActionEvent event) {
        tipoSeleccionado = TipoUsuario.ESPORADICO;
        marcarBotonActivo(btnEsporadico);
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String usuario = txfUsuario.getText();
        String password = txfPassword.getText();

        try {
            authService.login(usuario, password, tipoSeleccionado);
            abrirVentanaPrincipal();
        } catch (AuthService.AuthException e) {
            mostrarError(e.getMessage());
        } catch (java.sql.SQLException e) {
            // Mensaje claro según el tipo de error SQL
            String msg = e.getMessage();
            if (msg != null && msg.contains("Communications link failure")) {
                mostrarError("No se puede conectar a MySQL.\nVerifica que el servidor esté corriendo en el puerto 3306.");
            } else if (msg != null && msg.contains("Access denied")) {
                mostrarError("Acceso denegado a MySQL.\nRevisa el usuario y contraseña en ConexionDB.java.");
            } else if (msg != null && msg.contains("Unknown database")) {
                mostrarError("La base de datos 'mundial2026' no existe.\nEjecuta el script SQL primero.");
            } else {
                mostrarError("Error de base de datos: " + msg);
            }
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void marcarBotonActivo(Button activo) {
        btnAdmin.getStyleClass().remove("active");
        btnTradicional.getStyleClass().remove("active");
        btnEsporadico.getStyleClass().remove("active");
        activo.getStyleClass().add("active");
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void abrirVentanaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(
                        "/co/edu/uniquindio/cup/world_app/view/main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 800);
        Stage stage = (Stage) txfUsuario.getScene().getWindow();
        stage.setTitle("Mundial 2026 — Sistema de Gestión");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
