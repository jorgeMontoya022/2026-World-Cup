package co.edu.uniquindio.cup.world_app.model;

/**
 * Representa uno de los 12 grupos de la fase inicial del Mundial 2026.
 */
public class Grupo {

    private int id;
    private String nombre; // "A", "B", ... "L"

    public Grupo() {}

    public Grupo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return "Grupo " + nombre; }
}
