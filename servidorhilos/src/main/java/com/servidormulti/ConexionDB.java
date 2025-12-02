package com.servidormulti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    
    private static final String URL = "jdbc:sqlite:server_database.db";

    // Tabla de Usuarios
    private final String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                     "nombre TEXT NOT NULL UNIQUE," +
                                     "password TEXT NOT NULL" +
                                     ");";

    // Tabla de Grupos
    private final String sqlGrupos = "CREATE TABLE IF NOT EXISTS grupos (" +
                                   "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                   "nombre TEXT NOT NULL UNIQUE" +
                                   ");";
    
    // Tabla de Mensajes de Grupo
    private final String sqlGruposMensajes = "CREATE TABLE IF NOT EXISTS grupos_mensajes (" +
                                           "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                           "grupo_id INTEGER NOT NULL," +
                                           "remitente_nombre TEXT NOT NULL," +
                                           "contenido TEXT NOT NULL," +
                                           "timestamp INTEGER NOT NULL," +
                                           "FOREIGN KEY(grupo_id) REFERENCES grupos(id)" +
                                           ");";

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
    public void inicializarDB() {
        try (Connection conn = conectar()) {
            if (conn != null) {
                crearTablas(conn);
                inicializarDatosBase(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    private void crearTablas(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            
            // Creación de tablas
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlGrupos);
            stmt.execute(sqlGruposMensajes);
            
            System.out.println("Todas las tablas (usuarios, grupos y mensajes) verificadas o creadas.");

        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
            throw e; 
        }
    }

    private void inicializarDatosBase(Connection conn) throws SQLException {
        // Asegura que el grupo global "Todos" existe
        String checkGroup = "SELECT id FROM grupos WHERE nombre = 'Todos'";
        String insertGroup = "INSERT INTO grupos (nombre) VALUES ('Todos')";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkGroup)) {

            if (!rs.next()) {
                // Si no existe, lo inserta
                try (PreparedStatement pstmt = conn.prepareStatement(insertGroup)) {
                    pstmt.executeUpdate();
                    System.out.println("Grupo base 'Todos' creado.");
                }
            } else {
                // System.out.println("Grupo base 'Todos' ya existe.");
            }
        }
    }
}