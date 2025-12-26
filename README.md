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
2. **사용자 관리**: 학생, 점주, 관리자 등 역할별 프로필 관리
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
| :--- | :--- | :-- |
| `DB_ROOT_PASSWORD` | MySQL Root 비밀번호 | |
| `DB_USER` | MySQL 애플리케이션 계정 | |
| `DB_PASSWORD` | MySQL 애플리케이션 비번 | |
| `JWT_SECRET` | JWT 서명 비밀키 | 32자 이상 필수 |
| `GOOGLE_CLIENT_ID` | Google OAuth ID | |
| `GOOGLE_CLIENT_SECRET` | Google OAuth Secret | |
| `KAKAO_CLIENT_ID` | Kakao OAuth ID | |
| `FIREBASE_KEY_PATH` | Firebase 키 경로 | |
---

## 5. 배포 주소 및 URL

### 5.1. 서버 정보

본 프로젝트는 학교 JCloud 인프라에 배포되었습니다.
CI/CD 파이프라인(GitHub Actions)을 통해 메인 브랜치 푸시 시 자동 배포됩니다.

*   **배포 URL**: [http://175.123.55.182:3210](http://113.198.66.68:13233/swagger-ui/index.html)
*   **API 문서 (Swagger UI)**: [http://175.123.55.182:3210/docs](http://113.198.66.68:13233/swagger-ui/index.html)
*   **Health Check**: [http://175.123.55.182:3210/health](http://113.198.66.68:13233/health)

### 5.2. 배포 아키텍처 및 CI/CD

#### 인프라 아키텍처
*   **Container Request**: GitHub Actions가 코드를 빌드하고 Docker Image를 생성하여 Docker Hub에 Push합니다.
*   **Deploy Trigger**: 배포 서버(JCloud)에 SSH로 접속하여 최신 이미지를 Pull 받고 컨테이너를 재시작합니다.
*   **Service Orchestration**: `docker-compose`를 사용하여 Spring Boot 앱, MySQL, Redis를 하나의 네트워크로 관리합니다.
*   **Auto Healing**: `restart: always` 정책을 적용하여 서버 재부팅이나 예기치 않은 종료 시 컨테이너가 자동으로 복구되도록 설정했습니다.

#### CI/CD 파이프라인 (GitHub Actions)
| 단계 | 설명 |
| :--- | :--- |
| **CI (Continuous Integration)** | main 브랜치 Push 및 PR 시 트리거 |
| - Test | Gradle 기반 유닛/통합 테스트 수행 (Redis 서비스 컨테이너 활용) |
| - Build | 애플리케이션 빌드 검증 |
| **CD (Continuous Deployment)** | main 브랜치 Push 시에만 트리거 |
| - Login | Docker Hub 로그인 |
| - Push | Docker Image 빌드 및 태깅(latest) 후 레지스트리 전송 |
| - Deploy | JCloud 서버에 SSH 접속 -> `docker-compose pull` -> `up -d` 실행 |

#### 배포 환경 (Docker)
*   **Base Image**: `eclipse-temurin:17-jre-jammy`
*   **Multi-stage Build**: 빌드(Gradle)와 실행(JRE) 단계를 분리하여 이미지 크기를 최적화했습니다.

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
    - `User` 엔티티의 `username`, `email` 컬럼에 `@Column(unique = true)`를 적용하여 중복 방지 및 조회 성능을 보장합니다.
    - `Store` 엔티티의 `name` 필드에 `@Index`를 적용하여 상점 이름 검색 성능을 최적화했습니다.

2.  **페이지네이션**
    - 상점, 리뷰 등 대량의 데이터가 예상되는 조회 API에 `Pageable`을 적용하여 메모리 효율성을 확보하고 응답 속도를 개선했습니다.

3.  **지연 로딩 (Lazy Loading)**
    - 모든 연관관계(`@ManyToOne`, `@OneToOne`)에 `FetchType.LAZY`를 적용하여 불필요한 연관 엔티티의 즉시 로딩을 방지하고 N+1 발생 가능성을 최소화했습니다.

4.  **트랜잭션 최적화**
    - 모든 Service의 단순 조회 로직에는 `@Transactional(readOnly = true)`를 적용하여 영속성 컨텍스트의 Dirty Checking(변경 감지)을 생략, 메모리 및 CPU 자원을 절약했습니다.

5.  **Redis 캐싱**
    - Refresh Token 저장소로 In-Memory DB인 Redis를 사용하여 빠른 응답 속도를 보장하고 DB 부하를 줄였습니다.

### 보안 구현 (Security)

1.  **JWT & RTR (Refresh Token Rotation)**
    - Access Token은 Body로, Refresh Token은 **HttpOnly Secure Cookie**로 전달하여 XSS 공격 위험을 낮췄습니다.
    - 토큰 갱신 시 Refresh Token도 함께 재발급(Rotation)하여, 만약 탈취되더라도 이전 토큰을 무효화하여 재사용을 차단합니다.

2.  **입력값 검증 (Validation)**
    - DTO에서 `@NotBlank`, `@Email` 등의 Bean Validation 어노테이션을 사용하여 입력값을 1차로 검증합니다.
    - 검증 실패 시 `GlobalExceptionHandler`가 예외를 포착하여 상세한 필드별 에러 리스트를 JSON으로 반환합니다.

3.  **커스텀 예외 핸들링**
    - `AuthenticationEntryPoint`와 `AccessDeniedHandler`를 구현하여 인증/인가 실패 시 표준화된 JSON 에러 포맷(`CommonResponse`)을 반환합니다.

4.  **레이트 리밋 (Rate Limiting)**
    - **Bucket4j**를 인터셉터 단에서 사용하여 IP당 분당 100회로 요청을 제한, 공격적인 트래픽을 차단합니다.

5.  **패스워드 암호화**
    - `BCryptPasswordEncoder`를 사용하여 사용자 비밀번호를 안전하게 해시 암호화하여 저장합니다.
---

## 11. 한계와 개선 계획

### 현재의 한계

1.  **배포 시 다운타임 발생**: 현재의 `docker-compose down -> up` 배포 방식은 컨테이너 재시작 시간 동안 서비스 중단이 발생합니다.
2.  **인메모리 레이트 리밋**: Rate Limit 정보가 각 인스턴스의 메모리(`ConcurrentHashMap`)에 저장되어, 다중 서버 환경에서는 제한이 공유되지 않는 한계가 있습니다.
3.  **단일 DB 의존성**: 읽기/쓰기 트래픽이 MySQL 단일 인스턴스에 집중되어 있어 대규모 트래픽 발생 시 병목이 생길 수 있습니다.
4.  **동기적 처리 구조**: 쿠폰 발급 등 트래픽이 몰리는 로직이 동기식으로 구현되어 있어 응답 지연 가능성이 있습니다.

### 향후 개선 계획

1.  **무중단 배포 도입**
    - Nginx를 리버스 프록시로 두고 Blue/Green 배포 전략을 적용하여 배포 중 서비스 중단을 제거할 예정입니다.

2.  **비동기/이벤트 기반 아키텍처**
    - 쿠폰 발급 요청 등을 Kafka/RabbitMQ 메시지 큐로 버퍼링하여 처리량(Throughput)을 증대시키고 시스템 결합도를 낮출 예정입니다.

3.  **Redis 기반 분산 레이트 리밋**
    - Bucket4j의 저장소를 Redis로 확장하여 분산 환경에서도 정확한 요청 제한을 구현할 예정입니다.

4.  **검색 성능 고도화**
    - MySQL Full-Text Index 또는 Elasticsearch 도입을 통해 검색 성능 및 정확도 개선.

5.  **모니터링 & 로깅 (Observability)**
    - Prometheus + Grafana를 도입하여 JVM, Connection Pool 상태를 시각화.
    - ELK Stack을 통해 분산 로깅 환경 구축 및 에러 추적 용이성 확보.
