package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testExit.entity.TestType;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testDuring.scheduler.TestScheduler;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddTestService {

    private final TestRepository testRepository;

    private final TestTypeRepository testTypeRepository;

    private final MemberRepository memberRepository;

    private final TestScheduler testScheduler;
    @Transactional
    public Tests startTestByTestTypeId(TestType testType, Member member) {

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
        Long testId = test.getTestId();
        TestCheckDto testCheckDto = TestCheckDto.builder()
                .testId(testId)
                .testTypeId(testType.getTestTypeId())
                .bojId(member.getBojId())
                .build();

        testScheduler.addTest(testCheckDto);
        member.setCurrentTestId(testId);

        return test;
    }
}
