using Amazon.S3;
using Amazon.Lambda;
using Microsoft.EntityFrameworkCore;
using MsInsightApi.Models;
using MsInsightApi.Repositories;
using MsInsightApi.Repositories.Interfaces;
using MsInsightApi.Services;
using MsInsightApi.Services.Interfaces;

var builder = WebApplication.CreateBuilder(args);

// Cargar configuraciones
builder.Configuration
    .SetBasePath(Directory.GetCurrentDirectory())
    .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
    .AddJsonFile($"appsettings.{builder.Environment.EnvironmentName}.json", optional: true)
    .AddEnvironmentVariables();

// Configuración del Logger de AWS
builder.Logging.AddAWSProvider(builder.Configuration.GetAWSLoggingConfigSection(),
    formatter: (logLevel, message, exception) => $"[{DateTime.UtcNow}] {logLevel}: {message}");

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Configurar el servicio de Amazon S3 y Lambda
builder.Services.AddDefaultAWSOptions(builder.Configuration.GetAWSOptions());
builder.Services.AddAWSService<IAmazonS3>();
builder.Services.AddAWSService<IAmazonLambda>();

// Agregar el servicio CORS (Cross-Origin Resource Sharing)
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAllOrigins",
        builder =>
        {
            builder.AllowAnyOrigin() // Permite todos los orígenes
                .AllowAnyMethod() // Permite todos los métodos HTTP
                .AllowAnyHeader(); // Permite todas las cabeceras
        });
});

// Configuración de la conexión a MySQL
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");
builder.Services.AddDbContext<MsInsightContext>(options =>
    options.UseMySQL(connectionString)); // Aquí usamos UseMySQL

// Servicios inyectados
builder.Services.AddScoped<IScoresService, ScoresService>();
builder.Services.AddScoped<IViewsRepository, ViewRepository>();
builder.Services.AddScoped<IFilesManagementService, FilesManagementService>();

var app = builder.Build();

// Usar CORS
app.UseCors("AllowAllOrigins");

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseAuthorization();
app.MapControllers();
app.Run();