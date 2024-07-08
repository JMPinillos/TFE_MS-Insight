using Amazon.Lambda;
using Amazon.Lambda.Model;
using Microsoft.AspNetCore.Mvc;
using MsInsightApi.Services.Interfaces;

namespace MsInsightApi.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class LambdaActivationController : Controller
    {
        private readonly IFilesManagementService _filesManagementService;
        private readonly IAmazonLambda _lambdaClient;
        private readonly ILogger<LambdaActivationController> _logger;

        public LambdaActivationController(IFilesManagementService filesManagementService, IAmazonLambda lambdaClient, ILogger<LambdaActivationController> logger)
        {
            _filesManagementService = filesManagementService;
            _lambdaClient = lambdaClient;
            _logger = logger;
        }

        [HttpPost("checkAndActivateLambda")]
        public async Task<IActionResult> CheckAndActivateLambdaAsync()
        {
            try
            {
                // Verificar que todos los archivos requeridos están presentes en los buckets
                var checkResult = await _filesManagementService.CheckAllRequiredFilesInBuckets();
                if (!checkResult.Success)
                {
                    _logger.LogError("Error al comprobar archivos en los buckets: {Message}", checkResult.Message);
                    return StatusCode(500, checkResult.Message); // Internal Server Error
                }

                // Activar la función Lambda
                var invokeRequest = new InvokeRequest
                {
                    FunctionName = "Lambda_ETL_MS-Insight",
                    InvocationType = InvocationType.RequestResponse
                };

                var response = await _lambdaClient.InvokeAsync(invokeRequest);

                if (response.StatusCode != 200) // 200 es el código de estado para 'OK'
                {
                    var errorMessage = $"Error al invocar la función Lambda: {response.FunctionError}";
                    _logger.LogError(errorMessage);
                    return StatusCode(500, errorMessage);
                }

                // Leer la respuesta de la función Lambda
                var payloadStream = response.Payload;
                using (var reader = new StreamReader(payloadStream))
                {
                    var lambdaResponse = await reader.ReadToEndAsync();
                    _logger.LogInformation("Función Lambda ejecutada correctamente con respuesta: {LambdaResponse}", lambdaResponse);

                    if (lambdaResponse.Contains("LambdaETL executed successfully"))
                    {
                        return Ok("Función Lambda ejecutada correctamente y proceso de ingesta de datos finalizado con éxito.");
                    }
                    else
                    {
                        return StatusCode(500, $"Función Lambda ejecutada, pero ocurrió un error: {lambdaResponse}");
                    }
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al verificar archivos y activar la función Lambda.");
                return StatusCode(500, "Error interno del servidor.");
            }
        }
    }
}
