package com.msinsight.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

// CLASE PARA ACTUALIZAR LOS SCORES DE LOS TEST
public class ScoringService {

    // MÉTODO PÚBLICO PARA ACTUALIZAR LOS SCORES DE TODOS LOS TEST
    public void updateAllScores(Connection connection) throws SQLException {
        try (CallableStatement callableStatement = connection.prepareCall("{CALL UpdateAllPatientsFormsScores()}")) {
            // EJECUTAMOS LA ACTUALIZACIÓN PARA TODOS LOS PACIENTES
            callableStatement.execute();
        }
    }
}