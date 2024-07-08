using System;
using System.Collections.Generic;

namespace MsInsightApi.Models;

public partial class ViewPatientsDatum
{
    public int PatientId { get; set; }

    public string? Gender { get; set; }

    public DateTime BirthDate { get; set; }

    public DateTime OnsetSymptoms { get; set; }

    public string? BrainMri { get; set; }

    public string? SpinalMri { get; set; }
}
