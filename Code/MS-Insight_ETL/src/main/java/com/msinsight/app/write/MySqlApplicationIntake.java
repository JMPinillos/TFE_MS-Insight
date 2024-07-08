package com.msinsight.app.write;

import com.msinsight.utils.*;
import com.msinsight.config.AppConfig;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import com.msinsight.config.MySqlConnector;
import com.msinsight.services.*;
import com.msinsight.models.*;
import com.msinsight.utils.CustomLogHandlerUtil;
import com.msinsight.utils.S3Util;

import lombok.extern.slf4j.Slf4j;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class MySqlApplicationIntake {

    // DECLARAMOS UNA VARIABLE logger ESTÁTICA Y FINAL DE TIPO Logger OBTENIDA DESDE LA CLASE MySqlApplicationIntake.
    private static final Logger logger = Logger.getLogger(MySqlApplicationIntake.class.getName());

    // DECLARAMOS UNA VARIABLE customLogHandler ESTÁTICA Y FINAL DE TIPO CustomLogHandlerUtil.
    private static final CustomLogHandlerUtil customLogHandler = new CustomLogHandlerUtil();

    // BLOQUE ESTÁTICO QUE SE EJECUTA AL CARGAR LA CLASE.
    // CONFIGURA EL LOGGER AÑADIENDO customLogHandler COMO UNO DE SUS MANEJADORES.
    static {
        // AÑADE EL customLogHandler AL LOGGER PARA QUE GESTIONE LOS REGISTROS DE LOG.
        logger.addHandler(customLogHandler);
        // ESTABLECE EL NIVEL DEL LOGGER A INFO, LO QUE SIGNIFICA QUE SOLO LOS MENSAJES DE NIVEL INFO O SUPERIOR (WARN, ERROR, FATAL) SERÁN PROCESADOS.
        logger.setLevel(Level.INFO);
    }

    // DECLARA UNA VARIABLE PATH QUE CONTIENE LA RUTA DONDE SE DESCARGARÁN LOS FICHEROS CSV.
    private static final String PATH = "/tmp/";

    // DECLARAMOS LAS INSTANCIAS DE LOS SERVICIOS QUE VAMOS A UTILIZAR
    private static final DatabaseService DB_SERVICE = new DatabaseService();
    private static final DataIngestionService DATA_INGESTION_SERVICE = new DataIngestionService(DB_SERVICE);
    private static final DatabaseCleanupService DB_CLEANUP_SERVICE = new DatabaseCleanupService();
    private static final ScoringService SCORING_SERVICE = new ScoringService();


    // DECLARAMOS EL FORMATO DE LAS FECHAS QUE VAMOS A UTILIZAR
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    // IMPORTAMOS LAS VARIABLES DE ENTORNO QUE VAMOS A UTILIZAR
    private static final String HOST = AppConfig.HOST;
    private static final String PORT = AppConfig.PORT;
    private static final String DATABASE = AppConfig.DATABASE;
    private static final String PUBLIC_KEY = AppConfig.PUBLIC_KEY;
    private static final String SECRET_KEY = AppConfig.SECRET_KEY;
    private static final String S3_FIXED_DATA = AppConfig.S3_FIXED_DATA;
    private static final String S3_VARIABLE_DATA = AppConfig.S3_VARIABLE_DATA;
    private static final String PATIENTS_CSV = AppConfig.PATIENTS_CSV;
    private static final String FORMS_CSV = AppConfig.FORMS_CSV;
    private static final String RESULTS_CSV = AppConfig.RESULTS_CSV;
    private static final String MEDICAL_CONSULTATIONS_CSV = AppConfig.MEDICAL_CONSULTATIONS_CSV;


    // DECLARAMOS LAS LISTAS QUE VAMOS A UTILIZAR PARA ALMACENAR LOS DATOS DE LOS FICHEROS CSV
    private static final List<MySqlGenders> genders = new ArrayList<>();
    private static final List<MySqlBrainsMRIs> brains_MRIs = new ArrayList<>();
    private static final List<MySqlSpinalsMRIs> spinals_MRIs = new ArrayList<>();
    private static final List<MySqlFormsQuestions> forms_questions = new ArrayList<>();
    private static final List<MySqlQuestionsAnswers> questions_answers = new ArrayList<>();
    private static final List<MySqlPatientsForms> patients_forms = new ArrayList<>();
    private static final List<MySqlFormsResultsClosed> forms_results_closed = new ArrayList<>();
    private static final List<MySqlFormsResultsDate> forms_results_date = new ArrayList<>();
    private static final List<MySqlFormsResultsDouble> forms_results_double = new ArrayList<>();
    private static final List<MySqlFormsResultsText> forms_results_text = new ArrayList<>();


    // DECLARAMOS LOS MAPAS QUE VAMOS A UTILIZAR PARA ALMACENAR LOS DATOS DE LOS FICHEROS CSV
    private static final Map<String, Integer> genderIndexMap = new HashMap<>();
    private static final Map<String, Integer> brainMRIIndexMap = new HashMap<>();
    private static final Map<String, Integer> spinalMRIIndexMap = new HashMap<>();
    private static final Map<Integer, MySqlPatients> patientsMap = new HashMap<>();
    private static final Map<String, MySqlForms> formsMap = new HashMap<>();
    private static final Map<String, MySqlQuestions> questionsMap = new HashMap<>();
    private static final Map<String, MySqlAnswers> answersMap = new HashMap<>();


    // DECLARAMOS LAS LISTAS QUE VAMOS A UTILIZAR PARA ALMACENAR LOS MODELOS DE LOS FORMULARIOS
    private static final List<FormsModels> formModelsForResults = new LinkedList<>();
    private static final List<FormsModels> formModelsForConsultations = new LinkedList<>();

    static {
        formModelsForResults.add(new FormsModels(1, 8, 9, 37, 38, 5, null, 1, 5));
        formModelsForResults.add(new FormsModels(2, 39, 40, 72, 73, 5, null, 6, 11));
        formModelsForResults.add(new FormsModels(3, 74, 77, 84, 85, 5, 75, 12, 16));
        formModelsForResults.add(new FormsModels(4, 86, 89, 96, 97, 5, 87, 12, 25));
        formModelsForResults.add(new FormsModels(5, 98, 99, 107, 108,  5, null, 26, 33));
        formModelsForResults.add(new FormsModels(6, 109, 110, 165, 166, 5, null, 26, 99));

        formModelsForConsultations.add(new FormsModels(7, 5, 5, 22, 23, 6, null, 100, 109));
    }


    // MÉTODO PRINCIPAL
    public static void main(String[] args) {

        S3Util.downloadFilesS3(PUBLIC_KEY, SECRET_KEY, S3_FIXED_DATA, PATIENTS_CSV, PATIENTS_CSV);
        S3Util.downloadFilesS3(PUBLIC_KEY, SECRET_KEY, S3_FIXED_DATA, FORMS_CSV, FORMS_CSV);
        S3Util.downloadFilesS3(PUBLIC_KEY, SECRET_KEY, S3_VARIABLE_DATA, RESULTS_CSV, RESULTS_CSV);
        S3Util.downloadFilesS3(PUBLIC_KEY, SECRET_KEY, S3_VARIABLE_DATA, MEDICAL_CONSULTATIONS_CSV, MEDICAL_CONSULTATIONS_CSV);

        try {
            // LEEMOS LOS DATOS DE LOS FICHEROS CSV
            readPatientsData();
            logger.info("PACIENTES LEIDOS");
            readFormsData();
            logger.info("TEST LEIDOS");
            readDataFormsResults();
            logger.info("RESULTADOS LEIDOS");
            readDataMedicalConsultations();
            logger.info("CONSULTAS LEIDAS");
        }catch (Exception e) {
            logger.severe("ERROR AL LEER LOS ARCHIVOS: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // Creamos conexion. No es necesario indicar puerto en host si usamos el default, 1521
        // Try-with-resources. Se cierra la conexión automáticamente al salir del bloque try
        try (Connection connection = new MySqlConnector(HOST, PORT, DATABASE).getConnection()) {

            logger.info("CONEXION ESTABLECIDA CON LA BASE DE DATOS " + DATABASE);

            // BORRAMOS LOS DATOS DE LA BASE DE DATOS
            DB_CLEANUP_SERVICE.clearDatabase(connection);

            // INTRODUCIMOS LOS DATOS EN LA BASE DE DATOS
            DATA_INGESTION_SERVICE.intakeGenders(connection, genders);
            logger.info("GÉNEROS INSERTADOS");

            DATA_INGESTION_SERVICE.intakeBrainsMRIs(connection, brains_MRIs);
            DATA_INGESTION_SERVICE.intakeSpinalsMRIs(connection, spinals_MRIs);
            logger.info("MRIs INICIALES INSERTADAS");

            DATA_INGESTION_SERVICE.intakePatients(connection, patientsMap);
            logger.info("PACIENTES INSERTADOS");

            DATA_INGESTION_SERVICE.intakeForms(connection, formsMap);
            logger.info("TEST INSERTADOS");

            DATA_INGESTION_SERVICE.intakeQuestions(connection, questionsMap);
            logger.info("PREGUNTAS INSERTADAS");

            DATA_INGESTION_SERVICE.intakeAnswers(connection, answersMap);
            logger.info("RESPUESTAS INSERTADAS");

            DATA_INGESTION_SERVICE.intakeFormsQuestions(connection, forms_questions);
            logger.info("RELACIONES ENTRE FORMULARIOS Y PREGUNTAS INSERTADAS");

            DATA_INGESTION_SERVICE.intakeQuestionsAnswers(connection, questions_answers);
            logger.info("RELACIONES ENTRE FORMULARIOS, PREGUNTAS Y RESPUESTAS INSERTADAS");

            DATA_INGESTION_SERVICE.intakeFormsPatientsForms(connection, patients_forms);
            DATA_INGESTION_SERVICE.intakeFormsResultsClosed(connection, forms_results_closed);
            DATA_INGESTION_SERVICE.intakeFormsResultsText(connection, forms_results_text);
            DATA_INGESTION_SERVICE.intakeFormsResultsDate(connection, forms_results_date);
            DATA_INGESTION_SERVICE.intakeFormsResultsDouble(connection, forms_results_double);
            logger.info("RESULTADOS DE LOS TEST INSERTADOS");

            // ACTUALIZAMOS LOS SCORES DE LOS TEST
            SCORING_SERVICE.updateAllScores(connection);
            logger.info("SCORES DE TODOS LOS TEST ACTUALIZADOS");
            logger.info("PROCESO DE INGESTA DE DATOS FINALIZADO CON ÉXITO");

        } catch (Exception e) {
            logger.severe("ERROR AL REALIZAR EL PROCESO ETL: " + e.getMessage());
        }
    }

    // LEEMOS LOS DATOS DE LOS PACIENTES
    private static void readPatientsData() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(PATH + PATIENTS_CSV))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build()) {

            reader.skip(1); // Salta la primera línea de encabezados
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                int genderId = processGender(nextLine[1]);
                int brainMRIId = processBrainMRI(nextLine[4]);
                int spinalMRIId = processSpinalMRI(nextLine[5]);
                processPatientData(nextLine, genderId, brainMRIId, spinalMRIId);
            }
            logger.info("readPatientsData ---> FICHERO " + PATIENTS_CSV + " LEÍDO CON ÉXITO");
        } catch (IOException | CsvValidationException e) {
            logger.severe("readPatientsData ---> ERROR AL LEER EL FICHERO " + PATIENTS_CSV);
            throw new RuntimeException(e);
        }
    }

    // MÉTODO PARA PROCESAR LOS GENEROS
    private static int processGender(String gender) {
        return genderIndexMap.computeIfAbsent(gender, k -> {
            int newId = genders.size() + 1;
            genders.add(new MySqlGenders(newId, gender));
            return newId;
        });
    }

    // MÉTODO PARA PROCESAR LOS MRIs CEREBRALES
    private static int processBrainMRI(String brainMRI) {
        return brainMRIIndexMap.computeIfAbsent(brainMRI, k -> {
            int newId = brainMRIIndexMap.size() + 1;
            brains_MRIs.add(new MySqlBrainsMRIs(newId, brainMRI));
            return newId;
        });
    }

    // MÉTODO PARA PROCESAR LOS MRIs MEDULARES
    private static int processSpinalMRI(String spinalMRI) {
        return spinalMRIIndexMap.computeIfAbsent(spinalMRI, k -> {
            int newId = spinalMRIIndexMap.size() + 1;
            spinals_MRIs.add(new MySqlSpinalsMRIs(newId, spinalMRI));
            return newId;
        });
    }

    // MÉTODO PARA PROCESAR LOS DATOS DE LOS PACIENTES
    private static void processPatientData(String[] data, int genderId, int brainMRIId, int spinalMRIId) {
        int patientId = Integer.parseInt(data[0]);
        String birthDate = data[2];
        String symptomDate = data[3];

        try {
            Date parsedBirthDate = new Date(DATE_FORMAT.parse(birthDate).getTime());
            Date parsedSymptomDate = new Date(DATE_FORMAT.parse(symptomDate).getTime());

            patientsMap.put(patientId, new MySqlPatients(
                    patientId,
                    genderId,
                    parsedBirthDate,
                    parsedSymptomDate,
                    brainMRIId,
                    spinalMRIId
            ));
        } catch (ParseException e) {
            logger.severe("ERROR AL TRATAR LOS DATOS DEL PACIENTE " + patientId + e);
        }
    }

    // LEEMOS LOS DATOS DE LOS FORMULARIOS
    private static void readFormsData() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(PATH + FORMS_CSV))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build()) {
            reader.skip(1); // Salta la primera línea de encabezados

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                processForm(nextLine);
                processQuestion(nextLine);
                processAnswer(nextLine);
                processFormQuestion(nextLine);
                processQuestionsAnswers(nextLine);
            }
            logger.info("readFormsData ---> FICHERO " + FORMS_CSV + " LEÍDO CON ÉXITO");
        } catch (IOException | CsvValidationException e) {
            logger.severe("readFormsData ---> ERROR AL LEER EL FICHERO " + FORMS_CSV);
            throw new RuntimeException(e);
        }
    }

    // MÉTODO PARA PROCESAR LOS FORMULARIOS
    private static void processForm(String[] nextLine) {
        String formTitle = nextLine[1];
        double minimum_score = nextLine[4].isEmpty() ? Double.NaN : Double.parseDouble(nextLine[4].replace(",", "."));
        double maximum_score = nextLine[5].isEmpty() ? Double.NaN : Double.parseDouble(nextLine[5].replace(",", "."));

        if (!formsMap.containsKey(formTitle)) {
            MySqlForms newForm = new MySqlForms(formsMap.size() + 1, formTitle, nextLine[2], nextLine[3], minimum_score, maximum_score);
            formsMap.put(formTitle, newForm);
        }
    }

    // MÉTODO PARA PROCESAR LAS PREGUNTAS
    private static void processQuestion(String[] nextLine) {
        String questionTitle = nextLine[8];
        if (!questionsMap.containsKey(questionTitle)) {
            int questionNumber = Integer.parseInt(nextLine[7]);
            MySqlQuestions newQuestion = new MySqlQuestions(questionsMap.size() + 1, questionNumber, questionTitle);
            questionsMap.put(questionTitle, newQuestion);
        }
    }

    // MÉTODO PARA PROCESAR LAS RESPUESTAS
    private static void processAnswer(String[] nextLine) {
        String answerTitle = nextLine[10];
        String type = nextLine[11];
        double value = nextLine[12].isEmpty() ? Double.NaN : Double.parseDouble(nextLine[12].replace(",", "."));

        String answerKey = AnswerKeyUtil.generateAnswerKey(answerTitle, type, value);
        if (!answersMap.containsKey(answerKey)) {
            MySqlAnswers newAnswer = new MySqlAnswers(answersMap.size() + 1, answerTitle, type, value);
            answersMap.put(answerKey, newAnswer);
        }
    }

    // MÉTODO PARA PROCESAR LAS RELACIONES ENTRE FORMULARIOS Y PREGUNTAS
    private static void processFormQuestion(String[] nextLine) {
        String formTitle = nextLine[1];
        String questionTitle = nextLine[8];

        MySqlForms form = formsMap.get(formTitle);
        MySqlQuestions question = questionsMap.get(questionTitle);

        if (form != null && question != null && forms_questions.stream().noneMatch(fq -> fq.getForm_id() == form.getForm_id() && fq.getQuestion_id() == question.getQuestion_id())) {
            forms_questions.add(new MySqlFormsQuestions(form.getForm_id(), question.getQuestion_id()));
        }
    }

    // MÉTODO PARA PROCESAR LAS RELACIONES ENTRE PREGUNTAS Y RESPUESTAS
    private static void processQuestionsAnswers(String[] nextLine) {
        try {
            String formTitle = nextLine[1];
            String questionTitle = nextLine[8];
            String answerTitle = nextLine[10];
            String answerType = nextLine[11];
            double value = nextLine[12].isEmpty() ? Double.NaN : Double.parseDouble(nextLine[12].replace(",", "."));

            MySqlForms form = formsMap.get(formTitle);
            MySqlQuestions question = questionsMap.get(questionTitle);
            String answerKey = AnswerKeyUtil.generateAnswerKey(answerTitle, answerType, value);
            MySqlAnswers answer = answersMap.get(answerKey);

            if (form == null) {
                throw new IllegalStateException("Formulario '" + formTitle + "' no encontrado");
            }
            if (question == null) {
                throw new IllegalStateException("Pregunta '" + questionTitle + "' no encontrada");
            }
            if (answer == null) {
                throw new IllegalStateException("Respuesta '" + answerTitle + "' no encontrada con tipo '" + answerType + "' y valor '" + value + "'");
            }

            if (questions_answers.stream().noneMatch(qa -> qa.getForm_id() == form.getForm_id() && qa.getQuestion_id() == question.getQuestion_id() && qa.getAnswer_id() == answer.getAnswer_id())) {
                questions_answers.add(new MySqlQuestionsAnswers(form.getForm_id(), question.getQuestion_id(), answer.getAnswer_id()));
            }
        } catch (IllegalStateException e) {
            logger.severe("ERROR AL PROCESAR LAS RELACIONES ENTRE FORMULARIOS, PREGUNTAS Y RESPUESTAS: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // MÉTODO PARA LEER LOS RESULTADOS DE LOS TESTS
    private static void readDataFormsResults() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(PATH + RESULTS_CSV))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build()) {

            reader.skip(1); // SALTA LA PRIMERA LÍNEA, QUE CONTIENE LOS NUMEROS DE LAS COLUMNAS DEL CSV
            String[] questionsLine = reader.readNext();
            String[] answersLine;

            while ((answersLine = reader.readNext()) != null) {
                for (FormsModels model : formModelsForResults) {
                    if (answersLine[model.getCompleteColumn()].equals("Complete")) {
                        processModel(questionsLine, answersLine, model);
                    }
                }
            }
            logger.info("readDataFormsResults ---> FICHERO " + RESULTS_CSV + " LEÍDO CON ÉXITO");
        } catch (IOException e) {
            logger.severe("readDataFormsResults ---> ERROR AL LEER EL FICHERO " + RESULTS_CSV);
            throw new RuntimeException(e);
        } catch (CsvValidationException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // MÉTODO PARA LEER LOS DATOS DE LAS CONSULTAS MÉDICAS
    private static void readDataMedicalConsultations() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(PATH + MEDICAL_CONSULTATIONS_CSV))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build()) {

            reader.skip(1); // SALTA LA PRIMERA LÍNEA, QUE CONTIENE LOS NUMEROS DE LAS COLUMNAS DEL CSV
            String[] questionsLine = reader.readNext();
            String[] answersLine;

            while ((answersLine = reader.readNext()) != null) {
                for (FormsModels model : formModelsForConsultations) {
                    if (answersLine[model.getCompleteColumn()].equals("Complete")) {
                        processModel(questionsLine, answersLine, model);
                    }
                }
            }
            logger.info("readDataMedicalConsultations ---> FICHERO " + MEDICAL_CONSULTATIONS_CSV + " LEÍDO CON ÉXITO");
        } catch (IOException e) {
            logger.severe("readDataMedicalConsultations ---> ERROR AL LEER EL FICHERO " + MEDICAL_CONSULTATIONS_CSV);
            throw new RuntimeException(e);
        } catch (CsvValidationException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // MÉTODO PARA PROCESAR LOS MODELOS DE LOS FORMULARIOS
    private static void processModel(String[] questionsLine, String[] answersLine, FormsModels model) throws ParseException {
        for (int i = model.getFirstColumn(); i <= model.getLastColumn(); i++) {
            String normalizedQuestionTitle = StringUtil.normalize(questionsLine[i]);
            MySqlQuestions question = questionsMap.values().stream()
                    .filter(q -> StringUtil.normalize(q.getTitle()).contains(normalizedQuestionTitle))
                    .findFirst().orElse(null);

            if (question != null) {
                int questionId = question.getQuestion_id();
                ReadAnswersValuesUtil answerCSV = ParsingUtil.parseAnswer(answersLine[i]);

                if (!answersLine[i].isEmpty()) {
                    MySqlAnswers answer = findAnswer(model, answerCSV, answersLine[i]);

                    if (answer != null) {
                        int answerId = answer.getAnswer_id();
                        int patientId = Integer.parseInt(answersLine[model.getPatientIdColumn()]);
                        int formId = model.getFormId();
                        java.sql.Date completedAt = new java.sql.Date(DATE_FORMAT.parse(answersLine[model.getCompleteAtColumn()]).getTime());

                        MySqlPatientsForms patientForm = findOrCreatePatientForm(patients_forms, patientId, formId, completedAt, model.getTScoreColumn() == null ? Double.NaN : Double.parseDouble(answersLine[model.getTScoreColumn()].replace(",", ".")));

                        if (questionId != 0 && answerId != 0) {
                            storeFormResults(answer.getType(), formId, questionId, answerId, patientForm, answersLine[i]);
                        }
                    }
                }
            }
        }
    }

    // MÉTODO PARA ENCONTRAR LA RESPUESTA
    private static MySqlAnswers findAnswer(FormsModels model, ReadAnswersValuesUtil answerCSV, String answersLine) {
        MySqlAnswers answer = answersMap.values().stream()
                .filter(a -> a.getAnswer_id() >= model.getStartAnswerId() && a.getAnswer_id() <= model.getEndAnswerId())
                .filter(a -> StringUtil.normalize(a.getTitle()).equals(StringUtil.normalize(answerCSV.getTitle()))
                        && (Double.isNaN(answerCSV.getValue()) || a.getValue() == answerCSV.getValue()))
                .findFirst().orElse(null);

        if (answer == null) {
            String tipo = DataTypeDistinguisherUtil.distinguishDataType(answersLine);
            final String answerTitle = switch (tipo) {
                case "Date" -> "Fecha";
                case "Double" -> "Numérica abierta (Double)";
                case "String" -> "Textual abierta";
                default -> null;
            };

            if (answerTitle != null) {
                answer = answersMap.values().stream()
                        .filter(a -> a.getAnswer_id() >= model.getStartAnswerId() && a.getAnswer_id() <= model.getEndAnswerId())
                        .filter(a -> StringUtil.normalize(a.getTitle()).equals(StringUtil.normalize(answerTitle))
                                && (Double.isNaN(answerCSV.getValue()) || a.getValue() == answerCSV.getValue()))
                        .findFirst().orElse(null);
            }
        }
        return answer;
    }

    // MÉTODO PARA ENCONTRAR O CREAR UN FORMULARIO DE PACIENTE
    private static MySqlPatientsForms findOrCreatePatientForm(List<MySqlPatientsForms> patientsForms, int patientId, int formId, java.sql.Date completedAt, Double tScore) {
        return patientsForms.stream()
                .filter(pf -> pf.getPatient_id() == patientId && pf.getForm_id() == formId && pf.getCompleted_at().equals(completedAt))
                .findFirst().orElseGet(() -> {
                    MySqlPatientsForms newPatientForm = new MySqlPatientsForms(
                            patientId, formId, completedAt, (patientsForms.size() + 1), tScore
                    );
                    patientsForms.add(newPatientForm);
                    return newPatientForm;
                });
    }

    // MÉTODO PARA ALMACENAR LOS RESULTADOS DE LOS TESTS
    private static void storeFormResults(String answerType, int formId, int questionId, int answerId, MySqlPatientsForms patientForm, String answerLine) throws ParseException {
        if ("date".equals(answerType)) {
            java.sql.Date valueDate = new java.sql.Date(DATE_FORMAT.parse(answerLine).getTime());
            forms_results_date.add(new MySqlFormsResultsDate(formId, questionId, answerId, patientForm.getPatient_form_id(), forms_results_date.size() + 1, valueDate));
        } else if ("text".equals(answerType)) {
            forms_results_text.add(new MySqlFormsResultsText(formId, questionId, answerId, patientForm.getPatient_form_id(), forms_results_text.size() + 1, answerLine));
        } else if ("double".equals(answerType)) {
            forms_results_double.add(new MySqlFormsResultsDouble(formId, questionId, answerId, patientForm.getPatient_form_id(), forms_results_double.size() + 1, Double.parseDouble(answerLine)));
        } else if ("closed".equals(answerType)) {
            forms_results_closed.add(new MySqlFormsResultsClosed(formId, questionId, answerId, patientForm.getPatient_form_id(), forms_results_closed.size() + 1));
        }
    }

}
