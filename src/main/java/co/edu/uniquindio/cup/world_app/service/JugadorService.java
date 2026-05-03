package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.repository.JugadorRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de jugadores.
 */
public class JugadorService {

    private final JugadorRepository repo = new JugadorRepository();

    public List<Jugador> listarTodos() throws SQLException {
        return repo.listarTodos();
    }

    public List<Jugador> buscar(String texto, Integer equipoId, String posicion) throws SQLException {
        return repo.buscar(texto, equipoId, posicion);
    }

    public Optional<Jugador> buscarPorId(int id) throws SQLException {
        return repo.buscarPorId(id);
    }

    public void crear(Jugador j) throws SQLException, IllegalArgumentException {
        validar(j);
        repo.insertar(j);
    }

    public void actualizar(Jugador j) throws SQLException, IllegalArgumentException {
        validar(j);
        repo.actualizar(j);
    }

    public void eliminar(int id) throws SQLException {
        repo.eliminar(id);
    }

    public List<Jugador> jugadorMasCostosoPorConfederacion() throws SQLException {
        return repo.jugadorMasCostosoPorConfederacion();
    }

    public List<Object[]> jugadoresMenores21PorEquipo() throws SQLException {
        return repo.jugadoresMenores21PorEquipo();
    }

    public List<Jugador> filtrarPorPesoEstaturaEquipo(double pesoMin, double pesoMax,
                                                       double estaturaMin, double estaturaMax,
                                                       Integer equipoId) throws SQLException {
        return repo.filtrarPorPesoEstaturaEquipo(pesoMin, pesoMax, estaturaMin, estaturaMax, equipoId);
    }

    public List<Object[]> valorTotalPorEquipoEnConfederacion(int confederacionId) throws SQLException {
        return repo.valorTotalPorEquipoEnConfederacion(confederacionId);
    }

    private void validar(Jugador j) {
        if (j.getNombre() == null || j.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del jugador es obligatorio.");
        if (j.getApellido() == null || j.getApellido().isBlank())
            throw new IllegalArgumentException("El apellido del jugador es obligatorio.");
        if (j.getEquipoId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un equipo.");
        if (j.getPeso() <= 0)
            throw new IllegalArgumentException("El peso debe ser mayor a 0.");
        if (j.getEstatura() <= 0)
            throw new IllegalArgumentException("La estatura debe ser mayor a 0.");
    }
}
