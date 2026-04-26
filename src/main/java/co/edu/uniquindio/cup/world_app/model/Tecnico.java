package co.edu.uniquindio.cup.world_app.model;

/**
 * Representa el director técnico de un equipo participante.
 */
public class Tecnico {

    private int id;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private int equipoId;
    private String equipoNombre;
    private int titulosGanados;

    public Tecnico() {}

    public Tecnico(int id, String nombre, String apellido, String nacionalidad,
                   int equipoId, String equipoNombre, int titulosGanados) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.equipoId = equipoId;
        this.equipoNombre = equipoNombre;
        this.titulosGanados = titulosGanados;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    public String getEquipoNombre() { return equipoNombre; }
    public void setEquipoNombre(String equipoNombre) { this.equipoNombre = equipoNombre; }

    public int getTitulosGanados() { return titulosGanados; }
    public void setTitulosGanados(int titulosGanados) { this.titulosGanados = titulosGanados; }

    @Override
    public String toString() { return getNombreCompleto(); }
}
