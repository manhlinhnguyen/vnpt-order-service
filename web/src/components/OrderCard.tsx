import React from "react";
import { ordersApi, type OrderResponse } from "../api/ordersApi";

interface OrderCardProps {
  order: OrderResponse;
  onConfirm?: (orderId: string) => void;
}

/**
 * Hiển thị thông tin một đơn hàng VNPT.
 *
 * Copilot lab: thêm nút "Hủy đơn" khi implement cancelOrder endpoint.
 */
export const OrderCard: React.FC<OrderCardProps> = ({ order, onConfirm }) => {
  const statusColor: Record<OrderResponse["status"], string> = {
    PENDING: "#f59e0b",
    CONFIRMED: "#3b82f6",
    PROCESSING: "#8b5cf6",
    COMPLETED: "#10b981",
    CANCELLED: "#ef4444",
  };

  const statusLabel: Record<OrderResponse["status"], string> = {
    PENDING: "Chờ xác nhận",
    CONFIRMED: "Đã xác nhận",
    PROCESSING: "Đang xử lý",
    COMPLETED: "Hoàn thành",
    CANCELLED: "Đã hủy",
  };

  return (
    <div style={{ border: "1px solid #e5e7eb", borderRadius: 8, padding: 16, marginBottom: 12 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h3 style={{ margin: 0, fontSize: 16 }}>{order.orderId}</h3>
        <span
          style={{
            background: statusColor[order.status],
            color: "white",
            padding: "2px 10px",
            borderRadius: 12,
            fontSize: 12,
          }}
        >
          {statusLabel[order.status]}
        </span>
      </div>

      <p style={{ margin: "8px 0 4px", color: "#6b7280", fontSize: 13 }}>
        Khách hàng: <strong>{order.customerId}</strong>
      </p>

      {order.totalAmount !== undefined && (
        <p style={{ margin: "4px 0", fontSize: 13 }}>
          Cước:{" "}
          <strong>
            {new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(
              order.totalAmount
            )}
          </strong>
        </p>
      )}

      {order.shippingAddress && (
        <p style={{ margin: "4px 0", fontSize: 12, color: "#9ca3af" }}>{order.shippingAddress}</p>
      )}

      <div style={{ marginTop: 12, display: "flex", gap: 8 }}>
        {order.status === "PENDING" && onConfirm && (
          <button
            onClick={() => onConfirm(order.orderId)}
            style={{
              background: "#3b82f6",
              color: "white",
              border: "none",
              padding: "6px 14px",
              borderRadius: 6,
              cursor: "pointer",
            }}
          >
            Xác nhận
          </button>
        )}
      </div>
    </div>
  );
};
