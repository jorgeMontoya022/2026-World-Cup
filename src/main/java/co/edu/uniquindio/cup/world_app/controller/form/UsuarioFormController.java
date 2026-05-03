package co.edu.uniquindio.cup.world_app.controller.form;

import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.model.Usuario;
import co.edu.uniquindio.cup.world_app.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.sql.SQLException;

/**
 * Controlador del formulario de Usuario (crear / editar).
 */
public class UsuarioFormController extends FormBaseController {

    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private TextField txfUsername;
    @FXML private PasswordField txfPassword;
    @FXML private TextField txfNombre;
    @FXML private ComboBox<TipoUsuario> cmbTipo;
    @FXML private CheckBox chkActivo;

    private final UsuarioService service = new UsuarioService();
    private Usuario usuarioEditar;

    @FXML
    void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoUsuario.values()));
        cmbTipo.setConverter(new StringConverter<>() {
            @Override public String toString(TipoUsuario t) {
                return t == null ? "" : t.getEtiqueta();
            }
            @Override public TipoUsuario fromString(String s) { return null; }
        });
        cmbTipo.setValue(TipoUsuario.TRADICIONAL);
    }

    public void cargarUsuario(Usuario usuario) {
        this.usuarioEditar = usuario;
        lblTitulo.setText("EDITAR USUARIO");
        lblSubtitulo.setText("Deja la contraseña vacía para no cambiarla");
        txfUsername.setText(usuario.getUsername());
        txfNombre.setText(usuario.getNombreCompleto());
        cmbTipo.setValue(usuario.getTipo());
        chkActivo.setSelected(usuario.isActivo());
        // No se puede cambiar el tipo del admin
        if (usuario.getTipo() == TipoUsuario.ADMINISTRADOR) {
            cmbTipo.setDisable(true);
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        ocultarError();
        try {
            if (txfUsername.getText().isBlank()) {
                mostrarError("El nombre de usuario es obligatorio.");
                return;
            }
            if (txfNombre.getText().isBlank()) {
                mostrarError("El nombre completo es obligatorio.");
                return;
            }
            if (cmbTipo.getValue() == null) {
                mostrarError("Selecciona el tipo de usuario.");
                return;
            }

            if (usuarioEditar == null) {
                // Crear nuevo
                if (txfPassword.getText().isBlank()) {
                    mostrarError("La contraseña es obligatoria para nuevos usuarios.");
                    return;
                }
                service.crear(txfUsername.getText().trim(),
                              txfPassword.getText(),
                              txfNombre.getText().trim(),
                              cmbTipo.getValue());
            } else {
                // Actualizar existente
                service.actualizar(usuarioEditar.getId(),
                                   txfUsername.getText().trim(),
                                   txfPassword.getText(),
                                   txfNombre.getText().trim(),
                                   cmbTipo.getValue(),
                                   chkActivo.isSelected());
            }

            guardado = true;
            stage.close();

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error de base de datos: " + ex.getMessage());
        }
    }
}
