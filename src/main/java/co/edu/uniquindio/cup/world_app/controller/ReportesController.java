package co.edu.uniquindio.cup.world_app.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ReportesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> cmbR2Equipo;

    @FXML
    private ComboBox<?> cmbR3Confederacion;

    @FXML
    private ComboBox<?> cmbR4Pais;

    @FXML
    private DatePicker dpR1Desde;

    @FXML
    private DatePicker dpR1Hasta;

    @FXML
    private Label lblMensaje;

    @FXML
    private TextField txfEstaturaMax;

    @FXML
    private TextField txfEstaturaMin;

    @FXML
    private TextField txfPesoMax;

    @FXML
    private TextField txfPesoMin;

    @FXML
    private TextField txfR1HoraDesde;

    @FXML
    private TextField txfR1HoraHasta;

    @FXML
    void generarReporteJugadores(ActionEvent event) {

    }

    @FXML
    void generarReportePartidos(ActionEvent event) {

    }

    @FXML
    void generarReporteUsuarios(ActionEvent event) {

    }

    @FXML
    void generarReporteValor(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

}
