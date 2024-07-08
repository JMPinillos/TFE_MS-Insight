package com.msinsight.utils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.nio.file.Paths;
import java.util.logging.Logger;

public class S3Util {
    private static final Logger logger = Logger.getLogger(S3Util.class.getName());

    // MÉTODO PARA DESCARGAR FICHEROS DE S3
    public static void downloadFilesS3(String publicKey, String secretKey, String bucketName, String keyName, String fileName) {

        // CREAMOS UN OBJETO AwsBasicCredentials CON LAS CREDENCIALES DE ACCESO
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(publicKey, secretKey);

        // CREAMOS UN CLIENTE DE S3
        S3Client s3 = S3Client.builder()
                .region(Region.EU_SOUTH_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        // CREAMOS UN OBJETO GetObjectRequest CON EL NOMBRE DEL BUCKET Y EL NOMBRE DEL FICHERO
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        // CREAMOS UNA CADENA DE TEXTO CON LA RUTA DEL FICHERO
        String filePath = "/tmp/" + fileName;

        // DESCARGAMOS EL FICHERO DE S3
        s3.getObject(request, ResponseTransformer.toFile(Paths.get(filePath)));
        logger.info("ARCHIVO " + fileName + " DESCARGADO CORRECTAMENTE");
    }
}