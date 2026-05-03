package co.edu.uniquindio.cup.world_app.controller.form;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Tecnico;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;
import co.edu.uniquindio.cup.world_app.service.TecnicoService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador del formulario de Técnico (crear / editar).
 */
public class TecnicoFormController extends FormBaseController {

    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private TextField txfNombre;
    @FXML private TextField txfApellido;
    @FXML private TextField txfNacionalidad;
    @FXML private ComboBox<Equipo> cmbEquipo;
    @FXML private TextField txfTitulos;

    private final TecnicoService service = new TecnicoService();
    private final EquipoRepository equipoRepo = new EquipoRepository();
    private Tecnico tecnicoEditar;

    @FXML
    void initialize() {
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

    public void cargarTecnico(Tecnico tecnico) {
        this.tecnicoEditar = tecnico;
        lblTitulo.setText("EDITAR TÉCNICO");
        lblSubtitulo.setText("Modifica los datos del director técnico");
        txfNombre.setText(tecnico.getNombre());
        txfApellido.setText(tecnico.getApellido());
        txfNacionalidad.setText(tecnico.getNacionalidad());
        txfTitulos.setText(String.valueOf(tecnico.getTitulosGanados()));
        cmbEquipo.getItems().stream()
                .filter(e -> e.getId() == tecnico.getEquipoId())
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

            Tecnico t = tecnicoEditar != null ? tecnicoEditar : new Tecnico();
            t.setNombre(txfNombre.getText().trim());
            t.setApellido(txfApellido.getText().trim());
            t.setNacionalidad(txfNacionalidad.getText().trim());
            t.setEquipoId(cmbEquipo.getValue().getId());
            t.setEquipoNombre(cmbEquipo.getValue().getPais());
            try {
                t.setTitulosGanados(Integer.parseInt(txfTitulos.getText().trim()));
            } catch (NumberFormatException ex) {
                t.setTitulosGanados(0);
            }

            if (tecnicoEditar == null) service.crear(t);
            else service.actualizar(t);

            guardado = true;
            stage.close();

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error de base de datos: " + ex.getMessage());
        }
    }
}
