package swm_nm.morandi.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.auth.response.TokenDto;
import swm_nm.morandi.domain.auth.response.UserDto;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final OAuthServiceFactory oAuthServiceFactory;
    private final MemberLoginService memberService;

    public TokenDto login(String type, String authorization_code, Boolean isDev){

        OAuthService oAuthService = oAuthServiceFactory.getServiceByType(type);
        if (oAuthService == null) {
            throw new MorandiException(AuthErrorCode.INVALID_SOCIAL_TYPE);
        }
        String accessToken = oAuthService.getAccessToken(authorization_code, isDev);
        UserDto userDto = oAuthService.getMemberInfo(accessToken);

        return memberService.loginOrRegisterMember(userDto);


    }

    //이건 안 쓰는 코드
    public TokenDto OAuthJoinOrLogin(String type, String accessToken){
        OAuthService oAuthService = oAuthServiceFactory.getServiceByType(type);
        if (oAuthService == null) {
            throw new MorandiException(AuthErrorCode.INVALID_SOCIAL_TYPE);
        }
        UserDto userDto = oAuthService.getMemberInfo(accessToken);
        
        return memberService.loginOrRegisterMember(userDto);


    }
}
