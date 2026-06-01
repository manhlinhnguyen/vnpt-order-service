using Vnpt.Billing.Models;
using Vnpt.Billing.Services;

namespace Vnpt.Billing.Models;

/// <summary>
/// Bản ghi cước viễn thông cần đối soát với đơn hàng.
/// Lưu ý: Amount dùng decimal — không dùng float/double.
/// </summary>
public class BillingRecord
{
    public string RecordId { get; set; } = default!;
    public string CustomerId { get; set; } = default!;
    public string OrderId { get; set; } = default!;
    public decimal Amount { get; set; }
    public string Currency { get; set; } = "VND";
    public DateTime BillingDate { get; set; }
    public string Status { get; set; } = "PENDING";
    public string? Notes { get; set; }
}

/// <summary>
/// Kết quả đối soát một đơn hàng.
/// </summary>
public class ReconciliationResult
{
    public string OrderId { get; set; } = default!;
    public bool IsMatched { get; set; }
    public decimal? Variance { get; set; }
    public string? Reason { get; set; }
    public DateTime ProcessedAt { get; set; } = DateTime.UtcNow;
}
