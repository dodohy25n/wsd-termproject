# WSD Term Project: Real-world API Server

대학 상권 기반의 커뮤니티 및 상점 리뷰 플랫폼 API 서버입니다.
학생, 점주, 관리자가 상호작용하며 리뷰, 쿠폰, 즐겨찾기 기능을 제공합니다.

## 1. 프로젝트 개요

### 핵심 문제 정의

- 대학가 주변 상권의 정보 비대칭 해소
- 학생 혜택(쿠폰) 및 신뢰성 있는 리뷰 시스템 구축
- 상점 주인과 고객 간의 직접적인 소통 채널(단골/쿠폰) 부재 해결

### 주요 기능

1. **인증/인가**: JWT (Access/Refresh + RRT), OAuth2 (Kakao/Google), Firebase Auth, RBAC (Role-Based Access Control)
2. **사용자 관리**: 학생(대학 인증), 점주(사업자 인증), 관리자 등 역할별 프로필 관리
3. **상점/상품**: 점주의 상점/메뉴 CRUD, 위치 기반/카테고리별 검색
4. **리뷰/즐겨찾기**: 실사용자 리뷰 작성, 단골 등록 및 통계 제공
5. **쿠폰 시스템**: 점주가 발행하고 고객이 발급/사용하는 할인 쿠폰 (재고/유효기간 관리)

---

## 2. 기술 스택

### Backend
*   **Language**: Java 17
*   **Framework**: Spring Boot 3.4.0
*   **Build Tool**: Gradle 8.x

### Database & Infrastructure

*   **RDBMS**: MySQL 8.0 (JPA/Hibernate)
*   **NoSQL**: Redis (Refresh Token, Rate Limiting)
*   **Migration**: Flyway (Schema Management)
*   **Infra**: Docker, Docker Compose, JCloud (Linux)
*   **CI/CD**: GitHub Actions

### Security & Auth

*   **Core**: Spring Security, JWT (Access/Refresh + RRT)
*   **OAuth2**: Kakao, Google
*   **External**: Firebase Admin SDK
*   **Rate Limiting**: Bucket4j

---

## 3. 실행 방법

### 3.1. 요구 사항

- Docker & Docker Compose
- JDK 17+ (로컬 개발 시)

### 3.2. Docker 실행 (로컬 환경)

데이터베이스(MySQL), 캐시(Redis), API 서버를 한 번에 실행합니다.
초기 데이터(Seed Data)는 컨테이너 실행 시 Flyway를 통해 자동으로 주입됩니다.

```bash
# 1. 프로젝트 클론
git clone https://github.com/dodohy25n/wsd-termproject.git
cd wsd-termproject

# 2. Docker Compose 실행
docker-compose up -d --build

# 3. 로그 확인 (실행 완료 대기)
docker-compose logs -f app
```

### 3.3. 로컬 실행 (개발 모드)
DB와 Redis는 Docker로 띄우고, Spring Boot만 로컬에서 실행할 경우:

```bash
# 1. DB/Redis 실행
docker-compose up -d mysql redis

# 2. 애플리케이션 빌드 및 실행
# Linux/macOS
./gradlew clean build
java -jar build/libs/wsd-termproject-0.0.1-SNAPSHOT.jar

# Windows
./gradlew.bat clean build
java -jar build/libs/wsd-termproject-0.0.1-SNAPSHOT.jar
```

---

## 4. 환경변수 설정 (`.env`)

로컬 개발 시에는 `application.yml`의 기본값을 사용하거나 `.env` 파일을 생성하여 설정할 수 있습니다.

배포 시에는 GitHub Secrets를 통해 관리됩니다.

| 변수명 | 설명 | 비고 |
| :--- | :--- | :--- |
| `DB_ROOT_PASSWORD` | MySQL Root 비밀번호 | |
| `DB_USER` | MySQL 애플리케이션 계정 | |
| `DB_PASSWORD` | MySQL 애플리케이션 비번 | |
| `JWT_SECRET` | JWT 서명 비밀키 | 32자 이상 필수 |
| `GOOGLE_CLIENT_ID` | Google OAuth ID | |
| `GOOGLE_CLIENT_SECRET` | Google OAuth Secret | |
| `KAKAO_CLIENT_ID` | Kakao OAuth ID | |
| `FIREBASE_KEY_PATH` | Firebase 키 경로 | 배포 시 자동 생성됨 |
---

## 5. 배포 주소 및 URL

### 5.1. 서버 정보

본 프로젝트는 학교 JCloud 인프라에 배포되었습니다.
CI/CD 파이프라인(GitHub Actions)을 통해 메인 브랜치 푸시 시 자동 배포됩니다.

*   **배포 URL**: `http://113.198.66.68:13233` (외부 접속용)
*   **API 문서 (Swagger UI)**: [http://113.198.66.68:13233/swagger-ui/index.html](http://113.198.66.68:13233/swagger-ui/index.html)
*   **Health Check**: [http://113.198.66.68:13233/health](http://113.198.66.68:13233/health)

### 5.2. 배포 아키텍처

*   **GitHub Actions**: Build → Docker Hub Push → JCloud SSH Deploy
*   **Docker Compose**: Spring Boot, MySQL, Redis 컨테이너 오케스트레이션

---

## 6. 인증 및 역할 (Auth & Roles)

### 6.1. 상세 인증 플로우

본 프로젝트는 보안 강화를 위해 **JWT (JSON Web Token)**와 **RRT (Refresh Token Rotation)** 방식을 채택하고 있습니다.

1.  **로그인 (Login)**
    -   사용자가 ID/PW (`/api/auth/login`) 또는 소셜 계정(Google/Kakao)으로 로그인 요청을 보냅니다.
    -   서버는 인증 성공 시 **Access Token**과 **Refresh Token**을 생성합니다.
    -   **Access Token**: Response Body로 전달 (클라이언트 메모리 저장 권장).
    -   **Refresh Token**: `HttpOnly Secure Cookie`로 전달 (XSS 공격 방지).

2.  **토큰 갱신**
    -   Access Token이 만료되면 클라이언트는 `/api/auth/refresh` 엔드포인트를 호출합니다.
    -   서버는 쿠키에 담긴 Refresh Token을 검증하고 Redis에 저장된 값과 비교합니다.
    -   검증 성공 시 **새로운 Access Token**과 **새로운 Refresh Token**을 발급합니다 (RRT).
    -   기존 Refresh Token은 폐기하여 탈취 시 재사용을 방지합니다.

3.  **로그아웃 (Logout)**
    -   `/api/auth/logout` 호출 시 Redis에서 해당 사용자의 Refresh Token을 삭제합니다.
    -   클라이언트의 Refresh Token 쿠키를 만료 처리합니다.

### 6.2. 로그인 방식별 흐름

-   **일반 로그인 (General)**
    -   Client가 ID/PW 전송 → Server가 DB 검증 → 성공 시 JWT(RefreshToken/AccessToken) 발급

-   **소셜 로그인 (OAuth2 - Google/Kakao)**
    -   Client가 소셜 로그인 링크 접속 → Provider 인증 후 리다이렉트 → Server가 인가 코드로 Access Token 요청 → 사용자 정보 조회 → (첫 방문 시 `GUEST`로 회원가입) → JWT 발급

-   **파이어베이스 로그인 (Firebase)**
    -   Client가 Firebase SDK로 인증 후 ID Token 획득 → Server에 ID Token 전송 → Server가 Firebase Admin SDK로 검증 → 사용자 정보 추출 → (첫 방문 시 `GUEST`로 회원가입) → JWT 발급

### 6.3. 역할 권한표

| 역할 (`Role`) | 설명 | 접근 가능 범위 |
| :--- | :--- | :--- |
| **ROLE_GUEST** | 가입 대기 | 소셜 로그인 직후, 추가 정보 입력 전 상태 |
| **ROLE_CUSTOMER** | 일반/학생 | 상점 조회, 리뷰 작성, 쿠폰 발급/사용, 즐겨찾기 |
| **ROLE_OWNER** | 점주 | 본인 상점/상품/쿠폰 관리 (CRUD), 내 상점 통계 조회 |
| **ROLE_ADMIN** | 관리자 | 전체 사용자 관리, 토큰 강제 만료 등 시스템 관리 |

---

## 7. 예시 계정

초기 데이터(`V2__seed_data.sql`)로 생성된 테스트 계정입니다.
모든 계정의 비밀번호는 **`1234`** 입니다.

| 역할 | Username (Email) | 비밀번호 | 비고 |
| :--- | :--- | :--- | :--- |
| **관리자** | `admin@wsd.com` | `1234` | 모든 권한 보유 |
| **점주 1** | `owner1@wsd.com` | `1234` | '맛있는 식당' 보유 |
| **점주 2** | `owner2@wsd.com` | `1234` | '분위기 좋은 카페' 보유 |
| **사용자 1** | `user1@wsd.com` | `1234` | 구매자 (리뷰/쿠폰 테스트용) |
| **사용자 2** | `user2@wsd.com` | `1234` | 구매자 |

---

## 8. 엔드포인트 목록


| 도메인 | 메서드 | URL | 설명 | 권한 |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/auth/signup` | 회원가입 | ALL |
| | `POST` | `/api/auth/login` | 로그인 (JWT 발급) | ALL |
| | `POST` | `/api/auth/firebase` | 파이어베이스 로그인 | ALL |
| | `POST` | `/api/auth/refresh` | 토큰 갱신 | ALL |
| | `POST` | `/api/auth/logout` | 로그아웃 | ALL |
| | `POST` | `/api/auth/complete-social-signup` | 소셜 회원가입 완료 | GUEST |
| **Store** | `POST` | `/api/stores` | 상점 등록 | OWNER |
| | `GET` | `/api/stores` | 상점 목록 조회 (검색/필터) | ALL |
| | `GET` | `/api/stores/{storeId}` | 상점 단건 조회 | ALL |
| | `PATCH` | `/api/stores/{storeId}` | 상점 정보 수정 | OWNER (My) |
| | `DELETE` | `/api/stores/{storeId}` | 상점 삭제 | OWNER (My) |
| **Item** | `POST` | `/api/stores/{storeId}/items` | 상품 등록 | OWNER (My) |
| | `GET` | `/api/stores/{storeId}/items` | 상점별 상품 목록 조회 | ALL |
| | `GET` | `/api/items/{itemId}` | 상품 단건 조회 | ALL |
| | `PATCH` | `/api/items/{itemId}` | 상품 수정 | OWNER (My) |
| | `DELETE` | `/api/items/{itemId}` | 상품 삭제 | OWNER (My) |
| **Review** | `POST` | `/api/stores/{storeId}/reviews` | 리뷰 작성 | CUSTOMER |
| | `GET` | `/api/stores/{storeId}/reviews` | 상점 리뷰 목록 조회 | ALL |
| | `GET` | `/api/stores/{storeId}/reviews/stats` | 상점 리뷰 통계 조회 | ALL |
| | `PATCH` | `/api/reviews/{reviewId}` | 리뷰 수정 | CUSTOMER (My) |
| | `DELETE` | `/api/reviews/{reviewId}` | 리뷰 삭제 | CUSTOMER (My) |
| **Coupon** | `POST` | `/api/stores/{storeId}/coupons` | 쿠폰 생성 | OWNER (My) |
| | `GET` | `/api/stores/{storeId}/coupons` | 상점별 쿠폰 목록 조회 | ALL |
| | `PATCH` | `/api/coupons/{couponId}` | 쿠폰 수정 | OWNER (My) |
| | `DELETE` | `/api/coupons/{couponId}` | 쿠폰 삭제 | OWNER (My) |
| | `GET` | `/api/items/{itemId}/coupons` | 상품별 적용 가능 쿠폰 조회 | ALL |
| | `POST` | `/api/coupons/{couponId}/issue` | 쿠폰 발급 | CUSTOMER |
| | `POST` | `/api/my-coupons/{customerCouponId}/use` | 쿠폰 사용 | CUSTOMER |
| | `GET` | `/api/my-coupons` | 내 쿠폰 목록 조회 | CUSTOMER |
| **Favorite** | `POST` | `/api/stores/{storeId}/favorites` | 상점 즐겨찾기 추가 | CUSTOMER |
| | `DELETE` | `/api/stores/{storeId}/favorites` | 상점 즐겨찾기 취소 | CUSTOMER |
| | `GET` | `/api/stores/{storeId}/favorites/count` | 상점 즐겨찾기 수 조회 | ALL |
| | `GET` | `/api/favorites` | 내 단골 상점 목록 조회 | CUSTOMER |
| **Admin** | `GET` | `/api/admin/users` | 전체 사용자 조회 | ADMIN |
| | `PATCH` | `/api/admin/users/{userId}/role` | 사용자 권한 수정 | ADMIN |
| | `DELETE` | `/api/admin/users/{userId}` | 사용자 강제 탈퇴 | ADMIN |
| | `GET` | `/api/admin/tokens` | 전체 리프레시 토큰 조회 | ADMIN |
| | `GET` | `/api/admin/tokens/{userId}` | 특정 유저 토큰 조회 | ADMIN |
| | `DELETE` | `/api/admin/tokens/{userId}` | 특정 유저 토큰 삭제 (강제 로그아웃) | ADMIN |
| **Health** | `GET` | `/health` | 서버 상태 확인 (Health Check) | ALL |

---

## 9. 표준 응답 규격

모든 API 응답은 `CommonResponse`로 감싸서 반환됩니다.

### 9.1. 성공 응답

```json
{
  "success": true,
  "data": { ... },
  "error": null
}
```

### 9.2. 실패 응답

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "요청한 리소스를 찾을 수 없습니다.",
    "path": "/api/items/999"
  }
}
```

### 9.3. 페이지네이션 응답

`data` 필드 내부에 페이지 정보가 포함됩니다.
```json
{
  "success": true,
  "data": {
    "content": [ ... ],
    "pageNo": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "last": false
  },
  "error": null
}
```

---

## 10. 성능 및 보안 고려사항

### 성능 최적화

1.  **데이터베이스 인덱싱**
    - `store(name)`, `review(store_id)` 등 `WHERE` 조건이나 `JOIN`에 빈번히 사용되는 컬럼에 인덱스를 적용하여 조회 속도를 개선했습니다.

2.  **페이지네이션**
    - 대량 데이터 조회 API(상점, 리뷰, 사용자 등)에 `Pageable`을 적용하여 한번에 많은 데이터를 로딩하지 않도록 메모리 사용을 최적화했습니다.

3.  **N+1 문제 해결**
    - `@EntityGraph` 및 `FetchType.LAZY`를 적절히 사용하여 연관 엔티티 조회 시 발생하는 N+1 문제를 방지했습니다.

4.  **트랜잭션 최적화**
    - 단순 조회 로직에는 `@Transactional(readOnly = true)`를 적용하여 Dirty Checking을 생략, 메모리 및 CPU 자원을 절약했습니다.

5.  **Redis 캐싱**
    - Refresh Token 및 Rate Limit 정보와 같이 빈번하게 읽고 쓰이는 데이터는 In-Memory DB인 Redis에서 관리하여 DB 부하를 줄였습니다.

### 보안 구현 (Security)

1.  **JWT & RTR (Refresh Token Rotation)**
    - Access Token은 Body로, Refresh Token은 **HttpOnly Secure Cookie**로 전달하여 XSS 공격을 방지했습니다.
    - 토큰 갱신 시 Refresh Token도 함께 재발급(Rotation)하여 탈취된 토큰의 재사용을 영구적으로 차단합니다.

2.  **레이트 리밋 (Rate Limiting)**
    - **Bucket4j**를 도입하여 IP당 분당 100회로 요청을 제한, DDoS 공격 및 무분별한 API 호출로부터 서버를 보호합니다.

3.  **패스워드 암호화**
    - `BCryptPasswordEncoder`를 사용하여 사용자 비밀번호를 안전하게 해시 암호화하여 저장합니다.

---

## 11. 한계와 개선 계획

### 현재의 한계

1.  **동기적 처리**: 쿠폰 발급 등 높은 트래픽이 예상되는 로직이 동기(Synchronous) 방식으로 구현되어 있어 대량의 요청 시 병목 현상 발생 가능
2.  **단순 검색**: MySQL `LIKE` 쿼리를 사용한 단순 검색으로, 대용량 데이터 환경에서의 검색 성능 저하 우려
3.  **단일 API 서버**: 트래픽 증가 시 단일 서버 구조로 인한 확장의 한계 (Scale-up 의존)

### 향후 개선 계획

1.  **비동기/이벤트 기반 아키텍처 도입**
    - 쿠폰 발급 요청을 Kafka/RabbitMQ 등 메시지 큐로 처리하여 동시성 제어 및 처리량 증대

2.  **모니터링 & 로깅 시스템 고도화**
    - Prometheus + Grafana 도입으로 서버 메트릭 시각화
    - ELK Stack (Elasticsearch, Logstash, Kibana) 도입으로 로그 중앙화

3.  **CI/CD 자동화 및 오케스트레이션**
    - GitHub Actions 파이프라인 고도화 (자동 테스트 및 배포)
    - Kubernetes(k8s) 도입으로 컨테이너 오케스트레이션 및 오토스케일링 구현
