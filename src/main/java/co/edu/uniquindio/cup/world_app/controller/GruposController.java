package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de la vista de grupos.
 * Muestra los 12 grupos con sus equipos (solo lectura).
 */
public class GruposController {

    @FXML private TableView<Equipo> tablaGrupoA;
    @FXML private TableColumn<Equipo, Number> colPosA;
    @FXML private TableColumn<Equipo, String> colEquipoA;
    @FXML private TableColumn<Equipo, String> colPtsA;

    @FXML private TableView<Equipo> tablaGrupoB;
    @FXML private TableColumn<Equipo, Number> colPosB;
    @FXML private TableColumn<Equipo, String> colEquipoB;
    @FXML private TableColumn<Equipo, String> colPtsB;

    @FXML private TableView<Equipo> tablaGrupoC;
    @FXML private TableColumn<Equipo, Number> colPosC;
    @FXML private TableColumn<Equipo, String> colEquipoC;
    @FXML private TableColumn<Equipo, String> colPtsC;

    @FXML private TableView<Equipo> tablaGrupoD;
    @FXML private TableColumn<Equipo, Number> colPosD;
    @FXML private TableColumn<Equipo, String> colEquipoD;
    @FXML private TableColumn<Equipo, String> colPtsD;

    @FXML private TableView<Equipo> tablaGrupoE;
    @FXML private TableColumn<Equipo, Number> colPosE;
    @FXML private TableColumn<Equipo, String> colEquipoE;
    @FXML private TableColumn<Equipo, String> colPtsE;

    @FXML private TableView<Equipo> tablaGrupoF;
    @FXML private TableColumn<Equipo, Number> colPosF;
    @FXML private TableColumn<Equipo, String> colEquipoF;
    @FXML private TableColumn<Equipo, String> colPtsF;

    @FXML private TableView<Equipo> tablaGrupoG;
    @FXML private TableColumn<Equipo, Number> colPosG;
    @FXML private TableColumn<Equipo, String> colEquipoG;
    @FXML private TableColumn<Equipo, String> colPtsG;

    @FXML private TableView<Equipo> tablaGrupoH;
    @FXML private TableColumn<Equipo, Number> colPosH;
    @FXML private TableColumn<Equipo, String> colEquipoH;
    @FXML private TableColumn<Equipo, String> colPtsH;

    @FXML private TableView<Equipo> tablaGrupoI;
    @FXML private TableColumn<Equipo, Number> colPosI;
    @FXML private TableColumn<Equipo, String> colEquipoI;
    @FXML private TableColumn<Equipo, String> colPtsI;

    @FXML private TableView<Equipo> tablaGrupoJ;
    @FXML private TableColumn<Equipo, Number> colPosJ;
    @FXML private TableColumn<Equipo, String> colEquipoJ;
    @FXML private TableColumn<Equipo, String> colPtsJ;

    @FXML private TableView<Equipo> tablaGrupoK;
    @FXML private TableColumn<Equipo, Number> colPosK;
    @FXML private TableColumn<Equipo, String> colEquipoK;
    @FXML private TableColumn<Equipo, String> colPtsK;

    @FXML private TableView<Equipo> tablaGrupoL;
    @FXML private TableColumn<Equipo, Number> colPosL;
    @FXML private TableColumn<Equipo, String> colEquipoL;
    @FXML private TableColumn<Equipo, String> colPtsL;

    private final EquipoRepository equipoRepo = new EquipoRepository();

    @FXML
    void initialize() {
        try {
            List<Equipo> todos = equipoRepo.listarTodos();
            cargarGrupo("A", tablaGrupoA, colPosA, colEquipoA, colPtsA, todos);
            cargarGrupo("B", tablaGrupoB, colPosB, colEquipoB, colPtsB, todos);
            cargarGrupo("C", tablaGrupoC, colPosC, colEquipoC, colPtsC, todos);
            cargarGrupo("D", tablaGrupoD, colPosD, colEquipoD, colPtsD, todos);
            cargarGrupo("E", tablaGrupoE, colPosE, colEquipoE, colPtsE, todos);
            cargarGrupo("F", tablaGrupoF, colPosF, colEquipoF, colPtsF, todos);
            cargarGrupo("G", tablaGrupoG, colPosG, colEquipoG, colPtsG, todos);
            cargarGrupo("H", tablaGrupoH, colPosH, colEquipoH, colPtsH, todos);
            cargarGrupo("I", tablaGrupoI, colPosI, colEquipoI, colPtsI, todos);
            cargarGrupo("J", tablaGrupoJ, colPosJ, colEquipoJ, colPtsJ, todos);
            cargarGrupo("K", tablaGrupoK, colPosK, colEquipoK, colPtsK, todos);
            cargarGrupo("L", tablaGrupoL, colPosL, colEquipoL, colPtsL, todos);
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los grupos: " + e.getMessage());
        }
    }

    private void cargarGrupo(String nombreGrupo,
                              TableView<Equipo> tabla,
                              TableColumn<Equipo, Number> colPos,
                              TableColumn<Equipo, String> colEquipo,
                              TableColumn<Equipo, String> colPts,
                              List<Equipo> todos) {
        List<Equipo> equiposGrupo = todos.stream()
                .filter(e -> nombreGrupo.equals(e.getGrupoNombre()))
                .toList();

        colPos.setCellValueFactory(c -> new SimpleIntegerProperty(
                tabla.getItems().indexOf(c.getValue()) + 1));
        colEquipo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getBandera() + " " + c.getValue().getPais()));
        colPts.setCellValueFactory(c -> new SimpleStringProperty("0"));

        tabla.setItems(FXCollections.observableArrayList(equiposGrupo));
    }
}
