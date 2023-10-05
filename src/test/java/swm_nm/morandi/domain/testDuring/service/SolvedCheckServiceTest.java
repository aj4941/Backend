package swm_nm.morandi.domain.testDuring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.repository.ProblemRepository;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithMockUser(username = "1", roles = "USER")
class SolvedCheckServiceTest {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AttemptProblemRepository attemptProblemRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private SolvedCheckService solvedCheckService;
    @Test
    void isSolvedCheck() {
        // given
        Optional<Member> result = memberRepository.findById(1L);
        Member member = result.get();
        Tests test = Tests.builder()
                .member(member)
                .testDate(LocalDateTime.now())
                .testTime(120L)
                .build();
        Tests savedTest = testRepository.save(test);
        // 백준 문제 : 1002, 1003, 1004
        List<Problem> problems = problemRepository.findAllById(Arrays.asList(1L, 2L, 3L));

        IntStream.range(0, 3).mapToObj(i -> AttemptProblem.builder()
                .member(member)
                .test(savedTest)
                .isSolved(false)
                .problem(problems.get(i))
                .build()).forEachOrdered(attemptProblem -> attemptProblemRepository.save(attemptProblem));

        TestCheckDto testCheckDto = new TestCheckDto();
        testCheckDto.setTestId(savedTest.getTestId());
        testCheckDto.setBojId("aj4941");

        // when
        List<AttemptProblemDto> attemptProblemDtos = solvedCheckService.isSolvedCheck(testCheckDto);

        // aj4941 유저는 백준 문제 1002, 1003, 1004번 모두 정답이므로 3문제가 모두 정답처리 되어야 한다.
        int count = (int) attemptProblemDtos.stream().filter(AttemptProblemDto::getIsSolved).count();

        // then
        assertThat(count).isEqualTo(3);
    }
}