package hello.wsd.domain.coupon.controller;

import hello.wsd.common.BaseIntegrationTest;
import hello.wsd.domain.coupon.dto.CreateCouponRequest;
import hello.wsd.domain.store.dto.CreateStoreRequest;
import hello.wsd.domain.user.dto.AuthTokens;
import hello.wsd.domain.user.dto.LoginRequest;
import hello.wsd.domain.user.dto.SignupRequest;
import hello.wsd.domain.user.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CouponControllerTest extends BaseIntegrationTest {

        private String ownerToken;
        private String customerToken;
        private Long storeId;

        @BeforeEach
        void setUp() throws Exception {
                // Owner
                SignupRequest ownerSignup = SignupRequest.builder()
                                .name("couponOwner")
                                .email("couponOwner@example.com")
                                .password("password")
                                .role(Role.ROLE_OWNER)
                                .businessNumber("123-12-12345")
                                .build();
                authService.signUp(ownerSignup);
                AuthTokens ownerAuth = authService.login(new LoginRequest("couponOwner@example.com", "password"));
                ownerToken = "Bearer " + ownerAuth.getAccessToken();

                // Customer
                SignupRequest customerSignup = SignupRequest.builder()
                                .name("couponCustomer")
                                .email("couponCustomer@example.com")
                                .password("password")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                authService.signUp(customerSignup);
                AuthTokens customerAuth = authService.login(new LoginRequest("couponCustomer@example.com", "password"));
                customerToken = "Bearer " + customerAuth.getAccessToken();

                // Store
                CreateStoreRequest storeRequest = CreateStoreRequest.builder()
                                .name("Coupon Store")
                                .address("Addr")
                                .phoneNumber("Tel")
                                .storeCategory(hello.wsd.domain.store.entity.StoreCategory.RESTAURANT)
                                .build();
                String response = mockMvc.perform(post("/api/stores")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(storeRequest)))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();
                storeId = objectMapper.readTree(response).path("data").asLong();
        }

        @Test
        @DisplayName("쿠폰 생성 성공")
        void createCoupon_Success() throws Exception {
                // given
                CreateCouponRequest request = CreateCouponRequest.builder()
                                .title("Discount Coupon")
                                .description("10% Off")
                                .totalQuantity(100)
                                .limitPerUser(1)
                                .type(hello.wsd.domain.coupon.entity.CouponType.GENERAL) // Assuming Enum
                                .issueStartsAt(LocalDateTime.now())
                                .issueEndsAt(LocalDateTime.now().plusDays(7))
                                .build(); // when & then
                mockMvc.perform(post("/api/stores/" + storeId + "/coupons")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.isSuccess").value(true));
        }

        @Test
        @DisplayName("쿠폰 발급 성공")
        void issueCoupon_Success() throws Exception {
                // given
                // Create Coupon first
                CreateCouponRequest createRequest = CreateCouponRequest.builder()
                                .title("Free Coupon")
                                .description("Free")
                                .totalQuantity(100)
                                .limitPerUser(1)
                                .type(hello.wsd.domain.coupon.entity.CouponType.GENERAL)
                                .issueStartsAt(LocalDateTime.now())
                                .issueEndsAt(LocalDateTime.now().plusDays(7))
                                .build();
                String response = mockMvc.perform(post("/api/stores/" + storeId + "/coupons")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest)))
                                .andReturn().getResponse().getContentAsString();
                Long couponId = objectMapper.readTree(response).path("data").asLong();

                // when & then
                mockMvc.perform(post("/api/coupons/" + couponId + "/issue")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON)) // No body needed for issue
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.customerCouponId").exists());
        }

        @Test
        @DisplayName("쿠폰 사용 성공")
        void useCoupon_Success() throws Exception {
                // given
                // Create Coupon
                CreateCouponRequest createRequest = CreateCouponRequest.builder()
                                .title("Use Me")
                                .description("Desc")
                                .totalQuantity(100)
                                .limitPerUser(1)
                                .type(hello.wsd.domain.coupon.entity.CouponType.GENERAL)
                                .issueStartsAt(LocalDateTime.now())
                                .issueEndsAt(LocalDateTime.now().plusDays(7))
                                .build();
                String createResponse = mockMvc.perform(post("/api/stores/" + storeId + "/coupons")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest)))
                                .andReturn().getResponse().getContentAsString();
                Long couponId = objectMapper.readTree(createResponse).path("data").asLong();

                // Issue Coupon
                String issueResponse = mockMvc.perform(post("/api/coupons/" + couponId + "/issue")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse().getContentAsString();
                // Assuming Response contains customerCouponId (issueId)
                // Let's check IssueCouponResponse structure from previous context or infer.
                // Usually it returns the ID of the issued coupon record or the DTO contains it.
                // Checking previous 'IssueCouponResponse.java' from file list (didn't read
                // content).
                // If I assume it returns `id` field or similar.
                // Let's try to parse it. The response body is
                // CommonResponse<IssueCouponResponse>.
                // I will assume `IssueCouponResponse` has `id`.
                // If it fails, I'll fix it.
                Long issueId = objectMapper.readTree(issueResponse).path("data").path("customerCouponId").asLong();

                // when & then
                mockMvc.perform(post("/api/my-coupons/" + issueId + "/use")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.isSuccess").value(true));
        }
}
