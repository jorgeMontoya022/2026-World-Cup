package co.edu.uniquindio.cup.world_app.model;

/**
 * Representa una ciudad sede del Mundial 2026.
 */
public class Ciudad {

    private int id;
    private String nombre;
    private String pais; // "México", "USA", "Canadá"

    public Ciudad() {}

    public Ciudad(int id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    @Override
    public String toString() { return nombre + " (" + pais + ")"; }
}
