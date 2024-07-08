package com.msinsight.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseCleanupService.class);

    public void clearDatabase(Connection connection) {
        try (Statement deleteData = connection.createStatement()) {

            // BORRAMOS LOS DATOS DE LA BASE DE DATOS
            deleteData.executeUpdate("DELETE FROM patients_forms_answers_closed");
            deleteData.executeUpdate("DELETE FROM patients_forms_answers_date");
            deleteData.executeUpdate("DELETE FROM patients_forms_answers_text");
            deleteData.executeUpdate("DELETE FROM patients_forms_answers_double");
            deleteData.executeUpdate("DELETE FROM patients_forms");
            deleteData.executeUpdate("DELETE FROM patients");
            deleteData.executeUpdate("DELETE FROM genders");
            deleteData.executeUpdate("DELETE FROM brains_MRIs");
            deleteData.executeUpdate("DELETE FROM spinals_MRIs");
            deleteData.executeUpdate("DELETE FROM questions_answers");
            deleteData.executeUpdate("DELETE FROM forms_questions");
            deleteData.executeUpdate("DELETE FROM forms");
            deleteData.executeUpdate("DELETE FROM questions");
            deleteData.executeUpdate("DELETE FROM answers");

            logger.info("DATOS DE LA BASE DE DATOS BORRADOS CORRECTAMENTE");

        } catch (SQLException e) {
            logger.error("ERROR AL VACIAR LA BASE DE DATOS", e);
        }
    }
}
