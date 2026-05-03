package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Jugador.
 */
public class JugadorRepository {

    private Jugador mapear(ResultSet rs) throws SQLException {
        Jugador j = new Jugador();
        j.setId(rs.getInt("id"));
        j.setNombre(rs.getString("nombre"));
        j.setApellido(rs.getString("apellido"));
        Date fn = rs.getDate("fecha_nacimiento");
        if (fn != null) j.setFechaNacimiento(fn.toLocalDate());
        j.setPosicion(rs.getString("posicion"));
        j.setNumeroCamiseta(rs.getInt("numero_camiseta"));
        j.setPeso(rs.getDouble("peso"));
        j.setEstatura(rs.getDouble("estatura"));
        j.setValor(rs.getDouble("valor"));
        j.setEquipoId(rs.getInt("equipo_id"));
        j.setEquipoNombre(rs.getString("equipo_nombre"));
        j.setConfederacionNombre(rs.getString("confederacion_nombre"));
        return j;
    }

    private static final String BASE_SELECT = """
            SELECT j.id, j.nombre, j.apellido, j.fecha_nacimiento,
                   j.posicion, j.numero_camiseta, j.peso, j.estatura,
                   j.valor, j.equipo_id,
                   e.pais AS equipo_nombre,
                   c.nombre AS confederacion_nombre
            FROM jugadores j
            JOIN equipos e ON j.equipo_id = e.id
            JOIN confederaciones c ON e.confederacion_id = c.id
            """;

    public List<Jugador> listarTodos() throws SQLException {
        List<Jugador> lista = new ArrayList<>();
        String sql = BASE_SELECT + " ORDER BY j.apellido, j.nombre";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Jugador> buscar(String texto, Integer equipoId, String posicion) throws SQLException {
        List<Jugador> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT + " WHERE 1=1");
        if (texto != null && !texto.isBlank())
            sql.append(" AND (j.nombre LIKE ? OR j.apellido LIKE ? OR e.pais LIKE ?)");
        if (equipoId != null) sql.append(" AND j.equipo_id = ?");
        if (posicion != null && !posicion.isBlank()) sql.append(" AND j.posicion = ?");
        sql.append(" ORDER BY j.apellido, j.nombre");

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (texto != null && !texto.isBlank()) {
                String like = "%" + texto + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (equipoId != null) ps.setInt(idx++, equipoId);
            if (posicion != null && !posicion.isBlank()) ps.setString(idx, posicion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Optional<Jugador> buscarPorId(int id) throws SQLException {
        String sql = BASE_SELECT + " WHERE j.id = ?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public void insertar(Jugador j) throws SQLException {
        String sql = "INSERT INTO jugadores (nombre, apellido, fecha_nacimiento, posicion, numero_camiseta, peso, estatura, valor, equipo_id) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getApellido());
            ps.setDate(3, j.getFechaNacimiento() != null ? Date.valueOf(j.getFechaNacimiento()) : null);
            ps.setString(4, j.getPosicion());
            ps.setInt(5, j.getNumeroCamiseta());
            ps.setDouble(6, j.getPeso());
            ps.setDouble(7, j.getEstatura());
            ps.setDouble(8, j.getValor());
            ps.setInt(9, j.getEquipoId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) j.setId(keys.getInt(1));
            }
        }
    }

    public void actualizar(Jugador j) throws SQLException {
        String sql = "UPDATE jugadores SET nombre=?, apellido=?, fecha_nacimiento=?, posicion=?, numero_camiseta=?, peso=?, estatura=?, valor=?, equipo_id=? WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getApellido());
            ps.setDate(3, j.getFechaNacimiento() != null ? Date.valueOf(j.getFechaNacimiento()) : null);
            ps.setString(4, j.getPosicion());
            ps.setInt(5, j.getNumeroCamiseta());
            ps.setDouble(6, j.getPeso());
            ps.setDouble(7, j.getEstatura());
            ps.setDouble(8, j.getValor());
            ps.setInt(9, j.getEquipoId());
            ps.setInt(10, j.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM jugadores WHERE id=?";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Consulta: jugador más costoso por confederación. */
    public List<Jugador> jugadorMasCostosoPorConfederacion() throws SQLException {
        List<Jugador> lista = new ArrayList<>();
        String sql = """
                SELECT j.id, j.nombre, j.apellido, j.fecha_nacimiento,
                       j.posicion, j.numero_camiseta, j.peso, j.estatura,
                       j.valor, j.equipo_id,
                       e.pais AS equipo_nombre,
                       c.nombre AS confederacion_nombre
                FROM jugadores j
                JOIN equipos e ON j.equipo_id = e.id
                JOIN confederaciones c ON e.confederacion_id = c.id
                WHERE j.valor = (
                    SELECT MAX(j2.valor)
                    FROM jugadores j2
                    JOIN equipos e2 ON j2.equipo_id = e2.id
                    WHERE e2.confederacion_id = e.confederacion_id
                )
                ORDER BY c.nombre
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    /** Consulta: cantidad de jugadores menores de 21 años por equipo. */
    public List<Object[]> jugadoresMenores21PorEquipo() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
                SELECT e.pais, COUNT(*) AS cantidad
                FROM jugadores j
                JOIN equipos e ON j.equipo_id = e.id
                WHERE TIMESTAMPDIFF(YEAR, j.fecha_nacimiento, CURDATE()) < 21
                GROUP BY e.id, e.pais
                ORDER BY cantidad DESC, e.pais
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getString("pais"), rs.getInt("cantidad")});
            }
        }
        return lista;
    }

    /** Reporte R2: jugadores filtrados por peso, estatura y equipo. */
    public List<Jugador> filtrarPorPesoEstaturaEquipo(double pesoMin, double pesoMax,
                                                       double estaturaMin, double estaturaMax,
                                                       Integer equipoId) throws SQLException {
        List<Jugador> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        sql.append(" WHERE j.peso BETWEEN ? AND ? AND j.estatura BETWEEN ? AND ?");
        if (equipoId != null) sql.append(" AND j.equipo_id = ?");
        sql.append(" ORDER BY j.apellido, j.nombre");

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setDouble(1, pesoMin);
            ps.setDouble(2, pesoMax);
            ps.setDouble(3, estaturaMin);
            ps.setDouble(4, estaturaMax);
            if (equipoId != null) ps.setInt(5, equipoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Reporte R3: valor total de jugadores por equipo en una confederación. */
    public List<Object[]> valorTotalPorEquipoEnConfederacion(int confederacionId) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
                SELECT e.pais, SUM(j.valor) AS valor_total
                FROM jugadores j
                JOIN equipos e ON j.equipo_id = e.id
                WHERE e.confederacion_id = ?
                GROUP BY e.id, e.pais
                ORDER BY valor_total DESC
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, confederacionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{rs.getString("pais"), rs.getDouble("valor_total")});
                }
            }
        }
        return lista;
    }
}
