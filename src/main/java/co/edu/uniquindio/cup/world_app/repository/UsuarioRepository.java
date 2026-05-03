package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.TipoUsuario;
import co.edu.uniquindio.cup.world_app.model.Usuario;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Usuario. Acceso directo a MySQL sin frameworks.
 */
public class UsuarioRepository {

    // ── Mapeo ResultSet → Usuario ──────────────────────────────────────────────

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));
        u.setActivo(rs.getBoolean("activo"));
        Timestamp ts = rs.getTimestamp("ultimo_acceso");
        if (ts != null) u.setUltimoAcceso(ts.toLocalDateTime());
        return u;
    }

    // ── Consultas ──────────────────────────────────────────────────────────────

    public Optional<Usuario> buscarPorUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY username";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Usuario> listarPorTipo(TipoUsuario tipo) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE tipo = ? ORDER BY username";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Mutaciones ─────────────────────────────────────────────────────────────

    public void insertar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password_hash, nombre_completo, tipo, activo) VALUES (?,?,?,?,?)";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getTipo().name());
            ps.setBoolean(5, u.isActivo());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET username=?, password_hash=?, nombre_completo=?, tipo=?, activo=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getTipo().name());
            ps.setBoolean(5, u.isActivo());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }

    public void actualizarUltimoAcceso(int usuarioId, LocalDateTime fechaHora) throws SQLException {
        String sql = "UPDATE usuarios SET ultimo_acceso=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fechaHora));
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection con = ConexionDB.getInstancia().getConexion()) {
            con.setAutoCommit(false);
            try {
                // Primero eliminar registros de bitácora del usuario
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM bitacora WHERE usuario_id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                // Luego eliminar el usuario
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM usuarios WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public int contarAdministradores() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE tipo='ADMINISTRADOR'";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}
