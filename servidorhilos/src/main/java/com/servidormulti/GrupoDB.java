package com.servidormulti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrupoDB {

    private final ConexionDB conexionDB;

    public GrupoDB() {
        this.conexionDB = new ConexionDB();
    }

    /**
     * Obtiene el ID de un grupo basado en su nombre.
     * @param nombreGrupo El nombre del grupo.
     * @return El ID del grupo, o -1 si no se encuentra.
     * @throws SQLException Si ocurre un error de base de datos.
     */

    public int getGrupoId(String nombreGrupo) throws SQLException {
        String sql = "SELECT id FROM grupos WHERE nombre = ?";
        
        try (Connection conn = conexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreGrupo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1; 
    }

}