package co.edu.uniquindio.cup.world_app.model;

/**
 * Tipos de usuario del sistema.
 */
public enum TipoUsuario {
    ADMINISTRADOR("Administrador"),
    TRADICIONAL("Tradicional"),
    ESPORADICO("Esporádico");

    private final String etiqueta;

    TipoUsuario(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() { return etiqueta; }

    @Override
    public String toString() { return etiqueta; }
}
