package co.edu.uniquindio.cup.world_app.repository;

import co.edu.uniquindio.cup.world_app.model.Grupo;
import co.edu.uniquindio.cup.world_app.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Grupo.
 */
public class GrupoRepository {

    private Grupo mapear(ResultSet rs) throws SQLException {
        return new Grupo(rs.getInt("id"), rs.getString("nombre"));
    }

    public List<Grupo> listarTodos() throws SQLException {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM grupos ORDER BY nombre";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
}
