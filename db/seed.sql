-- ============================================================
-- seed.sql — Dữ liệu mẫu cho development/demo
-- Chạy SAU khi đã apply V1__init_orders.sql
-- KHÔNG chạy trên production
-- ============================================================

INSERT INTO orders (order_id, customer_id, items, shipping_address, status, total_amount)
VALUES
    ('ORD-DEMO-001', 'CUST-001', 'GOI_INTERNET_100M,GOI_TRUYEN_HINH',
     '123 Hoàng Quốc Việt, Cầu Giấy, Hà Nội', 'PENDING', 350000.00),

    ('ORD-DEMO-002', 'CUST-001', 'GOI_DIEN_THOAI_CO_DINH',
     '123 Hoàng Quốc Việt, Cầu Giấy, Hà Nội', 'CONFIRMED', 120000.00),

    ('ORD-DEMO-003', 'CUST-002', 'GOI_INTERNET_DOANH_NGHIEP_1G',
     '456 Nguyễn Văn Linh, Đà Nẵng', 'PROCESSING', 2500000.00),

    ('ORD-DEMO-004', 'CUST-002', 'GOI_CLOUD_STORAGE_100G',
     '456 Nguyễn Văn Linh, Đà Nẵng', 'COMPLETED', 150000.00);

-- Audit log mẫu
INSERT INTO order_status_history (order_id, old_status, new_status, changed_by)
VALUES
    ('ORD-DEMO-002', 'PENDING', 'CONFIRMED', 'system'),
    ('ORD-DEMO-003', 'PENDING', 'CONFIRMED', 'agent-001'),
    ('ORD-DEMO-003', 'CONFIRMED', 'PROCESSING', 'agent-001'),
    ('ORD-DEMO-004', 'PENDING', 'CONFIRMED', 'system'),
    ('ORD-DEMO-004', 'CONFIRMED', 'PROCESSING', 'system'),
    ('ORD-DEMO-004', 'PROCESSING', 'COMPLETED', 'system');
