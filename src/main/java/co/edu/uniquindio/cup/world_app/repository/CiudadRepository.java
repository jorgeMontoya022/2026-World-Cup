package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Ciudad;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Ciudad.
 */
public class CiudadRepository {

    private Ciudad mapear(ResultSet rs) throws SQLException {
        return new Ciudad(rs.getInt("id"), rs.getString("nombre"), rs.getString("pais"));
    }

    public List<Ciudad> listarTodas() throws SQLException {
        List<Ciudad> lista = new ArrayList<>();
        String sql = "SELECT * FROM ciudades ORDER BY pais, nombre";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
}
