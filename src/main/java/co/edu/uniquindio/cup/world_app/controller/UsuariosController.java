package co.edu.uniquindio.cup.world_app.controller;

import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.model.Usuario;
import co.edu.uniquindio.cup.world_app.service.UsuarioService;
import co.edu.uniquindio.cup.world_app.util.AlertaUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador para la gestión de usuarios del sistema.
 * Solo accesible por el Administrador.
 */
public class UsuariosController {

    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private ComboBox<TipoUsuario> cmbTipo;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colTipo;
    @FXML private TableColumn<Usuario, String> colEstado;
    @FXML private TableColumn<Usuario, String> colUltimoAcceso;
    @FXML private TableColumn<Usuario, String> colAcciones;
    @FXML private TableColumn<Object[], String> colPermiso;
    @FXML private TableColumn<Object[], String> colAdmin;
    @FXML private TableColumn<Object[], String> colTradicional;
    @FXML private TableColumn<Object[], String> colEsporadico;
    @FXML private Label lblPaginacion;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableView<Object[]> tablaPermisos;
    @FXML private TextField txfBuscar;

    private final UsuarioService service = new UsuarioService();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ObservableList<Usuario> todosLosUsuarios = FXCollections.observableArrayList();
    private final ObservableList<Usuario> usuariosPagina   = FXCollections.observableArrayList();

    private static final int ITEMS_POR_PAGINA = 10;
    private int paginaActual = 0;

    @FXML
    void initialize() {
        // Solo el admin puede acceder
        if (!SessionManager.getInstancia().isAdmin()) {
            AlertaUtil.advertencia("Acceso denegado", "Solo el Administrador puede gestionar usuarios.");
            return;
        }
        configurarColumnas();
        configurarFiltros();
        cargarTablaPermisos();
        cargarDatos();
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaPermisos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarColumnas() {
        colUsuario.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreCompleto()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo().getEtiqueta()));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isActivo() ? "Activo" : "Inactivo"));
        colUltimoAcceso.setCellValueFactory(c -> {
            if (c.getValue().getUltimoAcceso() == null) return new SimpleStringProperty("Nunca");
            return new SimpleStringProperty(c.getValue().getUltimoAcceso().format(FMT));
        });

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar   = new Button("✏️");
            private final Button btnEliminar = new Button("🗑️");
            {
                btnEditar.getStyleClass().add("btn-ghost-sm");
                btnEliminar.getStyleClass().add("btn-danger-sm");
                btnEditar.setOnAction(e -> editarUsuario(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarUsuario(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                javafx.scene.layout.HBox box = new javafx.scene.layout.HBox(4, btnEditar, btnEliminar);
                setGraphic(box);
            }
        });

        tablaUsuarios.setItems(usuariosPagina);
    }

    private void configurarFiltros() {
        cmbTipo.getItems().add(null);
        cmbTipo.getItems().addAll(TipoUsuario.values());
        cmbTipo.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(TipoUsuario t) { return t == null ? "Todos" : t.getEtiqueta(); }
            @Override public TipoUsuario fromString(String s) { return null; }
        });
    }

    private void cargarTablaPermisos() {
        colPermiso.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[0]));
        colAdmin.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[1]));
        colTradicional.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[2]));
        colEsporadico.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[3]));

        ObservableList<Object[]> permisos = FXCollections.observableArrayList(
                new Object[]{"Crear usuarios",          "✅", "❌", "❌"},
                new Object[]{"Editar usuarios",         "✅", "❌", "❌"},
                new Object[]{"Eliminar usuarios",       "✅", "❌", "❌"},
                new Object[]{"CRUD Equipos",            "✅", "✅", "❌"},
                new Object[]{"CRUD Jugadores",          "✅", "✅", "❌"},
                new Object[]{"CRUD Técnicos",           "✅", "✅", "❌"},
                new Object[]{"CRUD Estadios",           "✅", "✅", "❌"},
                new Object[]{"Ver consultas",           "✅", "✅", "✅"},
                new Object[]{"Generar reportes PDF",    "✅", "✅", "✅"},
                new Object[]{"Ver bitácora",            "✅", "❌", "❌"}
        );
        tablaPermisos.setItems(permisos);
    }

    private void cargarDatos() {
        try {
            TipoUsuario tipo = cmbTipo != null ? cmbTipo.getValue() : null;
            List<Usuario> lista = tipo != null ? service.listarPorTipo(tipo) : service.listarTodos();

            String filtro = txfBuscar != null ? txfBuscar.getText().toLowerCase() : "";
            if (!filtro.isBlank()) {
                lista = lista.stream()
                        .filter(u -> u.getUsername().toLowerCase().contains(filtro)
                                  || u.getNombreCompleto().toLowerCase().contains(filtro))
                        .toList();
            }

            todosLosUsuarios.setAll(lista);
            paginaActual = 0;
            actualizarPagina();
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    private void actualizarPagina() {
        int inicio = paginaActual * ITEMS_POR_PAGINA;
        int fin = Math.min(inicio + ITEMS_POR_PAGINA, todosLosUsuarios.size());
        usuariosPagina.setAll(todosLosUsuarios.subList(inicio, fin));
        lblPaginacion.setText(String.format("Mostrando %d-%d de %d usuarios",
                todosLosUsuarios.isEmpty() ? 0 : inicio + 1, fin, todosLosUsuarios.size()));
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(fin >= todosLosUsuarios.size());
    }

    @FXML void filtrarTabla(Event event) { cargarDatos(); }

    @FXML
    void paginaAnterior(ActionEvent event) {
        if (paginaActual > 0) { paginaActual--; actualizarPagina(); }
    }

    @FXML
    void paginaSiguiente(ActionEvent event) {
        if ((paginaActual + 1) * ITEMS_POR_PAGINA < todosLosUsuarios.size()) {
            paginaActual++;
            actualizarPagina();
        }
    }

    @FXML
    void abrirFormularioNuevo(ActionEvent event) {
        mostrarFormulario(null);
    }

    private void editarUsuario(Usuario usuario) {
        mostrarFormulario(usuario);
    }

    private void eliminarUsuario(Usuario usuario) {
        if (usuario.getTipo() == TipoUsuario.ADMINISTRADOR) {
            AlertaUtil.advertencia("No permitido", "No se puede eliminar al Administrador.");
            return;
        }
        if (!AlertaUtil.confirmar("Eliminar usuario",
                "¿Desea eliminar al usuario " + usuario.getUsername() + "?")) return;
        try {
            service.eliminar(usuario.getId());
            cargarDatos();
            AlertaUtil.info("Éxito", "Usuario eliminado correctamente.");
        } catch (SQLException e) {
            AlertaUtil.error("Error", "No se pudo eliminar el usuario: " + e.getMessage());
        }
    }

    private void mostrarFormulario(Usuario usuarioEditar) {
        try {
            Stage owner = (Stage) tablaUsuarios.getScene().getWindow();
            co.edu.uniquindio.cup.world_app.controller.form.UsuarioFormController ctrl =
                    co.edu.uniquindio.cup.world_app.controller.form.FormBaseController.abrir(
                            "/co/edu/uniquindio/cup/world_app/view/form/usuario-form.fxml",
                            usuarioEditar == null ? "Nuevo Usuario" : "Editar Usuario",
                            owner);
            if (usuarioEditar != null) ctrl.cargarUsuario(usuarioEditar);
            ctrl.mostrarYEsperar();
            if (ctrl.isGuardado()) {
                cargarDatos();
                AlertaUtil.info("Éxito", "Usuario guardado correctamente.");
            }
        } catch (Exception e) {
            AlertaUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }
}
