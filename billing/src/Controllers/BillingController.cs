using Vnpt.Billing.Models;
using Vnpt.Billing.Services;
using Microsoft.AspNetCore.Mvc;

namespace Vnpt.Billing.Controllers;

/// <summary>
/// REST Controller cho đối soát cước VNPT.
/// Controller KHÔNG chứa business logic — mọi logic trong ReconciliationService.
/// </summary>
[ApiController]
[Route("api/v1/billing")]
public class BillingController : ControllerBase
{
    private readonly IReconciliationService _reconciliationService;
    private readonly ILogger<BillingController> _logger;

    public BillingController(
        IReconciliationService reconciliationService,
        ILogger<BillingController> logger)
    {
        _reconciliationService = reconciliationService;
        _logger = logger;
    }

    /// <summary>
    /// POST /api/v1/billing/reconcile/{orderId}
    /// Đối soát cước một đơn hàng cụ thể.
    /// </summary>
    [HttpPost("reconcile/{orderId}")]
    [ProducesResponseType(typeof(ReconciliationResult), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<ReconciliationResult>> ReconcileOrder(
        string orderId,
        CancellationToken ct)
    {
        var result = await _reconciliationService.ReconcileAsync(orderId, ct);
        return Ok(result);
    }

    /// <summary>
    /// GET /api/v1/billing/pending?customerId={id}
    /// Lấy danh sách bản ghi cước chưa đối soát.
    /// </summary>
    [HttpGet("pending")]
    [ProducesResponseType(typeof(IEnumerable<BillingRecord>), StatusCodes.Status200OK)]
    public async Task<ActionResult<IEnumerable<BillingRecord>>> GetPendingRecords(
        [FromQuery] string customerId,
        CancellationToken ct)
    {
        var records = await _reconciliationService.GetPendingRecordsAsync(customerId, ct);
        return Ok(records);
    }
}
