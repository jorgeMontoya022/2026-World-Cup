package co.edu.uniquindio.cup.world_app.util;

import co.edu.uniquindio.cup.world_app.model.Usuario;

/**
 * Singleton que mantiene la sesión activa del usuario durante la ejecución.
 */
public class SessionManager {

    private static SessionManager instancia;
    private Usuario usuarioActual;
    private int bitacoraId; // ID del registro de bitácora abierto

    private SessionManager() {}

    public static SessionManager getInstancia() {
        if (instancia == null) {
            instancia = new SessionManager();
        }
        return instancia;
    }

    public void iniciarSesion(Usuario usuario, int bitacoraId) {
        this.usuarioActual = usuario;
        this.bitacoraId = bitacoraId;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        this.bitacoraId = 0;
    }

    public Usuario getUsuarioActual() { return usuarioActual; }

    public int getBitacoraId() { return bitacoraId; }

    public boolean isAdmin() {
        return usuarioActual != null &&
               usuarioActual.getTipo() == co.edu.uniquindio.cup.world_app.model.TipoUsuario.ADMINISTRADOR;
    }

    public boolean puedeEscribir() {
        if (usuarioActual == null) return false;
        return switch (usuarioActual.getTipo()) {
            case ADMINISTRADOR, TRADICIONAL -> true;
            default -> false;
        };
    }

    public boolean estaSesionActiva() { return usuarioActual != null; }
}
