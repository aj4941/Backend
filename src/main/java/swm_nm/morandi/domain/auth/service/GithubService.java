package swm_nm.morandi.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import swm_nm.morandi.domain.auth.constants.OAuthConstants;
import swm_nm.morandi.domain.auth.response.*;
import swm_nm.morandi.domain.member.entity.SocialType;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;

import java.util.Arrays;
import java.util.List;

//Test

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubService implements OAuthService {


    @Value("${oauth.github.client-id}")
    private String githubClientId;

    @Value("${oauth.github.client-secret}")
    private String githubClientSecret;


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Override

    public String getType() {
        return "github";
    }

    @Override
    public String getAccessToken(String authorization_code, Boolean isDev) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorization_code);
        params.add("client_id", githubClientId);
        params.add("client_secret", githubClientSecret);



        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity("https://github.com/login/oauth/access_token", requestEntity, String.class);

            //Access Token을 가져옴
            return Arrays.stream(responseEntity.getBody().split("&"))
                    .filter(s -> s.startsWith("access_token="))
                    .findFirst()
                    .orElse("")
                    .split("=")[1];

        } catch (RestClientException e) {
            log.error("params = {}, requestEntity = {}", params, requestEntity);
            throw new MorandiException(AuthErrorCode.SSO_SERVER_ERROR);
        } catch (NullPointerException e) {
            log.error("params = {}, requestEntity = {}, responseEntity = {}", params, requestEntity, responseEntity);
            throw new MorandiException(AuthErrorCode.SSO_ACCESS_TOKEN);
        }

    }

    @Override
    public UserDto getMemberInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = null;
        try{
            //유저 정보 가져오기
            responseEntity =restTemplate.exchange(OAuthConstants.GITHUB_USERINFO_REQUEST_URL, HttpMethod.GET,requestEntity,String.class);
            GithubUserDto githubUserDto = objectMapper.readValue(responseEntity.getBody(), GithubUserDto.class);
            //이메일 가져오기
            responseEntity =restTemplate.exchange(OAuthConstants.GITHUB_EMAIL_REQUEST_URL, HttpMethod.GET,requestEntity,String.class);
            List<GithubEmailDto> emailList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>(){} );

            githubUserDto.setType(SocialType.GITHUB);
            githubUserDto.setEmail(emailList.get(0).getEmail());

            return githubUserDto;
        }
        catch (RestClientException e){
            log.error("requestEntity = {}",requestEntity);
            throw new MorandiException(AuthErrorCode.SSO_SERVER_ERROR);
        }
        catch (JsonProcessingException | NullPointerException e)
        {
            log.error("responseEntity = {}",responseEntity);
            throw new MorandiException(AuthErrorCode.SSO_USERINFO);
        }


    }

}
