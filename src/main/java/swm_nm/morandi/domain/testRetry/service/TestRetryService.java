package swm_nm.morandi.domain.testRetry.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.codeSubmit.dto.SolutionIdResponse;
import swm_nm.morandi.domain.codeSubmit.service.BaekjoonSubmitService;
import swm_nm.morandi.domain.testDuring.dto.OutputDto;
import swm_nm.morandi.domain.testDuring.dto.factory.TempCodeFactory;
import swm_nm.morandi.domain.testDuring.service.RunCodeService;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.testRetry.request.RetryRunCodeRequest;
import swm_nm.morandi.domain.testRetry.request.RetryRunTestCaseRequest;
import swm_nm.morandi.domain.testRetry.request.RetrySubmitRequest;
import swm_nm.morandi.domain.testRetry.request.RetryTestRequest;
import swm_nm.morandi.domain.testRetry.response.AttemptProblemResponse;
import swm_nm.morandi.domain.testRetry.response.TestRetryResponse;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.RetryTestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestRetryService {

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final TempCodeFactory tempCodeFactory;

    private final BaekjoonSubmitService baekjoonSubmitService;

    private final RunCodeService runCodeService;

    public TestRetryResponse retryTest(RetryTestRequest retryTestRequest) {
        Tests test = testRepository.findTestByTestIdAndMember_MemberId(retryTestRequest.getTestId(), SecurityUtils.getCurrentMemberId())
                .orElseThrow(() -> new MorandiException(TestErrorCode.TEST_NOT_FOUND));

        List<AttemptProblemResponse> attemptProblemResponses =
                test.getAttemptProblems().stream()
                        .map(attemptProblem -> {
                            AttemptProblemResponse attemptProblemResponse = tempCodeFactory.getRetryAttemptProblemResponse();
                            attemptProblemResponse.initialRetryAttemptProblemResponse(attemptProblem.getSubmitCode(),
                                                                                      attemptProblem.getSubmitLanguage(),
                                                                                      attemptProblem.getProblem().getBojProblemId());
                            return attemptProblemResponse;
                        }).toList();

        return TestRetryResponse.builder()
                .testId(retryTestRequest.getTestId())
                .retryAttemptProblems(attemptProblemResponses)
                .build();

    }

    @Transactional
    public SolutionIdResponse submitRetryCode(RetrySubmitRequest retrySubmitRequest) {
        AttemptProblem attemptProblem = attemptProblemRepository.findAttemptProblemByTest_TestIdAndProblem_BojProblemId(
                retrySubmitRequest.getTestId(),
                retrySubmitRequest.getBojProblemId()).orElseThrow(() -> new MorandiException(RetryTestErrorCode.ATTEMPT_PROBLEM_NOT_FOUND));

        attemptProblem.setSubmitCode(retrySubmitRequest.getSourceCode());

        attemptProblemRepository.save(attemptProblem);

        return baekjoonSubmitService.submit(retrySubmitRequest);
    }

    public OutputDto runRetryCode(RetryRunCodeRequest retryRunCodeRequest) {
        return runCodeService.runCode(retryRunCodeRequest);
    }


    public List<OutputDto> runTestCaseRetryCode(RetryRunTestCaseRequest retryRunTestCaseRequest) {
        return runCodeService.runTestCaseCode(retryRunTestCaseRequest);
    }



}
