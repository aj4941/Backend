package swm_nm.morandi.domain.codeSubmit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import swm_nm.morandi.aop.annotation.MemberLock;
import swm_nm.morandi.domain.codeSubmit.constants.CodeVisuabilityConstants;
import swm_nm.morandi.domain.codeSubmit.dto.BaekjoonUserDto;
import swm_nm.morandi.domain.codeSubmit.dto.SolutionIdDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.testDuring.dto.TestInfo;
import swm_nm.morandi.domain.testDuring.service.TempCodeService;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.exception.errorcode.SubmitErrorCode;
import swm_nm.morandi.domain.codeSubmit.dto.SubmitCodeDto;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.redis.utils.RedisKeyGenerator;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class BaekjoonSubmitService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String USER_AGENT = "Mozilla/5.0";

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisKeyGenerator redisKeyGenerator;

    private final TempCodeService tempCodeService;

    private final MemberRepository memberRepository;

    private final AttemptProblemRepository attemptProblemRepository;
    //백준 로그인용 쿠키 저장
    //Redis에 현재 로그인한 사용자의 백준 제출용 쿠키를 저장
    @Transactional
    public String saveBaekjoonInfo(BaekjoonUserDto baekjoonUserDto) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MorandiException(MemberErrorCode.EXTENSION_MEMBER_NOT_FOUND));

        //validateBojId(member, baekjoonUserDto.getBojId());

        saveCookieToRedis(memberId, baekjoonUserDto.getCookie());

        //Member에 백준 아이디 초기화
       // if(member.getBojId()==null) {
            updateMemberInfo(member, baekjoonUserDto.getBojId());
       // }
        return baekjoonUserDto.getCookie();
    }

    private void validateBojId(Member member, String bojId) {
        String existingBojId = member.getBojId();
        //이미 백준 아이디가 존재하는 경우 -> TODO 크롬익스텐션에서 예외 잡아서 loginCookieValue null로 처리하고 팝업 하나 띄우기
        if (existingBojId == null && memberRepository.existsByBojId(bojId)) {
            throw new MorandiException(MemberErrorCode.DUPLICATED_BOJ_ID);
        }
        //백준 아이디가 기존에 저장된 id랑 다른 경우 -> TODO 크롬익스텐션에서 예외 잡아서 팝업 띄우고 기존 저장된 백준 id가 다르다고 알려주기
        if (existingBojId != null && !existingBojId.equals(bojId)) {
            throw new MorandiException(SubmitErrorCode.BAEKJOON_INVALID_ID);
        }

    }

    private void saveCookieToRedis(Long memberId,String cookie){
        String key = generateKey(memberId);
        //Redis에 쿠키 저장
        redisTemplate.opsForValue().set(key, cookie);
        redisTemplate.expire(key, 12, TimeUnit.HOURS);
    }
    private String generateKey(Long memberId) {
        return String.format("OnlineJudgeCookie:memberId:%s", memberId);
    }

    private void updateMemberInfo(Member member, String bojId){
        member.setBojId(bojId);
        memberRepository.save(member);

    }

    @MemberLock
    @Transactional
    public ResponseEntity<SolutionIdDto> submit(SubmitCodeDto submitCodeDto) {
        validateBojProblemId(submitCodeDto.getBojProblemId());

        Long memberId = SecurityUtils.getCurrentMemberId();
        String cookie = getCookieFromRedis(generateKey(memberId));
        String CSRFKey = getCSRFKey(cookie, submitCodeDto.getBojProblemId());

        SolutionIdDto solutionId=  sendSubmitRequest(cookie, CSRFKey, submitCodeDto);

        //제출한 코드 정보를 저장
        saveSubmitTempCode(submitCodeDto);

        return ResponseEntity.status(HttpStatus.OK).body(solutionId);
    }
    private void validateBojProblemId(String bojProblemId) {
        try {
            int problemId = Integer.parseInt(bojProblemId);
            if (problemId >= 30000 || problemId < 1000)
                throw new MorandiException(SubmitErrorCode.INVALID_BOJPROBLEM_NUMBER);
        }
        catch (NumberFormatException e) {
            throw new MorandiException(SubmitErrorCode.INVALID_BOJPROBLEM_NUMBER);
        }
    }
    private String getCookieFromRedis(String key){
        return Optional.ofNullable((String) redisTemplate.opsForValue().get(key))
                .orElseThrow(() -> new MorandiException(SubmitErrorCode.COOKIE_NOT_EXIST));
    }

    //POST 보낼 때 필요한 CSRF 키를 가져옴
    private String getCSRFKey(String cookie, String bojProblemId) {

        String acmicpcUrl = String.format("https://www.acmicpc.net/submit/%s", bojProblemId);

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

    //쿠키를 헤더에 추가
    private HttpHeaders createHeaders(String cookie) {
        HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", USER_AGENT);
            headers.set("Cookie", "OnlineJudge=" + cookie);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private SolutionIdDto sendSubmitRequest(String cookie, String CSRFKey, SubmitCodeDto submitCodeDto) {
        String acmicpcUrl = String.format("https://www.acmicpc.net/submit/%s", submitCodeDto.getBojProblemId());
        HttpHeaders headers = createHeaders(cookie);
        MultiValueMap<String, String> parameters = createParameters(submitCodeDto, CSRFKey);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(acmicpcUrl, request, String.class);
            String location = response.getHeaders().getLocation().toString();
            if(location.contains("status")) {
                if (!location.startsWith("http"))
                    location = URI.create(acmicpcUrl).resolve(location).toString();


                ResponseEntity<String> redirectedResponse = restTemplate.exchange(location, HttpMethod.GET, new HttpEntity<>(headers), String.class);



                // HTML에서 solution-id를 추출합니다.
                SolutionIdDto solutionId = extractSolutionIdFromHtml(redirectedResponse.getBody());
                
                if (solutionId.getSolutionId() != null) {
                    return solutionId;
                }
                else {
                    throw new MorandiException(SubmitErrorCode.CANT_FIND_SOLUTION_ID);
                }
            }


        }
        catch(HttpClientErrorException e) {
            throw new MorandiException(SubmitErrorCode.BAEKJOON_SERVER_ERROR);
        }
        catch(NullPointerException e) {
            throw new MorandiException(SubmitErrorCode.NULL_POINTER_EXCEPTION);
        }
        catch(Exception e) {       //알 수 없는 오류
            throw new MorandiException(SubmitErrorCode.BAEKJOON_UNKNOWN_ERROR);
        }

        throw new MorandiException(SubmitErrorCode.BAEKJOON_UNKNOWN_ERROR);
    }
    //POST로 보낼 때 필요한 파라미터들을 생성
    private MultiValueMap<String, String> createParameters(SubmitCodeDto submitCodeDto, String CSRFKey) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("problem_id", submitCodeDto.getBojProblemId());
            parameters.add("language", submitCodeDto.getLanguageId());
            parameters.add("code_open", CodeVisuabilityConstants.CLOSE.getCodeVisuability());
            parameters.add("source", submitCodeDto.getSourceCode());
            parameters.add("csrf_key", CSRFKey);
        return parameters;
    }

    private SolutionIdDto extractSolutionIdFromHtml(String html) {
        // HTML을 파싱합니다.
        Document doc = Jsoup.parse(html);

        // 테이블을 선택합니다.
        Element table = doc.getElementById("status-table");

        // 첫 번째 행을 선택합니다.
        Element firstRow = table.select("tbody tr").first();

        // 첫 번째 행에서 solution-id를 추출합니다.
        Element solutionIdElement = firstRow.select("td").first(); // 첫 번째 열에 있는 것이 solution-id 입니다.

        if (solutionIdElement != null) {
            String solutionId = solutionIdElement.text();

            return new SolutionIdDto(solutionId);

        } else {
           throw new MorandiException(SubmitErrorCode.CANT_FIND_SOLUTION_ID);
        }
    }

    private void saveSubmitTempCode(SubmitCodeDto submitCodeDto)
    {
        String ongoingTestKey = redisKeyGenerator.generateOngoingTestKey();
        Long testId = ((TestInfo) Optional.ofNullable(redisTemplate.opsForValue().get(ongoingTestKey))
                .orElseThrow(() -> new MorandiException(SubmitErrorCode.TEST_NOT_EXIST))).testId;

        //TODO
        //아래 코드는 프론트 고치면 추가하기
//        tempCodeService.saveTempCode(testId, submitCodeDto.getProblemNumber(), submitCodeDto.getLanguage(), submitCodeDto.getSourceCode());

        saveSubmitCodeToDatabase(testId,submitCodeDto);
    }

    private void saveSubmitCodeToDatabase(Long testId, SubmitCodeDto submitCodeDto) {

        AttemptProblem attemptProblem = attemptProblemRepository.findByTest_TestIdAndProblem_BojProblemId(testId,Long.parseLong(submitCodeDto.getBojProblemId()))
                .orElseThrow(() -> new MorandiException(SubmitErrorCode.TEST_NOT_EXIST));

        attemptProblem.setSubmitCode(submitCodeDto.getSourceCode());
        //TODO
        //attemptProblem에 마지막으로 제출한 language의 정보가 빠져있음 -> 이거 나중에 추가하던지 해야함
        attemptProblemRepository.save(attemptProblem);
    }
}
