package swm_nm.morandi.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ScriptOutputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.StringHelper;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverService implements OAuthService {

    @Value("${oauth.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.naver.client-secret}")
    private String naverClientSecret;

    @Value("${oauth.naver.redirect-uri}")
    private String naverClientRedirectUri;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public String getType() {
        return "naver";
    }

    @Override
    public String getAccessToken(String authorization_code, Boolean isDev) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", authorization_code);
        params.add("client_id", naverClientId);
        params.add("client_secret", naverClientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = restTemplate.postForEntity("https://nid.naver.com/oauth2.0/token", requestEntity, String.class);
            String accessToken = objectMapper.readValue(responseEntity.getBody(), TokenResponseDto.class).getAccess_token();
            return accessToken;
        } catch (RestClientException e) {
            log.error("params = {}, requestEntity = {}", params, requestEntity);
            throw new MorandiException(AuthErrorCode.SSO_SERVER_ERROR);
        } catch (NullPointerException e) {
            log.error("params = {}, requestEntity = {}, responseEntity = {}", params, requestEntity, responseEntity);
            throw new MorandiException(AuthErrorCode.SSO_ACCESS_TOKEN);
        } catch (JsonProcessingException e) {
            throw new MorandiException(AuthErrorCode.SSO_ACCESS_TOKEN);
        }
    }

    @Override
    public UserDto getMemberInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ accessToken);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = null;
        try {
            // 유저 정보 가져오기
            System.out.println("NaverService.getMemberInfo");
            responseEntity = restTemplate.exchange(OAuthConstants.NAVER_USERINFO_REQUEST_URL, HttpMethod.GET, requestEntity, String.class);
            String responseBody = responseEntity.getBody();

            // JSON 문자열을 JsonNode로 파싱
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // "response" 부분을 가져옴
            JsonNode responseNode = jsonNode.get("response");
            NaverUserDto naverUserDto = NaverUserDto.builder()
                    .id(responseNode.get("id").asText())
                    .email(responseNode.get("email").asText())
                    .profileImage(responseNode.get("profile_image").asText())
                    .build();

            return naverUserDto;
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
