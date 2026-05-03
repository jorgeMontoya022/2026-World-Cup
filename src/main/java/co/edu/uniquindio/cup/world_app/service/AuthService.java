package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.model.Usuario;
import co.edu.uniquindio.cup.world_app.repository.BitacoraRepository;
import co.edu.uniquindio.cup.world_app.repository.UsuarioRepository;
import co.edu.uniquindio.cup.world_app.util.SeguridadUtil;
import co.edu.uniquindio.cup.world_app.util.SessionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio de autenticación y gestión de sesión.
 */
public class AuthService {

    private final UsuarioRepository usuarioRepo = new UsuarioRepository();
    private final BitacoraRepository bitacoraRepo = new BitacoraRepository();

    /**
     * Intenta autenticar al usuario con las credenciales dadas.
     *
     * @param username nombre de usuario
     * @param password contraseña en texto plano
     * @param tipoEsperado tipo de usuario seleccionado en el login
     * @return el Usuario autenticado
     * @throws AuthException si las credenciales son inválidas o el tipo no coincide
     * @throws SQLException  si hay error de base de datos
     */
    public Usuario login(String username, String password, TipoUsuario tipoEsperado)
            throws AuthException, SQLException {

        if (username == null || username.isBlank())
            throw new AuthException("El usuario no puede estar vacío.");
        if (password == null || password.isBlank())
            throw new AuthException("La contraseña no puede estar vacía.");

        Optional<Usuario> opt = usuarioRepo.buscarPorUsername(username.trim());
        if (opt.isEmpty())
            throw new AuthException("Usuario o contraseña incorrectos.");

        Usuario usuario = opt.get();

        if (!usuario.isActivo())
            throw new AuthException("El usuario está desactivado. Contacte al administrador.");

        if (!SeguridadUtil.verificar(password, usuario.getPasswordHash()))
            throw new AuthException("Usuario o contraseña incorrectos.");

        if (tipoEsperado != null && usuario.getTipo() != tipoEsperado)
            throw new AuthException("El tipo de usuario seleccionado no coincide con el registrado.");

        // Registrar ingreso en bitácora
        LocalDateTime ahora = LocalDateTime.now();
        int bitacoraId = bitacoraRepo.registrarIngreso(usuario.getId(), ahora);
        usuarioRepo.actualizarUltimoAcceso(usuario.getId(), ahora);
        usuario.setUltimoAcceso(ahora);

        SessionManager.getInstancia().iniciarSesion(usuario, bitacoraId);
        return usuario;
    }

    /**
     * Cierra la sesión del usuario actual y registra la salida en bitácora.
     */
    public void logout() throws SQLException {
        SessionManager session = SessionManager.getInstancia();
        if (session.estaSesionActiva()) {
            bitacoraRepo.registrarSalida(session.getBitacoraId(), LocalDateTime.now());
            session.cerrarSesion();
        }
    }

    /** Excepción de autenticación con mensaje  para el usuario. */
    public static class AuthException extends Exception {
        public AuthException(String mensaje) { super(mensaje); }
    }
}
