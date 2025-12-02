// CustomOAuth2UserService.java
package hello.wsd.security.service;

import hello.wsd.domain.user.entity.Role;
import hello.wsd.domain.user.entity.SocialType;
import hello.wsd.domain.user.entity.User;
import hello.wsd.domain.user.repository.UserRepository;
import hello.wsd.security.details.PrincipalDetails;
import hello.wsd.security.oauth.GoogleOAuth2UserInfo;
import hello.wsd.security.oauth.KakaoOAuth2UserInfo;
import hello.wsd.security.oauth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 소셜 로그인 API를 통해 유저 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 어떤 소셜 서비스인지 확인 (google, kakao 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. 유저 정보 가공
        OAuth2UserInfo userInfo = extractUserInfo(registrationId, oAuth2User.getAttributes());

        // 4. User 엔티티 생성 또는 업데이트
        User user = saveOrUpdate(userInfo);

        // 5. PrincipalDetails 반환 (SecurityContext에 저장됨)
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo extractUserInfo(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if ("kakao".equals(registrationId)) {
            return new KakaoOAuth2UserInfo(attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
    }

    private User saveOrUpdate(OAuth2UserInfo userInfo) {
        // 소셜 식별자로 조회 (없으면 저장, 있으면 업데이트)
        // username 예시: google_12345678, kakao_87654321
        String username = userInfo.getProvider() + "_" + userInfo.getProviderId();

        return userRepository.findByUsername(username)
                .map(entity -> entity) // 필요시 정보 업데이트 로직 추가 가능
                .orElseGet(() -> {
                    return userRepository.save(User.builder()
                            .username(username)
                            .password(UUID.randomUUID().toString()) // 소셜로그인은 비밀번호 불필요하므로 랜덤값
                            .name(userInfo.getName())
                            .phoneNumber("") // 소셜에서 전화번호 안 주면 빈값 or 추가 입력 필요
                            .role(Role.ROLE_CUSTOMER)
                            .socialType(SocialType.valueOf(userInfo.getProvider().toUpperCase()))
                            .socialId(userInfo.getProviderId())
                            .build());
                });
    }
}