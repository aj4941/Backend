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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProblemsService {

    private final TypeProblemListRepository typeProblemListRepository;

    public void getProblemsByTestType(TestType testType, List<BojProblem> bojProblems) {
        List<TypeProblemList> typeProblemLists = typeProblemListRepository.findByTestType_TestTypeId(testType.getTestTypeId());
        List<Problem> problems = typeProblemLists.stream().map(TypeProblemList::getProblem).collect(Collectors.toList());
        List<DifficultyRange> difficultyRanges = testType.getDifficultyRanges();
        long index = 1;
        for (DifficultyRange difficultyRange : difficultyRanges) {
            int start = DifficultyLevel.getLevelByValue(difficultyRange.getStart());
            int end = DifficultyLevel.getLevelByValue(difficultyRange.getEnd());
            boolean flag = false;
            for (Problem problem : problems) {
                int problemLevel = DifficultyLevel.getLevelByValue(problem.getProblemDifficulty());
                if (start <= problemLevel && problemLevel <= end) {
                    BojProblem bojProblem = BojProblem.builder()
                            .testProblemId(index++)
                            .problemId(problem.getBojProblemId())
                            .level(DifficultyLevel.getLevelByValue(problem.getProblemDifficulty()))
                            .levelToString(problem.getProblemDifficulty().getFullName()).build();
                    bojProblems.add(bojProblem);
                    flag = true;
                    break;
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

    public void getProblemsByApi(TestType testType, String bojId, List<BojProblem> bojProblems) {
        System.out.println("testType : " + testType);
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
                String query = getString(testType, bojId, start, end);
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
                        if (!isKorean(itemsArray)) continue;
                        long prev = index;
                        index = getProblem(bojProblems, index, mapper, itemsArray);
                        if (prev == index) continue;
                        break;
                    }
                } catch (JsonProcessingException e) {
                    log.error("JsonProcessingException : {}", e.getMessage());
                    throw new MorandiException(TestErrorCode.JSON_PARSE_ERROR);
                }
            }
        }
    }

    private static String getString(TestType testType, String bojId, String start, String end) {
        String query = testType.getTestTypeId() == 7 ?
                String.format("tier:%s..%s ~solved_by:%s tag:simulation ~tag:ad_hoc ~tag:constructive ~tag:geometry" +
                        " ~tag:number_theory ~tag:math solved:200..", start, end, bojId) :
                String.format("tier:%s..%s ~solved_by:%s ~tag:ad_hoc ~tag:constructive ~tag:geometry" +
                        " ~tag:number_theory ~tag:simulation ~tag:math solved:200.. solved:..5000", start, end, bojId);
        return query;
    }

    private static long getProblem(List<BojProblem> bojProblems, long index, ObjectMapper mapper, JsonNode itemsArray)
            throws JsonProcessingException {
        JsonNode firstProblem = itemsArray.get(0);
        BojProblem apiProblem = mapper.treeToValue(firstProblem, BojProblem.class);
        if (apiProblem.getProblemId() > 28000)
            return index;
        BojProblem bojProblem = bojProblems.get((int) (index - 1));
        bojProblem.setProblemId(apiProblem.getProblemId());
        bojProblem.setLevel(apiProblem.getLevel());
        bojProblem.setTestProblemId(index++);
        bojProblem.setLevelToString(DifficultyLevel.getValueByLevel(bojProblem.getLevel()));
        return index;
    }

    private static boolean isKorean(JsonNode itemsArray) {
        JsonNode firstItemNode = itemsArray.get(0);
        String title = firstItemNode.get("titleKo").asText();
        return title.matches(".*[가-힣]+.*");
    }
}
