---
description: "Tạo Flyway migration script mới cho PostgreSQL."
---

# DB Migration Scaffold

Tạo migration script mới theo chuẩn Flyway.

## Quy Tắc

- Tên file: `V<next_number>__<mô_tả_ngắn>.sql` (double underscore, lowercase, no space)
- Đặt tại `db/migrations/`
- Script phải có comment giải thích mục đích ở đầu

## Input Cần Thiết

1. Mô tả thay đổi schema là gì?
2. Bảng nào bị ảnh hưởng?
3. Có cần data migration không?

## Template DDL

```sql
-- Migration: <mô tả>
-- Date: <YYYY-MM-DD>
-- Author: <tên>

-- Thêm cột mới
ALTER TABLE orders
    ADD COLUMN <column_name> <TYPE> [NOT NULL] [DEFAULT <value>];

-- Thêm index cho cột hay query
CREATE INDEX IF NOT EXISTS idx_orders_<column>
    ON orders (<column_name>);
```

## Template tạo bảng mới

```sql
CREATE TABLE <table_name> (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    -- ... các cột nghiệp vụ
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_<table>_<col> ON <table_name> (<column>);
```

## Lưu Ý VNPT

- Cột tiền/cước: `NUMERIC(15, 2)` — không phải `FLOAT` hay `DECIMAL`
- `status` column: dùng `VARCHAR(20)` với CHECK constraint
- Mọi bảng cần audit: thêm `changed_by VARCHAR(100)` và trigger `updated_at`
