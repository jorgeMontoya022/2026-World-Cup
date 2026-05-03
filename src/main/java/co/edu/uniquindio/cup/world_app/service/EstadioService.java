package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.Estadio;
import co.edu.uniquindio.cup.world_app.repository.EstadioRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de estadios.
 */
public class EstadioService {

    private final EstadioRepository repo = new EstadioRepository();

    public List<Estadio> listarTodos() throws SQLException {
        return repo.listarTodos();
    }

    public List<Estadio> buscar(String texto, String pais) throws SQLException {
        return repo.buscar(texto, pais);
    }

    public Optional<Estadio> buscarPorId(int id) throws SQLException {
        return repo.buscarPorId(id);
    }

    public void crear(Estadio e) throws SQLException, IllegalArgumentException {
        validar(e);
        repo.insertar(e);
    }

    public void actualizar(Estadio e) throws SQLException, IllegalArgumentException {
        validar(e);
        repo.actualizar(e);
    }

    public void eliminar(int id) throws SQLException {
        repo.eliminar(id);
    }

    private void validar(Estadio e) {
        if (e.getNombre() == null || e.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del estadio es obligatorio.");
        if (e.getCiudadId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar una ciudad.");
        if (e.getCapacidad() <= 0)
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
    }
}
