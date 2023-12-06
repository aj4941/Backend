package swm_nm.morandi.domain.testStart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import swm_nm.morandi.domain.problem.dto.BojProblem;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.problem.dto.DifficultyRange;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.entity.TypeProblemList;
import swm_nm.morandi.domain.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.domain.problem.repository.TypeProblemListRepository;
import swm_nm.morandi.domain.testInfo.entity.TestType;

import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProblemsService {

    private final ObjectMapper objectMapper;
    public List<BojProblem> getProblemsByApi(TestType testType, String bojId) {
        List<DifficultyRange> difficultyRanges = testType.getDifficultyRanges();
        List<BojProblem> bojProblems = BojProblem.initBojProblems(difficultyRanges);
        String apiUrl = "https://solved.ac/api/v3/search/problem";
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<CompletableFuture<Void>> futures = bojProblems.stream()
                .map(bojProblem -> CompletableFuture.runAsync(() -> {
                    String start = bojProblem.getStartLevel();
                    String end = bojProblem.getEndLevel();
                    String query = getString(testType, bojId, start, end);
                    String URL = apiUrl + "?query=" + query + "&page=1" + "&sort=random";

                    while (true) {
                        WebClient webClient = WebClient.builder().build();
                        String jsonString = webClient.get()
                                .uri(URL)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();
                        try {
                            JsonNode jsonNode = objectMapper.readTree(jsonString);
                            JsonNode itemsArray = jsonNode.get("items");
                            if (itemsArray != null && itemsArray.isArray() && itemsArray.size() > 0) {
                                if (getProblem(bojProblem, itemsArray))
                                    break;
                            }
                        } catch (JsonProcessingException e) {
                            throw new MorandiException(TestErrorCode.JSON_PARSE_ERROR);
                        }
                    }
                }, executorService)).collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return bojProblems;
    }

    private boolean getProblem(BojProblem bojProblem, JsonNode itemsArray) {
        for (JsonNode jsonNode : itemsArray) {
            Long problemId = jsonNode.get("problemId").asLong();
            String title = jsonNode.get("titleKo").asText();
            if (problemId > 28000 || !title.matches(".*[가-힣]+.*")) continue;
            bojProblem.setProblemId(problemId);
            return true;
        }
        return false;
    }
    private static String getString(TestType testType, String bojId, String start, String end) {
        String query = testType.getTestTypeId() == 7 ?
                String.format("tier:%s..%s ~solved_by:%s tag:simulation ~tag:ad_hoc ~tag:constructive ~tag:geometry" +
                        " ~tag:number_theory ~tag:math solved:200..", start, end, bojId) :
                String.format("tier:%s..%s ~solved_by:%s ~tag:ad_hoc ~tag:constructive ~tag:geometry" +
                        " ~tag:number_theory ~tag:simulation ~tag:math solved:200.. solved:..5000", start, end, bojId);
        return query;
    }
}