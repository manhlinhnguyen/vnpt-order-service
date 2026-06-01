-- ============================================================
-- VNPT Order Service — PostgreSQL Schema
-- Demo repo cho đào tạo GitHub Copilot
-- ============================================================

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ----------------------------------------------------------------
-- Bảng chính: orders
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    order_id         VARCHAR(50) PRIMARY KEY,
    customer_id      VARCHAR(50) NOT NULL,
    items            TEXT,                              -- JSON array dịch vụ đặt
    shipping_address VARCHAR(500),
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                     CHECK (status IN ('PENDING','CONFIRMED','PROCESSING','COMPLETED','CANCELLED')),
    estimated_delivery DATE,
    total_amount     NUMERIC(15, 2),                   -- Cước: dùng NUMERIC, không phải FLOAT
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Index cho query thường dùng
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders (customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_status      ON orders (status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at  ON orders (created_at DESC);

-- ----------------------------------------------------------------
-- Bảng audit: order_status_history
-- Mọi thay đổi trạng thái đều được ghi lại
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_status_history (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id     VARCHAR(50) NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    old_status   VARCHAR(20),
    new_status   VARCHAR(20) NOT NULL,
    changed_by   VARCHAR(100),
    changed_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    reason       VARCHAR(500)
);

CREATE INDEX IF NOT EXISTS idx_osh_order_id   ON order_status_history (order_id);
CREATE INDEX IF NOT EXISTS idx_osh_changed_at ON order_status_history (changed_at DESC);

-- ----------------------------------------------------------------
-- Trigger: tự động cập nhật updated_at khi UPDATE
-- ----------------------------------------------------------------
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_orders_updated_at ON orders;
CREATE TRIGGER trg_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
