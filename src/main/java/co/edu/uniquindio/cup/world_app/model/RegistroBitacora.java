package co.edu.uniquindio.cup.world_app.model;

import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Registro de ingreso/salida de un usuario en la bitácora del sistema.
 */
public class RegistroBitacora {

    private int id;
    private int usuarioId;
    private String usuarioUsername;
    private TipoUsuario usuarioTipo;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;

    public RegistroBitacora() {}

    public RegistroBitacora(int id, int usuarioId, String usuarioUsername,
                             TipoUsuario usuarioTipo, LocalDateTime fechaEntrada,
                             LocalDateTime fechaSalida) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioUsername = usuarioUsername;
        this.usuarioTipo = usuarioTipo;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    // ── Métodos calculados ─────────────────────────────────────────────────────

    /**
     * Calcula la duración de la sesión en formato "Xh Ym".
     */
    public String getDuracion() {
        if (fechaEntrada == null || fechaSalida == null) return "En sesión";
        Duration d = Duration.between(fechaEntrada, fechaSalida);
        long horas = d.toHours();
        long minutos = d.toMinutesPart();
        if (horas > 0) return horas + "h " + minutos + "m";
        return minutos + "m";
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioUsername() { return usuarioUsername; }
    public void setUsuarioUsername(String usuarioUsername) { this.usuarioUsername = usuarioUsername; }

    public TipoUsuario getUsuarioTipo() { return usuarioTipo; }
    public void setUsuarioTipo(TipoUsuario usuarioTipo) { this.usuarioTipo = usuarioTipo; }

    public LocalDateTime getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDateTime fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDateTime fechaSalida) { this.fechaSalida = fechaSalida; }
}
