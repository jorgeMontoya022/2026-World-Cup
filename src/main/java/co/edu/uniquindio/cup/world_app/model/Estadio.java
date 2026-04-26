package co.edu.uniquindio.cup.world_app.model;

/**
 * Representa un estadio sede del Mundial 2026.
 */
public class Estadio {

    private int id;
    private String nombre;
    private int ciudadId;
    private String ciudadNombre;
    private String pais;
    private int capacidad;
    private int partidosAsignados;

    public Estadio() {}

    public Estadio(int id, String nombre, int ciudadId, String ciudadNombre,
                   String pais, int capacidad, int partidosAsignados) {
        this.id = id;
        this.nombre = nombre;
        this.ciudadId = ciudadId;
        this.ciudadNombre = ciudadNombre;
        this.pais = pais;
        this.capacidad = capacidad;
        this.partidosAsignados = partidosAsignados;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCiudadId() { return ciudadId; }
    public void setCiudadId(int ciudadId) { this.ciudadId = ciudadId; }

    public String getCiudadNombre() { return ciudadNombre; }
    public void setCiudadNombre(String ciudadNombre) { this.ciudadNombre = ciudadNombre; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public int getPartidosAsignados() { return partidosAsignados; }
    public void setPartidosAsignados(int partidosAsignados) { this.partidosAsignados = partidosAsignados; }

    @Override
    public String toString() { return nombre; }
}
