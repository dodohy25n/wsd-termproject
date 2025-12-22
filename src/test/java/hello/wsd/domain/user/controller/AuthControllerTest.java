package hello.wsd.domain.user.controller;

import hello.wsd.common.BaseIntegrationTest;
import hello.wsd.domain.user.dto.LoginRequest;
import hello.wsd.domain.user.dto.SignupRequest;
import hello.wsd.domain.user.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

class AuthControllerTest extends BaseIntegrationTest {

        @Test
        @DisplayName("회원가입 성공")
        void signUp_Success() throws Exception {
                // given
                SignupRequest request = SignupRequest.builder()
                                .name("user1")
                                .email("user1@example.com")
                                .password("password")
                                .phoneNumber("010-1234-5678")
                                .role(Role.ROLE_CUSTOMER)
                                .build();

                // when & then
                mockMvc.perform(post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.isSuccess").value(true))
                                .andExpect(jsonPath("$.data").isNumber());
        }

        @Test
        @DisplayName("회원가입 실패 - 이메일 중복")
        void signUp_Fail_Duplicate() throws Exception {
                // given
                SignupRequest request1 = SignupRequest.builder()
                                .name("user1")
                                .email("duplicate@example.com")
                                .password("password")
                                .phoneNumber("010-1111-1111")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                mockMvc.perform(post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request1)));

                SignupRequest request2 = SignupRequest.builder()
                                .name("user2")
                                .email("duplicate@example.com")
                                .password("password")
                                .phoneNumber("010-2222-2222")
                                .role(Role.ROLE_CUSTOMER)
                                .build();

                // when & then
                mockMvc.perform(post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request2)))
                                .andDo(print())
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.isSuccess").value(false));
        }

        @Test
        @DisplayName("로그인 성공")
        void login_Success() throws Exception {
                // given
                SignupRequest signupRequest = SignupRequest.builder()
                                .name("user3")
                                .email("login@example.com")
                                .password("password")
                                .phoneNumber("010-3333-3333")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                mockMvc.perform(post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signupRequest)));

                LoginRequest loginRequest = new LoginRequest("login@example.com", "password");

                // when & then
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.isSuccess").value(true))
                                .andExpect(jsonPath("$.data.accessToken").exists())
                                .andExpect(cookie().exists("refreshToken"));
        }

        @Test
        @DisplayName("로그인 실패 - 잘못된 비밀번호")
        void login_Fail_Invalid() throws Exception {
                // given
                SignupRequest signupRequest = SignupRequest.builder()
                                .name("user4")
                                .email("invalid@example.com")
                                .password("password")
                                .phoneNumber("010-4444-4444")
                                .role(Role.ROLE_CUSTOMER)
                                .build();
                mockMvc.perform(post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signupRequest)));

                LoginRequest loginRequest = new LoginRequest("invalid@example.com", "wrongpassword");

                // when & then
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andDo(print())
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.isSuccess").value(false));
        }
}
