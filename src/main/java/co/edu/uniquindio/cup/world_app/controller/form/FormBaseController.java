package co.edu.uniquindio.cup.world_app.controller.form;

import co.edu.uniquindio.cup.world_app.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Clase base para todos los formularios modales del sistema.
 * Provee apertura de ventana estilizada y manejo de errores.
 */
public abstract class FormBaseController {

    @FXML protected Label lblError;

    protected Stage stage;
    protected boolean guardado = false;

    /** Abre el formulario en una ventana modal estilizada. */
    public static <T extends FormBaseController> T abrir(String fxmlPath,
                                                          String titulo,
                                                          Stage owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.setResizable(false);

        T controller = loader.getController();
        controller.stage = stage;
        return controller;
    }

    /** Muestra la ventana y espera a que se cierre. */
    public void mostrarYEsperar() {
        stage.showAndWait();
    }

    public boolean isGuardado() { return guardado; }

    // ── Acciones comunes ──────────────────────────────────────────────────────

    @FXML
    protected void cancelar() {
        stage.close();
    }

    protected void mostrarError(String mensaje) {
        lblError.setText("⚠  " + mensaje);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    protected void ocultarError() {
        lblError.setVisible(false);
        lblError.setManaged(false);
    }
}
