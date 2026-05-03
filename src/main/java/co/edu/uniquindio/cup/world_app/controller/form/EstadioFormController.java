package co.edu.uniquindio.cup.world_app.controller.form;

import co.edu.uniquindio.cup.world_app.model.Ciudad;
import co.edu.uniquindio.cup.world_app.model.Estadio;
import co.edu.uniquindio.cup.world_app.repository.CiudadRepository;
import co.edu.uniquindio.cup.world_app.service.EstadioService;
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
 * Controlador del formulario de Estadio (crear / editar).
 */
public class EstadioFormController extends FormBaseController {

    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private TextField txfNombre;
    @FXML private ComboBox<Ciudad> cmbCiudad;
    @FXML private TextField txfCapacidad;

    private final EstadioService service = new EstadioService();
    private final CiudadRepository ciudadRepo = new CiudadRepository();
    private Estadio estadioEditar;

    @FXML
    void initialize() {
        cargarCiudades();
    }

    private void cargarCiudades() {
        try {
            List<Ciudad> ciudades = ciudadRepo.listarTodas();
            cmbCiudad.setItems(FXCollections.observableArrayList(ciudades));
            cmbCiudad.setConverter(new StringConverter<>() {
                @Override public String toString(Ciudad c) {
                    return c == null ? "" : c.getNombre() + "  (" + c.getPais() + ")";
                }
                @Override public Ciudad fromString(String s) { return null; }
            });
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las ciudades: " + e.getMessage());
        }
    }

    public void cargarEstadio(Estadio estadio) {
        this.estadioEditar = estadio;
        lblTitulo.setText("EDITAR ESTADIO");
        lblSubtitulo.setText("Modifica los datos del estadio");
        txfNombre.setText(estadio.getNombre());
        txfCapacidad.setText(String.valueOf(estadio.getCapacidad()));
        cmbCiudad.getItems().stream()
                .filter(c -> c.getId() == estadio.getCiudadId())
                .findFirst().ifPresent(cmbCiudad::setValue);
    }

    @FXML
    void guardar(ActionEvent event) {
        ocultarError();
        try {
            if (txfNombre.getText().isBlank()) {
                mostrarError("El nombre del estadio es obligatorio.");
                return;
            }
            if (cmbCiudad.getValue() == null) {
                mostrarError("Selecciona una ciudad.");
                return;
            }

            Estadio e = estadioEditar != null ? estadioEditar : new Estadio();
            e.setNombre(txfNombre.getText().trim());
            e.setCiudadId(cmbCiudad.getValue().getId());
            e.setCiudadNombre(cmbCiudad.getValue().getNombre());
            e.setPais(cmbCiudad.getValue().getPais());
            try {
                e.setCapacidad(Integer.parseInt(txfCapacidad.getText().trim()));
            } catch (NumberFormatException ex) {
                mostrarError("La capacidad debe ser un número entero.");
                return;
            }

            if (estadioEditar == null) service.crear(e);
            else service.actualizar(e);

            guardado = true;
            stage.close();

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarError("Error de base de datos: " + ex.getMessage());
        }
    }
}
