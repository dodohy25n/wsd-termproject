package hello.wsd.domain.item.controller;

import hello.wsd.common.BaseIntegrationTest;
import hello.wsd.domain.item.dto.CreateItemRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest extends BaseIntegrationTest {

        private String ownerToken;
        private String customerToken;
        private Long storeId;

        @BeforeEach
        void setUp() throws Exception {
                // Owner User
                SignupRequest ownerSignup = SignupRequest.builder()
                                .name("itemOwner")
                                .email("itemOwner@example.com")
                                .password("password")
                                .role(Role.ROLE_OWNER)
                                .businessNumber("333-33-33333")
                                .build();
                authService.signUp(ownerSignup);
                AuthTokens ownerAuth = authService.login(new LoginRequest("itemOwner@example.com", "password"));
                ownerToken = "Bearer " + ownerAuth.getAccessToken();

                // Customer User
                SignupRequest customerSignup = SignupRequest.builder()
                                .name("itemCustomer")
                                .email("itemCustomer@example.com")
                                .password("password")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                authService.signUp(customerSignup);
                AuthTokens customerAuth = authService.login(new LoginRequest("itemCustomer@example.com", "password"));
                customerToken = "Bearer " + customerAuth.getAccessToken();

                // Create Store (Need store to create item)
                CreateStoreRequest storeRequest = CreateStoreRequest.builder()
                                .name("Item Store")
                                .address("Address")
                                .phoneNumber("010-0000-0000")
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
        @DisplayName("상품 생성 성공")
        void createItem_Success() throws Exception {
                // given
                CreateItemRequest request = CreateItemRequest.builder()
                                .name("Kimchi")
                                .price(10000)
                                .description("Tasty")
                                .build();

                // when & then
                mockMvc.perform(post("/api/stores/" + storeId + "/items")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.isSuccess").value(true));
        }

        @Test
        @DisplayName("상품 생성 실패 (가게 주인이 아님)")
        void createItem_Fail_NotOwner() throws Exception {
                // given
                CreateItemRequest request = CreateItemRequest.builder()
                                .name("Stolen Kimchi")
                                .price(5000)
                                .description("Stolen")
                                .build();

                // when & then
                mockMvc.perform(post("/api/stores/" + storeId + "/items")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("상품 조회 성공")
        void getItem_Success() throws Exception {
                // given
                CreateItemRequest request = CreateItemRequest.builder()
                                .name("Tasty Kimchi")
                                .price(12000)
                                .description("Really tasty")
                                .build();
                String response = mockMvc.perform(post("/api/stores/" + storeId + "/items")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andReturn().getResponse().getContentAsString();
                Long itemId = objectMapper.readTree(response).path("data").asLong();

                // when & then
                mockMvc.perform(get("/api/items/" + itemId))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.name").value("Tasty Kimchi"));
        }
}
