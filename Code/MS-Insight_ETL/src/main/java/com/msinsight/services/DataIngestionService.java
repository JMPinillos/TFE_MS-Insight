package com.msinsight.services;

import com.msinsight.models.*;
import com.msinsight.services.interfaces.PreparedStatementFiller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataIngestionService {

    private final DatabaseService dbService;

    public DataIngestionService(DatabaseService dbService) {
        this.dbService = dbService;
    }

    private <T> void batchInsert(Connection connection, String sql, List<T> items, PreparedStatementFiller<T> filler) throws SQLException {

        int batchSize = determineBatchSize(items.size());

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // DESACTIVAMOS EL AUTOCOMMIT PARA CONTROLAR LA TRANSACCIÓN MANUALMENTE
            connection.setAutoCommit(false);

            int batchCount = 0;

            for (T item : items) {
                // UTILIZAMOS EL FILLER PARA LLENAR EL PREPAREDSTATEMENT CON LOS DATOS DEL ITEM
                filler.fill(ps, item);
                // AÑADIMOS EL PREPAREDSTATEMENT AL BATCH
                ps.addBatch();
                // EJECUTAMOS EL BATCH CADA batchSize ELEMENTOS PARA MEJORAR EL RENDIMIENTO
                if (++batchCount % batchSize == 0) {
                    ps.executeBatch();
                }
            }

            // EJECUTAMOS EL RESTANTE DEL BATCH SI NO ES MULTIPLO DE batchSize
            if (batchCount % batchSize != 0) {
                ps.executeBatch();
            }

            // REALIZAMOS COMMIT DE TODAS LAS OPERACIONES REALIZADAS
            connection.commit();
        } catch (SQLException e) {
            // EN CASO DE ERROR, REALIZAMOS ROLLBACK PARA DESHACER TODOS LOS CAMBIOS
            connection.rollback();
            throw e;
        } finally {
            // RESTABLECEMOS EL AUTOCOMMIT AL TERMINAR
            connection.setAutoCommit(true);
        }
    }

    private int determineBatchSize(int itemCount) {
        if (itemCount < 100) {
            return 10; // Para pocos datos, batch más pequeños
        } else if (itemCount < 1000) {
            return 50; // Para tamaños intermedios, batch más pequeños
        } else if (itemCount < 5000) {
            return 100; // Para tamaños grandes, batch intermedios
        } else {
            return 500; // Para grandes volúmenes, batch más grandes pero manejables
        }
    }

    // INTRODUCIMOS LOS DATOS DEL GÉNERO EN LA TABLA genders
    public void intakeGenders(Connection connection, List<MySqlGenders> genders) throws SQLException {
        String sql = "INSERT INTO genders (gender_id, gender) VALUES (?, ?)";
        batchInsert(connection, sql, genders, dbService::fillInsertStatementGenders);
    }

    // INTRODUCIMOS LOS DATOS DE LAS RESONANCIAS MAGNETICAS CEREBRALES INICIALES EN LAS TABLA brains_MRIs
    public void intakeBrainsMRIs(Connection connection, List<MySqlBrainsMRIs> brains_MRIs) throws SQLException {
        String sql = "INSERT INTO brains_MRIs (brain_MRI_id, brain_MRI) VALUES (?, ?)";
        batchInsert(connection, sql, brains_MRIs, dbService::fillInsertStatementBrainsMRIs);
    }

    // INTRODUCIMOS LOS DATOS DE LAS RESONANCIAS MAGNETICAS MEDULARES INICIALES EN LAS TABLA spinals_MRIs
    public void intakeSpinalsMRIs(Connection connection, List<MySqlSpinalsMRIs> spinals_MRIs) throws SQLException {
        String sql = "INSERT INTO spinals_MRIs (spinal_MRI_id, spinal_MRI) VALUES (?, ?)";
        batchInsert(connection, sql, spinals_MRIs, dbService::fillInsertStatementSpinalsMRIs);
    }

    // INTRODUCIMOS LOS DATOS DE LOS PACIENTES EN LA TABLA patients
    public void intakePatients(Connection connection, Map<Integer, MySqlPatients> patientsMap) throws SQLException {
        List<MySqlPatients> patients = new ArrayList<>(patientsMap.values());
        String sql = "INSERT INTO patients (patient_id, gender_id, birth_date, onset_symptoms, brain_MRI_id, spinal_MRI_id) VALUES (?, ?, ?, ?, ?, ?)";
        batchInsert(connection, sql, patients, dbService::fillInsertStatementPatients);
    }

    // INTRODUCIMOS LOS DATOS DE LOS TEST EN LA TABLA forms
    public void intakeForms(Connection connection, Map<String, MySqlForms> formsMap) throws SQLException {
        List<MySqlForms> forms = new ArrayList<>(formsMap.values());
        String sql = "INSERT INTO forms (form_id, title, description, formula, minimum_score, maximum_score) VALUES (?, ?, ?, ?, ?, ?)";
        batchInsert(connection, sql, forms, dbService::fillInsertStatementForms);
    }

    // INTRODUCIMOS LOS DATOS DE LAS PREGUNTAS EN LA TABLA questions
    public void intakeQuestions(Connection connection, Map<String, MySqlQuestions> questionsMap) throws SQLException {
        List<MySqlQuestions> questions = new ArrayList<>(questionsMap.values());
        String sql = "INSERT INTO questions (question_id, number, title) VALUES (?, ?, ?)";
        batchInsert(connection, sql, questions, dbService::fillInsertStatementQuestions);
    }

    // INTRODUCIMOS LOS DATOS DE LAS RESPUESTAS EN LA TABLA answers
    public void intakeAnswers(Connection connection, Map<String, MySqlAnswers> answersMap) throws SQLException {
        List<MySqlAnswers> answers = new ArrayList<>(answersMap.values());
        String sql = "INSERT INTO answers (answer_id, title, type, value) VALUES (?, ?, ?, ?)";
        batchInsert(connection, sql, answers, dbService::fillInsertStatementAnswers);
    }

    // INTRODUCIMOS LOS DATOS DE LOS TESTS Y DE LAS PREGUNTAS EN LA TABLA forms_questions
    public void intakeFormsQuestions(Connection connection, List<MySqlFormsQuestions> formsQuestions) throws SQLException {
        String sql = "INSERT INTO forms_questions (form_id, question_id) VALUES (?, ?)";
        batchInsert(connection, sql, formsQuestions, dbService::fillInsertStatementFormsQuestions);
    }

    // INTRODUCIMOS LOS DATOS DE LOS TESTS, LAS PREGUNTAS Y DE LAS RESPUESTAS EN LA TABLA questions_answers
    public void intakeQuestionsAnswers(Connection connection, List<MySqlQuestionsAnswers> questionsAnswers) throws SQLException {
        String sql = "INSERT INTO questions_answers (form_id, question_id, answer_id) VALUES (?, ?, ?)";
        batchInsert(connection, sql, questionsAnswers, dbService::fillInsertStatementQuestionsAnswers);
    }

    // INTRODUCIMOS LOS DATOS DE LOS RESULTADOS DE LOS TESTS EN LAS TABLAS patients_forms
    public void intakeFormsPatientsForms(Connection connection, List<MySqlPatientsForms> patientsForms) throws SQLException {
        String sql = "INSERT INTO patients_forms (patient_id, form_id, completed_at, score, patient_form_id) VALUES (?, ?, ?, ?, ?)";
        batchInsert(connection, sql, patientsForms, dbService::fillInsertStatementPatientsForms);
    }

    // INTRODUCIMOS LOS DATOS DE LOS RESULTADOS DE LOS TESTS EN LAS TABLAS patients_forms_answers_closed
    public void intakeFormsResultsClosed(Connection connection, List<MySqlFormsResultsClosed> formsResultsClosed) throws SQLException {
        String sql = "INSERT INTO patients_forms_answers_closed (form_id, question_id, answer_id, patient_form_id, result_id) VALUES (?, ?, ?, ?, ?)";
        batchInsert(connection, sql, formsResultsClosed, dbService::fillInsertStatementFormsResultsClosed);
    }

    // INTRODUCIMOS LOS DATOS DE LOS RESULTADOS DE LOS TESTS EN LAS TABLAS patients_forms_answers_text
    public void intakeFormsResultsText(Connection connection, List<MySqlFormsResultsText> formsResultsText) throws SQLException {
        String sql = "INSERT INTO patients_forms_answers_text (form_id, question_id, answer_id, value, patient_form_id, result_id) VALUES (?, ?, ?, ?, ?, ?)";
        batchInsert(connection, sql, formsResultsText, dbService::fillInsertStatementFormsResultsText);
    }

    // INTRODUCIMOS LOS DATOS DE LOS RESULTADOS DE LOS TESTS EN LAS TABLAS patients_forms_answers_date
    public void intakeFormsResultsDate(Connection connection, List<MySqlFormsResultsDate> formsResultsDate) throws SQLException {
        String sql = "INSERT INTO patients_forms_answers_date (form_id, question_id, answer_id, value, patient_form_id, result_id) VALUES (?, ?, ?, ?, ?, ?)";
        batchInsert(connection, sql, formsResultsDate, dbService::fillInsertStatementFormsResultsDate);
    }

    // INTRODUCIMOS LOS DATOS DE LOS RESULTADOS DE LOS TESTS EN LAS TABLAS patients_forms_answers_double
    public void intakeFormsResultsDouble(Connection connection, List<MySqlFormsResultsDouble> formsResultsDouble) throws SQLException {
        String sql = "INSERT INTO patients_forms_answers_double (form_id, question_id, answer_id, value, patient_form_id, result_id) VALUES (?, ?, ?, ?, ?, ?)";
        batchInsert(connection, sql, formsResultsDouble, dbService::fillInsertStatementFormsResultsDouble);
    }

}
