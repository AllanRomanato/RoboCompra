package br.com.cryptoong.robocompra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Project Workspace
 * Created by Allan Romanato
 */
public class ConnectionFactory {
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://crypto.mysql.uhserver.com/crypto?autoReconnect=true", "cryptoong", "Bots*51381");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}