-- ============================================================
-- V1__init_orders.sql — Flyway migration khởi tạo schema
-- ============================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Bảng đơn hàng chính
CREATE TABLE orders (
    order_id         VARCHAR(50) PRIMARY KEY,
    customer_id      VARCHAR(50) NOT NULL,
    items            TEXT,
    shipping_address VARCHAR(500),
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                     CHECK (status IN ('PENDING','CONFIRMED','PROCESSING','COMPLETED','CANCELLED')),
    estimated_delivery DATE,
    total_amount     NUMERIC(15, 2),
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_orders_customer_id ON orders (customer_id);
CREATE INDEX idx_orders_status      ON orders (status);
CREATE INDEX idx_orders_created_at  ON orders (created_at DESC);

-- Audit log thay đổi trạng thái
CREATE TABLE order_status_history (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id     VARCHAR(50) NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    old_status   VARCHAR(20),
    new_status   VARCHAR(20) NOT NULL,
    changed_by   VARCHAR(100),
    changed_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    reason       VARCHAR(500)
);

CREATE INDEX idx_osh_order_id ON order_status_history (order_id);

-- Trigger auto-update updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
