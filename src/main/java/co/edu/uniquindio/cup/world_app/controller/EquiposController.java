package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Confederacion;
import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Grupo;
import co.edu.uniquindio.cup.world_app.repository.ConfederacionRepository;
import co.edu.uniquindio.cup.world_app.repository.GrupoRepository;
import co.edu.uniquindio.cup.world_app.service.EquipoService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador CRUD para la gestión de equipos.
 */
public class EquiposController {

    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnNuevo;
    @FXML private ComboBox<Confederacion> cmbConfederacion;
    @FXML private ComboBox<String> cmbPaisSede;
    @FXML private TableColumn<Equipo, String> colBandera;
    @FXML private TableColumn<Equipo, String> colPais;
    @FXML private TableColumn<Equipo, String> colConf;
    @FXML private TableColumn<Equipo, String> colDt;
    @FXML private TableColumn<Equipo, String> colValor;
    @FXML private TableColumn<Equipo, String> colGrupo;
    @FXML private TableColumn<Equipo, String> colAcciones;
    @FXML private Label lblPaginacion;
    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TextField txfBuscar;

    private final EquipoService service = new EquipoService();
    private final ConfederacionRepository confRepo = new ConfederacionRepository();
    private final GrupoRepository grupoRepo = new GrupoRepository();

    private ObservableList<Equipo> todosLosEquipos = FXCollections.observableArrayList();
    private ObservableList<Equipo> equiposPagina   = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 10;
    private int paginaActual = 0;

    @FXML
    void initialize() {
        configurarColumnas();
        cargarFiltros();
        cargarDatos();
        configurarPermisos();
    }

    // ── Configuración ─────────────────────────────────────────────────────────

    private void configurarColumnas() {
        colBandera.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBandera()));
        colPais.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPais()));
        colConf.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getConfederacionNombre()));
        colValor.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.1f M€", c.getValue().getValorPlantilla())));
        colGrupo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGrupoNombre()));

        // Columna DT: nombre del técnico obtenido del JOIN
        colDt.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTecnicoNombre() != null
                        ? c.getValue().getTecnicoNombre()
                        : "Sin asignar"));

        // Columna acciones con botones Editar / Eliminar
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEditar.getStyleClass().add("btn-ghost-sm");
                btnEliminar.getStyleClass().add("btn-danger-sm");
                btnEditar.setOnAction(e -> editarEquipo(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarEquipo(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                boolean puedeEscribir = SessionManager.getInstancia().puedeEscribir();
                if (puedeEscribir) {
                    javafx.scene.layout.HBox box = new javafx.scene.layout.HBox(4, btnEditar, btnEliminar);
                    setGraphic(box);
                } else {
                    setGraphic(null);
                }
            }
        });

        tablaEquipos.setItems(equiposPagina);
    }

    private void cargarFiltros() {
        try {
            List<Confederacion> confs = confRepo.listarTodas();
            cmbConfederacion.getItems().add(null); // opción "Todas"
            cmbConfederacion.getItems().addAll(confs);
            cmbConfederacion.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Confederacion c) { return c == null ? "Todas" : c.getSigla(); }
                @Override public Confederacion fromString(String s) { return null; }
            });

            cmbPaisSede.getItems().addAll("Todos", "México", "USA", "Canadá");
            cmbPaisSede.setValue("Todos");
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los filtros: " + e.getMessage());
        }
    }

    private void configurarPermisos() {
        boolean puedeEscribir = SessionManager.getInstancia().puedeEscribir();
        if (btnNuevo != null) btnNuevo.setVisible(puedeEscribir);
    }

    // ── Carga de datos ────────────────────────────────────────────────────────

    private void cargarDatos() {
        try {
            String texto = txfBuscar != null ? txfBuscar.getText() : "";
            Confederacion conf = cmbConfederacion != null ? cmbConfederacion.getValue() : null;
            Integer confId = conf != null ? conf.getId() : null;

            List<Equipo> lista = service.buscar(texto, confId);
            todosLosEquipos.setAll(lista);
            paginaActual = 0;
            actualizarPagina();
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los equipos: " + e.getMessage());
        }
    }

    private void actualizarPagina() {
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todosLosEquipos.size());
        equiposPagina.setAll(todosLosEquipos.subList(inicio, fin));

        lblPaginacion.setText(String.format("Mostrando %d-%d de %d equipos",
                todosLosEquipos.isEmpty() ? 0 : inicio + 1, fin, todosLosEquipos.size()));
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(fin >= todosLosEquipos.size());
    }

    // ── Eventos ───────────────────────────────────────────────────────────────

    @FXML void filtrarTabla(ActionEvent event) { cargarDatos(); }
    @FXML void filtrarTablaKey(KeyEvent event) { cargarDatos(); }

    @FXML
    void paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) { paginaActual--; actualizarPagina(); }
    }

    @FXML
    void paginaSiguiente(ActionEvent event) {
        if ((paginaActual + 1) * ITEMS_POR_PAGINA < todosLosEquipos.size()) {
            paginaActual++;
            actualizarPagina();
        }
    }

    @FXML
    void abrirFormularioNuevo(ActionEvent event) {
        mostrarFormulario(null);
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    private void editarEquipo(Equipo equipo) {
        mostrarFormulario(equipo);
    }

    private void eliminarEquipo(Equipo equipo) {
        if (!AlertaUtil.confirmar("Eliminar equipo",
                "¿Desea eliminar el equipo " + equipo.getPais() + "?\nEsta acción no se puede deshacer.")) return;
        try {
            service.eliminar(equipo.getId());
            cargarDatos();
            AlertaUtil.info("Éxito", "Equipo eliminado correctamente.");
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudo eliminar el equipo: " + e.getMessage());
        }
    }

    private void mostrarFormulario(Equipo equipoEditar) {
        try {
            Stage owner = (Stage) tablaEquipos.getScene().getWindow();
            co.edu.uniquindio.cup.world_app.controller.form.EquipoFormController ctrl =
                    co.edu.uniquindio.cup.world_app.controller.form.FormBaseController.abrir(
                            "/co/edu/uniquindio/cup/world_app/view/form/equipo-form.fxml",
                            equipoEditar == null ? "Nuevo Equipo" : "Editar Equipo",
                            owner);
            if (equipoEditar != null) ctrl.cargarEquipo(equipoEditar);
            ctrl.mostrarYEsperar();
            if (ctrl.isGuardado()) {
                cargarDatos();
                AlertaUtil.info("Éxito", "Equipo guardado correctamente.");
            }
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }
}
