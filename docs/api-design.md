# API 설계 문서 (API Design)

## 1. 개요
본 문서는 텀 프로젝트를 위해 설계된 API 엔드포인트를 설명합니다. API는 RESTful 원칙을 따르며 데이터 교환을 위해 JSON을 사용합니다. 모든 응답은 공통 응답 구조(`CommonResponse`)로 래핑되어 반환됩니다.

### 1.1 공통 응답 포맷 (Common Response)

서버의 모든 응답은 `CommonResponse` 객체에 감싸져 반환됩니다. `result` 필드를 통해 성공 여부를 즉시 파악할 수 있습니다.

#### 성공 응답 (Success)
```json
{
  "result": "SUCCESS",
  "data": {
    "userId": 1,
    "username": "user1@example.com",
    "role": "ROLE_CUSTOMER"
  },
  "errorCode": null,
  "message": null
}
```

#### 실패 응답 (Error)
```json
{
  "result": "FAIL",
  "data": null,
  "errorCode": "DUPLICATE_RESOURCE",
  "message": "이미 존재하는 이메일입니다."
}
```

#### 페이지네이션 응답 (Pagination)
목록 조회 시 `data` 필드 내부에 `content`와 페이징 메타데이터(`page`, `size`, `totalElements` 등)가 포함됩니다.

```json
{
  "result": "SUCCESS",
  "data": {
    "content": [
      { "storeId": 1, "name": "Best Coffee", "category": "CAFE" },
      { "storeId": 2, "name": "Yummy Burger", "category": "RESTAURANT" }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false
  },
  "errorCode": null,
  "message": null
}
```

## 2. API 엔드포인트

### 2.1 인증 (`/api/auth`)
| 메서드 | URI | 설명 | 권한 필요 |
| :--- | :--- | :--- | :--- |
| `POST` | `/signup` | 신규 사용자 회원가입 (로컬) | 없음 |
| `POST` | `/login` | 아이디/비밀번호 로그인 | 없음 |
| `POST` | `/firebase` | Firebase 토큰 로그인 (소셜) | 없음 |
| `POST` | `/refresh` | Refresh Token(쿠키)을 이용한 Access Token 갱신 | 없음 |
| `POST` | `/logout` | 로그아웃 (토큰 무효화) | 없음 |
| `POST` | `/complete-social-signup` | 소셜 로그인 사용자 추가 정보 입력 및 가입 완료 | 있음 (Guest) |

### 2.2 상점 관리 (`/api/stores`)
| 메서드 | URI | 설명 | 권한 필요 |
| :--- | :--- | :--- | :--- |
| `POST` | `/` | 신규 상점 생성 | 있음 (Owner) |
| `GET` | `/{storeId}` | 상점 상세 정보 조회 | 없음 |
| `GET` | `/` | 상점 목록 조회 (페이지네이션, 키워드/카테고리 검색) | 없음 |
| `PATCH` | `/{storeId}` | 상점 정보 수정 | 있음 (Owner) |
| `DELETE` | `/{storeId}` | 상점 삭제 | 있음 (Owner) |

### 2.3 상품 관리 (`/api`)
| 메서드 | URI | 설명 | 권한 필요 |
| :--- | :--- | :--- | :--- |
| `POST` | `/stores/{storeId}/items` | 상점에 신규 상품 추가 | 있음 (Owner) |
| `GET` | `/stores/{storeId}/items` | 상점의 상품 목록 조회 | 없음 |
| `GET` | `/items/{itemId}` | 상품 상세 조회 | 없음 |
| `PATCH` | `/items/{itemId}` | 상품 정보 수정 | 있음 (Owner) |
| `DELETE` | `/items/{itemId}` | 상품 삭제 | 있음 (Owner) |

### 2.4 리뷰 시스템 (`/api`)
| 메서드 | URI | 설명 | 권한 필요 |
| :--- | :--- | :--- | :--- |
| `POST` | `/stores/{storeId}/reviews` | 상점에 리뷰 작성 | 있음 (User) |
| `GET` | `/stores/{storeId}/reviews` | 상점 리뷰 목록 조회 (페이지네이션) | 없음 |
| `GET` | `/stores/{storeId}/reviews/stats` | 리뷰 통계 조회 (평점 평균, 별점별 개수) | 없음 |
| `PATCH` | `/reviews/{reviewId}` | 리뷰 수정 | 있음 (User) |
| `DELETE` | `/reviews/{reviewId}` | 리뷰 삭제 | 있음 (User) |

### 2.5 쿠폰 시스템 (`/api`)
| 메서드 | URI | 설명 | 권한 필요 |
| :--- | :--- | :--- | :--- |
| `POST` | `/stores/{storeId}/coupons` | 쿠폰 생성 | 있음 (Owner) |
| `PATCH` | `/coupons/{couponId}` | 쿠폰 정보 수정 | 있음 (Owner) |
| `DELETE` | `/coupons/{couponId}` | 쿠폰 삭제 | 있음 (Owner) |
| `GET` | `/stores/{storeId}/coupons` | 상점 쿠폰 목록 조회 | 없음 |
| `GET` | `/items/{itemId}/coupons` | 상품에 적용 가능한 쿠폰 조회 | 없음 |
| `POST` | `/coupons/{couponId}/issue` | 쿠폰 발급 (사용자) | 있음 (User) |
| `POST` | `/my-coupons/{customerCouponId}/use` | 발급된 쿠폰 사용 | 있음 (User) |
| `GET` | `/my-coupons` | 내 쿠폰 목록 조회 | 있음 (User) |

### 2.6 즐겨찾기 (`/api`)
| 메서드 | URI | 설명 | 권한 필요 |
| :--- | :--- | :--- | :--- |
| `POST` | `/stores/{storeId}/favorites` | 단골 상점 추가 | 있음 (User) |
| `DELETE` | `/stores/{storeId}/favorites` | 단골 상점 삭제 | 있음 (User) |
| `GET` | `/stores/{storeId}/favorites/count` | 상점의 총 단골 수 조회 | 없음 |
| `GET` | `/favorites` | 내 단골 상점 목록 조회 (페이지네이션) | 있음 (User) |

### 2.7 관리자 (`/api/admin`)
| 메서드 | URI | 설명 | 권한 필요 |
| :--- | :--- | :--- | :--- |
| `GET` | `/users` | 전체 사용자 목록 조회 (페이지네이션) | 있음 (Admin) |
| `PATCH` | `/users/{userId}/role` | 사용자 권한 수정 | 있음 (Admin) |
| `DELETE` | `/users/{userId}` | 사용자 강제 탈퇴 (Ban) | 있음 (Admin) |
| `GET` | `/tokens` | 전체 Refresh Token 목록 조회 | 있음 (Admin) |
| `GET` | `/tokens/{userId}` | 특정 사용자의 Refresh Token 조회 | 있음 (Admin) |
| `DELETE` | `/tokens/{userId}` | 특정 사용자의 Refresh Token 삭제 (강제 로그아웃) | 있음 (Admin) |
