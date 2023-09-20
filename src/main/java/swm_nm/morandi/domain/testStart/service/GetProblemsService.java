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
import swm_nm.morandi.domain.problem.entity.Algorithm;
import swm_nm.morandi.domain.problem.entity.AlgorithmProblemList;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.entity.TypeProblemList;
import swm_nm.morandi.domain.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.domain.problem.repository.TypeProblemListRepository;
import swm_nm.morandi.domain.testExit.entity.TestType;

import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProblemsService {


    private final TypeProblemListRepository typeProblemListRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    public void getProblemsByTestType(TestType testType, List<BojProblem> bojProblems) {
        List<TypeProblemList> typeProblemLists = typeProblemListRepository.findByTestType_TestTypeId(testType.getTestTypeId());
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

    public void getProblemsByApi(TestType testType, String bojId, List<BojProblem> bojProblems) {
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
                String query = testType.getTestTypeId() == 7 ? String.format("tier:%s..%s ~solved_by:%s tag:simulation ~tag:ad_hoc ~tag:constructive ~tag:geometry ~tag:number_theory ~tag:math solved:200..", start, end, bojId) :
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
