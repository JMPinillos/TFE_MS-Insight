using Amazon.S3;
using Amazon.S3.Model;
using MsInsightApi.Services.Interfaces;

namespace MsInsightApi.Services
{
    public class FilesManagementService : IFilesManagementService
    {
        private readonly IAmazonS3 _s3Client;
        private readonly ILogger<FilesManagementService> _logger;
        private readonly IConfiguration _configuration;

        public FilesManagementService(IAmazonS3 s3Client, ILogger<FilesManagementService> logger, IConfiguration configuration)
        {
            _s3Client = s3Client;
            _logger = logger;
            _configuration = configuration;
        }

        // Valida que los archivos requeridos estén presentes
        public async Task<List<string>> ValidateRequiredFiles(List<IFormFile> files, List<string> requiredFileNames)
        {
            var errors = new List<string>();

            if (files.Count < 1 || files.Count > 2)
            {
                errors.Add("Debe subir uno o dos archivos.");
                return errors;
            }

            foreach (var file in files)
            {
                if (!requiredFileNames.Any(f => f.Equals(file.FileName, StringComparison.OrdinalIgnoreCase)))
                {
                    errors.Add($"Archivo incorrecto: '{file.FileName}'.");
                }
            }

            return errors;
        }

        // Sube los archivos al bucket de S3
        public async Task<(bool Success, string Message)> UploadFilesToS3(List<IFormFile> files, string bucketName)
        {
            foreach (var file in files)
            {
                var request = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = file.FileName,
                    InputStream = file.OpenReadStream(),
                    ContentType = "text/csv"
                };

                int maxAttempts = 3;
                bool success = false;
                Exception lastException = null;

                for (int attempt = 1; attempt <= maxAttempts && !success; attempt++)
                {
                    try
                    {
                        await _s3Client.PutObjectAsync(request);
                        success = true; // Subida exitosa
                    }
                    catch (AmazonS3Exception e)
                    {
                        lastException = e;
                        if (attempt < maxAttempts)
                        {
                            Console.WriteLine($"Intento {attempt} fallido para subir {file.FileName}. Reintentando...");
                            await Task.Delay(1000 * attempt); // Espera progresivamente más tiempo entre reintentos.
                        }
                    }
                }

                if (!success && lastException != null)
                {
                    return (false, $"Error al subir el archivo {file.FileName} al bucket {bucketName} después de {maxAttempts} intentos: {lastException.Message}");
                }
            }

            return (true, "Todos los archivos fueron subidos correctamente a S3.");
        }

        // Crea un diccionario con los buckets y los archivos requeridos y los verifica
        public async Task<(bool Success, string Message, List<string> MissingFiles)> CheckAllRequiredFilesInBuckets()
        {
            var bucketsWithRequiredFiles = new Dictionary<string, List<string>>
            {
                { _configuration["S3:S3_FIXED_DATA"], new List<string> { _configuration["Files:FORMS"], _configuration["Files:PATIENTS"] } },
                { _configuration["S3:S3_VARIABLE_DATA"], new List<string> { _configuration["Files:MEDICAL"], _configuration["Files:RESULTS"] } }
            };

            var missingFiles = new List<string>();
            try
            {
                foreach (var bucketEntry in bucketsWithRequiredFiles)
                {
                    var listRequest = new ListObjectsV2Request { BucketName = bucketEntry.Key };
                    ListObjectsV2Response listResponse;

                    try
                    {
                        listResponse = await _s3Client.ListObjectsV2Async(listRequest);
                    }
                    catch (AmazonS3Exception e)
                    {
                        var errorMessage = $"Error al listar objetos en el bucket {bucketEntry.Key}: {e.Message}";
                        _logger.LogError(e, errorMessage);
                        return (false, errorMessage, null);
                    }
                    catch (Exception e)
                    {
                        var errorMessage = $"Error desconocido al listar objetos en el bucket {bucketEntry.Key}: {e.Message}";
                        _logger.LogError(e, errorMessage);
                        return (false, errorMessage, null);
                    }

                    var filesInBucket = listResponse.S3Objects.Select(o => o.Key).ToList();

                    var missingInBucket = bucketEntry.Value.Where(requiredFile => !filesInBucket.Contains(requiredFile, StringComparer.OrdinalIgnoreCase)).ToList();
                    if (missingInBucket.Any())
                    {
                        missingFiles.AddRange(missingInBucket);
                    }
                }

                if (missingFiles.Any())
                {
                    var missingFilesMessage = "Faltan los siguientes archivos: " + string.Join(", ", missingFiles);
                    _logger.LogWarning(missingFilesMessage);
                    return (false, missingFilesMessage, missingFiles);
                }

                _logger.LogInformation("Todos los archivos requeridos están presentes en los buckets especificados.");
                return (true, "Todos los archivos requeridos están presentes en los buckets especificados.", null);
            }
            catch (Exception ex)
            {
                var errorMessage = $"Error durante la verificación de archivos en los buckets: {ex.Message}";
                _logger.LogError(ex, errorMessage);
                return (false, errorMessage, null);
            }
        }
    }
}