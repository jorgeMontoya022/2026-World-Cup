package co.edu.uniquindio.cup.world_app.model;

/**
 * Representa uno de los 48 equipos participantes del Mundial 2026.
 */
public class Equipo {

    private int id;
    private String pais;
    private String bandera;          // emoji o código de bandera
    private int confederacionId;
    private String confederacionNombre;
    private int grupoId;
    private String grupoNombre;
    private double valorPlantilla;   // en millones de euros
    private String tecnicoNombre;    // nombre completo del DT (puede ser null)

    public Equipo() {}

    public Equipo(int id, String pais, String bandera,
                  int confederacionId, String confederacionNombre,
                  int grupoId, String grupoNombre, double valorPlantilla) {
        this.id = id;
        this.pais = pais;
        this.bandera = bandera;
        this.confederacionId = confederacionId;
        this.confederacionNombre = confederacionNombre;
        this.grupoId = grupoId;
        this.grupoNombre = grupoNombre;
        this.valorPlantilla = valorPlantilla;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getBandera() { return bandera; }
    public void setBandera(String bandera) { this.bandera = bandera; }

    public int getConfederacionId() { return confederacionId; }
    public void setConfederacionId(int confederacionId) { this.confederacionId = confederacionId; }

    public String getConfederacionNombre() { return confederacionNombre; }
    public void setConfederacionNombre(String confederacionNombre) { this.confederacionNombre = confederacionNombre; }

    public int getGrupoId() { return grupoId; }
    public void setGrupoId(int grupoId) { this.grupoId = grupoId; }

    public String getGrupoNombre() { return grupoNombre; }
    public void setGrupoNombre(String grupoNombre) { this.grupoNombre = grupoNombre; }

    public double getValorPlantilla() { return valorPlantilla; }
    public void setValorPlantilla(double valorPlantilla) { this.valorPlantilla = valorPlantilla; }

    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }

    @Override
    public String toString() { return pais; }
}
