package co.edu.uniquindio.cup.world_app.model;

/**
 * Representa una confederación de fútbol (UEFA, CONMEBOL, etc.)
 */
public class Confederacion {

    private int id;
    private String nombre;
    private String sigla;

    public Confederacion() {}

    public Confederacion(int id, String nombre, String sigla) {
        this.id = id;
        this.nombre = nombre;
        this.sigla = sigla;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    @Override
    public String toString() { return sigla + " - " + nombre; }
}
