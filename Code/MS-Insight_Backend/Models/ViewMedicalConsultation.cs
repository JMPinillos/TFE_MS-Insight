using System;
using System.Collections.Generic;

namespace MsInsightApi.Models;

public partial class ViewMedicalConsultation
{
    public int PatientId { get; set; }

    public int FormId { get; set; }

    public DateTime CompletedAt { get; set; }

    public string Title { get; set; } = null!;

    public string? Value { get; set; }
}
