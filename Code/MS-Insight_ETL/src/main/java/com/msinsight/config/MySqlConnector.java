package com.msinsight.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Getter
public class MySqlConnector {
    private final Connection connection;

    /**
     * Constructor de la clase. Se conecta a la base de datos.
     * @param host
     * @param port
     * @param database
     */
    public MySqlConnector(String host, String port, String database) {

        try {
            //Creamos la conexión a la base de datos
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database,
                    AppConfig.MYSQL_USER,
                    AppConfig.MYSQL_PASSWORD);

        } catch (SQLException e) {
            log.error("Error al conectar con la base de datos", e);
            throw new RuntimeException(e);
        }
    }
}
