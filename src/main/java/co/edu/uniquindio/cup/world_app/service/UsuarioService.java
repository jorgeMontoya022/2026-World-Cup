package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.model.Usuario;
import co.edu.uniquindio.cup.world_app.repository.UsuarioRepository;
import co.edu.uniquindio.cup.world_app.util.SeguridadUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de usuarios.
 * Solo el Administrador puede crear, editar y eliminar usuarios.
 */
public class UsuarioService {

    private final UsuarioRepository repo = new UsuarioRepository();

    public List<Usuario> listarTodos() throws SQLException {
        return repo.listarTodos();
    }

    public List<Usuario> listarPorTipo(TipoUsuario tipo) throws SQLException {
        return repo.listarPorTipo(tipo);
    }

    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        return repo.buscarPorId(id);
    }

    /**
     * Crea un nuevo usuario. Valida que el username no exista y que
     * no se intente crear un segundo administrador.
     */
    public void crear(String username, String password, String nombreCompleto,
                      TipoUsuario tipo) throws SQLException, IllegalArgumentException {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        if (nombreCompleto == null || nombreCompleto.isBlank())
            throw new IllegalArgumentException("El nombre completo es obligatorio.");

        if (repo.buscarPorUsername(username.trim()).isPresent())
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre de usuario.");

        if (tipo == TipoUsuario.ADMINISTRADOR && repo.contarAdministradores() >= 1)
            throw new IllegalArgumentException("Solo puede existir un usuario Administrador.");

        Usuario u = new Usuario();
        u.setUsername(username.trim());
        u.setPasswordHash(SeguridadUtil.hashSHA256(password));
        u.setNombreCompleto(nombreCompleto.trim());
        u.setTipo(tipo);
        u.setActivo(true);
        repo.insertar(u);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * Si password es null o vacío, no se cambia la contraseña.
     */
    public void actualizar(int id, String username, String password,
                           String nombreCompleto, TipoUsuario tipo,
                           boolean activo) throws SQLException, IllegalArgumentException {

        Optional<Usuario> opt = repo.buscarPorId(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado.");

        Usuario u = opt.get();

        // Verificar username único (excluyendo el propio)
        Optional<Usuario> existente = repo.buscarPorUsername(username.trim());
        if (existente.isPresent() && existente.get().getId() != id)
            throw new IllegalArgumentException("Ya existe otro usuario con ese nombre de usuario.");

        u.setUsername(username.trim());
        u.setNombreCompleto(nombreCompleto.trim());
        u.setTipo(tipo);
        u.setActivo(activo);

        if (password != null && !password.isBlank()) {
            if (password.length() < 6)
                throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
            u.setPasswordHash(SeguridadUtil.hashSHA256(password));
        }

        repo.actualizar(u);
    }

    public void eliminar(int id) throws SQLException {
        repo.eliminar(id);
    }
}
