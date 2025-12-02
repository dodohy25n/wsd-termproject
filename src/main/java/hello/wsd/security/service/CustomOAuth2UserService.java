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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 리소스 서버에서 받아온 유저 정보 할당
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 어떤 소셜 서비스인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo userInfo = extractUserInfo(registrationId, oAuth2User.getAttributes());

        // User 저장 또는 업데이트
        User user = saveOrUpdate(userInfo);

        // PrincipalDetails 반환 (SecurityContext에 저장됨)
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

        User user = userRepository.findByUsername(username)
                .map(entity -> entity.updateSocialInfo(userInfo)) // 소셜 정보 변경 시 DB에 반영 (Dirty Checking)
                .orElse(User.builder() // 없으면 생성
                        .username(username)
                        .password(UUID.randomUUID().toString()) // 소셜로그인은 비밀번호 불필요하므로 랜덤값
                        .name(userInfo.getName())
                        .phoneNumber("")
                        .role(Role.ROLE_CUSTOMER)
                        .socialType(SocialType.valueOf(userInfo.getProvider().toUpperCase()))
                        .socialId(userInfo.getProviderId())
                        .build());

        return userRepository.save(user);

    }
}