package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;
import co.edu.uniquindio.cup.world_app.service.JugadorService;
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
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador CRUD para la gestión de jugadores.
 */
public class JugadoresController {

    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnNuevo;
    @FXML private ComboBox<Equipo> cmbEquipo;
    @FXML private ComboBox<String> cmbPosicion;
    @FXML private TableColumn<Jugador, String> colNumero;
    @FXML private TableColumn<Jugador, String> colNombre;
    @FXML private TableColumn<Jugador, String> colEquipo;
    @FXML private TableColumn<Jugador, String> colPosicion;
    @FXML private TableColumn<Jugador, String> colEdad;
    @FXML private TableColumn<Jugador, String> colPeso;
    @FXML private TableColumn<Jugador, String> colEstatura;
    @FXML private TableColumn<Jugador, String> colValor;
    @FXML private TableColumn<Jugador, String> colAcciones;
    @FXML private Label lblPaginacion;
    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TextField txfBuscar;

    private final JugadorService service = new JugadorService();
    private final EquipoRepository equipoRepo = new EquipoRepository();

    private final ObservableList<Jugador> todosLosJugadores = FXCollections.observableArrayList();
    private final ObservableList<Jugador> jugadoresPagina   = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 10;
    private int paginaActual = 0;

    private static final List<String> POSICIONES = List.of(
            "Portero", "Defensa", "Centrocampista", "Delantero");

    @FXML
    void initialize() {
        configurarColumnas();
        cargarFiltros();
        cargarDatos();
        configurarPermisos();
    }

    private void configurarColumnas() {
        colNumero.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().getNumeroCamiseta())));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreCompleto()));
        colEquipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipoNombre()));
        colPosicion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPosicion()));
        colEdad.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getEdad())));
        colPeso.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.1f kg", c.getValue().getPeso())));
        colEstatura.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.2f m", c.getValue().getEstatura())));
        colValor.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.2f M€", c.getValue().getValor())));

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEditar.getStyleClass().add("btn-ghost-sm");
                btnEliminar.getStyleClass().add("btn-danger-sm");
                btnEditar.setOnAction(e -> editarJugador(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarJugador(getTableView().getItems().get(getIndex())));
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

        tablaJugadores.setItems(jugadoresPagina);
    }

    private void cargarFiltros() {
        try {
            List<Equipo> equipos = equipoRepo.listarTodos();
            cmbEquipo.getItems().add(null);
            cmbEquipo.getItems().addAll(equipos);
            cmbEquipo.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Equipo e) { return e == null ? "Todos" : e.getPais(); }
                @Override public Equipo fromString(String s) { return null; }
            });

            cmbPosicion.getItems().add(null);
            cmbPosicion.getItems().addAll(POSICIONES);
            cmbPosicion.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(String s) { return s == null ? "Todas" : s; }
                @Override public String fromString(String s) { return s; }
            });
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los filtros: " + e.getMessage());
        }
    }

    private void configurarPermisos() {
        if (btnNuevo != null) btnNuevo.setVisible(SessionManager.getInstancia().puedeEscribir());
    }

    private void cargarDatos() {
        try {
            String texto = txfBuscar != null ? txfBuscar.getText() : "";
            Equipo equipo = cmbEquipo != null ? cmbEquipo.getValue() : null;
            Integer equipoId = equipo != null ? equipo.getId() : null;
            String posicion = cmbPosicion != null ? cmbPosicion.getValue() : null;

            List<Jugador> lista = service.buscar(texto, equipoId, posicion);
            todosLosJugadores.setAll(lista);
            paginaActual = 0;
            actualizarPagina();
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los jugadores: " + e.getMessage());
        }
    }

    private void actualizarPagina() {
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todosLosJugadores.size());
        jugadoresPagina.setAll(todosLosJugadores.subList(inicio, fin));
        lblPaginacion.setText(String.format("Mostrando %d-%d de %d jugadores",
                todosLosJugadores.isEmpty() ? 0 : inicio + 1, fin, todosLosJugadores.size()));
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(fin >= todosLosJugadores.size());
    }

    @FXML void filtrarTabla(ActionEvent event) { cargarDatos(); }

    @FXML
    void paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) { paginaActual--; actualizarPagina(); }
    }

    @FXML
    void paginaSiguiente(ActionEvent event) {
        if ((paginaActual + 1) * ITEMS_POR_PAGINA < todosLosJugadores.size()) {
            paginaActual++;
            actualizarPagina();
        }
    }

    @FXML
    void abrirFormularioNuevo(ActionEvent event) {
        mostrarFormulario(null);
    }

    private void editarJugador(Jugador jugador) {
        mostrarFormulario(jugador);
    }

    private void eliminarJugador(Jugador jugador) {
        if (!AlertaUtil.confirmar("Eliminar jugador",
                "¿Desea eliminar a " + jugador.getNombreCompleto() + "?")) return;
        try {
            service.eliminar(jugador.getId());
            cargarDatos();
            AlertaUtil.info("Éxito", "Jugador eliminado correctamente.");
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudo eliminar el jugador: " + e.getMessage());
        }
    }

    private void mostrarFormulario(Jugador jugadorEditar) {
        try {
            Stage owner = (Stage) tablaJugadores.getScene().getWindow();
            co.edu.uniquindio.cup.world_app.controller.form.JugadorFormController ctrl =
                    co.edu.uniquindio.cup.world_app.controller.form.FormBaseController.abrir(
                            "/co/edu/uniquindio/cup/world_app/view/form/jugador-form.fxml",
                            jugadorEditar == null ? "Nuevo Jugador" : "Editar Jugador",
                            owner);
            if (jugadorEditar != null) ctrl.cargarJugador(jugadorEditar);
            ctrl.mostrarYEsperar();
            if (ctrl.isGuardado()) {
                cargarDatos();
                AlertaUtil.info("Éxito", "Jugador guardado correctamente.");
            }
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }
}
