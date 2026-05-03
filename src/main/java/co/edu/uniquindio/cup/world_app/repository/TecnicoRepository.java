package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Tecnico;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Tecnico (Director Técnico).
 */
public class TecnicoRepository {

    private Tecnico mapear(ResultSet rs) throws SQLException {
        Tecnico t = new Tecnico();
        t.setId(rs.getInt("id"));
        t.setNombre(rs.getString("nombre"));
        t.setApellido(rs.getString("apellido"));
        t.setNacionalidad(rs.getString("nacionalidad"));
        t.setEquipoId(rs.getInt("equipo_id"));
        t.setEquipoNombre(rs.getString("equipo_nombre"));
        t.setTitulosGanados(rs.getInt("titulos_ganados"));
        return t;
    }

    public List<Tecnico> listarTodos() throws SQLException {
        List<Tecnico> lista = new ArrayList<>();
        String sql = """
                SELECT t.id, t.nombre, t.apellido, t.nacionalidad,
                       t.equipo_id, e.pais AS equipo_nombre, t.titulos_ganados
                FROM tecnicos t
                JOIN equipos e ON t.equipo_id = e.id
                ORDER BY t.apellido, t.nombre
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Tecnico> buscar(String texto) throws SQLException {
        List<Tecnico> lista = new ArrayList<>();
        String sql = """
                SELECT t.id, t.nombre, t.apellido, t.nacionalidad,
                       t.equipo_id, e.pais AS equipo_nombre, t.titulos_ganados
                FROM tecnicos t
                JOIN equipos e ON t.equipo_id = e.id
                WHERE t.nombre LIKE ? OR t.apellido LIKE ? OR e.pais LIKE ?
                ORDER BY t.apellido, t.nombre
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + texto + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Optional<Tecnico> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT t.id, t.nombre, t.apellido, t.nacionalidad,
                       t.equipo_id, e.pais AS equipo_nombre, t.titulos_ganados
                FROM tecnicos t
                JOIN equipos e ON t.equipo_id = e.id
                WHERE t.id = ?
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

    public void insertar(Tecnico t) throws SQLException {
        String sql = "INSERT INTO tecnicos (nombre, apellido, nacionalidad, equipo_id, titulos_ganados) VALUES (?,?,?,?,?)";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getApellido());
            ps.setString(3, t.getNacionalidad());
            ps.setInt(4, t.getEquipoId());
            ps.setInt(5, t.getTitulosGanados());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) t.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Tecnico t) throws SQLException {
        String sql = "UPDATE tecnicos SET nombre=?, apellido=?, nacionalidad=?, equipo_id=?, titulos_ganados=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getApellido());
            ps.setString(3, t.getNacionalidad());
            ps.setInt(4, t.getEquipoId());
            ps.setInt(5, t.getTitulosGanados());
            ps.setInt(6, t.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM tecnicos WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
