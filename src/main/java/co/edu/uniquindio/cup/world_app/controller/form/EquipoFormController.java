package co.edu.uniquindio.cup.world_app.controller.form;

import co.edu.uniquindio.cup.world_app.model.Confederacion;
import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.model.Grupo;
import co.edu.uniquindio.cup.world_app.repository.ConfederacionRepository;
import co.edu.uniquindio.cup.world_app.repository.GrupoRepository;
import co.edu.uniquindio.cup.world_app.service.EquipoService;
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
 * Controlador del formulario de Equipo (crear / editar).
 */
public class EquipoFormController extends FormBaseController {

    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private TextField txfPais;
    @FXML private TextField txfBandera;
    @FXML private ComboBox<Confederacion> cmbConfederacion;
    @FXML private ComboBox<Grupo> cmbGrupo;
    @FXML private TextField txfValor;

    private final EquipoService service = new EquipoService();
    private final ConfederacionRepository confRepo = new ConfederacionRepository();
    private final GrupoRepository grupoRepo = new GrupoRepository();

    private Equipo equipoEditar;

    @FXML
    void initialize() {
        cargarCombos();
    }

    private void cargarCombos() {
        try {
            List<Confederacion> confs = confRepo.listarTodas();
            cmbConfederacion.setItems(FXCollections.observableArrayList(confs));
            cmbConfederacion.setConverter(new StringConverter<>() {
                @Override public String toString(Confederacion c) {
                    return c == null ? "" : c.getSigla() + " — " + c.getNombre();
                }
                @Override public Confederacion fromString(String s) { return null; }
            });

            List<Grupo> grupos = grupoRepo.listarTodos();
            cmbGrupo.setItems(FXCollections.observableArrayList(grupos));
            cmbGrupo.setConverter(new StringConverter<>() {
                @Override public String toString(Grupo g) {
                    return g == null ? "" : "Grupo " + g.getNombre();
                }
                @Override public Grupo fromString(String s) { return null; }
            });
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los datos: " + e.getMessage());
        }
    }

    /** Precarga los datos cuando se edita un equipo existente. */
    public void cargarEquipo(Equipo equipo) {
        this.equipoEditar = equipo;
        lblTitulo.setText("EDITAR EQUIPO");
        lblSubtitulo.setText("Modifica los datos del equipo");
        txfPais.setText(equipo.getPais());
        txfBandera.setText(equipo.getBandera());
        txfValor.setText(String.valueOf(equipo.getValorPlantilla()));

        cmbConfederacion.getItems().stream()
                .filter(c -> c.getId() == equipo.getConfederacionId())
                .findFirst().ifPresent(cmbConfederacion::setValue);
        cmbGrupo.getItems().stream()
                .filter(g -> g.getId() == equipo.getGrupoId())
                .findFirst().ifPresent(cmbGrupo::setValue);
    }

    @FXML
    void guardar(ActionEvent event) {
        ocultarError();
        try {
            Equipo e = equipoEditar != null ? equipoEditar : new Equipo();
            e.setPais(txfPais.getText().trim());
            e.setBandera(txfBandera.getText().trim());

            if (cmbConfederacion.getValue() == null) {
                mostrarError("Selecciona una confederación.");
                return;
            }
            if (cmbGrupo.getValue() == null) {
                mostrarError("Selecciona un grupo.");
                return;
            }
            e.setConfederacionId(cmbConfederacion.getValue().getId());
            e.setConfederacionNombre(cmbConfederacion.getValue().getNombre());
            e.setGrupoId(cmbGrupo.getValue().getId());
            e.setGrupoNombre(cmbGrupo.getValue().getNombre());

            try {
                e.setValorPlantilla(Double.parseDouble(txfValor.getText().trim()));
            } catch (NumberFormatException ex) {
                mostrarError("El valor de plantilla debe ser un número.");
                return;
            }

            if (equipoEditar == null) service.crear(e);
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
