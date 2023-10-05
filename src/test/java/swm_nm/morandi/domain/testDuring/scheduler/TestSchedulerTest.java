package swm_nm.morandi.domain.testDuring.scheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import swm_nm.morandi.config.security.AuthDetails;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.TestType;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.domain.testStart.dto.TestStartResponseDto;
import swm_nm.morandi.domain.testStart.service.TestStartUseCase;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.*;

@SpringBootTest
class TestSchedulerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestScheduler testScheduler;

    @Autowired
    private TestRepository testRepository;
    @Test
    @Transactional
    void testScheduler() {
        // given
        Optional<Member> result = memberRepository.findById(1L);
        Member member = result.get();
        range(0, 5000).mapToObj(i -> Tests.builder()
                .remainingTime(5000L)
                .testStatus(TestStatus.IN_PROGRESS)
                .testTime(300L)
                .testDate(LocalDateTime.now())
                .member(member)
                .build()).map(test -> testRepository.save(test)).map(savedTest -> TestCheckDto.builder()
                .testId(savedTest.getTestId())
                .bojId("12345")
                .build()).forEach(testCheckDto -> testScheduler.addTest(testCheckDto));

        // when
        long startTime = System.currentTimeMillis();
        Future<Void> future = testScheduler.callApiPeriodically();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();

        // then
        System.out.println("Scheduler Time (ms) : " + (endTime - startTime) / 1000.0);
    }
}