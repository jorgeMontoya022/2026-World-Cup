package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Ciudad;
import co.edu.uniquindio.cup.world_app.model.Estadio;
import co.edu.uniquindio.cup.world_app.repository.CiudadRepository;
import co.edu.uniquindio.cup.world_app.service.EstadioService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador CRUD para la gestión de estadios.
 */
public class EstadiosController {

    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnNuevo;
    @FXML private ComboBox<String> cmbPais;
    @FXML private TableColumn<Estadio, String> colNombre;
    @FXML private TableColumn<Estadio, String> colCiudad;
    @FXML private TableColumn<Estadio, String> colPais;
    @FXML private TableColumn<Estadio, String> colCapacidad;
    @FXML private TableColumn<Estadio, String> colPartidos;
    @FXML private TableColumn<Estadio, String> colAcciones;
    @FXML private Label lblPaginacion;
    @FXML private TableView<Estadio> tablaEstadios;
    @FXML private TextField txfBuscar;

    private final EstadioService service = new EstadioService();
    private final CiudadRepository ciudadRepo = new CiudadRepository();

    private final ObservableList<Estadio> todosLosEstadios = FXCollections.observableArrayList();
    private final ObservableList<Estadio> estadiosPagina   = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 10;
    private int paginaActual = 0;

    @FXML
    void initialize() {
        configurarColumnas();
        cargarFiltros();
        cargarDatos();
        configurarPermisos();
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colCiudad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCiudadNombre()));
        colPais.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPais()));
        colCapacidad.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%,d", c.getValue().getCapacidad())));
        colPartidos.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getPartidosAsignados())));

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEditar.getStyleClass().add("btn-ghost-sm");
                btnEliminar.getStyleClass().add("btn-danger-sm");
                btnEditar.setOnAction(e -> editarEstadio(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarEstadio(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                if (SessionManager.getInstancia().puedeEscribir()) {
                    javafx.scene.layout.HBox box = new javafx.scene.layout.HBox(4, btnEditar, btnEliminar);
                    setGraphic(box);
                } else {
                    setGraphic(null);
                }
            }
        });

        tablaEstadios.setItems(estadiosPagina);
    }

    private void cargarFiltros() {
        cmbPais.getItems().addAll("Todos", "México", "USA", "Canadá");
        cmbPais.setValue("Todos");
    }

    private void configurarPermisos() {
        if (btnNuevo != null) btnNuevo.setVisible(SessionManager.getInstancia().puedeEscribir());
    }

    private void cargarDatos() {
        try {
            String texto = txfBuscar != null ? txfBuscar.getText() : "";
            String pais = cmbPais != null && !"Todos".equals(cmbPais.getValue()) ? cmbPais.getValue() : null;
            List<Estadio> lista = service.buscar(texto, pais);
            todosLosEstadios.setAll(lista);
            paginaActual = 0;
            actualizarPagina();
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los estadios: " + e.getMessage());
        }
    }

    private void actualizarPagina() {
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todosLosEstadios.size());
        estadiosPagina.setAll(todosLosEstadios.subList(inicio, fin));
        lblPaginacion.setText(String.format("Mostrando %d-%d de %d estadios",
                todosLosEstadios.isEmpty() ? 0 : inicio + 1, fin, todosLosEstadios.size()));
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(fin >= todosLosEstadios.size());
    }

    @FXML void filtrarTabla(ActionEvent event) { cargarDatos(); }

    @FXML
    void paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) { paginaActual--; actualizarPagina(); }
    }

    @FXML
    void paginaSiguiente(ActionEvent event) {
        if ((paginaActual + 1) * ITEMS_POR_PAGINA < todosLosEstadios.size()) {
            paginaActual++;
            actualizarPagina();
        }
    }

    @FXML
    void abrirFormularioNuevo(ActionEvent event) {
        mostrarFormulario(null);
    }

    private void editarEstadio(Estadio estadio) {
        mostrarFormulario(estadio);
    }

    private void eliminarEstadio(Estadio estadio) {
        if (!AlertaUtil.confirmar("Eliminar estadio",
                "¿Desea eliminar el estadio " + estadio.getNombre() + "?")) return;
        try {
            service.eliminar(estadio.getId());
            cargarDatos();
            AlertaUtil.info("Éxito", "Estadio eliminado correctamente.");
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudo eliminar el estadio: " + e.getMessage());
        }
    }

    private void mostrarFormulario(Estadio estadioEditar) {
        try {
            Stage owner = (Stage) tablaEstadios.getScene().getWindow();
            co.edu.uniquindio.cup.world_app.controller.form.EstadioFormController ctrl =
                    co.edu.uniquindio.cup.world_app.controller.form.FormBaseController.abrir(
                            "/co/edu/uniquindio/cup/world_app/view/form/estadio-form.fxml",
                            estadioEditar == null ? "Nuevo Estadio" : "Editar Estadio",
                            owner);
            if (estadioEditar != null) ctrl.cargarEstadio(estadioEditar);
            ctrl.mostrarYEsperar();
            if (ctrl.isGuardado()) {
                cargarDatos();
                AlertaUtil.info("Éxito", "Estadio guardado correctamente.");
            }
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }
}
