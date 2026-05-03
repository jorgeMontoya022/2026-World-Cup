package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Estadio;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Estadio.
 */
public class EstadioRepository {

    private Estadio mapear(ResultSet rs) throws SQLException {
        Estadio e = new Estadio();
        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("nombre"));
        e.setCiudadId(rs.getInt("ciudad_id"));
        e.setCiudadNombre(rs.getString("ciudad_nombre"));
        e.setPais(rs.getString("pais"));
        e.setCapacidad(rs.getInt("capacidad"));
        e.setPartidosAsignados(rs.getInt("partidos_asignados"));
        return e;
    }

    public List<Estadio> listarTodos() throws SQLException {
        List<Estadio> lista = new ArrayList<>();
        String sql = """
                SELECT est.id, est.nombre, est.ciudad_id,
                       c.nombre AS ciudad_nombre, c.pais,
                       est.capacidad,
                       (SELECT COUNT(*) FROM partidos p WHERE p.estadio_id = est.id) AS partidos_asignados
                FROM estadios est
                JOIN ciudades c ON est.ciudad_id = c.id
                ORDER BY c.pais, est.nombre
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Estadio> buscar(String texto, String pais) throws SQLException {
        List<Estadio> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT est.id, est.nombre, est.ciudad_id,
                       c.nombre AS ciudad_nombre, c.pais,
                       est.capacidad,
                       (SELECT COUNT(*) FROM partidos p WHERE p.estadio_id = est.id) AS partidos_asignados
                FROM estadios est
                JOIN ciudades c ON est.ciudad_id = c.id
                WHERE 1=1
                """);
        if (texto != null && !texto.isBlank()) sql.append(" AND est.nombre LIKE ?");
        if (pais != null && !pais.isBlank()) sql.append(" AND c.pais = ?");
        sql.append(" ORDER BY c.pais, est.nombre");

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (texto != null && !texto.isBlank()) ps.setString(idx++, "%" + texto + "%");
            if (pais != null && !pais.isBlank()) ps.setString(idx, pais);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Optional<Estadio> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT est.id, est.nombre, est.ciudad_id,
                       c.nombre AS ciudad_nombre, c.pais,
                       est.capacidad,
                       (SELECT COUNT(*) FROM partidos p WHERE p.estadio_id = est.id) AS partidos_asignados
                FROM estadios est
                JOIN ciudades c ON est.ciudad_id = c.id
                WHERE est.id = ?
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public void insertar(Estadio e) throws SQLException {
        String sql = "INSERT INTO estadios (nombre, ciudad_id, capacidad) VALUES (?,?,?)";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getCiudadId());
            ps.setInt(3, e.getCapacidad());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Estadio e) throws SQLException {
        String sql = "UPDATE estadios SET nombre=?, ciudad_id=?, capacidad=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getCiudadId());
            ps.setInt(3, e.getCapacidad());
            ps.setInt(4, e.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM estadios WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
