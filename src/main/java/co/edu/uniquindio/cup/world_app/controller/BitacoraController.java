package co.edu.uniquindio.cup.world_app.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BitacoraController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnSiguiente;

    @FXML
    private TableColumn<?, ?> colDuracion;

    @FXML
    private TableColumn<?, ?> colEntrada;

    @FXML
    private TableColumn<?, ?> colSalida;

    @FXML
    private TableColumn<?, ?> colTipo;

    @FXML
    private TableColumn<?, ?> colUsuario;

    @FXML
    private DatePicker dpDesde;

    @FXML
    private DatePicker dpHasta;

    @FXML
    private Label lblPaginacion;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<?> tablaBitacora;

    @FXML
    void filtrarPorFecha(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

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
