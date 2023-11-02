package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.dto.TestInfo;
import swm_nm.morandi.domain.testInfo.entity.TestType;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testExit.service.TestExitService;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;
import swm_nm.morandi.redis.utils.RedisKeyGenerator;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestProgressCheckService {

    private final TestRepository testRepository;
    private final TestTypeRepository testTypeRepository;
    private final TestExitService testExitService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisKeyGenerator redisKeyGenerator;
    private void setRemainingTestTime(Tests test) {
        Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());
        test.setRemainingTime(test.getTestTime() * 60 - duration.getSeconds());
    }

    private void testExit(TestType testType, Member member, Tests test) {
        TestCheckDto testCheckDto = TestCheckDto.builder()
                .testId(test.getTestId())
                .bojId(member.getBojId())
                .build();
        testExitService.testExit(testCheckDto,member,test,testType);
    }

    //TODO Redis 이용하여
    // 현재 테스트가 진행중인지 확인하도록
    public Tests getOngoingTest(Member member) {

        // 현재 테스트가 진행중이 아니라면
        String ongoingTestKey = redisKeyGenerator.generateOngoingTestKey();
        TestInfo testInfo = (TestInfo) redisTemplate.opsForValue().get(ongoingTestKey);

        if (testInfo == null) {
            return null;
        }

        //만약 종료시간이 현재 시간을 지났으면? -> 테스트 종료 후 없다고 반환 후 redis 캐시 삭제
        if (testInfo.getEndTime().isBefore(LocalDateTime.now())) {
            Tests test = testRepository.findById(testInfo.getTestId()).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
            TestType testType = testTypeRepository.findTestTypeByTestTypename(test.getTestTypename())
                    .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
            testExit(testType, member, test);
            return null;
        }
        else {
            Tests test = testRepository.findById(testInfo.getTestId()).orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
            setRemainingTestTime(test);
            return test;
        }

    }
}
