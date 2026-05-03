package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Confederacion;
import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.model.Partido;
import co.edu.uniquindio.cup.world_app.model.RegistroBitacora;
import co.edu.uniquindio.cup.world_app.repository.ConfederacionRepository;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;
import co.edu.uniquindio.cup.world_app.service.BitacoraService;
import co.edu.uniquindio.cup.world_app.service.JugadorService;
import co.edu.uniquindio.cup.world_app.service.PartidoService;
import co.edu.uniquindio.cup.world_app.service.ReporteService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador para la generación de reportes PDF.
 * Diseño de panel lateral con área de parámetros dinámica.
 */
public class ReportesController {

    // ── Menú lateral ──────────────────────────────────────────────────────────
    @FXML private Button btnMenuR1;
    @FXML private Button btnMenuR2;
    @FXML private Button btnMenuR3;
    @FXML private Button btnMenuR4;

    // ── Encabezado dinámico ───────────────────────────────────────────────────
    @FXML private Label lblReporteTitulo;
    @FXML private Label lblReporteDesc;

    // ── Paneles de parámetros ─────────────────────────────────────────────────
    @FXML private VBox panelR1;
    @FXML private VBox panelR2;
    @FXML private VBox panelR3;
    @FXML private VBox panelR4;

    // ── Campos R1 ─────────────────────────────────────────────────────────────
    @FXML private DatePicker dpR1Desde;
    @FXML private DatePicker dpR1Hasta;
    @FXML private TextField txfR1HoraDesde;
    @FXML private TextField txfR1HoraHasta;

    // ── Campos R2 ─────────────────────────────────────────────────────────────
    @FXML private TextField txfPesoMin;
    @FXML private TextField txfPesoMax;
    @FXML private TextField txfEstaturaMin;
    @FXML private TextField txfEstaturaMax;
    @FXML private ComboBox<Equipo> cmbR2Equipo;

    // ── Campos R3 ─────────────────────────────────────────────────────────────
    @FXML private ComboBox<Confederacion> cmbR3Confederacion;

    // ── Campos R4 ─────────────────────────────────────────────────────────────
    @FXML private ComboBox<String> cmbR4Pais;

    // ── Mensaje de resultado ──────────────────────────────────────────────────
    @FXML private Label lblMensaje;

    private final ReporteService reporteService = new ReporteService();
    private final BitacoraService bitacoraService = new BitacoraService();
    private final JugadorService jugadorService = new JugadorService();
    private final PartidoService partidoService = new PartidoService();
    private final ConfederacionRepository confRepo = new ConfederacionRepository();
    private final EquipoRepository equipoRepo = new EquipoRepository();

    /** Metadatos de cada reporte para el encabezado dinámico */
    private static final String[][] REPORTE_META = {
        {"R1 · Usuarios por Fecha y Hora",
         "Usuarios que ingresaron y salieron del sistema en un rango de fecha y hora específico."},
        {"R2 · Jugadores por Peso y Estatura",
         "Jugadores filtrados por rango de peso, estatura y equipo."},
        {"R3 · Valor Total por Confederación",
         "Valor total de los jugadores por equipo dentro de una confederación."},
        {"R4 · Partidos por País Anfitrión",
         "Todos los partidos que se jugarán en México, USA o Canadá."}
    };

    @FXML
    void initialize() {
        lblMensaje.setVisible(false);
        lblMensaje.setManaged(false);
        cargarCombos();
        // R1 activo por defecto
        mostrarPanel(0);
    }

    // ── Navegación entre reportes ─────────────────────────────────────────────

    @FXML void seleccionarR1(ActionEvent e) { mostrarPanel(0); }
    @FXML void seleccionarR2(ActionEvent e) { mostrarPanel(1); }
    @FXML void seleccionarR3(ActionEvent e) { mostrarPanel(2); }
    @FXML void seleccionarR4(ActionEvent e) { mostrarPanel(3); }

    private void mostrarPanel(int indice) {
        // Ocultar todos los paneles
        VBox[] paneles = {panelR1, panelR2, panelR3, panelR4};
        Button[] botones = {btnMenuR1, btnMenuR2, btnMenuR3, btnMenuR4};

        for (int i = 0; i < paneles.length; i++) {
            boolean activo = (i == indice);
            paneles[i].setVisible(activo);
            paneles[i].setManaged(activo);
            botones[i].getStyleClass().remove("reporte-menu-active");
            if (activo) botones[i].getStyleClass().add("reporte-menu-active");
        }

        // Actualizar encabezado
        lblReporteTitulo.setText(REPORTE_META[indice][0]);
        lblReporteDesc.setText(REPORTE_META[indice][1]);

        // Ocultar mensaje de resultado al cambiar de reporte
        lblMensaje.setVisible(false);
        lblMensaje.setManaged(false);
    }

    private void cargarCombos() {
        try {
            // R2: equipos
            cmbR2Equipo.getItems().add(null);
            cmbR2Equipo.getItems().addAll(equipoRepo.listarTodos());
            cmbR2Equipo.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Equipo e) { return e == null ? "Todos" : e.getPais(); }
                @Override public Equipo fromString(String s) { return null; }
            });

            // R3: confederaciones
            cmbR3Confederacion.getItems().addAll(confRepo.listarTodas());
            cmbR3Confederacion.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Confederacion c) { return c == null ? "" : c.getSigla() + " - " + c.getNombre(); }
                @Override public Confederacion fromString(String s) { return null; }
            });

            // R4: países
            cmbR4Pais.getItems().addAll("México", "USA", "Canadá");

        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los filtros: " + e.getMessage());
        }
    }

    // ── R1: Usuarios por fecha/hora ───────────────────────────────────────────

    @FXML
    void generarReporteUsuarios(ActionEvent event) {
        if (dpR1Desde.getValue() == null || dpR1Hasta.getValue() == null) {
            AlertaUtil.advertencia("Campos requeridos", "Seleccione el rango de fechas.");
            return;
        }
        try {
            LocalTime horaDesde = parsearHora(txfR1HoraDesde.getText(), LocalTime.MIN);
            LocalTime horaHasta = parsearHora(txfR1HoraHasta.getText(), LocalTime.MAX);

            LocalDateTime desde = dpR1Desde.getValue().atTime(horaDesde);
            LocalDateTime hasta = dpR1Hasta.getValue().atTime(horaHasta);

            List<RegistroBitacora> registros = bitacoraService.listarPorRangoFechaHora(desde, hasta);

            String ruta = elegirRutaGuardado("reporte_usuarios.pdf");
            if (ruta == null) return;

            reporteService.generarReporteUsuarios(registros, ruta);
            mostrarExito("Reporte R1 generado: " + ruta + " (" + registros.size() + " registros)");
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo generar el reporte: " + e.getMessage());
        }
    }

    // ── R2: Jugadores por peso/estatura ───────────────────────────────────────

    @FXML
    void generarReporteJugadores(ActionEvent event) {
        try {
            double pesoMin = parseDouble(txfPesoMin.getText(), 0);
            double pesoMax = parseDouble(txfPesoMax.getText(), 200);
            double estMin  = parseDouble(txfEstaturaMin.getText(), 0);
            double estMax  = parseDouble(txfEstaturaMax.getText(), 3);

            Equipo equipo = cmbR2Equipo.getValue();
            Integer equipoId = equipo != null ? equipo.getId() : null;

            List<Jugador> jugadores = jugadorService.filtrarPorPesoEstaturaEquipo(
                    pesoMin, pesoMax, estMin, estMax, equipoId);

            String ruta = elegirRutaGuardado("reporte_jugadores.pdf");
            if (ruta == null) return;

            reporteService.generarReporteJugadores(jugadores, ruta);
            mostrarExito("Reporte R2 generado: " + ruta + " (" + jugadores.size() + " jugadores)");
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo generar el reporte: " + e.getMessage());
        }
    }

    // ── R3: Valor por confederación ───────────────────────────────────────────

    @FXML
    void generarReporteValor(ActionEvent event) {
        if (cmbR3Confederacion.getValue() == null) {
            AlertaUtil.advertencia("Campo requerido", "Seleccione una confederación.");
            return;
        }
        try {
            Confederacion conf = cmbR3Confederacion.getValue();
            List<Object[]> datos = jugadorService.valorTotalPorEquipoEnConfederacion(conf.getId());

            String ruta = elegirRutaGuardado("reporte_valor_" + conf.getSigla() + ".pdf");
            if (ruta == null) return;

            reporteService.generarReporteValor(datos, conf.getNombre(), ruta);
            mostrarExito("Reporte R3 generado: " + ruta);
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo generar el reporte: " + e.getMessage());
        }
    }

    // ── R4: Partidos por país anfitrión ───────────────────────────────────────

    @FXML
    void generarReportePartidos(ActionEvent event) {
        if (cmbR4Pais.getValue() == null) {
            AlertaUtil.advertencia("Campo requerido", "Seleccione un país anfitrión.");
            return;
        }
        try {
            String pais = cmbR4Pais.getValue();
            List<Partido> partidos = partidoService.listarPorPaisAnfitrion(pais);

            String ruta = elegirRutaGuardado("reporte_partidos_" + pais.replace(" ", "_") + ".pdf");
            if (ruta == null) return;

            reporteService.generarReportePartidos(partidos, pais, ruta);
            mostrarExito("Reporte R4 generado: " + ruta + " (" + partidos.size() + " partidos)");
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo generar el reporte: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String elegirRutaGuardado(String nombreSugerido) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar reporte PDF");
        chooser.setInitialFileName(nombreSugerido);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        File archivo = chooser.showSaveDialog(lblMensaje.getScene().getWindow());
        return archivo != null ? archivo.getAbsolutePath() : null;
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText("✅ " + mensaje);
        lblMensaje.setVisible(true);
        lblMensaje.setManaged(true);
    }

    private LocalTime parsearHora(String texto, LocalTime defecto) {
        if (texto == null || texto.isBlank()) return defecto;
        try { return LocalTime.parse(texto.trim()); }
        catch (DateTimeParseException e) { return defecto; }
    }

    private double parseDouble(String texto, double defecto) {
        if (texto == null || texto.isBlank()) return defecto;
        try { return Double.parseDouble(texto.trim()); }
        catch (NumberFormatException e) { return defecto; }
    }
}
