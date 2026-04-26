package co.edu.uniquindio.cup.world_app.model;

import java.time.LocalDateTime;

/**
 * Representa un partido de la fase de grupos del Mundial 2026.
 */
public class Partido {

    private int id;
    private int equipoLocalId;
    private String equipoLocalNombre;
    private int equipoVisitanteId;
    private String equipoVisitanteNombre;
    private int estadioId;
    private String estadioNombre;
    private String estadioPais;
    private int grupoId;
    private String grupoNombre;
    private LocalDateTime fechaHora;
    private Integer golesLocal;
    private Integer golesVisitante;

    public Partido() {}

    // ── Métodos calculados ─────────────────────────────────────────────────────

    public String getDescripcion() {
        return equipoLocalNombre + " vs " + equipoVisitanteNombre;
    }

    public String getResultado() {
        if (golesLocal == null || golesVisitante == null) return "Por jugar";
        return golesLocal + " - " + golesVisitante;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEquipoLocalId() { return equipoLocalId; }
    public void setEquipoLocalId(int equipoLocalId) { this.equipoLocalId = equipoLocalId; }

    public String getEquipoLocalNombre() { return equipoLocalNombre; }
    public void setEquipoLocalNombre(String equipoLocalNombre) { this.equipoLocalNombre = equipoLocalNombre; }

    public int getEquipoVisitanteId() { return equipoVisitanteId; }
    public void setEquipoVisitanteId(int equipoVisitanteId) { this.equipoVisitanteId = equipoVisitanteId; }

    public String getEquipoVisitanteNombre() { return equipoVisitanteNombre; }
    public void setEquipoVisitanteNombre(String equipoVisitanteNombre) { this.equipoVisitanteNombre = equipoVisitanteNombre; }

    public int getEstadioId() { return estadioId; }
    public void setEstadioId(int estadioId) { this.estadioId = estadioId; }

    public String getEstadioNombre() { return estadioNombre; }
    public void setEstadioNombre(String estadioNombre) { this.estadioNombre = estadioNombre; }

    public String getEstadioPais() { return estadioPais; }
    public void setEstadioPais(String estadioPais) { this.estadioPais = estadioPais; }

    public int getGrupoId() { return grupoId; }
    public void setGrupoId(int grupoId) { this.grupoId = grupoId; }

    public String getGrupoNombre() { return grupoNombre; }
    public void setGrupoNombre(String grupoNombre) { this.grupoNombre = grupoNombre; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }

    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }

    @Override
    public String toString() { return getDescripcion(); }
}
