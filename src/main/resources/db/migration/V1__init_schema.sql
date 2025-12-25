-- V1__init_schema.sql
-- 전체 엔티티 스키마 정의 (MySQL 8.0)

-- 1. University (단과대학/학교 정보)
CREATE TABLE IF NOT EXISTS `university` (
    `university_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `email_domain` VARCHAR(255) UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Affiliation (소속 - 학과, 동아리 등)
CREATE TABLE IF NOT EXISTS `affiliation` (
    `affiliation_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `university_id` BIGINT NOT NULL,
    `category` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `expires_at` DATETIME(6),
    `created_at` DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_affiliation_university` FOREIGN KEY (`university_id`) REFERENCES `university` (`university_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. User (사용자)
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
    `created_at` DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. UserAffiliation (사용자 소속 연결)
CREATE TABLE IF NOT EXISTS `user_affiliation` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `affiliation_id` BIGINT NOT NULL,
    CONSTRAINT `fk_user_affiliation_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_user_affiliation_affiliation` FOREIGN KEY (`affiliation_id`) REFERENCES `affiliation` (`affiliation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Store (상점)
CREATE TABLE IF NOT EXISTS `store` (
    `store_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `latitude` DOUBLE,
    `longitude` DOUBLE,
    `phone_number` VARCHAR(255),
    `introduction` TEXT,
    `operating_hours` TEXT,
    `store_category` VARCHAR(50) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_store_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. StoreAffiliation (상점 제휴 소속)
CREATE TABLE IF NOT EXISTS `store_affiliation` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL,
    `affiliation_id` BIGINT NOT NULL,
    CONSTRAINT `uk_store_affiliation` UNIQUE (`store_id`, `affiliation_id`),
    CONSTRAINT `fk_store_affiliation_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`),
    CONSTRAINT `fk_store_affiliation_affiliation` FOREIGN KEY (`affiliation_id`) REFERENCES `affiliation` (`affiliation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. StoreImage (상점 이미지)
CREATE TABLE IF NOT EXISTS `store_image` (
    `store_image_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL,
    `image_url` VARCHAR(255) NOT NULL,
    `is_thumbnail` BOOLEAN NOT NULL DEFAULT FALSE,
    `order_index` INT NOT NULL DEFAULT 0,
    CONSTRAINT `fk_store_image_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Item (상품/메뉴)
CREATE TABLE IF NOT EXISTS `item` (
    `item_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `price` INT NOT NULL,
    `description` TEXT,
    `image_url` VARCHAR(255),
    `is_sold_out` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_item_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Coupon (쿠폰)
CREATE TABLE IF NOT EXISTS `coupon` (
    `coupon_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `target_affiliation_id` BIGINT,
    `issue_starts_at` DATETIME(6),
    `issue_ends_at` DATETIME(6),
    `total_quantity` INT NOT NULL,
    `limit_per_user` INT NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_coupon_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`),
    CONSTRAINT `fk_coupon_affiliation` FOREIGN KEY (`target_affiliation_id`) REFERENCES `affiliation` (`affiliation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. CouponItem (쿠폰 적용 상품)
CREATE TABLE IF NOT EXISTS `coupon_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `coupon_id` BIGINT NOT NULL,
    `item_id` BIGINT NOT NULL,
    CONSTRAINT `uk_coupon_item` UNIQUE (`coupon_id`, `item_id`),
    CONSTRAINT `fk_coupon_item_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`),
    CONSTRAINT `fk_coupon_item_item` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. CustomerCoupon (발급된 쿠폰)
CREATE TABLE IF NOT EXISTS `customer_coupon` (
    `customer_coupon_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `coupon_id` BIGINT NOT NULL,
    `coupon_code` VARCHAR(255) NOT NULL UNIQUE,
    `status` VARCHAR(50) NOT NULL,
    `issued_at` DATETIME(6),
    `used_at` DATETIME(6),
    `expires_at` DATETIME(6) NOT NULL,
    CONSTRAINT `fk_customer_coupon_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_customer_coupon_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. Review (리뷰)
CREATE TABLE IF NOT EXISTS `review` (
    `review_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `store_id` BIGINT NOT NULL,
    `customer_coupon_id` BIGINT UNIQUE,
    `parent_review_id` BIGINT,
    `rating` INT NOT NULL,
    `content` TEXT,
    `is_private` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_review_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`),
    CONSTRAINT `fk_review_customer_coupon` FOREIGN KEY (`customer_coupon_id`) REFERENCES `customer_coupon` (`customer_coupon_id`),
    CONSTRAINT `fk_review_parent` FOREIGN KEY (`parent_review_id`) REFERENCES `review` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. FavoriteStore (즐겨찾기 상점)
CREATE TABLE IF NOT EXISTS `favorite_store` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `store_id` BIGINT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_favorite_store_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `fk_favorite_store_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
