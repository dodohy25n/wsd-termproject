package hello.wsd.domain.favorite.controller;

import hello.wsd.common.BaseIntegrationTest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteControllerTest extends BaseIntegrationTest {

        private String ownerToken;
        private String customerToken;
        private Long storeId;

        @BeforeEach
        void setUp() throws Exception {
                // Owner
                SignupRequest ownerSignup = SignupRequest.builder()
                                .name("favOwner")
                                .email("favOwner@example.com")
                                .password("password")
                                .role(Role.ROLE_OWNER)
                                .businessNumber("123-12-12345")
                                .build();
                authService.signUp(ownerSignup);
                AuthTokens ownerAuth = authService.login(new LoginRequest("favOwner@example.com", "password"));
                ownerToken = "Bearer " + ownerAuth.getAccessToken();

                // Customer
                SignupRequest customerSignup = SignupRequest.builder()
                                .name("favCustomer")
                                .email("favCustomer@example.com")
                                .password("password")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                authService.signUp(customerSignup);
                AuthTokens customerAuth = authService.login(new LoginRequest("favCustomer@example.com", "password"));
                customerToken = "Bearer " + customerAuth.getAccessToken();

                // Store
                CreateStoreRequest storeRequest = CreateStoreRequest.builder()
                                .name("Favorite Store")
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
        @DisplayName("즐겨찾기 추가 성공")
        void addFavorite_Success() throws Exception {
                // when & then
                mockMvc.perform(post("/api/stores/" + storeId + "/favorites")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.isSuccess").value(true));
        }

        @Test
        @DisplayName("즐겨찾기 추가 실패 (이미 추가됨)")
        void addFavorite_Fail_Duplicate() throws Exception {
                // given
                mockMvc.perform(post("/api/stores/" + storeId + "/favorites")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON));

                // when & then
                mockMvc.perform(post("/api/stores/" + storeId + "/favorites")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.isSuccess").value(false));
        }
}
