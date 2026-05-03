package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.repository.EquipoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de equipos.
 */
public class EquipoService {

    private final EquipoRepository repo = new EquipoRepository();

    public List<Equipo> listarTodos() throws SQLException {
        return repo.listarTodos();
    }

    public List<Equipo> buscar(String texto, Integer confederacionId) throws SQLException {
        return repo.buscar(texto, confederacionId);
    }

    public Optional<Equipo> buscarPorId(int id) throws SQLException {
        return repo.buscarPorId(id);
    }

    public void crear(Equipo e) throws SQLException, IllegalArgumentException {
        validar(e);
        repo.insertar(e);
    }

    public void actualizar(Equipo e) throws SQLException, IllegalArgumentException {
        validar(e);
        repo.actualizar(e);
    }

    public void eliminar(int id) throws SQLException {
        repo.eliminar(id);
    }

    public List<Equipo> equipoMasCostosoPorPaisSede() throws SQLException {
        return repo.equipoMasCostosoPorPaisSede();
    }

    private void validar(Equipo e) {
        if (e.getPais() == null || e.getPais().isBlank())
            throw new IllegalArgumentException("El nombre del país es obligatorio.");
        if (e.getConfederacionId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar una confederación.");
        if (e.getGrupoId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un grupo.");
        if (e.getValorPlantilla() < 0)
            throw new IllegalArgumentException("El valor de la plantilla no puede ser negativo.");
    }
}
