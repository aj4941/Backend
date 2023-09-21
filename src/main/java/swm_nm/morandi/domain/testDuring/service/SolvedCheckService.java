package swm_nm.morandi.domain.testDuring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolvedCheckService {

    private final AttemptProblemRepository attemptProblemRepository;

    private final TestRepository testRepository;

    private final ObjectMapper objectMapper;

    public List<AttemptProblemDto> isSolvedCheck(TestCheckDto testCheckDto) {
        Long testId = testCheckDto.getTestId();
        String bojId = testCheckDto.getBojId();
        Tests test = testRepository.findById(testId)
                .orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        checkAttemptedProblemResult(test, bojId);
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
    public void checkAttemptedProblemResult(Tests test, String bojId) {
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(test.getTestId());

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


    public boolean isSolvedProblem(AttemptProblem attemptProblem, String bojId) {
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
}
