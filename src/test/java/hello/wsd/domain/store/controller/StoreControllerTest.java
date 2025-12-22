package hello.wsd.domain.store.controller;

import hello.wsd.domain.affliation.entity.University;
import hello.wsd.domain.affliation.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import hello.wsd.common.BaseIntegrationTest;
import hello.wsd.domain.store.dto.CreateStoreRequest;
import hello.wsd.domain.store.dto.UpdateStoreRequest;
import hello.wsd.domain.user.dto.AuthTokens;
import hello.wsd.domain.user.dto.LoginRequest;
import hello.wsd.domain.user.dto.SignupRequest;
import hello.wsd.domain.user.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreControllerTest extends BaseIntegrationTest {

        private String ownerToken;
        private String customerToken;

        @Autowired
        private UniversityRepository universityRepository;

        private Long universityId;

        @BeforeEach
        void setUp() {
                // Owner User
                SignupRequest ownerSignup = SignupRequest.builder()
                                .name("owner1")
                                .email("owner1@example.com")
                                .password("password")
                                .phoneNumber("010-1234-5678")
                                .role(Role.ROLE_OWNER)
                                .businessNumber("123-45-67890")
                                .build();
                authService.signUp(ownerSignup);
                AuthTokens ownerAuth = authService.login(new LoginRequest("owner1@example.com", "password"));
                ownerToken = "Bearer " + ownerAuth.getAccessToken();

                // Customer User
                University university = University.builder()
                                .name("Test Univ")
                                .emailDomain("test.ac.kr")
                                .build();
                universityRepository.save(university);
                universityId = university.getId();

                SignupRequest customerSignup = SignupRequest.builder()
                                .name("customer1")
                                .email("customer1@example.com")
                                .password("password")
                                .phoneNumber("010-1111-2222")
                                .role(Role.ROLE_CUSTOMER)
                                .universityId(universityId)
                                .build();
                authService.signUp(customerSignup);
                AuthTokens customerAuth = authService.login(new LoginRequest("customer1@example.com", "password"));
                customerToken = "Bearer " + customerAuth.getAccessToken();
        }

        @Test
        @DisplayName("상점 생성 성공 (점주)")
        void createStore_Success() throws Exception {
                // given
                CreateStoreRequest request = CreateStoreRequest.builder()
                                .name("My Store")
                                .address("Seoul, Korea")
                                .phoneNumber("02-1234-5678")
                                .storeCategory(hello.wsd.domain.store.entity.StoreCategory.RESTAURANT)
                                .build();

                // when & then
                mockMvc.perform(post("/api/stores")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.isSuccess").value(true))
                                .andExpect(jsonPath("$.data").isNumber());
        }

        @Test
        @DisplayName("상점 생성 실패 (고객)")
        void createStore_Fail_Customer() throws Exception {
                // given
                CreateStoreRequest request = CreateStoreRequest.builder()
                                .name("Customer Store")
                                .address("Seoul, Korea")
                                .phoneNumber("02-1111-2222")
                                .storeCategory(hello.wsd.domain.store.entity.StoreCategory.RESTAURANT)
                                .build();

                // when & then
                mockMvc.perform(post("/api/stores")
                                .header(HttpHeaders.AUTHORIZATION, customerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("상점 수정 성공")
        void updateStore_Success() throws Exception {
                // given
                // Create Store first
                CreateStoreRequest createRequest = CreateStoreRequest.builder()
                                .name("Old Store")
                                .address("Old Address")
                                .phoneNumber("02-0000-0000")
                                .storeCategory(hello.wsd.domain.store.entity.StoreCategory.RESTAURANT)
                                .build();
                String responseContent = mockMvc.perform(post("/api/stores")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest)))
                                .andReturn().getResponse().getContentAsString();

                // Extract Store ID (Assuming standard response structure)
                // Note: For robustness, we might want to parse JSON properly, but simple string
                // manipulation or json path works for integration test if structure is known.
                // Or better, capture the ID from the service call if possible, but here we
                // integration test the API.
                // Let's rely on the fact that result returns data as Long.

                // Actually, let's just create store using service directly to get ID for update
                // test?
                // No, keep it pure integration via API if possible, or mix.
                // Let's parse the response.
                Long storeId = objectMapper.readTree(responseContent).path("data").asLong();

                UpdateStoreRequest updateRequest = UpdateStoreRequest.builder()
                                .name("New Store Name")
                                .address("New Address")
                                .phoneNumber("02-9999-8888")
                                .build();

                // when & then
                mockMvc.perform(patch("/api/stores/" + storeId)
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest)))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.isSuccess").value(true));
        }

        @Test
        @DisplayName("상점 수정 실패 (본인 상점 아님)")
        void updateStore_Fail_NotOwner() throws Exception {
                // given
                // Create store by owner
                CreateStoreRequest createRequest = CreateStoreRequest.builder()
                                .name("Owner Store")
                                .address("Addr")
                                .phoneNumber("Tel")
                                .storeCategory(hello.wsd.domain.store.entity.StoreCategory.RESTAURANT)
                                .build();
                String responseContent = mockMvc.perform(post("/api/stores")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest)))
                                .andReturn().getResponse().getContentAsString();
                Long storeId = objectMapper.readTree(responseContent).path("data").asLong();

                // New Owner
                SignupRequest otherOwnerSignup = SignupRequest.builder()
                                .name("owner2")
                                .email("owner2@example.com")
                                .password("password")
                                .phoneNumber("010-9876-5432")
                                .role(Role.ROLE_OWNER)
                                .businessNumber("987-65-43210")
                                .build();
                authService.signUp(otherOwnerSignup);
                AuthTokens otherOwnerAuth = authService.login(new LoginRequest("owner2@example.com", "password"));
                String otherOwnerToken = "Bearer " + otherOwnerAuth.getAccessToken();

                UpdateStoreRequest updateRequest = UpdateStoreRequest.builder()
                                .name("Hacked Store")
                                .address("Hacked Addr")
                                .phoneNumber("02-HACK-ED")
                                .build();

                // when & then
                mockMvc.perform(patch("/api/stores/" + storeId)
                                .header(HttpHeaders.AUTHORIZATION, otherOwnerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest)))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("상점 조회 성공")
        void getStore_Success() throws Exception {
                // given
                CreateStoreRequest createRequest = CreateStoreRequest.builder()
                                .name("Get Store")
                                .address("Addr")
                                .phoneNumber("Tel")
                                .storeCategory(hello.wsd.domain.store.entity.StoreCategory.RESTAURANT)
                                .build();
                String responseContent = mockMvc.perform(post("/api/stores")
                                .header(HttpHeaders.AUTHORIZATION, ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest)))
                                .andReturn().getResponse().getContentAsString();
                Long storeId = objectMapper.readTree(responseContent).path("data").asLong();

                // when & then
                mockMvc.perform(get("/api/stores/" + storeId)
                                .header(HttpHeaders.AUTHORIZATION, customerToken)) // Public or accessible
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.name").value("Get Store"));
        }
}
