package co.edu.uniquindio.cup.world_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación Mundial 2026.
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(
                        "/co/edu/uniquindio/cup/world_app/view/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Mundial 2026 — Sistema de Gestión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
