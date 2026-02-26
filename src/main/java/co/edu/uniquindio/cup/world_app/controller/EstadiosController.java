package co.edu.uniquindio.cup.world_app.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class EstadiosController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnSiguiente;

    @FXML
    private ComboBox<?> cmbPais;

    @FXML
    private TableColumn<?, ?> colAcciones;

    @FXML
    private TableColumn<?, ?> colCapacidad;

    @FXML
    private TableColumn<?, ?> colCiudad;

    @FXML
    private TableColumn<?, ?> colNombre;

    @FXML
    private TableColumn<?, ?> colPais;

    @FXML
    private TableColumn<?, ?> colPartidos;

    @FXML
    private Label lblPaginacion;

    @FXML
    private TableView<?> tablaEstadios;

    @FXML
    private TextField txfBuscar;

    @FXML
    void abrirFormularioNuevo(ActionEvent event) {

    }

    @FXML
    void filtrarTabla(ActionEvent event) {

    }

    @FXML
    void paginaAnterior(ActionEvent event) {

    }

    @FXML
    void paginaSiguiente(ActionEvent event) {

    }

    @FXML
    void initialize() {


    }

}
