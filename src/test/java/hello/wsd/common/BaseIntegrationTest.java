package hello.wsd.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected hello.wsd.domain.user.service.AuthService authService;

    @Autowired
    protected hello.wsd.domain.user.repository.UserRepository userRepository;

    @MockitoBean
    protected hello.wsd.security.service.FirebaseAuthService firebaseAuthService;

    @MockitoBean
    protected hello.wsd.domain.user.service.RefreshTokenService refreshTokenService;

    @MockitoBean
    protected org.springframework.security.oauth2.client.registration.ClientRegistrationRepository clientRegistrationRepository;
}
