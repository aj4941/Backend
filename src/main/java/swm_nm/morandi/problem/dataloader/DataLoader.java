package swm_nm.morandi.problem.dataloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.repository.AttemptProblemRepository;
import swm_nm.morandi.problem.domain.Algorithm;
import swm_nm.morandi.problem.domain.AlgorithmProblemList;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.problem.domain.TypeProblemList;
import swm_nm.morandi.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.problem.repository.AlgorithmRepository;
import swm_nm.morandi.problem.repository.ProblemRepository;
import swm_nm.morandi.problem.repository.TypeProblemListRepository;
import swm_nm.morandi.test.domain.Test;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.problem.dto.DifficultyRange;
import swm_nm.morandi.test.repository.TestRepository;
import swm_nm.morandi.test.repository.TestTypeRepository;
import swm_nm.morandi.testResult.entity.AttemptProblem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TestTypeRepository testTypeRepository;
    private final ProblemRepository problemRepository;
    private final TypeProblemListRepository typeProblemListRepository;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemListRepository algorithmProblemListRepository;
    private final AttemptProblemRepository attemptProblemRepository;
    private final TestRepository testRepository;
    String[] algorithmNames = {
            "구현", "그래프", "그리디", "문자열", "이분탐색",
            "자료 구조", "정렬", "최단 경로", "bfs와 dfs", "dp"
    };
    @Override
    public void run(String... args) {
        // Sample Data
        insertAlgorithmData(algorithmNames);
        insertTestTypeData();
        // insertProblemData();
        // connectProblemsToTestType();
        // connectAttemptProblemToTest();
    }
    private void insertTestTypeData() {
        // PK 1번 : 코딩테스트 대비 초급 (Small)
        List<DifficultyRange> difficultyRanges1 = new ArrayList<>();
        difficultyRanges1.add(new DifficultyRange(DifficultyLevel.B5, DifficultyLevel.B2));
        difficultyRanges1.add(new DifficultyRange(DifficultyLevel.B2, DifficultyLevel.S5));
        difficultyRanges1.add(new DifficultyRange(DifficultyLevel.S5, DifficultyLevel.S3));

        TestType testType1 = TestType.builder()
                .testTypename("코딩테스트 대비 초급 (Small)")
                .testTime(60)
                .problemCount(3)
                .startDifficulty(DifficultyLevel.B5)
                .endDifficulty(DifficultyLevel.S3)
                .difficultyRanges(difficultyRanges1)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType1);

        // PK 2번 : 코딩테스트 대비 중급 (Small)
        List<DifficultyRange> difficultyRanges2 = new ArrayList<>();
        difficultyRanges2.add(new DifficultyRange(DifficultyLevel.B2, DifficultyLevel.S5));
        difficultyRanges2.add(new DifficultyRange(DifficultyLevel.S5, DifficultyLevel.S2));
        difficultyRanges2.add(new DifficultyRange(DifficultyLevel.S2, DifficultyLevel.G4));

        TestType testType2 = TestType.builder()
                .testTypename("코딩테스트 대비 중급 (Small)")
                .testTime(60)
                .problemCount(3)
                .startDifficulty(DifficultyLevel.B2)
                .endDifficulty(DifficultyLevel.G4)
                .difficultyRanges(difficultyRanges2)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType2);

        // PK 3번 : 코딩테스트 대비 고급 (Small)
        List<DifficultyRange> difficultyRanges3 = new ArrayList<>();
        difficultyRanges3.add(new DifficultyRange(DifficultyLevel.S4, DifficultyLevel.G5));
        difficultyRanges3.add(new DifficultyRange(DifficultyLevel.G5, DifficultyLevel.G2));
        difficultyRanges3.add(new DifficultyRange(DifficultyLevel.G2, DifficultyLevel.P5));

        TestType testType3 = TestType.builder()
                .testTypename("코딩테스트 대비 고급 (Small)")
                .testTime(60)
                .problemCount(3)
                .startDifficulty(DifficultyLevel.S4)
                .endDifficulty(DifficultyLevel.P5)
                .difficultyRanges(difficultyRanges3)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType3);

        // PK 4번 : 코딩테스트 대비 초급 (Large)
        List<DifficultyRange> difficultyRanges4 = new ArrayList<>();
        difficultyRanges4.add(new DifficultyRange(DifficultyLevel.B5, DifficultyLevel.B3));
        difficultyRanges4.add(new DifficultyRange(DifficultyLevel.B3, DifficultyLevel.B1));
        difficultyRanges4.add(new DifficultyRange(DifficultyLevel.B1, DifficultyLevel.S5));
        difficultyRanges4.add(new DifficultyRange(DifficultyLevel.S5, DifficultyLevel.S4));
        difficultyRanges4.add(new DifficultyRange(DifficultyLevel.S4, DifficultyLevel.S3));

        TestType testType4 = TestType.builder()
                .testTypename("코딩테스트 대비 초급 (Large)")
                .testTime(120)
                .problemCount(5)
                .startDifficulty(DifficultyLevel.B5)
                .endDifficulty(DifficultyLevel.S3)
                .difficultyRanges(difficultyRanges4)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType4);

        // PK 5번 : 코딩테스트 대비 중급 (Large)
        List<DifficultyRange> difficultyRanges5 = new ArrayList<>();
        difficultyRanges5.add(new DifficultyRange(DifficultyLevel.B2, DifficultyLevel.S5));
        difficultyRanges5.add(new DifficultyRange(DifficultyLevel.S5, DifficultyLevel.S3));
        difficultyRanges5.add(new DifficultyRange(DifficultyLevel.S3, DifficultyLevel.S1));
        difficultyRanges5.add(new DifficultyRange(DifficultyLevel.S1, DifficultyLevel.G5));
        difficultyRanges5.add(new DifficultyRange(DifficultyLevel.G5, DifficultyLevel.G4));

        TestType testType5 = TestType.builder()
                .testTypename("코딩테스트 대비 중급 (Large)")
                .testTime(120)
                .problemCount(5)
                .startDifficulty(DifficultyLevel.B2)
                .endDifficulty(DifficultyLevel.G4)
                .difficultyRanges(difficultyRanges5)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType5);

        // PK 6번 : 코딩테스트 대비 고급 (Large)
        List<DifficultyRange> difficultyRanges6 = new ArrayList<>();
        difficultyRanges6.add(new DifficultyRange(DifficultyLevel.S4, DifficultyLevel.S2));
        difficultyRanges6.add(new DifficultyRange(DifficultyLevel.S2, DifficultyLevel.G5));
        difficultyRanges6.add(new DifficultyRange(DifficultyLevel.G5, DifficultyLevel.G3));
        difficultyRanges6.add(new DifficultyRange(DifficultyLevel.G3, DifficultyLevel.G1));
        difficultyRanges6.add(new DifficultyRange(DifficultyLevel.G1, DifficultyLevel.P5));

        TestType testType6 = TestType.builder()
                .testTypename("코딩테스트 대비 고급 (Large)")
                .testTime(120)
                .problemCount(5)
                .startDifficulty(DifficultyLevel.S4)
                .endDifficulty(DifficultyLevel.P5)
                .difficultyRanges(difficultyRanges6)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType6);

        // PK 7번 : 카카오 코딩테스트 대비
        List<DifficultyRange> difficultyRanges7 = new ArrayList<>();
        difficultyRanges7.add(new DifficultyRange(DifficultyLevel.S5, DifficultyLevel.S4));
        difficultyRanges7.add(new DifficultyRange(DifficultyLevel.S2, DifficultyLevel.S1));
        difficultyRanges7.add(new DifficultyRange(DifficultyLevel.S1, DifficultyLevel.G5));
        difficultyRanges7.add(new DifficultyRange(DifficultyLevel.G5, DifficultyLevel.G4));
        difficultyRanges7.add(new DifficultyRange(DifficultyLevel.G4, DifficultyLevel.G3));
        difficultyRanges7.add(new DifficultyRange(DifficultyLevel.G2, DifficultyLevel.G1));
        difficultyRanges7.add(new DifficultyRange(DifficultyLevel.G1, DifficultyLevel.P5));

        TestType testType7 = TestType.builder()
                .testTypename("카카오 코딩테스트 대비")
                .testTime(300)
                .problemCount(7)
                .startDifficulty(DifficultyLevel.S3)
                .endDifficulty(DifficultyLevel.P5)
                .difficultyRanges(difficultyRanges7)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType7);

        // PK 8번 : 네이버 코딩테스트 대비
        List<DifficultyRange> difficultyRanges8 = new ArrayList<>();
        difficultyRanges8.add(new DifficultyRange(DifficultyLevel.S2, DifficultyLevel.G5));
        difficultyRanges8.add(new DifficultyRange(DifficultyLevel.G3, DifficultyLevel.G2));
        difficultyRanges8.add(new DifficultyRange(DifficultyLevel.G3, DifficultyLevel.G2));
        difficultyRanges8.add(new DifficultyRange(DifficultyLevel.G2, DifficultyLevel.P5));

        TestType testType8 = TestType.builder()
                .testTypename("네이버 코딩테스트 대비")
                .testTime(120)
                .problemCount(4)
                .startDifficulty(DifficultyLevel.S2)
                .endDifficulty(DifficultyLevel.P5)
                .difficultyRanges(difficultyRanges8)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType8);

        // PK 9번 : 라인 코딩테스트 대비
        List<DifficultyRange> difficultyRanges9 = new ArrayList<>();
        difficultyRanges9.add(new DifficultyRange(DifficultyLevel.S4, DifficultyLevel.S3));
        difficultyRanges9.add(new DifficultyRange(DifficultyLevel.S2, DifficultyLevel.S1));
        difficultyRanges9.add(new DifficultyRange(DifficultyLevel.G5, DifficultyLevel.G4));
        difficultyRanges9.add(new DifficultyRange(DifficultyLevel.G5, DifficultyLevel.G4));
        difficultyRanges9.add(new DifficultyRange(DifficultyLevel.G3, DifficultyLevel.G2));
        difficultyRanges9.add(new DifficultyRange(DifficultyLevel.G2, DifficultyLevel.G1));

        TestType testType9 = TestType.builder()
                .testTypename("라인 코딩테스트 대비")
                .testTime(180)
                .problemCount(6)
                .startDifficulty(DifficultyLevel.S4)
                .endDifficulty(DifficultyLevel.G1)
                .difficultyRanges(difficultyRanges9)
                .numberOfTestTrial(0)
                .averageCorrectAnswerRate(0.0)
                .build();

        testTypeRepository.save(testType9);
    }
    private int getRandomAlgorithmId() {
        return new Random().nextInt(10) + 1;
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
    private void connectAttemptProblemToTest() {
        Optional<TestType> result = testTypeRepository.findById(1L);
        TestType testType = result.get();
        Test test = Test.builder()
                .testDate(LocalDateTime.now())
                .testTime(testType.getTestTime())
                .problemCount(testType.getProblemCount())
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .testTypename(testType.getTestTypename())
                .build();
        testRepository.save(test);

        for (long i = 1; i <= test.getProblemCount(); i++) {
            Optional<Problem> res = problemRepository.findById(i);
            Problem problem = res.get();
            AttemptProblem attemptProblem = AttemptProblem.builder()
                    .isSolved(true)
                    .testDate(LocalDate.now())
                    .executionTime(10L)
                    .member(null)
                    .test(test)
                    .problem(problem)
                    .build();

            attemptProblemRepository.save(attemptProblem);
        }
    }
}
