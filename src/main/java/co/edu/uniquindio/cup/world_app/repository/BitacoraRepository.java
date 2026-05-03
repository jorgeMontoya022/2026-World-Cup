package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.RegistroBitacora;
import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la bitácora de accesos del sistema.
 */
public class BitacoraRepository {

    private RegistroBitacora mapear(ResultSet rs) throws SQLException {
        RegistroBitacora r = new RegistroBitacora();
        r.setId(rs.getInt("id"));
        r.setUsuarioId(rs.getInt("usuario_id"));
        r.setUsuarioUsername(rs.getString("username"));
        r.setUsuarioTipo(TipoUsuario.valueOf(rs.getString("tipo")));
        Timestamp entrada = rs.getTimestamp("fecha_entrada");
        if (entrada != null) r.setFechaEntrada(entrada.toLocalDateTime());
        Timestamp salida = rs.getTimestamp("fecha_salida");
        if (salida != null) r.setFechaSalida(salida.toLocalDateTime());
        return r;
    }

    /** Registra el ingreso de un usuario y retorna el ID del registro. */
    public int registrarIngreso(int usuarioId, LocalDateTime fechaEntrada) throws SQLException {
        String sql = "INSERT INTO bitacora (usuario_id, fecha_entrada) VALUES (?,?)";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, usuarioId);
            ps.setTimestamp(2, Timestamp.valueOf(fechaEntrada));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    /** Registra la salida del usuario en el registro abierto. */
    public void registrarSalida(int bitacoraId, LocalDateTime fechaSalida) throws SQLException {
        String sql = "UPDATE bitacora SET fecha_salida=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fechaSalida));
            ps.setInt(2, bitacoraId);
            ps.executeUpdate();
        }
    }

    public List<RegistroBitacora> listarTodos() throws SQLException {
        List<RegistroBitacora> lista = new ArrayList<>();
        String sql = """
                SELECT b.id, b.usuario_id, u.username, u.tipo,
                       b.fecha_entrada, b.fecha_salida
                FROM bitacora b
                JOIN usuarios u ON b.usuario_id = u.id
                ORDER BY b.fecha_entrada DESC
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<RegistroBitacora> listarPorRango(LocalDate desde, LocalDate hasta) throws SQLException {
        List<RegistroBitacora> lista = new ArrayList<>();
        String sql = """
                SELECT b.id, b.usuario_id, u.username, u.tipo,
                       b.fecha_entrada, b.fecha_salida
                FROM bitacora b
                JOIN usuarios u ON b.usuario_id = u.id
                WHERE DATE(b.fecha_entrada) BETWEEN ? AND ?
                ORDER BY b.fecha_entrada DESC
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Reporte R1: usuarios que ingresaron y salieron en un rango de fecha y hora. */
    public List<RegistroBitacora> listarPorRangoFechaHora(LocalDateTime desde,
                                                           LocalDateTime hasta) throws SQLException {
        List<RegistroBitacora> lista = new ArrayList<>();
        String sql = """
                SELECT b.id, b.usuario_id, u.username, u.tipo,
                       b.fecha_entrada, b.fecha_salida
                FROM bitacora b
                JOIN usuarios u ON b.usuario_id = u.id
                WHERE b.fecha_entrada >= ? AND b.fecha_salida <= ?
                ORDER BY b.fecha_entrada DESC
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(desde));
            ps.setTimestamp(2, Timestamp.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }
}
