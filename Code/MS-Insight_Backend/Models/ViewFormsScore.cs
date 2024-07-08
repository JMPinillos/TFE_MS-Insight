using System;
using System.Collections.Generic;

namespace MsInsightApi.Models;

public partial class ViewFormsScore
{
    public int FormId { get; set; }

    public string Title { get; set; } = null!;

    public double? MinimumScore { get; set; }

    public double? MaximumScore { get; set; }
}
