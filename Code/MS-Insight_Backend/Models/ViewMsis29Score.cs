using System;
using System.Collections.Generic;

namespace MsInsightApi.Models;

public partial class ViewMsis29Score
{
    public int NumeroPaciente { get; set; }

    public string Test { get; set; } = null!;

    public DateTime Completado { get; set; }

    public double? Score { get; set; }
}
