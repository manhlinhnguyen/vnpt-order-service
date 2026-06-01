import React, { useEffect, useState } from "react";
import { ordersApi, type OrderResponse } from "../api/ordersApi";
import { OrderCard } from "../components/OrderCard";

/**
 * Trang danh sách đơn hàng — portal CSKH VNPT.
 *
 * Copilot lab: thêm form tạo đơn mới ở đây.
 */
export const OrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [customerId, setCustomerId] = useState("CUST-001");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loadOrders = async () => {
    if (!customerId.trim()) return;
    setLoading(true);
    setError(null);
    try {
      const data = await ordersApi.getOrdersByCustomer(customerId);
      setOrders(data);
    } catch (err) {
      setError("Không thể tải danh sách đơn hàng. Kiểm tra kết nối backend.");
    } finally {
      setLoading(false);
    }
  };

  const handleConfirm = async (orderId: string) => {
    try {
      await ordersApi.confirmOrder(orderId);
      await loadOrders(); // refresh
    } catch {
      setError("Không thể xác nhận đơn hàng.");
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: 24 }}>
      <h1 style={{ fontSize: 24, marginBottom: 20 }}>Quản Lý Đơn Hàng VNPT</h1>

      <div style={{ display: "flex", gap: 8, marginBottom: 24 }}>
        <input
          value={customerId}
          onChange={(e) => setCustomerId(e.target.value)}
          placeholder="Nhập mã khách hàng..."
          style={{ flex: 1, padding: "8px 12px", border: "1px solid #d1d5db", borderRadius: 6 }}
        />
        <button
          onClick={loadOrders}
          disabled={loading}
          style={{
            background: "#1d4ed8",
            color: "white",
            border: "none",
            padding: "8px 20px",
            borderRadius: 6,
            cursor: "pointer",
          }}
        >
          {loading ? "Đang tải..." : "Tìm kiếm"}
        </button>
      </div>

      {error && (
        <div style={{ background: "#fef2f2", border: "1px solid #fecaca", borderRadius: 6, padding: 12, marginBottom: 16, color: "#dc2626" }}>
          {error}
        </div>
      )}

      {orders.length === 0 && !loading && (
        <p style={{ color: "#6b7280" }}>Không có đơn hàng nào.</p>
      )}

      {orders.map((order) => (
        <OrderCard key={order.orderId} order={order} onConfirm={handleConfirm} />
      ))}
    </div>
  );
};
