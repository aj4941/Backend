package swm_nm.morandi.domain.customTest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.aop.annotation.MemberLock;
import swm_nm.morandi.domain.customTest.request.CustomTestRequest;
import swm_nm.morandi.domain.customTest.response.CustomTestResponse;
import swm_nm.morandi.domain.customTest.response.CustomTestResponses;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.repository.ProblemRepository;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testDuring.dto.TestInfo;
import swm_nm.morandi.domain.testDuring.dto.factory.TempCodeFactory;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.redis.utils.RedisKeyGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomTestService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;
    private final TestRepository testRepository;
    private final AttemptProblemRepository attemptProblemRepository;
    private final ProblemRepository problemRepository;
    private final TempCodeFactory tempCodeFactory;
    private final RedisKeyGenerator redisKeyGenerator;

    private static final String TEST_PREFIX = "testing:memberId:";

    @MemberLock
    @Transactional
    public CustomTestResponses generateCustomTest(Long memberId, CustomTestRequest customTestRequest) {
        validateMemberId(memberId);
        List<Member> members = getMembers(customTestRequest);
        List<Tests> tests = createTests(customTestRequest, members);
        List<Problem> problems = getProblemsInOrder(customTestRequest.getBojProblems());

        final List<CustomTestResponse> customTestResponses =
            tests.stream().map(test -> {
                                String testingKey = createTestingKey(test.getMember().getMemberId());
                                storeTestInfoInRedis(testingKey, test, customTestRequest);
                                List<AttemptProblem> attemptProblems = createAttemptProblems(customTestRequest, test,problems);
                                attemptProblemRepository.saveAll(attemptProblems);
                                setupTempCode(test, customTestRequest);

            return CustomTestResponse
                    .builder()
                    .testTypename(test.getTestTypename())
                    .customTestId(test.getTestId())
                    .bojId(test.getMember().getBojId())
                    .memberId(test.getMember().getMemberId())
                    .build();
        }).collect(Collectors.toList());

        return CustomTestResponses.builder()
                .customTests(customTestResponses).build();
    }

    private void validateMemberId(Long memberId) {
        if (!memberId.equals(1L)) {
            throw new MorandiException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private List<Member> getMembers(CustomTestRequest customTestRequest) {
        return memberRepository.findAllByBojIdIn(customTestRequest.getBojIds());
    }

    private List<Tests> createTests(CustomTestRequest customTestRequest, List<Member> members) {
        clearTestInfoInRedis(members);
        final List<Tests> tests = members.stream()
                .map(member -> Tests.builder()
                        .member(member)
                        .testTypename(customTestRequest.getTestTypename())
                        .testTime(customTestRequest.getTestTime())
                        .testDate(customTestRequest.getStartTime())
                        .problemCount(customTestRequest.getBojProblems().size())
                        .endDifficulty(DifficultyLevel.G5)
                        .startDifficulty(DifficultyLevel.S3)
                        .build())
                .collect(Collectors.toList());
        testRepository.saveAll(tests);
        return tests;
    }

    private List<Problem> getProblemsInOrder(List<Long> bojProblemIds) {
        List<Problem> problems = problemRepository.findAllByBojProblemIdIn(bojProblemIds);

        Map<Long, Problem> idToProblemMap = problems.stream()
                .collect(Collectors.toMap(Problem::getBojProblemId, problem -> problem));

        return bojProblemIds.stream()
                .map(idToProblemMap::get)
                .collect(Collectors.toList());
    }

    private void clearTestInfoInRedis(List<Member> members) {
        for (Member member : members) {
            String testingKey = createTestingKey(member.getMemberId());
            if(Boolean.TRUE.equals(redisTemplate.hasKey(testingKey))) {
                redisTemplate.delete(testingKey);
            }
        }
    }

    private String createTestingKey(Long memberId) {
        return TEST_PREFIX + memberId;
    }

    private void storeTestInfoInRedis(String testingKey, Tests test, CustomTestRequest customTestRequest) {
        TestInfo testInfo = TestInfo.builder()
                .testId(test.getTestId())
                .endTime(customTestRequest.getStartTime().plusMinutes(customTestRequest.getTestTime()))
                .build();
        redisTemplate.opsForValue().set(testingKey, testInfo);
    }


    private List<AttemptProblem> createAttemptProblems(CustomTestRequest customTestRequest, Tests test, List<Problem> problems) {
        return problems.stream()
                .map(problem -> {
                    final AttemptProblem attemptProblem = AttemptProblem.builder()
                            .testDate(customTestRequest.getStartTime().toLocalDate())
                            .member(test.getMember())
                            .problem(problem)
                            .isSolved(false)
                            .test(test)
                            .build();
                        attemptProblem.setTest(test);
                        return attemptProblem;
                }).collect(Collectors.toList());
    }

    private void setupTempCode(Tests test, CustomTestRequest customTestRequest) {
        String tempCodeKey = redisKeyGenerator.generateTempCodeKey(test.getTestId());
        TempCodeDto initialTempCode = tempCodeFactory.getTempCodeDto();
        int problemCount = test.getProblemCount();
        HashOperations<String, String, TempCodeDto> hashOps = redisTemplate.opsForHash();
        for (int problemNumber = 1; problemNumber <= problemCount; problemNumber++) {
            hashOps.put(tempCodeKey, String.valueOf(problemNumber), initialTempCode);
        }
        setTempCodeExpiry(customTestRequest, tempCodeKey);
    }

    private void setTempCodeExpiry(CustomTestRequest customTestRequest, String tempCodeKey) {
        long expireTime = Duration.between(LocalDateTime.now(), customTestRequest.getStartTime().plusMinutes(customTestRequest.getTestTime())).toMinutes();
        redisTemplate.expire(tempCodeKey, expireTime, TimeUnit.MINUTES);
    }
}
