package com.msinsight.services;

import com.msinsight.models.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

// CLASE PARA RELLENAR LAS SENTENCIAS DE INSERCIÓN
public class DatabaseService {

    // MÉTODO PARA RELLENAR LOS GENEROS
    public void fillInsertStatementGenders(PreparedStatement statement, MySqlGenders genders) throws
            SQLException {
        statement.setInt(1, genders.getGender_id());
        statement.setString(2, genders.getGender());
    }

    // MÉTODO PARA RELLENAR LOS BRAIN MRIs
    public void fillInsertStatementBrainsMRIs(PreparedStatement statement, MySqlBrainsMRIs brainsMRIs) throws
            SQLException {
        statement.setInt(1, brainsMRIs.getBrain_MRI_id());
        statement.setString(2, brainsMRIs.getBrain_MRI());
    }

    // MÉTODO PARA RELLENAR LOS SPINAL MRIs
    public void fillInsertStatementSpinalsMRIs(PreparedStatement statement, MySqlSpinalsMRIs spinalsMRIs) throws
            SQLException {
        statement.setInt(1, spinalsMRIs.getSpinal_MRI_id());
        statement.setString(2, spinalsMRIs.getSpinal_MRI());
    }

    // MÉTODO PARA RELLENAR LOS PACIENTES
    public void fillInsertStatementPatients(PreparedStatement statement, MySqlPatients patients) throws
            SQLException {
        statement.setInt(1, patients.getPatient_id());
        statement.setInt(2, patients.getGender_id());
        statement.setDate(3, patients.getBirth_date());
        statement.setDate(4, patients.getOnset_symptoms());
        statement.setInt(5, patients.getBrain_MRI_id());
        statement.setInt(6, patients.getSpinal_MRI_id());
    }

    // MÉTODO PARA RELLENAR LOS FORMULARIOS
    public void fillInsertStatementForms(PreparedStatement statement, MySqlForms forms) throws SQLException {
        statement.setInt(1, forms.getForm_id());
        statement.setString(2, forms.getTitle());
        statement.setString(3, forms.getDescription());
        statement.setString(4, forms.getFormula());

        if (Double.isNaN(forms.getMinimum_score())) {
            statement.setNull(5, Types.DOUBLE); // Establece el campo como NULL si el valor es null
        } else {
            statement.setDouble(5, forms.getMinimum_score()); // Establece el valor del campo si no es null
        }

        if (Double.isNaN(forms.getMaximum_score())) {
            statement.setNull(6, Types.DOUBLE); // Establece el campo como NULL si el valor es null
        } else {
            statement.setDouble(6, forms.getMaximum_score()); // Establece el valor del campo si no es null
        }
    }

    // MÉTODO PARA RELLENAR LAS PREGUNTAS
    public void fillInsertStatementQuestions(PreparedStatement statement, MySqlQuestions questions) throws
            SQLException {
        statement.setInt(1, questions.getQuestion_id());
        statement.setInt(2, questions.getNumber());
        statement.setString(3, questions.getTitle());
    }

    // MÉTODO PARA RELLENAR LAS RESPUESTAS
    public void fillInsertStatementAnswers(PreparedStatement statement, MySqlAnswers answers) throws
            SQLException {
        statement.setInt(1, answers.getAnswer_id());
        statement.setString(2, answers.getTitle());
        statement.setString(3, answers.getType());

        if (Double.isNaN(answers.getValue())) {
            statement.setNull(4, Types.DOUBLE); // Establece el campo como NULL si el valor es null
        } else {
            statement.setDouble(4, answers.getValue()); // Establece el valor del campo si no es null
        }
    }

    // MÉTODO PARA RELLENAR LA RELACIÓN ENTRE FORMULARIOS Y PREGUNTAS
    public void fillInsertStatementFormsQuestions(PreparedStatement statement, MySqlFormsQuestions
            formsQuestions) throws SQLException {
        statement.setInt(1, formsQuestions.getForm_id());
        statement.setInt(2, formsQuestions.getQuestion_id());
    }

    // MÉTODO PARA RELLENAR LA RELACIÓN ENTRE FORMULARIOS, PREGUNTAS Y RESPUESTAS
    public void fillInsertStatementQuestionsAnswers(PreparedStatement statement, MySqlQuestionsAnswers
            questionsAnswers) throws SQLException {
        statement.setInt(1, questionsAnswers.getForm_id());
        statement.setInt(2, questionsAnswers.getQuestion_id());
        statement.setInt(3, questionsAnswers.getAnswer_id());
    }

    // MÉTODOS PARA RELLENAR LAS RELACIONES ENTRE LOS PACIENTES Y LOS FORMULARIOS
    public void fillInsertStatementPatientsForms(PreparedStatement statement, MySqlPatientsForms
            formsResults) throws SQLException {
        statement.setInt(1, formsResults.getPatient_id());
        statement.setInt(2, formsResults.getForm_id());
        statement.setDate(3, (Date) formsResults.getCompleted_at());

        if (Double.isNaN(formsResults.getScore())) {
            statement.setNull(4, Types.DOUBLE); // Establece el campo como NULL si el valor es null
        } else {
            statement.setDouble(4, formsResults.getScore()); // Establece el valor del campo si no es null
        }

        statement.setInt(5, formsResults.getPatient_form_id());
    }

    // MÉTODOS PARA RELLENAR LOS RESULTADOS DE LAS RESPUESTAS CERRADAS
    public void fillInsertStatementFormsResultsClosed(PreparedStatement statement, MySqlFormsResultsClosed
            forms_results_closed) throws SQLException {
        statement.setInt(1, forms_results_closed.getForm_id());
        statement.setInt(2, forms_results_closed.getQuestion_id());
        statement.setInt(3, forms_results_closed.getAnswer_id());
        statement.setInt(4, forms_results_closed.getPatient_form_id());
        statement.setInt(5, forms_results_closed.getResult_id());
    }

    // MÉTODOS PARA RELLENAR LOS RESULTADOS DE LAS RESPUESTAS ABIERTAS DE TIPO DE TEXT
    public void fillInsertStatementFormsResultsText(PreparedStatement statement, MySqlFormsResultsText
            forms_results_text) throws SQLException {
        statement.setInt(1, forms_results_text.getForm_id());
        statement.setInt(2, forms_results_text.getQuestion_id());
        statement.setInt(3, forms_results_text.getAnswer_id());
        statement.setString(4, forms_results_text.getValue());
        statement.setInt(5, forms_results_text.getPatient_form_id());
        statement.setInt(6, forms_results_text.getResult_id());
    }

    // MÉTODOS PARA RELLENAR LOS RESULTADOS DE LAS RESPUESTAS ABIERTAS DE TIPO DE DATE
    public void fillInsertStatementFormsResultsDate(PreparedStatement statement, MySqlFormsResultsDate
            forms_results_date) throws SQLException {
        statement.setInt(1, forms_results_date.getForm_id());
        statement.setInt(2, forms_results_date.getQuestion_id());
        statement.setInt(3, forms_results_date.getAnswer_id());
        statement.setDate(4, forms_results_date.getValue());
        statement.setInt(5, forms_results_date.getPatient_form_id());
        statement.setInt(6, forms_results_date.getResult_id());
    }

    // MÉTODOS PARA RELLENAR LOS RESULTADOS DE LAS RESPUESTAS ABIERTAS DE TIPO DE DOUBLE
    public void fillInsertStatementFormsResultsDouble(PreparedStatement statement, MySqlFormsResultsDouble
            forms_results_double) throws SQLException {
        statement.setInt(1, forms_results_double.getForm_id());
        statement.setInt(2, forms_results_double.getQuestion_id());
        statement.setInt(3, forms_results_double.getAnswer_id());
        statement.setDouble(4, forms_results_double.getValue());
        statement.setInt(5, forms_results_double.getPatient_form_id());
        statement.setInt(6, forms_results_double.getResult_id());
    }

}
