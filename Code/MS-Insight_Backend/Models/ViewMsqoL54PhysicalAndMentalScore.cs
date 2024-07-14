using System;
using System.Collections.Generic;

namespace MsInsightApi.Models;

public partial class ViewMsqoL54PhysicalAndMentalScore
{
    public int NumeroPaciente { get; set; }

    public string Test { get; set; } = null!;

    public DateTime Completado { get; set; }

    public double? SaludFisica { get; set; }

    public double? SaludMental { get; set; }
}
