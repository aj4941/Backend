package swm_nm.morandi.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import swm_nm.morandi.member.entity.Member;
import swm_nm.morandi.member.repository.MemberRepository;
import swm_nm.morandi.problem.entity.Problem;
import swm_nm.morandi.problem.dto.DifficultyLevel;
import swm_nm.morandi.problem.repository.ProblemRepository;
import swm_nm.morandi.test.dto.TestRecordDto;
import swm_nm.morandi.test.repository.TestRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class TestServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TestService testService;

    @Test
    public void getTestRecord() {
        Member member = Member.builder()
                .email("john@example.com")
                .bojId("john_boj_id")
                .rating(1800L)
                .build();
        memberRepository.save(member);
        for (int i = 1; i <= 3; i++) {
            swm_nm.morandi.test.entity.Test test = swm_nm.morandi.test.entity.Test.builder()
                    .testDate(LocalDateTime.now().minusDays(i)) // Test dates for the last 3 days
                    .testTime(120L) // Test duration of 2 hours (120 minutes)
                    .problemCount(5) // 5 problems in the test
                    .startDifficulty(DifficultyLevel.B5) // Starting difficulty level: Bronze 5
                    .endDifficulty(DifficultyLevel.S3) // Ending difficulty level: Silver 3
                    .testTypename("Mock Test")
                    .testRating(1800L) // Test rating
                    .member(member) // Assign the Member to the Test
                    .build();

            testRepository.save(test);
            TestRecordDto testRecordDto = testService.getTestRecordDtoByTestId((long) i);

        }
    }

    @Test
    public void testEnumValue() {
        Optional<Problem> result = problemRepository.findById(201L);
        Problem problem = result.get();
        System.out.println(problem.getProblemDifficulty());
    }
}