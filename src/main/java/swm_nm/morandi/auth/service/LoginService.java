package swm_nm.morandi.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.auth.response.GoogleUserDto;
import swm_nm.morandi.auth.response.TokenDto;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.member.service.MemberService;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final OAuthServiceFactory oAuthServiceFactory;
    private final MemberService memberService;

    public TokenDto login(String type, String authorization_code){

        OAuthService oAuthService = oAuthServiceFactory.getServiceByType(type);
        if (oAuthService == null) {
            throw new MorandiException(AuthErrorCode.INVALID_SOCIAL_TYPE);
        }
        String accessToken = oAuthService.getAccessToken(authorization_code);

        GoogleUserDto googleUserDto = oAuthService.getMemberInfo(accessToken);

        return memberService.loginOrRegisterMember(googleUserDto);


    }

    public TokenDto loginforDev(String type, String authorization_code){

        OAuthService oAuthService = oAuthServiceFactory.getServiceByType(type);
        if (oAuthService == null) {
            throw new MorandiException(AuthErrorCode.INVALID_SOCIAL_TYPE);
        }
        String accessToken = oAuthService.getAccessTokenDev(authorization_code);

        GoogleUserDto googleUserDto = oAuthService.getMemberInfoDev(accessToken);

        return memberService.loginOrRegisterMember(googleUserDto);


    }
    public TokenDto OAuthJoinOrLogin(String type, String accessToken){
        OAuthService oAuthService = oAuthServiceFactory.getServiceByType(type);
        if (oAuthService == null) {
            throw new MorandiException(AuthErrorCode.INVALID_SOCIAL_TYPE);
        }
        GoogleUserDto googleUserDto = oAuthService.getMemberInfo(accessToken);
        
        return memberService.loginOrRegisterMember(googleUserDto);


    }
}
