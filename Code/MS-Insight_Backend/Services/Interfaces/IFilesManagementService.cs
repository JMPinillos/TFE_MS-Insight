namespace MsInsightApi.Services.Interfaces
{
    public interface IFilesManagementService
    {
        // Valida que los archivos requeridos estén presentes
        Task<List<string>> ValidateRequiredFiles(List<IFormFile> files, List<string> requiredFileNames);

        // Sube los archivos al bucket de S3 y devuelve un resultado con éxito y mensaje
        Task<(bool Success, string Message)> UploadFilesToS3(List<IFormFile> files, string bucketName);

        // Verifica que los archivos requeridos estén presentes en múltiples buckets
        Task<(bool Success, string Message, List<string> MissingFiles)> CheckAllRequiredFilesInBuckets();
    }
}