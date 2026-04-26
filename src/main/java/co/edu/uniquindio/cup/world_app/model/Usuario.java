package co.edu.uniquindio.cup.world_app.model;

import java.time.LocalDateTime;

/**
 * Representa un usuario del sistema de gestión del Mundial 2026.
 */
public class Usuario {

    private int id;
    private String username;
    private String passwordHash;   // SHA-256 almacenado en BD
    private String nombreCompleto;
    private TipoUsuario tipo;
    private boolean activo;
    private LocalDateTime ultimoAcceso;

    public Usuario() {}

    public Usuario(int id, String username, String passwordHash,
                   String nombreCompleto, TipoUsuario tipo, boolean activo,
                   LocalDateTime ultimoAcceso) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.tipo = tipo;
        this.activo = activo;
        this.ultimoAcceso = ultimoAcceso;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    @Override
    public String toString() { return username + " (" + tipo + ")"; }
}
