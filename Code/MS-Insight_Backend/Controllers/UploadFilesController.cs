using Amazon.S3;
using Microsoft.AspNetCore.Mvc;
using MsInsightApi.Services.Interfaces;

namespace MsInsightApi.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class UploadFilesController : Controller
    {
        private readonly IAmazonS3 _s3Client;
        private readonly IFilesManagementService _filesManagementService;
        private readonly ILogger<UploadFilesController> _logger;
        private readonly IConfiguration _configuration;

        public UploadFilesController(IAmazonS3 s3Client, IFilesManagementService filesManagementService, ILogger<UploadFilesController> logger, IConfiguration configuration)
        {
            _s3Client = s3Client;
            _filesManagementService = filesManagementService;
            _logger = logger;
            _configuration = configuration;
        }

        [HttpPost("uploadFixedData")]
        public async Task<IActionResult> UploadFixedDataAsync(List<IFormFile> files)
        {
            return await UploadFilesAsync(files, "S3:S3_FIXED_DATA", new List<string>
            {
                _configuration["Files:FORMS"],
                _configuration["Files:PATIENTS"]
            });
        }
        
        [HttpPost("uploadVariableData")]
        public async Task<IActionResult> UploadVariableDataAsync(List<IFormFile> files)
        {
            return await UploadFilesAsync(files, "S3:S3_VARIABLE_DATA", new List<string>
            {
                _configuration["Files:MEDICAL"],
                _configuration["Files:RESULTS"]
            });
        }

        private async Task<IActionResult> UploadFilesAsync(List<IFormFile> files, string bucketConfigKey, List<string> requiredFiles)
        {
            string bucketName = _configuration[bucketConfigKey];

            if (files.Count < 1 || files.Count > 2)
            {
                return BadRequest("Solo puede subir uno o dos archivos.");
            }

            try
            {
                // Validar archivos
                var validationErrors = await _filesManagementService.ValidateRequiredFiles(files, requiredFiles);
                if (validationErrors.Any())
                {
                    _logger.LogWarning("Errores de validación: {ValidationErrors}", string.Join(", ", validationErrors));
                    return BadRequest(validationErrors);
                }

                // Subir archivos a S3
                var uploadResult = await _filesManagementService.UploadFilesToS3(files, bucketName);
                if (!uploadResult.Success)
                {
                    _logger.LogError("Error al subir archivos: {Message}", uploadResult.Message);
                    return StatusCode(500, uploadResult.Message); // Internal Server Error
                }

                _logger.LogInformation("Archivos subidos correctamente a S3.");
                return Ok("Archivos subidos correctamente a S3.");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Error durante la subida de archivos al bucket {bucketName}.");
                return StatusCode(500, "Error interno del servidor.");
            }
        }
    }
}
