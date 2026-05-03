package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Confederacion;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Confederacion.
 */
public class ConfederacionRepository {

    private Confederacion mapear(ResultSet rs) throws SQLException {
        return new Confederacion(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("sigla")
        );
    }

    public List<Confederacion> listarTodas() throws SQLException {
        List<Confederacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM confederaciones ORDER BY nombre";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
}
