module co.edu.uniquindio.cup.world_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.cup.world_app to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app;

    opens co.edu.uniquindio.cup.world_app.controller;
    exports co.edu.uniquindio.cup.world_app.controller;
}