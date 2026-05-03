package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Tecnico;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;
import co.edu.uniquindio.cup.world_app.service.TecnicoService;
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
 * Controlador CRUD para la gestión de directores técnicos.
 */
public class TecnicosController {

    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnNuevo;
    @FXML private TableColumn<Tecnico, String> colNombre;
    @FXML private TableColumn<Tecnico, String> colNacionalidad;
    @FXML private TableColumn<Tecnico, String> colEquipo;
    @FXML private TableColumn<Tecnico, String> colTitulos;
    @FXML private TableColumn<Tecnico, String> colAcciones;
    @FXML private Label lblPaginacion;
    @FXML private TableView<Tecnico> tablaTecnicos;
    @FXML private TextField txfBuscar;

    private final TecnicoService service = new TecnicoService();
    private final EquipoRepository equipoRepo = new EquipoRepository();

    private final ObservableList<Tecnico> todosTecnicos  = FXCollections.observableArrayList();
    private final ObservableList<Tecnico> tecnicosPagina = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 10;
    private int paginaActual = 0;

    @FXML
    void initialize() {
        configurarColumnas();
        cargarDatos();
        configurarPermisos();
        tablaTecnicos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreCompleto()));
        colNacionalidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNacionalidad()));
        colEquipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipoNombre()));
        colTitulos.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getTitulosGanados())));

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar   = new Button("✏️");
            private final Button btnEliminar = new Button("🗑️");
            {
                btnEditar.getStyleClass().add("btn-ghost-sm");
                btnEliminar.getStyleClass().add("btn-danger-sm");
                btnEditar.setOnAction(e -> editarTecnico(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarTecnico(getTableView().getItems().get(getIndex())));
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

        tablaTecnicos.setItems(tecnicosPagina);
    }

    private void configurarPermisos() {
        if (btnNuevo != null) btnNuevo.setVisible(SessionManager.getInstancia().puedeEscribir());
    }

    private void cargarDatos() {
        try {
            String texto = txfBuscar != null ? txfBuscar.getText() : "";
            List<Tecnico> lista = service.buscar(texto);
            todosTecnicos.setAll(lista);
            paginaActual = 0;
            actualizarPagina();
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los técnicos: " + e.getMessage());
        }
    }

    private void actualizarPagina() {
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todosTecnicos.size());
        tecnicosPagina.setAll(todosTecnicos.subList(inicio, fin));
        lblPaginacion.setText(String.format("Mostrando %d-%d de %d técnicos",
                todosTecnicos.isEmpty() ? 0 : inicio + 1, fin, todosTecnicos.size()));
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(fin >= todosTecnicos.size());
    }

    @FXML void filtrarTabla(KeyEvent event) { cargarDatos(); }

    @FXML
    void paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) { paginaActual--; actualizarPagina(); }
    }

    @FXML
    void paginaSiguiente(ActionEvent event) {
        if ((paginaActual + 1) * ITEMS_POR_PAGINA < todosTecnicos.size()) {
            paginaActual++;
            actualizarPagina();
        }
    }

    @FXML
    void abrirFormularioNuevo(ActionEvent event) {
        mostrarFormulario(null);
    }

    private void editarTecnico(Tecnico tecnico) {
        mostrarFormulario(tecnico);
    }

    private void eliminarTecnico(Tecnico tecnico) {
        if (!AlertaUtil.confirmar("Eliminar técnico",
                "¿Desea eliminar a " + tecnico.getNombreCompleto() + "?")) return;
        try {
            service.eliminar(tecnico.getId());
            cargarDatos();
            AlertaUtil.info("Éxito", "Técnico eliminado correctamente.");
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudo eliminar el técnico: " + e.getMessage());
        }
    }

    private void mostrarFormulario(Tecnico tecnicoEditar) {
        try {
            Stage owner = (Stage) tablaTecnicos.getScene().getWindow();
            co.edu.uniquindio.cup.world_app.controller.form.TecnicoFormController ctrl =
                    co.edu.uniquindio.cup.world_app.controller.form.FormBaseController.abrir(
                            "/co/edu/uniquindio/cup/world_app/view/form/tecnico-form.fxml",
                            tecnicoEditar == null ? "Nuevo Técnico" : "Editar Técnico",
                            owner);
            if (tecnicoEditar != null) ctrl.cargarTecnico(tecnicoEditar);
            ctrl.mostrarYEsperar();
            if (ctrl.isGuardado()) {
                cargarDatos();
                AlertaUtil.info("Éxito", "Técnico guardado correctamente.");
            }
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }
}
