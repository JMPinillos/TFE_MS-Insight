using System;
using System.Collections.Generic;

namespace MsInsightApi.Models;

public partial class ViewFssScore
{
    public int NumeroPaciente { get; set; }

    public string Test { get; set; } = null!;

    public DateTime Completado { get; set; }

    public double? Score { get; set; }
}
