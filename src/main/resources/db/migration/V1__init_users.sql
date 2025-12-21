CREATE TABLE IF NOT EXISTS `user` (
    `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255),
    `email` VARCHAR(255) UNIQUE,
    `name` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255),
    `role` VARCHAR(50) NOT NULL,
    `social_type` VARCHAR(50),
    `social_id` VARCHAR(255),
    `created_at` DATETIME(6),
    `modified_at` DATETIME(6),
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default users
-- Password is '1234' hashed with BCrypt
INSERT INTO `user` (`username`, `password`, `email`, `name`, `role`, `created_at`, `modified_at`) VALUES
('admin@test.com', '$2a$10$ko/DIWEzHhEfOwbiYnn6c.qfF3b10A5E4TK6Bm9PUU26GI49/xSo.', 'admin@test.com', 'Admin User', 'ROLE_ADMIN', NOW(), NOW()),
('owner@test.com', '$2a$10$ko/DIWEzHhEfOwbiYnn6c.qfF3b10A5E4TK6Bm9PUU26GI49/xSo.', 'owner@test.com', 'Owner User', 'ROLE_OWNER', NOW(), NOW()),
('customer@test.com', '$2a$10$ko/DIWEzHhEfOwbiYnn6c.qfF3b10A5E4TK6Bm9PUU26GI49/xSo.', 'customer@test.com', 'Customer User', 'ROLE_CUSTOMER', NOW(), NOW());
