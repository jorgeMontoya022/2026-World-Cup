package co.edu.uniquindio.cup.world_app.controller.form;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;
import co.edu.uniquindio.cup.world_app.service.JugadorService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador del formulario de Jugador (crear / editar).
 */
public class JugadorFormController extends FormBaseController {

    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private TextField txfNombre;
    @FXML private TextField txfApellido;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txfNumero;
    @FXML private ComboBox<String> cmbPosicion;
    @FXML private ComboBox<Equipo> cmbEquipo;
    @FXML private TextField txfPeso;
    @FXML private TextField txfEstatura;
    @FXML private TextField txfValor;

    private final JugadorService service = new JugadorService();
    private final EquipoRepository equipoRepo = new EquipoRepository();

    private static final List<String> POSICIONES =
            List.of("Portero", "Defensa", "Centrocampista", "Delantero");

    private Jugador jugadorEditar;

    @FXML
    void initialize() {
        cmbPosicion.setItems(FXCollections.observableArrayList(POSICIONES));
        cargarEquipos();
    }

    private void cargarEquipos() {
        try {
            List<Equipo> equipos = equipoRepo.listarTodos();
            cmbEquipo.setItems(FXCollections.observableArrayList(equipos));
            cmbEquipo.setConverter(new StringConverter<>() {
                @Override public String toString(Equipo e) {
                    return e == null ? "" : e.getBandera() + "  " + e.getPais();
                }
                @Override public Equipo fromString(String s) { return null; }
            });
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los equipos: " + e.getMessage());
        }
    }

    public void cargarJugador(Jugador jugador) {
        this.jugadorEditar = jugador;
        lblTitulo.setText("EDITAR JUGADOR");
        lblSubtitulo.setText("Modifica los datos del jugador");
        txfNombre.setText(jugador.getNombre());
        txfApellido.setText(jugador.getApellido());
        dpFechaNacimiento.setValue(jugador.getFechaNacimiento());
        txfNumero.setText(String.valueOf(jugador.getNumeroCamiseta()));
        txfPeso.setText(String.valueOf(jugador.getPeso()));
        txfEstatura.setText(String.valueOf(jugador.getEstatura()));
        txfValor.setText(String.valueOf(jugador.getValor()));
        cmbPosicion.setValue(jugador.getPosicion());
        cmbEquipo.getItems().stream()
                .filter(e -> e.getId() == jugador.getEquipoId())
                .findFirst().ifPresent(cmbEquipo::setValue);
    }

    @FXML
    void guardar(ActionEvent event) {
        ocultarError();
        try {
            if (txfNombre.getText().isBlank() || txfApellido.getText().isBlank()) {
                mostrarError("Nombre y apellido son obligatorios.");
                return;
            }
            if (cmbEquipo.getValue() == null) {
                mostrarError("Selecciona un equipo.");
                return;
            }
            if (cmbPosicion.getValue() == null) {
                mostrarError("Selecciona una posición.");
                return;
            }

            Jugador j = jugadorEditar != null ? jugadorEditar : new Jugador();
            j.setNombre(txfNombre.getText().trim());
            j.setApellido(txfApellido.getText().trim());
            j.setFechaNacimiento(dpFechaNacimiento.getValue());
            j.setPosicion(cmbPosicion.getValue());
            j.setEquipoId(cmbEquipo.getValue().getId());
            j.setEquipoNombre(cmbEquipo.getValue().getPais());

            try {
                j.setNumeroCamiseta(Integer.parseInt(txfNumero.getText().trim()));
                j.setPeso(Double.parseDouble(txfPeso.getText().trim()));
                j.setEstatura(Double.parseDouble(txfEstatura.getText().trim()));
                j.setValor(Double.parseDouble(txfValor.getText().trim()));
            } catch (NumberFormatException ex) {
                mostrarError("Número, peso, estatura y valor deben ser numéricos.");
                return;
            }

            if (jugadorEditar == null) service.crear(j);
            else service.actualizar(j);

            guardado = true;
            stage.close();

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error de base de datos: " + ex.getMessage());
        }
    }
}
