package swm_nm.morandi.domain.testRetry.service;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testDuring.dto.factory.TempCodeFactory;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.testRetry.request.RetryTestRequest;
import swm_nm.morandi.domain.testRetry.response.RetryAttemptProblemResponse;
import swm_nm.morandi.domain.testRetry.response.TestRetryResponse;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.redis.utils.RedisKeyGenerator;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TestRetryService {

    private final TestRepository testRepository;

    private final RedisKeyGenerator redisKeyGenerator;

    private final RedisTemplate<String, Object> redisTemplate;

    private final TempCodeFactory tempCodeFactory;
    private final Long expireTime = 60000L;

    public TestRetryResponse retryTest(RetryTestRequest retryTestRequest) {
        Tests test = testRepository.findTestByTestIdAndMember_MemberId(retryTestRequest.getTestId(), SecurityUtils.getCurrentMemberId())
                .orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));


        List<RetryAttemptProblemResponse> attemptProblemResponses =
                test.getAttemptProblems().stream()
                        .map(attemptProblem -> {
                            RetryAttemptProblemResponse attemptProblemResponse = tempCodeFactory.getRetryAttemptProblemResponse();
                            attemptProblemResponse.initialRetryAttemptProblemResponse(attemptProblem.getSubmitCode(),
                                                                                      attemptProblem.getSubmitLanguage(),
                                                                                      attemptProblem.getProblem().getBojProblemId());
                            return attemptProblemResponse;
                        }).toList();



        saveCodeToRedis(retryTestRequest.getTestId(), attemptProblemResponses);

        return TestRetryResponse.builder()
                .testId(retryTestRequest.getTestId())
                .retryAttemptProblems(attemptProblemResponses)
                .build();

    }

    private void saveCodeToRedis(Long testId, List<RetryAttemptProblemResponse> attemptProblemResponses) {
        String retryTestTempCodeKey = redisKeyGenerator.generateRetryTestTempCodeKey(testId);

        HashOperations<String, String, RetryAttemptProblemResponse> hashOps = redisTemplate.opsForHash();
        int problemCount = attemptProblemResponses.size();
        IntStream.rangeClosed(1, problemCount).forEach(problemNumber ->
                hashOps.put(retryTestTempCodeKey,
                        String.valueOf(problemNumber),
                        attemptProblemResponses.get(problemNumber-1))
        );
        redisTemplate.expire(retryTestTempCodeKey, expireTime, TimeUnit.MINUTES);
    }



}
