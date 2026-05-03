package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.RegistroBitacora;
import co.edu.uniquindio.cup.world_app.service.BitacoraService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador de la bitácora del sistema.
 * Solo accesible por el Administrador.
 */
public class BitacoraController {

    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private TableColumn<RegistroBitacora, String> colUsuario;
    @FXML private TableColumn<RegistroBitacora, String> colTipo;
    @FXML private TableColumn<RegistroBitacora, String> colEntrada;
    @FXML private TableColumn<RegistroBitacora, String> colSalida;
    @FXML private TableColumn<RegistroBitacora, String> colDuracion;
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private Label lblPaginacion;
    @FXML private Label lblTotal;
    @FXML private TableView<RegistroBitacora> tablaBitacora;

    private final BitacoraService service = new BitacoraService();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final ObservableList<RegistroBitacora> todosLosRegistros = FXCollections.observableArrayList();
    private final ObservableList<RegistroBitacora> registrosPagina   = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 10;
    private int paginaActual = 0;

    @FXML
    void initialize() {
        if (!SessionManager.getInstancia().isAdmin()) {
            AlertaUtil.advertencia("Acceso denegado", "Solo el Administrador puede ver la bitácora.");
            return;
        }
        configurarColumnas();
        cargarDatos();
    }

    private void configurarColumnas() {
        colUsuario.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsuarioUsername()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsuarioTipo().getEtiqueta()));
        colEntrada.setCellValueFactory(c -> {
            if (c.getValue().getFechaEntrada() == null) return new SimpleStringProperty("-");
            return new SimpleStringProperty(c.getValue().getFechaEntrada().format(FMT));
        });
        colSalida.setCellValueFactory(c -> {
            if (c.getValue().getFechaSalida() == null) return new SimpleStringProperty("En sesión");
            return new SimpleStringProperty(c.getValue().getFechaSalida().format(FMT));
        });
        colDuracion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDuracion()));
        tablaBitacora.setItems(registrosPagina);
    }

    private void cargarDatos() {
        try {
            List<RegistroBitacora> lista;
            if (dpDesde.getValue() != null && dpHasta.getValue() != null) {
                lista = service.listarPorRango(dpDesde.getValue(), dpHasta.getValue());
            } else {
                lista = service.listarTodos();
            }
            todosLosRegistros.setAll(lista);
            paginaActual = 0;
            actualizarPagina();
            lblTotal.setText("Total registros: " + lista.size());
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudo cargar la bitácora: " + e.getMessage());
        }
    }

    private void actualizarPagina() {
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todosLosRegistros.size());
        registrosPagina.setAll(todosLosRegistros.subList(inicio, fin));
        lblPaginacion.setText(String.format("Mostrando %d-%d de %d registros",
                todosLosRegistros.isEmpty() ? 0 : inicio + 1, fin, todosLosRegistros.size()));
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(fin >= todosLosRegistros.size());
    }

    @FXML void filtrarPorFecha(ActionEvent event) { cargarDatos(); }

    @FXML
    void limpiarFiltros(ActionEvent event) {
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        cargarDatos();
    }

    @FXML
    void paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) { paginaActual--; actualizarPagina(); }
    }

    @FXML
    void paginaSiguiente(ActionEvent event) {
        if ((paginaActual + 1) * ITEMS_POR_PAGINA < todosLosRegistros.size()) {
            paginaActual++;
            actualizarPagina();
        }
    }
}
