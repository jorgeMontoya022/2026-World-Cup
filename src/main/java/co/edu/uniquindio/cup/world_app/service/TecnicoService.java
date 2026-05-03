package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.Tecnico;
import co.edu.uniquindio.cup.world_app.repository.TecnicoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de directores técnicos.
 */
public class TecnicoService {

    private final TecnicoRepository repo = new TecnicoRepository();

    public List<Tecnico> listarTodos() throws SQLException {
        return repo.listarTodos();
    }

    public List<Tecnico> buscar(String texto) throws SQLException {
        if (texto == null || texto.isBlank()) return listarTodos();
        return repo.buscar(texto);
    }

    public Optional<Tecnico> buscarPorId(int id) throws SQLException {
        return repo.buscarPorId(id);
    }

    public void crear(Tecnico t) throws SQLException, IllegalArgumentException {
        validar(t);
        repo.insertar(t);
    }

    public void actualizar(Tecnico t) throws SQLException, IllegalArgumentException {
        validar(t);
        repo.actualizar(t);
    }

    public void eliminar(int id) throws SQLException {
        repo.eliminar(id);
    }

    private void validar(Tecnico t) {
        if (t.getNombre() == null || t.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del técnico es obligatorio.");
        if (t.getApellido() == null || t.getApellido().isBlank())
            throw new IllegalArgumentException("El apellido del técnico es obligatorio.");
        if (t.getEquipoId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un equipo.");
    }
}
