import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const http = axios.create({
  baseURL: `${API_BASE}/api/v1`,
  headers: { "Content-Type": "application/json" },
  timeout: 10_000,
});

// DTO types — khớp với OrderResponse.java
export interface OrderResponse {
  orderId: string;
  customerId: string;
  itemsJson: string;
  shippingAddress?: string;
  status: "PENDING" | "CONFIRMED" | "PROCESSING" | "COMPLETED" | "CANCELLED";
  estimatedDelivery?: string;
  totalAmount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateOrderRequest {
  customerId: string;
  items: string[];
  shippingAddress?: string;
  estimatedDelivery?: string;
  totalAmount?: number;
}

// API calls — tất cả đều async, không fetch trực tiếp trong component
export const ordersApi = {
  getOrder: async (orderId: string): Promise<OrderResponse> => {
    const { data } = await http.get<OrderResponse>(`/orders/${orderId}`);
    return data;
  },

  getOrdersByCustomer: async (customerId: string): Promise<OrderResponse[]> => {
    const { data } = await http.get<OrderResponse[]>("/orders", {
      params: { customerId },
    });
    return data;
  },

  createOrder: async (request: CreateOrderRequest): Promise<OrderResponse> => {
    const { data } = await http.post<OrderResponse>("/orders", request);
    return data;
  },

  confirmOrder: async (orderId: string): Promise<OrderResponse> => {
    const { data } = await http.post<OrderResponse>(`/orders/${orderId}/confirm`);
    return data;
  },
};
