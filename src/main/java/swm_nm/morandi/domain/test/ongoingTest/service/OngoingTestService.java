package swm_nm.morandi.domain.test.ongoingTest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.test.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.member.service.MemberService;
import swm_nm.morandi.domain.test.ongoingTest.scheduler.TestScheduler;
import swm_nm.morandi.domain.test.entity.AttemptProblem;
import swm_nm.morandi.domain.test.postTest.request.AttemptProblemDto;
import swm_nm.morandi.domain.test.postTest.service.PostTestService;
import swm_nm.morandi.domain.test.dto.TestCheckDto;
import swm_nm.morandi.domain.test.dto.TestInputData;
import swm_nm.morandi.domain.test.dto.TestStartResponseDto;
import swm_nm.morandi.domain.test.dto.TestStatus;
import swm_nm.morandi.domain.test.entity.TestType;
import swm_nm.morandi.domain.test.entity.Tests;
import swm_nm.morandi.domain.test.repository.TestRepository;
import swm_nm.morandi.domain.test.repository.TestTypeRepository;
import swm_nm.morandi.domain.problem.dto.BojProblem;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.problem.dto.DifficultyRange;
import swm_nm.morandi.domain.test.dto.OutputDto;
import swm_nm.morandi.domain.problem.entity.Algorithm;
import swm_nm.morandi.domain.problem.entity.AlgorithmProblemList;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.entity.TypeProblemList;
import swm_nm.morandi.domain.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.domain.problem.repository.TypeProblemListRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class OngoingTestService {

    private final MemberRepository memberRepository;

    private final TestRepository testRepository;

    private final TestTypeRepository testTypeRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final TypeProblemListRepository typeProblemListRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    private final MemberService memberService;

    private final PostTestService testResultService;

    private final TempCodeService tempCodeService;

    private final TestScheduler testScheduler;

    private final ObjectMapper objectMapper;

    public OutputDto runCode(TestInputData testInputData) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://10.0.102.184:8080";

        ObjectMapper objectMapper = new ObjectMapper();
        String inputDataJson = objectMapper.writeValueAsString(testInputData);

        // Create POST request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(inputDataJson));

        // Send POST request
        HttpResponse response = httpClient.execute(httpPost);

        // Check response status code
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String responseJson = EntityUtils.toString(response.getEntity());
            OutputDto outputDto = objectMapper.readValue(responseJson, OutputDto.class);
            return outputDto;
        } else {
            throw new Exception("HTTP request failed with status code: " + statusCode);
        }
    }

    public List<AttemptProblemDto> isSolvedCheck(TestCheckDto testCheckDto) {
        Long testId = testCheckDto.getTestId();
        String bojId = testCheckDto.getBojId();
        checkAttemptedProblemResult(testId, bojId);
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);
        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        long number = 1;
        for (AttemptProblem attemptProblem : attemptProblems) {
            AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
            attemptProblemDto.setTestProblemId(number++);
            attemptProblemDtos.add(attemptProblemDto);
        }
        return attemptProblemDtos;
    }

    @Transactional
    public void checkAttemptedProblemResult(Long testId, String bojId) {
        Tests test = testRepository.findById(testId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);

        attemptProblems.stream()
                .filter(attemptProblem -> !attemptProblem.getIsSolved())
                .filter(attemptProblem -> isSolvedProblem(attemptProblem, bojId))
                .forEach(attemptProblem -> {
                    Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
                    Long minutes = duration.toMinutes();
                    if (minutes <= test.getTestTime()) {
                        attemptProblem.setIsSolved(true);
                        attemptProblem.setExecutionTime(minutes);
                    }
                });
    }
    private boolean isSolvedProblem(AttemptProblem attemptProblem, String bojId) {
        Problem problem = attemptProblem.getProblem();
        Long bojProblemId = problem.getBojProblemId();
        String apiURL = "https://solved.ac/api/v3/search/problem";
        String query = apiURL + "/?query=" + "id:" + bojProblemId.toString() + "%26@" + bojId;

        URI uri = URI.create(query);

        WebClient webClient = WebClient.builder().build();
        String jsonString = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode checkNode = jsonNode.get("count");
            return checkNode.asLong()==1L;

        }
        catch(NullPointerException e){
            throw new RuntimeException("Node null 반환했습니다.");
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("json 파싱에 실패했습니다.");
        }
    }

    public TestStartResponseDto getTestStartsData(Long testTypeId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 현재 테스트가 진행중인지 확인
        if (member.getCurrentTestId() == null)
            member.setCurrentTestId(-1L);

        // 현재 테스트가 진행중이라면
        if (member.getCurrentTestId() != -1) {
            Long currentTestId = member.getCurrentTestId();
            Tests test = testRepository.findById(currentTestId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));

            // 테스트 시작 시간과 현재 시간을 비교하여 남은 시간 계산
            Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
            test.setRemainingTime(test.getTestTime() * 60 - duration.getSeconds());

            if (test.getRemainingTime() > 0) { // 시간이 남았을경우 진행중인 테스트 반환
                List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(currentTestId);
                List<Long> bojProblemIds = attemptProblems.stream().map(AttemptProblem::getProblem)
                        .map(Problem::getBojProblemId).collect(Collectors.toList());
                TestStartResponseDto testStartResponseDto
                        = TestStartResponseDto.builder()
                        .testId(currentTestId)
                        .bojProblemIds(bojProblemIds)
                        .remainingTime(test.getRemainingTime())
                        .build();

                // 테스트 시작에 대한 ResponseDto 반환
                return testStartResponseDto;
            }
            else { // 시간이 마감된 테스트 종료
                TestCheckDto testCheckDto = TestCheckDto.builder()
                        .testId(test.getTestId())
                        .testTypeId(testTypeId)
                        .bojId(member.getBojId())
                        .build();
                testResultService.testExit(testCheckDto);
            }
        }
        // 현재 진행중인 테스트가 없을 경우 아래 로직 진행
        Tests test = startTestByTestTypeId(testTypeId, memberId);
        Long testId = test.getTestId();

        TestCheckDto testCheckDto = TestCheckDto.builder()
                .testId(testId)
                .testTypeId(testTypeId)
                .bojId(member.getBojId())
                .build();

        testScheduler.addTest(testCheckDto);
        member.setCurrentTestId(testId);

        String bojId = memberService.getBojId(memberId);
        List<BojProblem> bojProblems = new ArrayList<>();
        getProblemsByTestType(testTypeId, bojProblems);
        getProblemsByApi(testTypeId, bojId, bojProblems);

        List<Long> bojProblemIds = testResultService.saveAttemptProblems(memberId, testId, bojProblems);

        // 테스트 시작시 코드 캐시 초기화
        tempCodeService.initTempCodeCacheWhenTestStart(test);

        //TODO
        // 테스트 시작에 대한 ResponseDto 반환시
        // 백준의 ID가 아니라 attemptProblemID도 함께 반환하도록
        // 이후 주기적으로 Redis에 코드를 저장할 때 attemptProblemId를 반환하게
        TestStartResponseDto testStartResponseDto
                = TestStartResponseDto.builder()
                .testId(testId)
                .bojProblemIds(bojProblemIds)
                .remainingTime(test.getRemainingTime())
                .build();

        return testStartResponseDto;
    }

    @Transactional
    public Tests startTestByTestTypeId(Long testTypeId, Long memberId) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(()-> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        Tests test = Tests.builder()
                .testDate(LocalDateTime.now()) // 테스트가 시작된 시간
                .testTime(testType.getTestTime())
                .problemCount(testType.getProblemCount())
                .remainingTime(testType.getTestTime() * 60L)
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .testTypename(testType.getTestTypename())
                .testRating(null)
                .testStatus(TestStatus.IN_PROGRESS)
                .member(member)
                .build();

        testRepository.save(test);

        return test;
    }

    public void getProblemsByTestType(Long testTypeId, List<BojProblem> bojProblems) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        List<TypeProblemList> typeProblemLists = typeProblemListRepository.findByTestType_TestTypeId(testTypeId);
        List<Problem> problems = typeProblemLists.stream().map(TypeProblemList::getProblem).collect(Collectors.toList());
        List<DifficultyRange> difficultyRanges = testType.getDifficultyRanges();
        Random random = new Random();
        long randomNumber = random.nextInt(10);
        long index = 1;
        for (DifficultyRange difficultyRange : difficultyRanges) {
            int start = DifficultyLevel.getLevelByValue(difficultyRange.getStart());
            int end = DifficultyLevel.getLevelByValue(difficultyRange.getEnd());
            boolean flag = false;
            for (Problem problem : problems) {
                int problemLevel = DifficultyLevel.getLevelByValue(problem.getProblemDifficulty());
                List<AlgorithmProblemList> algorithmProblemLists
                        = algorithmProblemListRepository.findByProblem_ProblemId(problem.getProblemId());
                List<Algorithm> algorithms = algorithmProblemLists.stream().map(AlgorithmProblemList::getAlgorithm).collect(Collectors.toList());
                if (start <= problemLevel && problemLevel <= end) {
//                    long testTypeAlgorithmId = randomNumber + 1;
//                    for (Algorithm algorithm : algorithms) {
//                        if (algorithm.getAlgorithmId() == testTypeAlgorithmId)
//                            flag = true;
//                    }
//                    if (flag) {
                    BojProblem bojProblem = BojProblem.builder()
                            .testProblemId(index++)
                            .problemId(problem.getBojProblemId())
                            .level(DifficultyLevel.getLevelByValue(problem.getProblemDifficulty()))
                            .levelToString(problem.getProblemDifficulty().getFullName()).build();
                    bojProblems.add(bojProblem);
                    flag = true;
//                        randomNumber = (randomNumber + 1) % 10;
                    break;
//                    }
                }
            }

            if (!flag) {
                BojProblem bojProblem = BojProblem.builder()
                        .testProblemId(index++)
                        .problemId(0L)
                        .build();
                bojProblems.add(bojProblem);
            }
        }
    }

    public void getProblemsByApi(Long testTypeId, String bojId, List<BojProblem> bojProblems) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        List<DifficultyRange> difficultyRanges = testType.getDifficultyRanges();
        long index = 1;
        for (DifficultyRange difficultyRange : difficultyRanges) {
            if (bojProblems.get((int) (index - 1)).getProblemId() != 0) {
                index++;
                continue;
            }
            String start = difficultyRange.getStart().getShortName();
            String end = difficultyRange.getEnd().getShortName();
            String apiUrl = "https://solved.ac/api/v3/search/problem";
            while (true) {
                String query = testTypeId == 7 ? String.format("tier:%s..%s ~solved_by:%s tag:simulation ~tag:ad_hoc ~tag:constructive ~tag:geometry ~tag:number_theory ~tag:math solved:200..", start, end, bojId) :
                        String.format("tier:%s..%s ~solved_by:%s ~tag:ad_hoc ~tag:constructive ~tag:geometry ~tag:number_theory ~tag:simulation ~tag:math solved:200.. solved:..5000", start, end, bojId);
                WebClient webClient = WebClient.builder().build();
                String jsonString = webClient.get()
                        .uri(apiUrl + "?query=" + query + "&page=1" + "&sort=random")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                ObjectMapper mapper = new ObjectMapper();
                try {
                    JsonNode rootNode = mapper.readTree(jsonString);
                    JsonNode itemsArray = rootNode.get("items");
                    if (itemsArray != null && itemsArray.isArray() && itemsArray.size() > 0) {
                        JsonNode firstItemNode = itemsArray.get(0);
                        String title = firstItemNode.get("titleKo").asText();
                        int alpha = (int) IntStream.range(0, 2).filter(i -> ('a' <= title.charAt(i) && title.charAt(i) <= 'z')
                                || ('A' <= title.charAt(i) && title.charAt(i) <= 'Z')).count();

                        if (alpha == 2)
                            continue;

                        JsonNode firstProblem = itemsArray.get(0);
                        BojProblem apiProblem = mapper.treeToValue(firstProblem, BojProblem.class);
                        BojProblem bojProblem = bojProblems.get((int) (index - 1));
                        bojProblem.setProblemId(apiProblem.getProblemId());
                        bojProblem.setLevel(apiProblem.getLevel());
                        bojProblem.setTestProblemId(index++);
                        bojProblem.setLevelToString(DifficultyLevel.getValueByLevel(bojProblem.getLevel()));
                        break;
                    }
                } catch (JsonProcessingException e) {
                    log.error("JsonProcessingException : {}", e.getMessage());
                    throw new MorandiException(TestErrorCode.JSON_PARSE_ERROR);
                }
            }
        }
    }
}
