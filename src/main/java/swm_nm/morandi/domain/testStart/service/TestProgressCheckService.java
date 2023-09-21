package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testExit.entity.TestType;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testExit.service.TestExitService;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestProgressCheckService {

    private final TestRepository testRepository;
    private final TestTypeRepository testTypeRepository;
    private final TestExitService testExitService;

    private void extracted(Tests test) {
        Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
        test.setRemainingTime(test.getTestTime() * 60 - duration.getSeconds());
    }

    private void extracted(TestType testType, Member member, Tests test) {
        TestCheckDto testCheckDto = TestCheckDto.builder()
                .testId(test.getTestId())
                .testTypeId(testType.getTestTypeId())
                .bojId(member.getBojId())
                .build();
        testExitService.testExit(testCheckDto,member,test,testType);
    }

    //TODO Redis 이용하여
    // 현재 테스트가 진행중인지 확인하도록
    public Tests isTestinProgress(Member member){
        if (member.getCurrentTestId() == null)
            member.setCurrentTestId(-1L);

        // 현재 테스트가 진행중이라면
        if (member.getCurrentTestId() != -1) {
            Long currentTestId = member.getCurrentTestId();
            Tests test = testRepository.findById(currentTestId).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));

            // 테스트 시작 시간과 현재 시간을 비교하여 남은 시간 계산
            extracted(test);

            if (test.getRemainingTime() > 0) { // 시간이 남았을경우 진행중인 테스트 반환
                return test;//getTestStartResponseDto(currentTestId, test);
            }
            else { // 시간이 마감된 테스트 종료
                TestType testType = testTypeRepository.findTestTypeByTestTypename(test.getTestTypename())
                        .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
                extracted(testType, member, test);
            }
        }
        return null;
    }
}
