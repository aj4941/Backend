package swm_nm.morandi.problem.dataloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.problem.dto.DifficultyRange;
import swm_nm.morandi.test.repository.TestTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TestTypeRepository testTypeRepository;
    @Override
    public void run(String... args) throws Exception {
        insertTestTypeData();
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
                .build();

        testTypeRepository.save(testType);
    }
}
