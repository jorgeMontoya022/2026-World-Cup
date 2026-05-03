package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.Partido;
import co.edu.uniquindio.cup.world_app.repository.PartidoRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de negocio para la gestión de partidos.
 */
public class PartidoService {

    private final PartidoRepository repo = new PartidoRepository();

    public List<Partido> listarTodos() throws SQLException {
        return repo.listarTodos();
    }

    public List<Partido> listarPorEstadio(int estadioId) throws SQLException {
        return repo.listarPorEstadio(estadioId);
    }

    public List<Partido> listarPorPaisAnfitrion(String pais) throws SQLException {
        return repo.listarPorPaisAnfitrion(pais);
    }

    public List<Partido> listarPorGrupo(int grupoId) throws SQLException {
        return repo.listarPorGrupo(grupoId);
    }

    public void crear(Partido p) throws SQLException, IllegalArgumentException {
        validar(p);
        repo.insertar(p);
    }

    public void actualizar(Partido p) throws SQLException, IllegalArgumentException {
        validar(p);
        repo.actualizar(p);
    }

    public void registrarResultado(int id, int golesLocal, int golesVisitante)
            throws SQLException, IllegalArgumentException {

        Partido p = repo.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado."));

        if (p.getFechaHora() == null)
            throw new IllegalArgumentException("El partido no tiene fecha asignada.");

        if (p.getFechaHora().isAfter(java.time.LocalDateTime.now()))
            throw new IllegalArgumentException(
                    "No se puede registrar el resultado: el partido aún no ha terminado.");

        if (golesLocal < 0 || golesVisitante < 0)
            throw new IllegalArgumentException("Los goles no pueden ser negativos.");

        p.setGolesLocal(golesLocal);
        p.setGolesVisitante(golesVisitante);
        repo.actualizarResultado(p);
    }

    public void eliminar(int id) throws SQLException {
        repo.eliminar(id);
    }

    private void validar(Partido p) {
        if (p.getEquipoLocalId() <= 0 || p.getEquipoVisitanteId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar ambos equipos.");
        if (p.getEquipoLocalId() == p.getEquipoVisitanteId())
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        if (p.getEstadioId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un estadio.");
        if (p.getGrupoId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un grupo.");
    }
}
