package swm_nm.morandi.domain.codeSubmit.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import swm_nm.morandi.domain.codeSubmit.constants.CodeVisuabilityConstants;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.SubmitErrorCode;
import swm_nm.morandi.domain.codeSubmit.dto.SubmitCodeDto;

import java.net.URI;

@Service
public class BaekjoonSubmitService {

    private final RestTemplate restTemplate;
    private final String USER_AGENT = "Mozilla/5.0";
    //private final String COOKIE = "OnlineJudge=sfvgti0buhle3m14tcvdh8fh7m";  // 실제 쿠키 값으로 변경 필요

    public BaekjoonSubmitService() {
        this.restTemplate = new RestTemplate();
    }

    //쿠키를 헤더에 추가
    private HttpHeaders createHeaders(String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", USER_AGENT);
        headers.set("Cookie", "OnlineJudge=" + cookie);
        return headers;
    }

    //POST 보낼 때 필요한 CSRF 키를 가져옴
    public String getCSRFKey(String cookie, String problemId) {
        String acmicpcUrl = "https://www.acmicpc.net/submit/" + problemId;

        HttpHeaders headers = createHeaders(cookie);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try{
            ResponseEntity<String> response = restTemplate.exchange(acmicpcUrl, HttpMethod.GET, entity, String.class);

            Document document = Jsoup.parse(response.getBody());
            return document.select("input[name=csrf_key]").attr("value");
        }
        catch(HttpClientErrorException e){
            throw new MorandiException(SubmitErrorCode.BAEKJOON_SERVER_ERROR);
        }

    }
    //POST로 보낼 때 필요한 파라미터들을 생성
    private MultiValueMap<String, String> createParameters(SubmitCodeDto submitCodeDto, String CSRFKey, String problemId) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("problem_id", problemId);
        parameters.add("language", submitCodeDto.getLanguageId());
        parameters.add("code_open", CodeVisuabilityConstants.CLOSE.getCodeVisuability());
        parameters.add("source", submitCodeDto.getSourceCode());
        parameters.add("csrf_key", CSRFKey);
        return parameters;
    }
    public ResponseEntity<String> submit(SubmitCodeDto submitCodeDto) {
        String cookie = submitCodeDto.getCookie();
        String problemId = submitCodeDto.getProblemId();

        String CSRFKey = getCSRFKey(cookie, problemId);

        String acmicpcUrl = "https://www.acmicpc.net/submit/" + problemId;

        HttpHeaders headers = createHeaders(cookie);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = createParameters(submitCodeDto, CSRFKey, problemId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(acmicpcUrl, request, String.class);
            String location = response.getHeaders().getLocation().toString();

            if(location.contains("status")) {
                if (!location.startsWith("http"))
                    location = URI.create(acmicpcUrl).resolve(location).toString();
                ResponseEntity<String> redirectedResponse = restTemplate.exchange(location, HttpMethod.GET, new HttpEntity<>(headers), String.class);
                return ResponseEntity.status(redirectedResponse.getStatusCode()).body(redirectedResponse.getBody());
                //return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
            else
                throw new MorandiException(SubmitErrorCode.BAEKJOON_LOGIN_ERROR);
        }
        catch(HttpClientErrorException e) {
            throw new MorandiException(SubmitErrorCode.BAEKJOON_SERVER_ERROR);
        }
    }


}
