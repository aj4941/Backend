package swm_nm.morandi.problem.dataloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import swm_nm.morandi.problem.domain.Algorithm;
import swm_nm.morandi.problem.domain.AlgorithmProblemList;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.problem.domain.TypeProblemList;
import swm_nm.morandi.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.problem.repository.AlgorithmRepository;
import swm_nm.morandi.problem.repository.ProblemRepository;
import swm_nm.morandi.problem.repository.TypeProblemListRepository;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.problem.dto.DifficultyRange;
import swm_nm.morandi.test.repository.TestTypeRepository;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TestTypeRepository testTypeRepository;
    private final ProblemRepository problemRepository;
    private final TypeProblemListRepository typeProblemListRepository;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    String[] algorithmNames = {
            "구현", "그래프", "그리디", "문자열", "이분탐색",
            "자료 구조", "정렬", "최단 경로", "bfs와 dfs", "dp"
    };
    @Override
    public void run(String... args) throws Exception {
        // Sample Data
        insertAlgorithmData(algorithmNames);
        insertTestTypeData();
        insertProblemData();
        connectProblemsToTestType();
    }
    private String testTypename;
    private Integer testTime;
    private Integer problemCount;
    private DifficultyLevel startDifficulty;
    private DifficultyLevel endDifficulty;
    private void insertTestTypeData() {
        List<DifficultyRange> difficultyRanges = new ArrayList<>();
        difficultyRanges.add(new DifficultyRange(DifficultyLevel.B5, DifficultyLevel.B2));
        difficultyRanges.add(new DifficultyRange(DifficultyLevel.B2, DifficultyLevel.S5));
        difficultyRanges.add(new DifficultyRange(DifficultyLevel.S5, DifficultyLevel.S3));

        TestType testType = TestType.builder()
                .testTypename("코딩테스트 초급")
                .testTime(60)
                .problemCount(3)
                .startDifficulty(DifficultyLevel.B5)
                .endDifficulty(DifficultyLevel.S3)
                .difficultyRanges(difficultyRanges)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType);
    }

    private void insertProblemData() {
        for (int i = 1000; i <= 1199; i++) {
            long bojProblemId = i;
            DifficultyLevel randomDifficulty = getRandomDifficulty();
            Problem problem = Problem.builder()
                    .bojProblemId(bojProblemId)
                    .problemDifficulty(randomDifficulty)
                    .build();
            problemRepository.save(problem);

            Set<Integer> chosenAlgorithmIds = new HashSet<>();

            while (chosenAlgorithmIds.size() < 3) {
                int randomAlgorithmId = getRandomAlgorithmId();
                chosenAlgorithmIds.add(randomAlgorithmId);
            }

            for (long algorithmId : chosenAlgorithmIds) {
                Algorithm algorithm = algorithmRepository.findById(algorithmId).orElse(null);
                if (algorithm != null) {
                    AlgorithmProblemList algorithmProblemList = AlgorithmProblemList.builder()
                                    .algorithm(algorithm)
                                    .problem(problem)
                                    .build();
                    algorithmProblemListRepository.save(algorithmProblemList);
                }
            }
        }
    }

    private int getRandomAlgorithmId() {
        return new Random().nextInt(10) + 1;
    }

    private DifficultyLevel getRandomDifficulty() {
        DifficultyLevel[] difficultyLevels = DifficultyLevel.values();
        int randomIndex = new Random().nextInt(difficultyLevels.length);
        return difficultyLevels[randomIndex];
    }

    private void connectProblemsToTestType() {
        TestType testType = testTypeRepository.findById(1L).orElse(null);
        List<Problem> problems = problemRepository.findAll();
        for (Problem problem : problems) {
            TypeProblemList typeProblemList = TypeProblemList.builder()
                    .testType(testType)
                    .problem(problem)
                    .build();
            typeProblemListRepository.save(typeProblemList);
        }
    }

    private void insertAlgorithmData(String[] algorithmNames) {
        for (long i = 0; i < algorithmNames.length; i++) {
            Algorithm algorithm = Algorithm.builder()
                            .algorithmId(i + 1)
                            .algorithmName(algorithmNames[(int) i])
                            .build();
            algorithmRepository.save(algorithm);
        }
    }
}
