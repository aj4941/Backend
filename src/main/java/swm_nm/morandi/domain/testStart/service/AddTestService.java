package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.TestType;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testDuring.scheduler.TestScheduler;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.member.entity.Member;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddTestService {

    private final TestRepository testRepository;

    private final TestScheduler testScheduler;

    private final RedisTemplate<String, Object> redisTemplate;


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
                .originRating(null)
                .testStatus(TestStatus.IN_PROGRESS)
                .member(member)
                .build();

        testRepository.save(test);
        Long testId = test.getTestId();
        TestCheckDto testCheckDto = TestCheckDto.builder()
                .testId(testId)
                .bojId(member.getBojId())
                .build();

        //testScheduler.addTest(testCheckDto);


        return test;
    }

    public String generateTestKdy(Long testId) {
        return "test:" + testId;
    }


    public void initializeTempCode(){
        HashOperations<String, String, TempCodeDto> hashOps = redisTemplate.opsForHash();



    }

}
