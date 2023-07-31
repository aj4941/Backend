package swm_nm.morandi.auth.service;

import swm_nm.morandi.auth.response.GoogleUserDto;

public interface OAuthService {
    String getType();
    String getAccessToken(String authorization_code, Boolean isDev);
    GoogleUserDto getMemberInfo(String accessToken);

//    String getAccessTokenDev(String authorization_code);
//
//    GoogleUserDto getMemberInfoDev(String accessToken);
}
