-- V2__seed_data.sql
-- 1. Users (Admin)
INSERT INTO `user` (username, password, name, phone_number, role, social_type, created_at, modified_at) VALUES 
('admin@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '관리자', '010-0000-0000', 'ROLE_ADMIN', 'LOCAL', NOW(), NOW());

-- 2. Users (Owners - 10)
INSERT INTO `user` (username, password, name, phone_number, role, social_type, created_at, modified_at) VALUES 
('owner1@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '김점주', '010-1000-0001', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner2@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '이점주', '010-1000-0002', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner3@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '박점주', '010-1000-0003', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner4@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '최점주', '010-1000-0004', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner5@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '정점주', '010-1000-0005', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner6@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '강점주', '010-1000-0006', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner7@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '조점주', '010-1000-0007', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner8@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '윤점주', '010-1000-0008', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner9@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '장점주', '010-1000-0009', 'ROLE_OWNER', 'LOCAL', NOW(), NOW()),
('owner10@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '임점주', '010-1000-0010', 'ROLE_OWNER', 'LOCAL', NOW(), NOW());

-- 3. Users (Customers - 20)
INSERT INTO `user` (username, password, name, phone_number, role, social_type, created_at, modified_at) VALUES 
('cust1@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '홍길동', '010-2000-0001', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust2@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '김철수', '010-2000-0002', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust3@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '이영희', '010-2000-0003', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust4@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '박민수', '010-2000-0004', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust5@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '최지우', '010-2000-0005', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust6@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '정우성', '010-2000-0006', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust7@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '한지민', '010-2000-0007', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust8@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '송강호', '010-2000-0008', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust9@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '전지현', '010-2000-0009', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust10@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '현빈', '010-2000-0010', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust11@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '손예진', '010-2000-0011', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust12@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '공유', '010-2000-0012', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust13@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '김고은', '010-2000-0013', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust14@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '박서준', '010-2000-0014', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust15@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '아이유', '010-2000-0015', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust16@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '수지', '010-2000-0016', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust17@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '김수현', '010-2000-0017', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust18@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '박보검', '010-2000-0018', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust19@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '송중기', '010-2000-0019', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW()),
('cust20@wsd.com', '$2a$10$1aCJl/Qo/M/ZkhjxLCAhTuh1UBnQ7.1j60DkRl830pWsiYfVcSQ32', '송혜교', '010-2000-0020', 'ROLE_CUSTOMER', 'LOCAL', NOW(), NOW());

-- 4. Stores (10 Stores, linked to Owners 2-11)
-- StoreCategory: BAR, CAFE, RESTAURANT, ETC
INSERT INTO `store` (user_id, name, address, phone_number, store_category, created_at, modified_at) VALUES 
(2, '버거킹 강남점', '서울 강남구 강남대로 123', '02-111-1111', 'RESTAURANT', NOW(), NOW()),
(3, '스타벅스 마포점', '서울 마포구 마포대로 456', '02-222-2222', 'CAFE', NOW(), NOW()),
(4, '맥도날드 서초점', '서울 서초구 서초대로 789', '02-333-3333', 'RESTAURANT', NOW(), NOW()),
(5, '서브웨이 해운대점', '부산 해운대구 해운대해변로 100', '051-444-4444', 'RESTAURANT', NOW(), NOW()),
(6, '도미노피자 송도점', '인천 연수구 송도과학로 200', '032-555-5555', 'RESTAURANT', NOW(), NOW()),
(7, 'KFC 동성로점', '대구 중구 동성로 300', '053-666-6666', 'RESTAURANT', NOW(), NOW()),
(8, '피자헛 상무점', '광주 서구 상무중앙로 400', '062-777-7777', 'RESTAURANT', NOW(), NOW()),
(9, '배스킨라빈스 둔산점', '대전 서구 둔산로 500', '042-888-8888', 'CAFE', NOW(), NOW()),
(10, '던킨도너츠 삼산점', '울산 남구 삼산로 600', '052-999-9999', 'CAFE', NOW(), NOW()),
(11, '공차 수원인계점', '경기 수원시 팔달구 인계로 700', '031-000-0000', 'CAFE', NOW(), NOW());

-- 5. Items (50 Items, 5 per Store)
-- Store 1 (버거킹)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES 
(1, '와퍼', 8000, '불맛 가득한 버거킹 대표 메뉴', false, NOW(), NOW()),
(1, '치즈버거', 5000, '클래식한 치즈의 풍미', false, NOW(), NOW()),
(1, '감자튀김', 3000, '바삭바삭한 감자튀김', false, NOW(), NOW()),
(1, '코카콜라', 2000, '시원한 탄산 음료', false, NOW(), NOW()),
(1, '너겟킹', 4000, '바삭한 치킨 너겟', false, NOW(), NOW());
-- Store 2 (스타벅스)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES 
(2, '아메리카노', 4500, '깔끔한 에스프레소의 맛', false, NOW(), NOW()),
(2, '카페라떼', 5000, '부드러운 우유와 에스프레소', false, NOW(), NOW()),
(2, '카푸치노', 5000, '몽실몽실한 거품 가득', false, NOW(), NOW()),
(2, '카페모카', 5500, '달콤한 초콜릿 풍미', false, NOW(), NOW()),
(2, '그린티', 4000, '제주 유기농 녹차', false, NOW(), NOW());
-- Store 3 (맥도날드)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES 
(3, '빅맥', 6000, '두 장의 쇠고기 패티', false, NOW(), NOW()),
(3, '맥치킨', 5000, '담백한 치킨 버거', false, NOW(), NOW()),
(3, '맥플러리', 3000, '달콤한 오레오 아이스크림', false, NOW(), NOW()),
(3, '애플파이', 2000, '따뜻한 사과 파이', false, NOW(), NOW()),
(3, '해쉬브라운', 1500, '아침에만 만나는 감자', false, NOW(), NOW());
-- Store 4 (서브웨이)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES
(4, '서브웨이 클럽', 7000, '터키, 햄, 로스트비프의 조화', false, NOW(), NOW()),
(4, '베지 딜라이트', 6000, '신선한 야채가 가득', false, NOW(), NOW()),
(4, '쿠키', 1000, '쫀득한 초코칩 쿠키', false, NOW(), NOW()),
(4, '칩', 1500, '바삭한 감자칩', false, NOW(), NOW()),
(4, '탄산음료', 2000, '리필 가능한 음료', false, NOW(), NOW());
-- Store 5 (도미노피자)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES
(5, '페퍼로니 피자', 20000, '짭짤한 페퍼로니의 맛', false, NOW(), NOW()),
(5, '치즈 피자', 18000, '풍부한 모짜렐라 치즈', false, NOW(), NOW()),
(5, '핫봉', 8000, '매콤한 치킨 윙', false, NOW(), NOW()),
(5, '스파게티', 9000, '토마토 오븐 스파게티', false, NOW(), NOW()),
(5, '피클', 500, '아삭한 수제 피클', false, NOW(), NOW());
-- Store 6 (KFC)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES
(6, '오리지널 치킨', 18000, '11가지 비밀 양념', false, NOW(), NOW()),
(6, '핫크리스피', 19000, '매콤하고 바삭한 맛', false, NOW(), NOW()),
(6, '비스켓', 3000, '버터 풍미 가득 비스켓', false, NOW(), NOW()),
(6, '코울슬로', 2000, '새콤달콤 양배추 샐러드', false, NOW(), NOW()),
(6, '콘샐러드', 2000, '톡톡 터지는 옥수수', false, NOW(), NOW());
-- Store 7 (피자헛)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES
(7, '슈프림 피자', 25000, '모든 토핑이 듬뿍', false, NOW(), NOW()),
(7, '바비큐 치킨', 24000, '달콤한 바비큐 소스', false, NOW(), NOW()),
(7, '리치 치즈 파스타', 8000, '치즈 오븐 파스타', false, NOW(), NOW()),
(7, '샐러드바', 5000, '다양한 샐러드 무제한', false, NOW(), NOW()),
(7, '갈릭 브레드', 3000, '마늘향 가득한 빵', false, NOW(), NOW());
-- Store 8 (배스킨라빈스)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES
(8, '민트 초콜릿 칩', 4000, '호불호 갈리는 상쾌함', false, NOW(), NOW()),
(8, '슈팅스타', 4000, '입안에서 톡톡 터지는 맛', false, NOW(), NOW()),
(8, '바닐라', 4000, '기본에 충실한 맛', false, NOW(), NOW()),
(8, '초콜릿', 4000, '진한 초콜릿 아이스크림', false, NOW(), NOW()),
(8, '베리베리 스트로베리', 4000, '상큼한 딸기 과육', false, NOW(), NOW());
-- Store 9 (던킨도너츠)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES
(9, '글레이즈드', 1500, '달콤하고 부드러운 맛', false, NOW(), NOW()),
(9, '카카오 후로스티드', 1800, '진한 초코 코팅', false, NOW(), NOW()),
(9, '바바리안 필드', 2000, '부드러운 크림 가득', false, NOW(), NOW()),
(9, '던킨 커피', 3000, '도넛과 어울리는 커피', false, NOW(), NOW()),
(9, '먼치킨', 5000, '한입 크기 미니 도넛', false, NOW(), NOW());
-- Store 10 (공차)
INSERT INTO `item` (store_id, name, price, description, is_sold_out, created_at, modified_at) VALUES
(10, '블랙 밀크티', 4500, '쫀득한 펄 추가 필수', false, NOW(), NOW()),
(10, '타로 밀크티', 4500, '고소하고 달콤한 타로', false, NOW(), NOW()),
(10, '제주 그린티', 4000, '쌉싸름한 녹차 맛', false, NOW(), NOW()),
(10, '우롱티', 4000, '구수한 우롱차', false, NOW(), NOW()),
(10, '망고 스무디', 5500, '시원한 망고 스무디', false, NOW(), NOW());

-- 6. Coupons (50 Coupons, 5 per Store)
-- CouponType: GENERAL, AFFILIATION, STAMP
-- CouponStatus: ACTIVE, DRAFT, SCHEDULED, STOPPED, EXPIRED
INSERT INTO `coupon` (store_id, title, description, total_quantity, limit_per_user, type, status, created_at, modified_at) VALUES
(1, '와퍼 반값 할인', '와퍼 단품 50% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (1, '감자튀김 무료', '세트 주문 시 감자튀김 무료', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (1, '세트 업그레이드', '단품 구매 시 세트 업그레이드', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (1, '신규 가입 쿠폰', '신규 회원 전용', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (1, '비오는 날 할인', '우산 지참 시 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(2, '모닝 커피 할인', '오전 10시 이전 주문 시 10% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (2, '점심 할인', '500원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (2, '텀블러 할인', '개인 컵 사용 시 300원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (2, '케이크 세트 할인', '음료+케이크 주문 시 1000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (2, '해피 아워', '오후 3-5시 20% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(3, '맥모닝 할인', '아침 메뉴 10% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (3, '버거 세트 할인', '1000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (3, '스낵 할인', '500원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (3, '아이스크림 반값', '여름 맞이 행사', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (3, '심야 할인', '밤 10시 이후 20% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(4, '이달의 샌드위치', '15cm 콤보 5000원', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (4, '쿠키 증정', '세트 구매 시 쿠키 증정', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (4, '음료 무료', '샌드위치 구매 시 음료 무료', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (4, '설문조사 쿠키', '설문 참여 시 쿠키 증정', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (4, '아침 메뉴 할인', '모닝 콤보 3500원', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(5, '화요일 피자 할인', '방문 포장 40% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (5, '방문 포장 할인', '상시 30% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (5, '사이드 반값', '피자 주문 시 사이드 50% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (5, '신제품 할인', '신메뉴 25% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (5, '패밀리 세트', '5000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(6, '치킨 나이트', '밤 9시 이후 1+1', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (6, '버거 박스 할인', '10% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (6, '앱 주문 할인', '2000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (6, '징거 세트 할인', '1000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (6, '에그타르트 행사', '3+1 증정', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(7, '프리미엄 피자 할인', '30% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (7, '평일 런치', '피자 1인 세트 9900원', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (7, '주만 할인', '20% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (7, '커플 세트 할인', '3000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (7, '온라인 주문 할인', '20% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(8, '파인트 할인', '2000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (8, '쿼터 할인', '4000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (8, '패밀리 할인', '6000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (8, '케이크 할인', '10% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (8, '블록팩 행사', '2+1 증정', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(9, '도넛 팩 할인', '2000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (9, '커피 도넛 세트', '모닝 콤보 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (9, '금요일 행사', '글레이즈드팩 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (9, '핫샌드위치', '1000원 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (9, '해피포인트', '무료 적립', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()),
(10, '펄 추가 무료', '음료 주문 시 펄 무료', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (10, '사이즈 업', 'L사이즈 업그레이드', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (10, '스탬프 10개', '무료 음료 1잔', 100, 1, 'STAMP', 'ACTIVE', NOW(), NOW()), (10, '신메뉴 할인', '10% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW()), (10, '학생 할인', '교복 착용 시 10% 할인', 100, 1, 'GENERAL', 'ACTIVE', NOW(), NOW());

-- 7. Reviews (100 Reviews, 10 per Store)
-- Store 1
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(12, 1, '버거가 정말 맛있어요!', 5, false, NOW(), NOW()), (13, 1, '역시 와퍼네요.', 4, false, NOW(), NOW()), (14, 1, '가격이 좀 올랐네요.', 3, false, NOW(), NOW()), (15, 1, '매장이 깔끔해요.', 5, false, NOW(), NOW()), (16, 1, '직원들이 친절합니다.', 5, false, NOW(), NOW()),
(17, 1, '감자튀김이 좀 눅눅해요.', 2, false, NOW(), NOW()), (18, 1, '주문이 빨리 나와서 좋아요.', 4, false, NOW(), NOW()), (19, 1, '사람이 너무 많아요.', 3, false, NOW(), NOW()), (20, 1, '행사 많이 해주세요.', 4, false, NOW(), NOW()), (21, 1, '최고의 햄버거!', 5, false, NOW(), NOW());

-- Store 2
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(12, 2, '커피 맛이 좋아요.', 5, false, NOW(), NOW()), (13, 2, '분위기가 좋습니다.', 4, false, NOW(), NOW()), (14, 2, '자리가 없어요.', 3, false, NOW(), NOW()), (15, 2, '라떼가 부드러워요.', 5, false, NOW(), NOW()), (16, 2, '공부하기 좋아요.', 4, false, NOW(), NOW()),
(17, 2, '너무 시끄러워요.', 2, false, NOW(), NOW()), (18, 2, '친절한 서비스 감사합니다.', 5, false, NOW(), NOW()), (19, 2, '가격은 비싸지만 맛있어요.', 4, false, NOW(), NOW()), (20, 2, '매장이 추워요.', 3, false, NOW(), NOW()), (21, 2, '매일 가고 싶어요.', 5, false, NOW(), NOW());

-- Store 3
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(12, 3, '간단하게 먹기 좋아요.', 4, false, NOW(), NOW()), (13, 3, '맥모닝 최고!', 5, false, NOW(), NOW()), (14, 3, '좀 짜요.', 3, false, NOW(), NOW()), (15, 3, '아이들이 좋아해요.', 5, false, NOW(), NOW()), (16, 3, '24시간이라 편리해요.', 5, false, NOW(), NOW()),
(17, 3, '테이블이 더러웠어요.', 2, false, NOW(), NOW()), (18, 3, '아이스크림 맛있어요.', 4, false, NOW(), NOW()), (19, 3, '가성비 좋습니다.', 5, false, NOW(), NOW()), (20, 3, '드라이브 스루 편해요.', 4, false, NOW(), NOW()), (21, 3, '그저 그래요.', 3, false, NOW(), NOW());

-- Store 4
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(12, 4, '야채가 신선해요.', 5, false, NOW(), NOW()), (13, 4, '건강한 맛이에요.', 5, false, NOW(), NOW()), (14, 4, '주문하기가 복잡해요.', 3, false, NOW(), NOW()), (15, 4, '쿠키가 진짜 맛있어요.', 4, false, NOW(), NOW()), (16, 4, '다이어트할 때 좋아요.', 5, false, NOW(), NOW()),
(17, 4, '소스 선택이 어려워요.', 4, false, NOW(), NOW()), (18, 4, '음식이 너무 늦게 나와요.', 2, false, NOW(), NOW()), (19, 4, '직원분이 추천해줘서 좋았어요.', 4, false, NOW(), NOW()), (20, 4, '빵이 너무 딱딱해요.', 3, false, NOW(), NOW()), (21, 4, '매일 먹어도 안 질려요.', 5, false, NOW(), NOW());

-- Store 5
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(22, 5, '피자가 따뜻하게 왔어요.', 5, false, NOW(), NOW()), (23, 5, '배달이 늦었어요.', 2, false, NOW(), NOW()), (24, 5, '치즈가 정말 많아요.', 5, false, NOW(), NOW()), (25, 5, '방문 포장 할인이 쏠쏠해요.', 4, false, NOW(), NOW()), (26, 5, '좀 짜게 느껴졌어요.', 3, false, NOW(), NOW()),
(27, 5, '페퍼로니 최고!', 5, false, NOW(), NOW()), (28, 5, '할인 안 받으면 비싸요.', 3, false, NOW(), NOW()), (29, 5, '파티용으로 딱입니다.', 4, false, NOW(), NOW()), (30, 5, '스파게티는 별로...', 3, false, NOW(), NOW()), (31, 5, '또 시켜 먹을게요.', 5, false, NOW(), NOW());

-- Store 6
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(22, 6, '치킨이 바삭해요.', 5, false, NOW(), NOW()), (23, 6, '기름기가 너무 많아요.', 3, false, NOW(), NOW()), (24, 6, '비스켓 맛집 인정.', 5, false, NOW(), NOW()), (25, 6, '오래된 기름 냄새가 났어요.', 2, false, NOW(), NOW()), (26, 6, '징거버거 맛있어요.', 4, false, NOW(), NOW()),
(27, 6, '밤에 먹으면 더 맛있어요.', 5, false, NOW(), NOW()), (28, 6, '너무 짜요.', 3, false, NOW(), NOW()), (29, 6, '빨리 나와서 좋아요.', 4, false, NOW(), NOW()), (30, 6, '추억의 맛입니다.', 4, false, NOW(), NOW()), (31, 6, '코울슬로 필수!', 5, false, NOW(), NOW());

-- Store 7
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(22, 7, '샐러드바가 없어져서 아쉬워요.', 2, false, NOW(), NOW()), (23, 7, '팬피자가 맛있어요.', 4, false, NOW(), NOW()), (24, 7, '토핑이 풍부합니다.', 5, false, NOW(), NOW()), (25, 7, '가격 대비 양이 적어요.', 3, false, NOW(), NOW()), (26, 7, '클래식한 맛.', 5, false, NOW(), NOW()),
(27, 7, '치즈 크러스트 추가 필수.', 4, false, NOW(), NOW()), (28, 7, '배달이 너무 늦었어요.', 2, false, NOW(), NOW()), (29, 7, '직원들이 친절해요.', 5, false, NOW(), NOW()), (30, 7, '파스타가 괜찮네요.', 4, false, NOW(), NOW()), (31, 7, '따뜻해서 좋았어요.', 5, false, NOW(), NOW());

-- Store 8
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(22, 8, '너무 달아요.', 3, false, NOW(), NOW()), (23, 8, '매장이 너무 추워요.', 3, false, NOW(), NOW()), (24, 8, '민트초코 사랑해요.', 5, false, NOW(), NOW()), (25, 8, '양이 좀 줄어든 것 같아요.', 3, false, NOW(), NOW()), (26, 8, '새로운 맛 도전 성공.', 4, false, NOW(), NOW()),
(27, 8, '아이들이 좋아해요.', 5, false, NOW(), NOW()), (28, 8, '사람이 많아서 앉을 데가 없어요.', 3, false, NOW(), NOW()), (29, 8, '직원들이 지쳐 보여요.', 3, false, NOW(), NOW()), (30, 8, '디저트로 딱입니다.', 5, false, NOW(), NOW()), (31, 8, '31라빈스 최고!', 5, false, NOW(), NOW());

-- Store 9
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(22, 9, '도넛이 퍽퍽해요.', 2, false, NOW(), NOW()), (23, 9, '커피 맛있어요.', 4, false, NOW(), NOW()), (24, 9, '아침 메뉴로 좋아요.', 5, false, NOW(), NOW()), (25, 9, '너무 달아서 하나만 먹었어요.', 4, false, NOW(), NOW()), (26, 9, '저렴해서 좋아요.', 4, false, NOW(), NOW()),
(27, 9, '간식으로 최고.', 5, false, NOW(), NOW()), (28, 9, '진열대가 비어있었어요.', 2, false, NOW(), NOW()), (29, 9, '종류가 다양해요.', 4, false, NOW(), NOW()), (30, 9, '당 충전 완료.', 5, false, NOW(), NOW()), (31, 9, '선물용으로 샀어요.', 4, false, NOW(), NOW());

-- Store 10
INSERT INTO `review` (user_id, store_id, content, rating, is_private, created_at, modified_at) VALUES
(22, 10, '펄이 쫀득해요.', 5, false, NOW(), NOW()), (23, 10, '현아 너무 사랑해.', 5, false, NOW(), NOW()), (24, 10, '너무 달아요. 당도 조절 필수.', 3, false, NOW(), NOW()), (25, 10, '가격이 좀 비싼 듯.', 3, false, NOW(), NOW()), (26, 10, '시원하고 맛있어요.', 5, false, NOW(), NOW()),
(27, 10, '최애 음료입니다.', 5, false, NOW(), NOW()), (28, 10, '줄이 너무 길어요.', 3, false, NOW(), NOW()), (29, 10, '맛있는데 양이 적어요.', 4, false, NOW(), NOW()), (30, 10, '직원들이 친절해요.', 5, false, NOW(), NOW()), (31, 10, '타로 밀크티 최고!', 5, false, NOW(), NOW());
