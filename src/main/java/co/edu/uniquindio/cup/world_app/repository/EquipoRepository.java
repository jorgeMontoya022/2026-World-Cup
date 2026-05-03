package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Equipo;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Equipo.
 */
public class EquipoRepository {

    private Equipo mapear(ResultSet rs) throws SQLException {
        Equipo e = new Equipo();
        e.setId(rs.getInt("id"));
        e.setPais(rs.getString("pais"));
        e.setBandera(rs.getString("bandera"));
        e.setConfederacionId(rs.getInt("confederacion_id"));
        e.setConfederacionNombre(rs.getString("confederacion_nombre"));
        e.setGrupoId(rs.getInt("grupo_id"));
        e.setGrupoNombre(rs.getString("grupo_nombre"));
        e.setValorPlantilla(rs.getDouble("valor_plantilla"));
        e.setTecnicoNombre(rs.getString("tecnico_nombre")); // puede ser null
        return e;
    }

    /** SELECT base reutilizable con LEFT JOIN a técnicos */
    private static final String BASE_SELECT = """
            SELECT e.id, e.pais, e.bandera, e.confederacion_id,
                   c.nombre  AS confederacion_nombre,
                   e.grupo_id, g.nombre AS grupo_nombre,
                   e.valor_plantilla,
                   CASE WHEN t.id IS NOT NULL
                        THEN CONCAT(t.nombre, ' ', t.apellido)
                        ELSE NULL
                   END AS tecnico_nombre
            FROM equipos e
            JOIN confederaciones c ON e.confederacion_id = c.id
            JOIN grupos g          ON e.grupo_id = g.id
            LEFT JOIN tecnicos t   ON t.equipo_id = e.id
            """;

    public List<Equipo> listarTodos() throws SQLException {
        List<Equipo> lista = new ArrayList<>();
        String sql = BASE_SELECT + " ORDER BY e.pais";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Equipo> buscar(String texto, Integer confederacionId) throws SQLException {
        List<Equipo> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT + " WHERE 1=1");
        if (texto != null && !texto.isBlank()) sql.append(" AND e.pais LIKE ?");
        if (confederacionId != null) sql.append(" AND e.confederacion_id = ?");
        sql.append(" ORDER BY e.pais");

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (texto != null && !texto.isBlank()) ps.setString(idx++, "%" + texto + "%");
            if (confederacionId != null) ps.setInt(idx, confederacionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Optional<Equipo> buscarPorId(int id) throws SQLException {
        String sql = BASE_SELECT + " WHERE e.id = ?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public void insertar(Equipo e) throws SQLException {
        String sql = "INSERT INTO equipos (pais, bandera, confederacion_id, grupo_id, valor_plantilla) VALUES (?,?,?,?,?)";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getPais());
            ps.setString(2, e.getBandera());
            ps.setInt(3, e.getConfederacionId());
            ps.setInt(4, e.getGrupoId());
            ps.setDouble(5, e.getValorPlantilla());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Equipo e) throws SQLException {
        String sql = "UPDATE equipos SET pais=?, bandera=?, confederacion_id=?, grupo_id=?, valor_plantilla=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getPais());
            ps.setString(2, e.getBandera());
            ps.setInt(3, e.getConfederacionId());
            ps.setInt(4, e.getGrupoId());
            ps.setDouble(5, e.getValorPlantilla());
            ps.setInt(6, e.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM equipos WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Consulta: equipo más costoso por país sede (México, USA, Canadá). */
    public List<Equipo> equipoMasCostosoPorPaisSede() throws SQLException {
        List<Equipo> lista = new ArrayList<>();
        String sql = """
                SELECT e.id, e.pais, e.bandera, e.confederacion_id,
                       c.nombre AS confederacion_nombre,
                       e.grupo_id, g.nombre AS grupo_nombre,
                       e.valor_plantilla,
                       CASE WHEN t.id IS NOT NULL
                            THEN CONCAT(t.nombre, ' ', t.apellido)
                            ELSE NULL
                       END AS tecnico_nombre
                FROM equipos e
                JOIN confederaciones c  ON e.confederacion_id = c.id
                JOIN grupos g           ON e.grupo_id = g.id
                LEFT JOIN tecnicos t    ON t.equipo_id = e.id
                JOIN partidos p         ON (p.equipo_local_id = e.id OR p.equipo_visitante_id = e.id)
                JOIN estadios est       ON p.estadio_id = est.id
                JOIN ciudades ciu       ON est.ciudad_id = ciu.id
                WHERE ciu.pais IN ('México','USA','Canadá')
                  AND e.valor_plantilla = (
                      SELECT MAX(e2.valor_plantilla)
                      FROM equipos e2
                      JOIN partidos p2   ON (p2.equipo_local_id = e2.id OR p2.equipo_visitante_id = e2.id)
                      JOIN estadios est2 ON p2.estadio_id = est2.id
                      JOIN ciudades ciu2 ON est2.ciudad_id = ciu2.id
                      WHERE ciu2.pais = ciu.pais
                  )
                GROUP BY ciu.pais, e.id
                ORDER BY ciu.pais
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
}
