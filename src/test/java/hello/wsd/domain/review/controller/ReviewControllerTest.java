package hello.wsd.domain.review.controller;

import hello.wsd.common.BaseIntegrationTest;
import hello.wsd.domain.review.dto.CreateReviewRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends BaseIntegrationTest {

        private String ownerToken;
        private String customerToken;
        private String otherCustomerToken;
        private Long storeId;

        @BeforeEach
        void setUp() throws Exception {
                // Owner
                SignupRequest ownerSignup = SignupRequest.builder()
                                .name("reviewOwner")
                                .email("reviewOwner@example.com")
                                .password("password")
                                .role(Role.ROLE_OWNER)
                                .businessNumber("555-55-55555")
                                .build();
                authService.signUp(ownerSignup);
                AuthTokens ownerAuth = authService.login(new LoginRequest("reviewOwner@example.com", "password"));
                ownerToken = "Bearer " + ownerAuth.getAccessToken();

                // Customer
                SignupRequest customerSignup = SignupRequest.builder()
                                .name("reviewCustomer")
                                .email("reviewCustomer@example.com")
                                .password("password")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                authService.signUp(customerSignup);
                AuthTokens customerAuth = authService.login(new LoginRequest("reviewCustomer@example.com", "password"));
                customerToken = "Bearer " + customerAuth.getAccessToken();

                // Other Customer (for auth fail test)
                SignupRequest otherCustomerSignup = SignupRequest.builder()
                                .name("reviewOther")
                                .email("reviewOther@example.com")
                                .password("password")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                authService.signUp(otherCustomerSignup);
                AuthTokens otherAuth = authService.login(new LoginRequest("reviewOther@example.com", "password"));
                otherCustomerToken = "Bearer " + otherAuth.getAccessToken();

                // Store
                CreateStoreRequest storeRequest = CreateStoreRequest.builder()
                                .name("Review Store")
                                .address("Addr")
                                .phoneNumber("Tel")
                                .storeCategory(hello.wsd.domain.store.entity.StoreCategory.RESTAURANT)
                                .build();
                String response = mockMvc.perform(post("/api/stores")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(storeRequest)))
                                .andReturn().getResponse().getContentAsString();
                storeId = objectMapper.readTree(response).path("data").asLong();
        }

        @Test
        @DisplayName("리뷰 작성 성공")
        void createReview_Success() throws Exception {
                // given
                CreateReviewRequest request = new CreateReviewRequest("Great Store!", 5);

                // when & then
                mockMvc.perform(post("/api/stores/" + storeId + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.isSuccess").value(true));
        }

        @Test
        @DisplayName("리뷰 삭제 실패 (작성자가 아님)")
        void deleteReview_Fail_NotAuthor() throws Exception {
                // given
                CreateReviewRequest request = new CreateReviewRequest("To be deleted", 3);
                String response = mockMvc.perform(post("/api/stores/" + storeId + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andReturn().getResponse().getContentAsString();
                Long reviewId = objectMapper.readTree(response).path("data").asLong();

                // when & then (Other Customer tries to delete)
                mockMvc.perform(delete("/api/reviews/" + reviewId)
                                .header(HttpHeaders.AUTHORIZATION, otherCustomerToken))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("상점 리뷰 조회 성공")
        void getReviews_Success() throws Exception {
                // given
                CreateReviewRequest request1 = new CreateReviewRequest("Review 1", 5);
                CreateReviewRequest request2 = new CreateReviewRequest("Review 2", 4);
                mockMvc.perform(post("/api/stores/" + storeId + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request1)))
                                .andExpect(status().isCreated()); // Ensure success

                mockMvc.perform(post("/api/stores/" + storeId + "/reviews")
                                .header(HttpHeaders.AUTHORIZATION, otherCustomerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request2)))
                                .andExpect(status().isCreated()); // Ensure success

                // when & then
                mockMvc.perform(get("/api/stores/" + storeId + "/reviews"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.content").isArray())
                                .andExpect(jsonPath("$.data.content.length()").value(2));
        }
}
