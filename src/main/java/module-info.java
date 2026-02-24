module co.edu.uniquindio.lab4.world_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.lab4.world_app to javafx.fxml;
    exports co.edu.uniquindio.lab4.world_app;
}