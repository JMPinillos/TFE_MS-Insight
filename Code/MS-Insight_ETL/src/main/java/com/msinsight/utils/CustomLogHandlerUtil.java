package com.msinsight.utils;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CustomLogHandlerUtil extends Handler {
    // UTILIZA UN STRINGBUILDER PARA ACUMULAR MENSAJES DE LOG.
    private static final StringBuilder logMessages = new StringBuilder();
    private static final CustomLogHandlerUtil customLogHandler = new CustomLogHandlerUtil();

    // SOBRESCRIBE EL MÉTODO PUBLISH PARA ESPECIFICAR CÓMO SE DEBEN MANEJAR LOS REGISTROS DE LOG.
    @Override
    public void publish(LogRecord record) {
        // VERIFICA SI EXISTE UN FORMATEADOR ASOCIADO CON ESTE HANDLER.
        if (getFormatter() != null) {
            // SI EXISTE UN FORMATEADOR, USA ESTE PARA FORMATEAR EL REGISTRO DE LOG
            // Y LUEGO LO AGREGA AL STRINGBUILDER.
            logMessages.append(getFormatter().format(record));
        } else {
            // SI NO HAY UN FORMATEADOR, SIMPLEMENTE AGREGA EL NIVEL DEL LOG Y EL MENSAJE
            // AL STRINGBUILDER, SEGUIDO DE UN SALTO DE LÍNEA.
            logMessages.append(record.getLevel()).append(": ").append(record.getMessage()).append("\n");
        }
    }

    // SOBRESCRIBE EL MÉTODO FLUSH, QUE SE UTILIZA PARA ASEGURAR QUE TODOS LOS DATOS DE LOG SE HAN ESCRITO COMPLETAMENTE.
    // EN ESTE CASO, NO ES NECESARIO REALIZAR NINGUNA ACCIÓN ESPECÍFICA AL VACIAR EL BUFFER, POR LO QUE ESTÁ VACÍO.
    @Override
    public void flush() {}

    // SOBRESCRIBE EL MÉTODO CLOSE PARA MANEJAR EL CIERRE DEL HANDLER.
    // EN ESTE CONTEXTO, NO ES NECESARIO MANEJAR NADA ESPECIAL AL CERRAR, PERO SE DEBE PROPORCIONAR LA IMPLEMENTACIÓN.
    @Override
    public void close() throws SecurityException {}

    // PROPORCIONA UN MÉTODO PÚBLICO PARA ACCEDER A LOS MENSAJES DE LOG ACUMULADOS.
    public static String getLogMessages() {
        // DEVUELVE TODOS LOS MENSAJES DE LOG ACUMULADOS COMO UNA CADENA DE TEXTO.
        return logMessages.toString();
    }

    // Método para configurar el logger con el customLogHandler
    public static void setupLogger(java.util.logging.Logger logger) {
        logger.addHandler(customLogHandler);
    }
}