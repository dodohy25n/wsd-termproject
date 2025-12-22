
-- V2__seed_data.sql

-- 1. 테이블 생성
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
    `created_at` DATETIME(6),
    `modified_at` DATETIME(6),
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_store_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `item` (
    `item_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `price` INT NOT NULL,
    `description` TEXT,
    `image_url` VARCHAR(255),
    `is_sold_out` BOOLEAN DEFAULT FALSE,
    CONSTRAINT `fk_item_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
    `created_at` DATETIME(6),
    `modified_at` DATETIME(6),
    `created_by` VARCHAR(255),
    `last_modified_by` VARCHAR(255),
    CONSTRAINT `fk_coupon_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `coupon_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `coupon_id` BIGINT NOT NULL,
    `item_id` BIGINT NOT NULL,
    UNIQUE KEY `uk_coupon_item` (`coupon_id`, `item_id`),
    CONSTRAINT `fk_coupon_item_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`),
    CONSTRAINT `fk_coupon_item_item` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 데이터 삽입

-- Owner ID 조회
-- V1에서 생성된 owner@test.com 사용자의 ID를 가져옵니다.

INSERT INTO `store` (`user_id`, `name`, `address`, `latitude`, `longitude`, `phone_number`, `introduction`, `operating_hours`, `store_category`, `created_at`, `modified_at`)
SELECT `user_id`, '에브리데이 카페', '경기도 성남시 분당구 판교로 123', 37.4000, 127.1000, '010-1234-5678', '판교 최고의 커피 맛집입니다.', '{"mon": "09:00-18:00", "tue": "09:00-18:00"}', 'CAFE', NOW(), NOW()
FROM `user` WHERE `email` = 'owner@test.com' LIMIT 1;

-- Store ID 설정
SET @store_id = (SELECT `store_id` FROM `store` WHERE `name` = '에브리데이 카페' ORDER BY `store_id` DESC LIMIT 1);

-- 메뉴(Item) 10개 삽입
INSERT INTO `item` (`store_id`, `name`, `price`, `description`, `is_sold_out`) VALUES
(@store_id, '아메리카노', 3000, '깊고 진한 풍미의 커피', false),
(@store_id, '카페라떼', 3500, '부드러운 우유와 에스프레소의 조화', false),
(@store_id, '바닐라 라떼', 4000, '달콤한 바닐라 향이 가득', false),
(@store_id, '카라멜 마키아또', 4500, '진한 카라멜 소스의 달콤함', false),
(@store_id, '카푸치노', 3500, '풍성한 거품의 부드러움', false),
(@store_id, '에스프레소', 2500, '커피 본연의 강렬한 맛', false),
(@store_id, '콜드브루', 4000, '깔끔하고 시원한 맛', false),
(@store_id, '레모네이드', 4500, '상큼한 레몬의 톡 쏘는 맛', false),
(@store_id, '아이스티', 3500, '시원하고 달콤한 복숭아 맛', false),
(@store_id, '크로플', 5000, '바삭하고 쫀득한 크로와상 와플', false);

-- 쿠폰 삽입
-- 1. 일반 할인 쿠폰 (활성 상태)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`)
VALUES
(@store_id, '아메리카노 1000원 할인', '아메리카노 주문 시 1000원 할인해드립니다.', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW());

SET @coupon1_id = LAST_INSERT_ID();

-- 2. 샷 추가 무료 쿠폰 (예약 상태)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`)
VALUES
(@store_id, '샷 추가 무료', '음료 주문 시 샷 추가 무료 제공', 50, 2, 'GENERAL', 'SCHEDULED', NOW(), NOW());

SET @coupon2_id = LAST_INSERT_ID();

-- 3. 500원 할인 쿠폰 (일시중지 상태)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`)
VALUES
(@store_id, '전 메뉴 500원 할인', '모든 메뉴 500원 할인', 200, 1, 'GENERAL', 'STOPPED', NOW(), NOW());

SET @coupon3_id = LAST_INSERT_ID();

-- 4. 런치타임 할인 쿠폰 (만료 상태)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`)
VALUES
(@store_id, '런치타임 10% 할인', '점심시간(12:00~13:00) 방문 시 10% 할인', 30, 1, 'GENERAL', 'EXPIRED', NOW(), NOW());

SET @coupon4_id = LAST_INSERT_ID();

-- 5. 스탬프 쿠폰 (활성 상태)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`)
VALUES
(@store_id, '스탬프 10개 적립 시 무료 음료', '스탬프 모으기 이벤트', 1000, 1, 'STAMP', 'ACTIVE', NOW(), NOW());

SET @coupon5_id = LAST_INSERT_ID();

-- 쿠폰 적용 상품 연결
-- 쿠폰 1: 아메리카노 적용
INSERT INTO `coupon_item` (`coupon_id`, `item_id`)
SELECT @coupon1_id, `item_id` FROM `item` WHERE `store_id` = @store_id AND `name` = '아메리카노';

-- 쿠폰 2: 카페라떼, 카푸치노 적용
INSERT INTO `coupon_item` (`coupon_id`, `item_id`)
SELECT @coupon2_id, `item_id` FROM `item` WHERE `store_id` = @store_id AND (`name` = '카페라떼' OR `name` = '카푸치노');

-- 쿠폰 3: 모든 메뉴 적용 (예시로 3개 메뉴 연결)
INSERT INTO `coupon_item` (`coupon_id`, `item_id`)
SELECT @coupon3_id, `item_id` FROM `item` WHERE `store_id` = @store_id LIMIT 3;

-- 쿠폰 4, 5는 전체 적용이라 가정하거나 연결 없음 (비즈니스 로직에 따라 다름, 여기서는 편의상 생략 혹은 일부 연결)
-- 쿠폰 5: 아메리카노 연결
INSERT INTO `coupon_item` (`coupon_id`, `item_id`)
SELECT @coupon5_id, `item_id` FROM `item` WHERE `store_id` = @store_id AND `name` = '아메리카노';


