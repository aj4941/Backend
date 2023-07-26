package swm_nm.morandi.testResult.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.auth.security.SecurityUtils;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.repository.MemberRepository;
import swm_nm.morandi.test.domain.Test;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.test.repository.TestRepository;
import swm_nm.morandi.test.repository.TestTypeRepository;
import swm_nm.morandi.testResult.request.TestResultDto;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TestResultService {
    private final MemberRepository memberRepository;

    private final TestTypeRepository testTypeRepository;

    private final TestRepository testRepository;

    private final AttemptProblemService attemptProblemService;

    @Transactional
    public void saveTestResult(Long testTypeId, TestResultDto testResultDto){
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));

        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new RuntimeException("테스트 타입을 찾을 수 없습니다."));
        //테스트 저장

        Test test = Test.builder()
                .member(member)
                .testDate(testResultDto.getTestDate())
                .problemCount(testType.getProblemCount())
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .testTypename(testType.getTestTypename())

                .build();

        //테스트 결과 저장
        testRepository.save(test);

        Long testId = test.getTestId();
        //문제별 결과 목록 저장
        attemptProblemService.saveAttemptedProblemResult(testId, testResultDto.getAttemptProblemDtos());

        //테스트 레이팅 저장
        test.setTestRating(1111L);



    }

}

