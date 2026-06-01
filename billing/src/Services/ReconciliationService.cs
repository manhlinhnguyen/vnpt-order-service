using Vnpt.Billing.Models;

namespace Vnpt.Billing.Services;

/// <summary>
/// Interface cho đối soát cước VNPT.
/// Inject qua constructor — không dùng service locator.
/// </summary>
public interface IReconciliationService
{
    Task<ReconciliationResult> ReconcileAsync(string orderId, CancellationToken ct = default);
    Task<IEnumerable<BillingRecord>> GetPendingRecordsAsync(string customerId, CancellationToken ct = default);
}

/// <summary>
/// Dịch vụ đối soát cước viễn thông VNPT.
///
/// Copilot lab §3.4: dùng file này để demo .NET context trong GitHub Copilot.
/// Yêu cầu Copilot: "Thêm logic tính phí bậc thang cho gói internet".
/// </summary>
public class ReconciliationService : IReconciliationService
{
    private readonly ILogger<ReconciliationService> _logger;

    // TODO (Copilot lab): inject IBillingRepository để truy cập DB thật
    public ReconciliationService(ILogger<ReconciliationService> logger)
    {
        _logger = logger;
    }

    /// <summary>
    /// Đối soát cước một đơn hàng với hệ thống billing.
    /// </summary>
    public async Task<ReconciliationResult> ReconcileAsync(string orderId, CancellationToken ct = default)
    {
        _logger.LogInformation("Bắt đầu đối soát cước cho đơn hàng {OrderId}", orderId);

        // STUB: gọi billing system thật trong production
        await Task.Delay(50, ct); // Simulate I/O

        // Copilot lab: thay stub này bằng logic đối soát thực tế
        var result = new ReconciliationResult
        {
            OrderId = orderId,
            IsMatched = true,
            Variance = 0,
            Reason = "Auto-reconciled (stub)"
        };

        _logger.LogInformation(
            "Đối soát cước hoàn tất: OrderId={OrderId} IsMatched={IsMatched}",
            orderId, result.IsMatched);

        return result;
    }

    /// <summary>
    /// Lấy danh sách bản ghi cước chưa đối soát của khách hàng.
    /// </summary>
    public async Task<IEnumerable<BillingRecord>> GetPendingRecordsAsync(
        string customerId, CancellationToken ct = default)
    {
        _logger.LogInformation("Lấy bản ghi cước PENDING cho customerId={CustomerId}", customerId);

        await Task.Delay(20, ct); // Simulate I/O

        // STUB: trả empty — thay bằng EF Core query
        return Enumerable.Empty<BillingRecord>();
    }
}
