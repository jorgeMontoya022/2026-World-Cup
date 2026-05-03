module co.edu.uniquindio.cup.world_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires itextpdf;

    opens co.edu.uniquindio.cup.world_app to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app;

    opens co.edu.uniquindio.cup.world_app.controller to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app.controller;

    opens co.edu.uniquindio.cup.world_app.controller.form to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app.controller.form;

    opens co.edu.uniquindio.cup.world_app.model to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app.model;

    opens co.edu.uniquindio.cup.world_app.service to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app.service;

    opens co.edu.uniquindio.cup.world_app.repository to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app.repository;

    opens co.edu.uniquindio.cup.world_app.util to javafx.fxml;
    exports co.edu.uniquindio.cup.world_app.util;
}
