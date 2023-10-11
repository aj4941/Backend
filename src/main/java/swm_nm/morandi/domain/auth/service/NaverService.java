package swm_nm.morandi.domain.auth.service;

import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.auth.response.UserDto;

@Service
public class NaverService implements OAuthService {
    @Override
    public String getType() {
        return "naver";
    }

    @Override
    public String getAccessToken(String authorization_code, Boolean isDev) {
        return null;
    }

    @Override
    public UserDto getMemberInfo(String accessToken) {
        return null;
    }
}
