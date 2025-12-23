-- V2__seed_data.sql
-- 초기 데이터 적재

-- 1. User 시드 데이터 (3명)
-- 비밀번호는 '1234'를 BCrypt로 암호화한 값: $2a$10$ko/DIWEzHhEfOwbiYnn6c.qfF3b10A5E4TK6Bm9PUU26GI49/xSo.
INSERT INTO `user` (`username`, `password`, `email`, `name`, `phone_number`, `role`, `created_at`, `modified_at`, `created_by`, `last_modified_by`)
VALUES
('admin@test.com', '$2a$10$ko/DIWEzHhEfOwbiYnn6c.qfF3b10A5E4TK6Bm9PUU26GI49/xSo.', 'admin@test.com', 'Admin User', '010-0000-0001', 'ROLE_ADMIN', NOW(), NOW(), 'system', 'system'),
('owner@test.com', '$2a$10$ko/DIWEzHhEfOwbiYnn6c.qfF3b10A5E4TK6Bm9PUU26GI49/xSo.', 'owner@test.com', 'Owner User', '010-0000-0002', 'ROLE_OWNER', NOW(), NOW(), 'system', 'system'),
('customer@test.com', '$2a$10$ko/DIWEzHhEfOwbiYnn6c.qfF3b10A5E4TK6Bm9PUU26GI49/xSo.', 'customer@test.com', 'Customer User', '010-0000-0003', 'ROLE_CUSTOMER', NOW(), NOW(), 'system', 'system');

-- 2. Store 시드 데이터 (1개)
-- owner@test.com 사용자가 소유한 상점
SET @owner_id = (SELECT `user_id` FROM `user` WHERE `email` = 'owner@test.com');

INSERT INTO `store` (`user_id`, `name`, `address`, `latitude`, `longitude`, `phone_number`, `introduction`, `operating_hours`, `store_category`, `created_at`, `modified_at`, `created_by`, `last_modified_by`)
VALUES
(@owner_id, '에브리데이 카페', '경기도 성남시 분당구 판교로 123', 37.4000, 127.1000, '010-1234-5678', '판교 최고의 커피 맛집입니다.', '{"mon": "09:00-18:00", "tue": "09:00-18:00", "wed": "09:00-18:00", "thu": "09:00-18:00", "fri": "09:00-18:00", "sat": "10:00-20:00", "sun": "CLOSED"}', 'CAFE', NOW(), NOW(), 'system', 'system');

SET @store_id = LAST_INSERT_ID();

-- 3. Item 시드 데이터 (10개)
INSERT INTO `item` (`store_id`, `name`, `price`, `description`, `is_sold_out`)
VALUES
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

-- 4. Coupon 시드 데이터 (5개)
-- 4.1. 일반 할인 쿠폰 (Active)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`, `created_by`, `last_modified_by`)
VALUES
(@store_id, '아메리카노 1000원 할인', '아메리카노 주문 시 1000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW(), 'system', 'system');
SET @coupon1_id = LAST_INSERT_ID();

-- 4.2. 샷 추가 무료 (Scheduled)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`, `created_by`, `last_modified_by`)
VALUES
(@store_id, '샷 추가 무료', '음료 주문 시 샷 추가 무료 제공', 50, 2, 'GENERAL', 'SCHEDULED', NOW(), NOW(), 'system', 'system');
SET @coupon2_id = LAST_INSERT_ID();

-- 4.3. 전 메뉴 500원 할인 (Stopped)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`, `created_by`, `last_modified_by`)
VALUES
(@store_id, '전 메뉴 500원 할인', '모든 메뉴 500원 할인', 200, 1, 'GENERAL', 'STOPPED', NOW(), NOW(), 'system', 'system');
SET @coupon3_id = LAST_INSERT_ID();

-- 4.4. 런치타임 할인 (Expired)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`, `created_by`, `last_modified_by`)
VALUES
(@store_id, '런치타임 10% 할인', '점심시간 10% 할인', 30, 1, 'GENERAL', 'EXPIRED', NOW(), NOW(), 'system', 'system');
SET @coupon4_id = LAST_INSERT_ID();

-- 4.5. 5000원 이상 구매시 10% 할인 (Active, 선착순)
INSERT INTO `coupon` (`store_id`, `title`, `description`, `total_quantity`, `limit_per_user`, `type`, `status`, `created_at`, `modified_at`, `created_by`, `last_modified_by`)
VALUES
(@store_id, '5000원 이상 구매시 10% 할인', '5000원 이상 구매 시 사용 가능', 500, 1, 'FCFS', 'ACTIVE', NOW(), NOW(), 'system', 'system');
SET @coupon5_id = LAST_INSERT_ID();


-- 5. Coupon-Item 연결 (적용 대상 상품)
-- 쿠폰1 (아메리카노 1000원 할인) -> 아메리카노 아이템 연결
INSERT INTO `coupon_item` (`coupon_id`, `item_id`)
SELECT @coupon1_id, `item_id` FROM `item` WHERE `name` = '아메리카노' AND `store_id` = @store_id;

-- 쿠폰2 (샷 추가 무료) -> 카페라떼, 카푸치노, 바닐라 라떼 연결
INSERT INTO `coupon_item` (`coupon_id`, `item_id`)
SELECT @coupon2_id, `item_id` FROM `item` WHERE `name` IN ('카페라떼', '카푸치노', '바닐라 라떼') AND `store_id` = @store_id;

-- 쿠폰3 (전 메뉴 500원 할인) -> 전체 아이템 중 상위 5개 연결 (예시)
INSERT INTO `coupon_item` (`coupon_id`, `item_id`)
SELECT @coupon3_id, `item_id` FROM `item` WHERE `store_id` = @store_id LIMIT 5;

-- 쿠폰4, 5는 연결 없음 (전체 적용 로직 가정 또는 데이터 없음)

