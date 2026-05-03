package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.RegistroBitacora;
import co.edu.uniquindio.cup.world_app.repository.BitacoraRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de negocio para la bitácora del sistema.
 */
public class BitacoraService {

    private final BitacoraRepository repo = new BitacoraRepository();

    public List<RegistroBitacora> listarTodos() throws SQLException {
        return repo.listarTodos();
    }

    public List<RegistroBitacora> listarPorRango(LocalDate desde, LocalDate hasta) throws SQLException {
        return repo.listarPorRango(desde, hasta);
    }

    public List<RegistroBitacora> listarPorRangoFechaHora(LocalDateTime desde,
                                                           LocalDateTime hasta) throws SQLException {
        return repo.listarPorRangoFechaHora(desde, hasta);
    }
}
