package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.model.Usuario;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;
import co.edu.uniquindio.cup.world_app.repository.JugadorRepository;
import co.edu.uniquindio.cup.world_app.service.BitacoraService;
import co.edu.uniquindio.cup.world_app.service.EstadioService;
import co.edu.uniquindio.cup.world_app.service.JugadorService;
import co.edu.uniquindio.cup.world_app.service.PartidoService;
import co.edu.uniquindio.cup.world_app.service.TecnicoService;
import co.edu.uniquindio.cup.world_app.service.UsuarioService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador del Dashboard — pantalla de inicio con estadísticas del sistema.
 */
public class DashboardController {

    @FXML private Label lblBienvenida;

    // Tarjetas de conteo
    @FXML private Label lblTotalEquipos;
    @FXML private Label lblTotalJugadores;
    @FXML private Label lblTotalTecnicos;
    @FXML private Label lblTotalEstadios;
    @FXML private Label lblTotalPartidos;
    @FXML private Label lblTotalGrupos;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblSesionesHoy;

    // Equipos por país sede
    @FXML private Label lblEqMexico;
    @FXML private Label lblEqUSA;
    @FXML private Label lblEqCanada;

    // Jugador más valioso
    @FXML private Label lblMvpNombre;
    @FXML private Label lblMvpEquipo;
    @FXML private Label lblMvpValor;

    // Sesión actual
    @FXML private Label lblSesionUsuario;
    @FXML private Label lblSesionRol;
    @FXML private Label lblSesionHora;
    @FXML private Label lblSesionPermisos;

    // Grid de grupos rápidos
    @FXML private GridPane gridGruposRapidos;

    // Servicios
    private final EquipoRepository  equipoRepo    = new EquipoRepository();
    private final JugadorRepository  jugadorRepo   = new JugadorRepository();
    private final TecnicoService     tecnicoSvc    = new TecnicoService();
    private final EstadioService     estadioSvc    = new EstadioService();
    private final PartidoService     partidoSvc    = new PartidoService();
    private final UsuarioService     usuarioSvc    = new UsuarioService();
    private final BitacoraService    bitacoraSvc   = new BitacoraService();
    private final JugadorService     jugadorSvc    = new JugadorService();

    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    void initialize() {
        cargarBienvenida();
        cargarContadores();
        cargarEquiposPorPais();
        cargarJugadorMasValioso();
        cargarInfoSesion();
        cargarGruposRapidos();
    }

    // ── Bienvenida ────────────────────────────────────────────────────────────

    private void cargarBienvenida() {
        Usuario u = SessionManager.getInstancia().getUsuarioActual();
        if (u != null) {
            lblBienvenida.setText("BIENVENIDO, " + u.getNombreCompleto().toUpperCase());
        }
    }

    // ── Contadores ────────────────────────────────────────────────────────────

    private void cargarContadores() {
        try {
            lblTotalEquipos.setText(String.valueOf(equipoRepo.listarTodos().size()));
        } catch (SQLException e) { lblTotalEquipos.setText("—"); }

        try {
            lblTotalJugadores.setText(String.valueOf(jugadorRepo.listarTodos().size()));
        } catch (SQLException e) { lblTotalJugadores.setText("—"); }

        try {
            lblTotalTecnicos.setText(String.valueOf(tecnicoSvc.listarTodos().size()));
        } catch (SQLException e) { lblTotalTecnicos.setText("—"); }

        try {
            lblTotalEstadios.setText(String.valueOf(estadioSvc.listarTodos().size()));
        } catch (SQLException e) { lblTotalEstadios.setText("—"); }

        try {
            lblTotalPartidos.setText(String.valueOf(partidoSvc.listarTodos().size()));
        } catch (SQLException e) { lblTotalPartidos.setText("—"); }

        try {
            lblTotalUsuarios.setText(String.valueOf(usuarioSvc.listarTodos().size()));
        } catch (SQLException e) { lblTotalUsuarios.setText("—"); }

        try {
            long sesionesHoy = bitacoraSvc
                    .listarPorRango(LocalDate.now(), LocalDate.now())
                    .size();
            lblSesionesHoy.setText(String.valueOf(sesionesHoy));
        } catch (SQLException e) { lblSesionesHoy.setText("—"); }
    }

    // ── Equipos por país sede ─────────────────────────────────────────────────

    private void cargarEquiposPorPais() {
        try {
            List<Equipo> todos = equipoRepo.listarTodos();

            // Contar equipos que juegan en cada país sede según partidos
            // Simplificación: contamos equipos cuyo grupo tiene partidos en ese país
            // Para el dashboard usamos una query directa
            long mexico = contarEquiposPorPaisSede(todos, "México");
            long usa    = contarEquiposPorPaisSede(todos, "USA");
            long canada = contarEquiposPorPaisSede(todos, "Canadá");

            lblEqMexico.setText(mexico + " equipos");
            lblEqUSA.setText(usa    + " equipos");
            lblEqCanada.setText(canada + " equipos");
        } catch (SQLException e) {
            lblEqMexico.setText("—");
            lblEqUSA.setText("—");
            lblEqCanada.setText("—");
        }
    }

    /**
     * Cuenta cuántos equipos distintos tienen al menos un partido en el país dado.
     * El país viene de ciudades, no de estadios.
     */
    private long contarEquiposPorPaisSede(List<Equipo> todos, String pais) throws SQLException {
        return partidoSvc.listarPorPaisAnfitrion(pais).stream()
                .flatMap(p -> java.util.stream.Stream.of(p.getEquipoLocalId(), p.getEquipoVisitanteId()))
                .distinct()
                .count();
    }

    // ── Jugador más valioso ───────────────────────────────────────────────────

    private void cargarJugadorMasValioso() {
        try {
            List<Jugador> todos = jugadorRepo.listarTodos();
            todos.stream()
                 .max(java.util.Comparator.comparingDouble(Jugador::getValor))
                 .ifPresent(j -> {
                     lblMvpNombre.setText(j.getNombreCompleto());
                     lblMvpEquipo.setText(j.getEquipoNombre());
                     lblMvpValor.setText(String.format("%.0f M€", j.getValor()));
                 });
        } catch (SQLException e) {
            lblMvpNombre.setText("—");
        }
    }

    // ── Info de sesión ────────────────────────────────────────────────────────

    private void cargarInfoSesion() {
        Usuario u = SessionManager.getInstancia().getUsuarioActual();
        if (u == null) return;

        lblSesionUsuario.setText(u.getUsername());
        lblSesionRol.setText(u.getTipo().getEtiqueta());
        lblSesionHora.setText(
                u.getUltimoAcceso() != null
                        ? u.getUltimoAcceso().format(FMT_HORA)
                        : LocalDateTime.now().format(FMT_HORA));

        String permisos = switch (u.getTipo()) {
            case ADMINISTRADOR -> "Lectura + Escritura + Admin";
            case TRADICIONAL   -> "Lectura + Escritura";
            case ESPORADICO    -> "Solo lectura";
        };
        lblSesionPermisos.setText(permisos);
    }

    // ── Grupos rápidos ────────────────────────────────────────────────────────

    private void cargarGruposRapidos() {
        try {
            List<Equipo> todos = equipoRepo.listarTodos();
            String[] grupos = {"A","B","C","D","E","F","G","H","I","J","K","L"};

            int col = 0, row = 0;
            for (String g : grupos) {
                List<Equipo> equiposGrupo = todos.stream()
                        .filter(e -> g.equals(e.getGrupoNombre()))
                        .toList();

                VBox card = new VBox(4);
                card.getStyleClass().add("dash-grupo-mini");
                card.setPadding(new Insets(8, 10, 8, 10));

                Label titulo = new Label("Grupo " + g);
                titulo.getStyleClass().add("dash-grupo-titulo");
                card.getChildren().add(titulo);

                for (Equipo e : equiposGrupo) {
                    Label lbl = new Label(e.getBandera() + "  " + e.getPais());
                    lbl.getStyleClass().add("dash-grupo-equipo");
                    card.getChildren().add(lbl);
                }

                gridGruposRapidos.add(card, col, row);
                col++;
                if (col == 4) { col = 0; row++; }
            }
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los grupos: " + e.getMessage());
        }
    }
}
