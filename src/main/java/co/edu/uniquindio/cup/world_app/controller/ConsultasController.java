package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Estadio;
import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.model.Partido;
import co.edu.uniquindio.cup.world_app.repository.EstadioRepository;
import co.edu.uniquindio.cup.world_app.service.EquipoService;
import co.edu.uniquindio.cup.world_app.service.JugadorService;
import co.edu.uniquindio.cup.world_app.service.PartidoService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de consultas especiales del sistema.
 * Accesible por todos los tipos de usuario.
 */
public class ConsultasController {

    // ── C1: Jugador más costoso por confederación ──────────────────────────────
    @FXML private TableView<Jugador> tablaC1;
    @FXML private TableColumn<Jugador, String> colC1Confederacion;
    @FXML private TableColumn<Jugador, String> colC1Jugador;
    @FXML private TableColumn<Jugador, String> colC1Equipo;
    @FXML private TableColumn<Jugador, String> colC1Valor;

    // ── C2: Partidos en un estadio ─────────────────────────────────────────────
    @FXML private ComboBox<Estadio> cmbEstadioC2;
    @FXML private TableView<Partido> tablaC2;
    @FXML private TableColumn<Partido, String> colC2Local;
    @FXML private TableColumn<Partido, String> colC2Visitante;
    @FXML private TableColumn<Partido, String> colC2Fecha;
    @FXML private TableColumn<Partido, String> colC2Grupo;

    // ── C3: Equipo más costoso por país sede ───────────────────────────────────
    @FXML private TableView<Equipo> tablaC3;
    @FXML private TableColumn<Equipo, String> colC3Pais;
    @FXML private TableColumn<Equipo, String> colC3Equipo;
    @FXML private TableColumn<Equipo, String> colC3Valor;

    // ── C4: Jugadores menores de 21 por equipo ─────────────────────────────────
    @FXML private TableView<Object[]> tablaC4;
    @FXML private TableColumn<Object[], String> colC4Equipo;
    @FXML private TableColumn<Object[], String> colC4Cantidad;

    private final JugadorService jugadorService = new JugadorService();
    private final EquipoService equipoService = new EquipoService();
    private final PartidoService partidoService = new PartidoService();
    private final EstadioRepository estadioRepo = new EstadioRepository();

    @FXML
    void initialize() {
        configurarColumnas();
        cargarEstadios();
        ejecutarC1();
        ejecutarC3();
        ejecutarC4();
    }

    private void configurarColumnas() {
        // C1
        colC1Confederacion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getConfederacionNombre()));
        colC1Jugador.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreCompleto()));
        colC1Equipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipoNombre()));
        colC1Valor.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.2f M€", c.getValue().getValor())));

        // C2
        colC2Local.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipoLocalNombre()));
        colC2Visitante.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEquipoVisitanteNombre()));
        colC2Fecha.setCellValueFactory(c -> {
            if (c.getValue().getFechaHora() == null) return new SimpleStringProperty("-");
            return new SimpleStringProperty(c.getValue().getFechaHora()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        });
        colC2Grupo.setCellValueFactory(c -> new SimpleStringProperty("Grupo " + c.getValue().getGrupoNombre()));

        // C3
        colC3Pais.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPais()));
        colC3Equipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPais()));
        colC3Valor.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%.1f M€", c.getValue().getValorPlantilla())));

        // C4
        colC4Equipo.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[0]));
        colC4Cantidad.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue()[1])));
    }

    private void cargarEstadios() {
        try {
            List<Estadio> estadios = estadioRepo.listarTodos();
            cmbEstadioC2.getItems().addAll(estadios);
            cmbEstadioC2.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Estadio e) { return e == null ? "" : e.getNombre() + " (" + e.getPais() + ")"; }
                @Override public Estadio fromString(String s) { return null; }
            });
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los estadios: " + e.getMessage());
        }
    }

    private void ejecutarC1() {
        try {
            List<Jugador> resultado = jugadorService.jugadorMasCostosoPorConfederacion();
            tablaC1.setItems(FXCollections.observableArrayList(resultado));
        } catch (SQLException e) {
            AlertaUtil.error("Error C1", e.getMessage());
        }
    }

    @FXML
    void ejecutarC2(ActionEvent event) {
        if (cmbEstadioC2.getValue() == null) {
            AlertaUtil.advertencia("Campo requerido", "Seleccione un estadio.");
            return;
        }
        try {
            List<Partido> partidos = partidoService.listarPorEstadio(cmbEstadioC2.getValue().getId());
            tablaC2.setItems(FXCollections.observableArrayList(partidos));
        } catch (SQLException e) {
            AlertaUtil.error("Error C2", e.getMessage());
        }
    }

    private void ejecutarC3() {
        try {
            List<Equipo> resultado = equipoService.equipoMasCostosoPorPaisSede();
            tablaC3.setItems(FXCollections.observableArrayList(resultado));
        } catch (SQLException e) {
            AlertaUtil.error("Error C3", e.getMessage());
        }
    }

    private void ejecutarC4() {
        try {
            List<Object[]> resultado = jugadorService.jugadoresMenores21PorEquipo();
            tablaC4.setItems(FXCollections.observableArrayList(resultado));
        } catch (SQLException e) {
            AlertaUtil.error("Error C4", e.getMessage());
        }
    }
}
