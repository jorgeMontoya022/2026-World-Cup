package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.*;
import co.edu.uniquindio.cup.world_app.repository.*;
import co.edu.uniquindio.cup.world_app.service.PartidoService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controlador CRUD para la gestión de partidos.
 */
public class PartidosController {

    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnNuevo;
    @FXML private ComboBox<Grupo> cmbGrupo;
    @FXML private ComboBox<Estadio> cmbEstadio;
    @FXML private TableColumn<Partido, String> colGrupo;
    @FXML private TableColumn<Partido, String> colLocal;
    @FXML private TableColumn<Partido, String> colVisitante;
    @FXML private TableColumn<Partido, String> colEstadio;
    @FXML private TableColumn<Partido, String> colFecha;
    @FXML private TableColumn<Partido, String> colResultado;
    @FXML private TableColumn<Partido, String> colAcciones;
    @FXML private Label lblPaginacion;
    @FXML private TableView<Partido> tablaPartidos;

    private final PartidoService service = new PartidoService();
    private final GrupoRepository grupoRepo = new GrupoRepository();
    private final EstadioRepository estadioRepo = new EstadioRepository();
    private final EquipoRepository equipoRepo = new EquipoRepository();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final ObservableList<Partido> todosLosPartidos = FXCollections.observableArrayList();
    private final ObservableList<Partido> partidosPagina   = FXCollections.observableArrayList();
    private static final int ITEMS_POR_PAGINA = 10;
    private int paginaActual = 0;

    @FXML
    void initialize() {
        configurarColumnas();
        cargarFiltros();
        cargarDatos();
        if (btnNuevo != null) btnNuevo.setVisible(SessionManager.getInstancia().puedeEscribir());
    }

    private void configurarColumnas() {
        colGrupo.setCellValueFactory(c -> new SimpleStringProperty("Grupo " + c.getValue().getGrupoNombre()));
        colLocal.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipoLocalNombre()));
        colVisitante.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipoVisitanteNombre()));
        colEstadio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstadioNombre()));
        colFecha.setCellValueFactory(c -> {
            if (c.getValue().getFechaHora() == null) return new SimpleStringProperty("-");
            return new SimpleStringProperty(c.getValue().getFechaHora().format(FMT));
        });
        colResultado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getResultado()));

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEditar.getStyleClass().add("btn-ghost-sm");
                btnEliminar.getStyleClass().add("btn-danger-sm");
                btnEditar.setOnAction(e -> editarPartido(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarPartido(getTableView().getItems().get(getIndex())));
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

        tablaPartidos.setItems(partidosPagina);
    }

    private void cargarFiltros() {
        try {
            cmbGrupo.getItems().add(null);
            cmbGrupo.getItems().addAll(grupoRepo.listarTodos());
            cmbGrupo.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Grupo g) { return g == null ? "Todos" : "Grupo " + g.getNombre(); }
                @Override public Grupo fromString(String s) { return null; }
            });

            cmbEstadio.getItems().add(null);
            cmbEstadio.getItems().addAll(estadioRepo.listarTodos());
            cmbEstadio.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Estadio e) { return e == null ? "Todos" : e.getNombre(); }
                @Override public Estadio fromString(String s) { return null; }
            });
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los filtros: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            List<Partido> lista;
            Grupo grupo = cmbGrupo != null ? cmbGrupo.getValue() : null;
            Estadio estadio = cmbEstadio != null ? cmbEstadio.getValue() : null;

            if (estadio != null) {
                lista = service.listarPorEstadio(estadio.getId());
            } else if (grupo != null) {
                lista = service.listarPorGrupo(grupo.getId());
            } else {
                lista = service.listarTodos();
            }

            todosLosPartidos.setAll(lista);
            paginaActual = 0;
            actualizarPagina();
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los partidos: " + e.getMessage());
        }
    }

    private void actualizarPagina() {
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todosLosPartidos.size());
        partidosPagina.setAll(todosLosPartidos.subList(inicio, fin));
        lblPaginacion.setText(String.format("Mostrando %d-%d de %d partidos",
                todosLosPartidos.isEmpty() ? 0 : inicio + 1, fin, todosLosPartidos.size()));
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(fin >= todosLosPartidos.size());
    }

    @FXML void filtrarTabla(ActionEvent event) { cargarDatos(); }

    @FXML void paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) { paginaActual--; actualizarPagina(); }
    }

    @FXML void paginaSiguiente(ActionEvent event) {
        if ((paginaActual + 1) * ITEMS_POR_PAGINA < todosLosPartidos.size()) {
            paginaActual++;
            actualizarPagina();
        }
    }

    @FXML void abrirFormularioNuevo(ActionEvent event) { mostrarFormulario(null); }

    private void editarPartido(Partido partido) { mostrarFormulario(partido); }

    private void eliminarPartido(Partido partido) {
        if (!AlertaUtil.confirmar("Eliminar partido",
                "¿Desea eliminar el partido " + partido.getDescripcion() + "?")) return;
        try {
            service.eliminar(partido.getId());
            cargarDatos();
        } catch (SQLException e) {
            AlertaUtil.error("Error", e.getMessage());
        }
    }

    private void mostrarFormulario(Partido partidoEditar) {
        try {
            List<Equipo> equipos = equipoRepo.listarTodos();
            List<Estadio> estadios = estadioRepo.listarTodos();
            List<Grupo> grupos = grupoRepo.listarTodos();

            Dialog<Partido> dialog = new Dialog<>();
            dialog.setTitle(partidoEditar == null ? "Nuevo Partido" : "Editar Partido");
            ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.setPadding(new Insets(20)); grid.setMinWidth(420);

            ComboBox<Equipo> cmbLocal = new ComboBox<>(FXCollections.observableArrayList(equipos));
            ComboBox<Equipo> cmbVisitante = new ComboBox<>(FXCollections.observableArrayList(equipos));
            ComboBox<Estadio> cmbEst = new ComboBox<>(FXCollections.observableArrayList(estadios));
            ComboBox<Grupo> cmbGrp = new ComboBox<>(FXCollections.observableArrayList(grupos));
            TextField txfFecha = new TextField();
            txfFecha.setPromptText("dd/MM/yyyy HH:mm");

            javafx.util.StringConverter<Equipo> convEquipo = new javafx.util.StringConverter<>() {
                @Override public String toString(Equipo e) { return e == null ? "" : e.getPais(); }
                @Override public Equipo fromString(String s) { return null; }
            };
            cmbLocal.setConverter(convEquipo);
            cmbVisitante.setConverter(convEquipo);
            cmbEst.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Estadio e) { return e == null ? "" : e.getNombre(); }
                @Override public Estadio fromString(String s) { return null; }
            });
            cmbGrp.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Grupo g) { return g == null ? "" : "Grupo " + g.getNombre(); }
                @Override public Grupo fromString(String s) { return null; }
            });

            for (ComboBox<?> cb : List.of(cmbLocal, cmbVisitante, cmbEst, cmbGrp))
                cb.setMaxWidth(Double.MAX_VALUE);

            if (partidoEditar != null) {
                equipos.stream().filter(e -> e.getId() == partidoEditar.getEquipoLocalId()).findFirst().ifPresent(cmbLocal::setValue);
                equipos.stream().filter(e -> e.getId() == partidoEditar.getEquipoVisitanteId()).findFirst().ifPresent(cmbVisitante::setValue);
                estadios.stream().filter(e -> e.getId() == partidoEditar.getEstadioId()).findFirst().ifPresent(cmbEst::setValue);
                grupos.stream().filter(g -> g.getId() == partidoEditar.getGrupoId()).findFirst().ifPresent(cmbGrp::setValue);
                if (partidoEditar.getFechaHora() != null)
                    txfFecha.setText(partidoEditar.getFechaHora().format(FMT));
            }

            int row = 0;
            grid.add(new Label("Equipo local:"), 0, row);     grid.add(cmbLocal, 1, row++);
            grid.add(new Label("Equipo visitante:"), 0, row); grid.add(cmbVisitante, 1, row++);
            grid.add(new Label("Estadio:"), 0, row);          grid.add(cmbEst, 1, row++);
            grid.add(new Label("Grupo:"), 0, row);            grid.add(cmbGrp, 1, row++);
            grid.add(new Label("Fecha/Hora:"), 0, row);       grid.add(txfFecha, 1, row);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(bt -> {
                if (bt != btnGuardar) return null;
                Partido p = partidoEditar != null ? partidoEditar : new Partido();
                if (cmbLocal.getValue() != null) {
                    p.setEquipoLocalId(cmbLocal.getValue().getId());
                    p.setEquipoLocalNombre(cmbLocal.getValue().getPais());
                }
                if (cmbVisitante.getValue() != null) {
                    p.setEquipoVisitanteId(cmbVisitante.getValue().getId());
                    p.setEquipoVisitanteNombre(cmbVisitante.getValue().getPais());
                }
                if (cmbEst.getValue() != null) {
                    p.setEstadioId(cmbEst.getValue().getId());
                    p.setEstadioNombre(cmbEst.getValue().getNombre());
                }
                if (cmbGrp.getValue() != null) {
                    p.setGrupoId(cmbGrp.getValue().getId());
                    p.setGrupoNombre(cmbGrp.getValue().getNombre());
                }
                try {
                    if (!txfFecha.getText().isBlank())
                        p.setFechaHora(LocalDateTime.parse(txfFecha.getText(), FMT));
                } catch (Exception ignored) {}
                return p;
            });

            Optional<Partido> resultado = dialog.showAndWait();
            resultado.ifPresent(p -> {
                try {
                    if (partidoEditar == null) service.crear(p);
                    else service.actualizar(p);
                    cargarDatos();
                    AlertaUtil.info("Éxito", "Partido guardado correctamente.");
                } catch (Exception ex) {
                    AlertaUtil.error("Error", ex.getMessage());
                }
            });

        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }
}
