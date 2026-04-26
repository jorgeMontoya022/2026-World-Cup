package co.edu.uniquindio.cup.world_app.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Representa un jugador de uno de los 48 equipos del Mundial 2026.
 */
public class Jugador {

    private int id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String posicion;
    private int numeroCamiseta;
    private double peso;       // kg
    private double estatura;   // metros
    private double valor;      // millones de euros
    private int equipoId;
    private String equipoNombre;
    private String confederacionNombre;

    public Jugador() {}

    public Jugador(int id, String nombre, String apellido, LocalDate fechaNacimiento,
                   String posicion, int numeroCamiseta, double peso, double estatura,
                   double valor, int equipoId, String equipoNombre, String confederacionNombre) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.peso = peso;
        this.estatura = estatura;
        this.valor = valor;
        this.equipoId = equipoId;
        this.equipoNombre = equipoNombre;
        this.confederacionNombre = confederacionNombre;
    }

    // ── Métodos calculados ─────────────────────────────────────────────────────

    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public int getNumeroCamiseta() { return numeroCamiseta; }
    public void setNumeroCamiseta(int numeroCamiseta) { this.numeroCamiseta = numeroCamiseta; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getEstatura() { return estatura; }
    public void setEstatura(double estatura) { this.estatura = estatura; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    public String getEquipoNombre() { return equipoNombre; }
    public void setEquipoNombre(String equipoNombre) { this.equipoNombre = equipoNombre; }

    public String getConfederacionNombre() { return confederacionNombre; }
    public void setConfederacionNombre(String confederacionNombre) { this.confederacionNombre = confederacionNombre; }

    @Override
    public String toString() { return getNombreCompleto(); }
}
