package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Partido;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Partido.
 */
public class PartidoRepository {

    private Partido mapear(ResultSet rs) throws SQLException {
        Partido p = new Partido();
        p.setId(rs.getInt("id"));
        p.setEquipoLocalId(rs.getInt("equipo_local_id"));
        p.setEquipoLocalNombre(rs.getString("equipo_local"));
        p.setEquipoVisitanteId(rs.getInt("equipo_visitante_id"));
        p.setEquipoVisitanteNombre(rs.getString("equipo_visitante"));
        p.setEstadioId(rs.getInt("estadio_id"));
        p.setEstadioNombre(rs.getString("estadio_nombre"));
        p.setEstadioPais(rs.getString("estadio_pais"));
        p.setGrupoId(rs.getInt("grupo_id"));
        p.setGrupoNombre(rs.getString("grupo_nombre"));
        Timestamp ts = rs.getTimestamp("fecha_hora");
        if (ts != null) p.setFechaHora(ts.toLocalDateTime());
        int golesL = rs.getInt("goles_local");
        if (!rs.wasNull()) p.setGolesLocal(golesL);
        int golesV = rs.getInt("goles_visitante");
        if (!rs.wasNull()) p.setGolesVisitante(golesV);
        return p;
    }

    private static final String BASE_SELECT = """
            SELECT p.id,
                   p.equipo_local_id,  el.pais AS equipo_local,
                   p.equipo_visitante_id, ev.pais AS equipo_visitante,
                   p.estadio_id, est.nombre AS estadio_nombre, c.pais AS estadio_pais,
                   p.grupo_id, g.nombre AS grupo_nombre,
                   p.fecha_hora, p.goles_local, p.goles_visitante
            FROM partidos p
            JOIN equipos el  ON p.equipo_local_id = el.id
            JOIN equipos ev  ON p.equipo_visitante_id = ev.id
            JOIN estadios est ON p.estadio_id = est.id
            JOIN ciudades c   ON est.ciudad_id = c.id
            JOIN grupos g     ON p.grupo_id = g.id
            """;

    public List<Partido> listarTodos() throws SQLException {
        List<Partido> lista = new ArrayList<>();
        String sql = BASE_SELECT + " ORDER BY p.fecha_hora";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    /** Consulta: partidos en un estadio específico. */
    public List<Partido> listarPorEstadio(int estadioId) throws SQLException {
        List<Partido> lista = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE p.estadio_id = ? ORDER BY p.fecha_hora";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estadioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Reporte R4: partidos por país anfitrión. */
    public List<Partido> listarPorPaisAnfitrion(String pais) throws SQLException {
        List<Partido> lista = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE c.pais = ? ORDER BY p.fecha_hora";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pais);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Partido> listarPorGrupo(int grupoId) throws SQLException {
        List<Partido> lista = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE p.grupo_id = ? ORDER BY p.fecha_hora";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, grupoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void insertar(Partido p) throws SQLException {
        String sql = "INSERT INTO partidos (equipo_local_id, equipo_visitante_id, estadio_id, grupo_id, fecha_hora) VALUES (?,?,?,?,?)";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getEquipoLocalId());
            ps.setInt(2, p.getEquipoVisitanteId());
            ps.setInt(3, p.getEstadioId());
            ps.setInt(4, p.getGrupoId());
            ps.setTimestamp(5, p.getFechaHora() != null ? Timestamp.valueOf(p.getFechaHora()) : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Partido p) throws SQLException {
        String sql = "UPDATE partidos SET equipo_local_id=?, equipo_visitante_id=?, estadio_id=?, grupo_id=?, fecha_hora=?, goles_local=?, goles_visitante=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getEquipoLocalId());
            ps.setInt(2, p.getEquipoVisitanteId());
            ps.setInt(3, p.getEstadioId());
            ps.setInt(4, p.getGrupoId());
            ps.setTimestamp(5, p.getFechaHora() != null ? Timestamp.valueOf(p.getFechaHora()) : null);
            if (p.getGolesLocal() != null) ps.setInt(6, p.getGolesLocal()); else ps.setNull(6, Types.INTEGER);
            if (p.getGolesVisitante() != null) ps.setInt(7, p.getGolesVisitante()); else ps.setNull(7, Types.INTEGER);
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM partidos WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
