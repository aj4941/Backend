package swm_nm.morandi.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swm_nm.morandi.domain.auth.response.GoogleUserDto;
import swm_nm.morandi.domain.auth.response.TokenDto;
import swm_nm.morandi.config.security.JwtProvider;
import swm_nm.morandi.domain.member.dto.*;
import swm_nm.morandi.domain.problem.entity.Algorithm;
import swm_nm.morandi.domain.problem.entity.AlgorithmProblemList;
import swm_nm.morandi.domain.problem.repository.AlgorithmProblemListRepository;
import swm_nm.morandi.domain.problem.repository.AlgorithmRepository;
import swm_nm.morandi.domain.test.dto.TestRatingDto;
import swm_nm.morandi.domain.test.dto.TestRecordDto;
import swm_nm.morandi.domain.test.dto.TestStatus;
import swm_nm.morandi.domain.test.entity.AttemptProblem;
import swm_nm.morandi.domain.test.entity.Tests;
import swm_nm.morandi.domain.test.postTest.request.AttemptProblemDto;
import swm_nm.morandi.domain.test.preTest.mapper.TestRecordMapper;
import swm_nm.morandi.domain.test.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.test.repository.TestRepository;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    private final AlgorithmProblemListRepository algorithmProblemListRepository;

    private final AlgorithmRepository algorithmRepository;

    private final TestRepository testRepository;

    private final JwtProvider jwtProvider;

    public TokenDto loginOrRegisterMember(GoogleUserDto googleUserDto){
        Member member = memberRepository.findByEmail(googleUserDto.getEmail())
                .orElseGet(() -> memberRepository.save(
                                Member.builder()
                                        .email(googleUserDto.getEmail())
                                        .introduceInfo("")
                                        .thumbPhoto(googleUserDto.getPicture())
                                        .socialInfo(googleUserDto.getType())
                                        .rating(1000L)
                                        .currentTestId(-1L)
                                        .build()
                        )
                );

        return jwtProvider.getTokens(member);
    }
    private String userHome = System.getProperty("user.home");
    private String uploadFolder = userHome + "/SWM/morandi-backend/morandi-backend/uploads";
    public String editThumbPhoto(Long memberId, MultipartFile thumbPhotoFile) throws IOException {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        String thumbPhoto = member.getThumbPhoto();
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + thumbPhotoFile.getOriginalFilename();
        String saveName = uploadFolder + "/" + fileName;
        Path savePath = Paths.get(saveName);
        try {
            thumbPhotoFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 기존 프로필 사진 삭제
        String decodedFileName = URLDecoder.decode(thumbPhoto, "UTF-8");
        String existingFilePath = uploadFolder + "/" + decodedFileName;
        File existingFile = new File(existingFilePath);
        if (existingFile.exists()) {
            existingFile.delete();
        }

        return URLEncoder.encode(fileName, "UTF-8");
    }

    public MemberInfoDto getMemberInfo() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setIntroduceInfo(member.getIntroduceInfo());
        memberInfoDto.setBojId(member.getBojId());
        return memberInfoDto;
    }

    public ThumbURLDto getMemberThumbDto(Long memberId) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        ThumbURLDto thumbURLDto = new ThumbURLDto();
        try {
            String fileName = URLDecoder.decode(member.getThumbPhoto(), "UTF-8");
            File file = new File(uploadFolder + "/" + fileName);
            BufferedImage originalImage = ImageIO.read(file);

            BufferedImage resizedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, 200, 200, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            byte[] resizedBytes = baos.toByteArray();
            thumbURLDto.setThumbPhoto(resizedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return thumbURLDto;
    }
    @Transactional
    public void editProfile(String nickname, String bojId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.editProfile(nickname, bojId);
    }
    public String getBojId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(AuthErrorCode.BAEKJOON_ID_NULL));
        return member.getBojId();
    }

    public RegisterInfoDto memberInitialize(RegisterInfoDto registerInfoDto) {
        Long userId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(userId).orElseThrow(()-> new MorandiException(AuthErrorCode.MEMBER_NOT_FOUND));
        member.setBojId(registerInfoDto.getBojId());
        memberRepository.save(member);
        return registerInfoDto;
    }

    public java.util.List<GrassDto> getGrassDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        java.util.List<GrassDto> grassDtos = new ArrayList<>();
        java.util.List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);
        if (!attemptProblems.isEmpty()) {
            Map<LocalDate, Integer> grassMap = new HashMap<>();
            attemptProblems.forEach(attemptProblem -> {
                LocalDate testDate = attemptProblem.getTestDate();
                if (attemptProblem.getIsSolved()) {
                    Integer currentValue = grassMap.getOrDefault(testDate, 0);
                    Integer newValue = currentValue + 1;
                    grassMap.put(testDate, newValue);
                }
            });
            grassMap.entrySet().forEach(entry -> {
                LocalDate testDate = entry.getKey();
                Integer solvedCount = entry.getValue();
                GrassDto grassDto = GrassDto.builder()
                        .testDate(testDate)
                        .solvedCount(solvedCount)
                        .build();
                grassDtos.add(grassDto);
            });
        }

        if (grassDtos.size() == 0) {
            GrassDto grassDto = GrassDto.builder()
                    .testDate(null)
                    .solvedCount(null)
                    .build();
            grassDtos.add(grassDto);
        }
        return grassDtos;
    }

    public java.util.List<GraphDto> getGraphDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Map<String, Long> totalCount = new HashMap<>();
        Map<String, Long> Count = new HashMap<>();
        java.util.List<Algorithm> algorithms = algorithmRepository.findAll();

        algorithms.stream().map(Algorithm::getAlgorithmName).forEach(algorithmName -> {
            totalCount.put(algorithmName, 0L);
            Count.put(algorithmName, 0L);
        });

        java.util.List<AttemptProblem> attemptProblems = attemptProblemRepository.findAllByMember_MemberId(memberId);

        java.util.List<GraphDto> graphDtos = new ArrayList<>();
        if (!attemptProblems.isEmpty()) {
            attemptProblems.forEach(attemptProblem -> {
                Long problemId = attemptProblem.getProblem().getProblemId();
                List<AlgorithmProblemList> algorithmProblemLists =
                        algorithmProblemListRepository.findByProblem_ProblemId(problemId);
                algorithmProblemLists.stream().map(algorithmProblemList ->
                        algorithmProblemList.getAlgorithm().getAlgorithmName()).forEach(algorithmName -> {
                    Long currentTotalCount = totalCount.getOrDefault(algorithmName, 0L);
                    Long currentCount = Count.getOrDefault(algorithmName, 0L);
                    totalCount.put(algorithmName, currentTotalCount + 1L);
                    if (attemptProblem.getIsSolved())
                        Count.put(algorithmName, currentCount + 1L);
                });
            });

            algorithms.stream().map(Algorithm::getAlgorithmName).forEach(algorithmName -> {
                Long solvedRate = totalCount.get(algorithmName) == 0 ? 0L :
                        Count.get(algorithmName) * 100L / totalCount.get(algorithmName);
                GraphDto graphDto = GraphDto.builder()
                        .algorithmName(algorithmName)
                        .solvedRate(solvedRate)
                        .build();
                graphDtos.add(graphDto);
            });
        }

        return graphDtos;
    }

    public List<TestRatingDto> getTestRatingDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        List<Tests> tests = testRepository.findAllByMember_MemberId(memberId);
        List<TestRatingDto> testRatingDtos = new ArrayList<>();
        tests.stream()
                .filter(test -> test.getTestStatus() != TestStatus.IN_PROGRESS)
                .forEach(test -> {
                    LocalDateTime testDate = test.getTestDate();
                    String testTypename = test.getTestTypename();
                    Long testRating = test.getTestRating();
                    Long testId = test.getTestId();
                    TestRatingDto testRatingDto = TestRatingDto.builder()
                            .testId(testId)
                            .testTypeName(testTypename)
                            .testDate(testDate)
                            .testRating(testRating)
                            .build();
                    testRatingDtos.add(testRatingDto);
                });

        if (testRatingDtos.size() == 0) {
            TestRatingDto testRatingDto = TestRatingDto.builder()
                    .testId(null)
                    .testTypeName(null)
                    .testDate(null)
                    .testRating(null)
                    .build();
            testRatingDtos.add(testRatingDto);
        }

        return testRatingDtos;
    }

    public CurrentRatingDto getCurrentRatingDto() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        CurrentRatingDto currentRatingDto = CurrentRatingDto.builder()
                .rating(member.getRating())
                .build();
        return currentRatingDto;
    }

    public TestRecordDto getTestRecordDtoByTestId(Long testId) {
        Tests test = testRepository.findById(testId).orElseThrow(()-> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        TestRecordDto testRecordDto = TestRecordMapper.convertToDto(test);
        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAllByTest_TestId(testId);
        if (!attemptProblems.isEmpty()) {
            long index = 1;
            for (AttemptProblem attemptProblem : attemptProblems) {
                AttemptProblemDto attemptProblemDto =
                        AttemptProblemDto.builder()
                                .testProblemId(index++)
                                .bojProblemId(attemptProblem.getProblem().getBojProblemId())
                                .isSolved(attemptProblem.getIsSolved())
                                .executionTime(attemptProblem.getExecutionTime())
                                .build();
                testRecordDto.getAttemptProblemDto().add(attemptProblemDto);
            }
        }
        return testRecordDto;
    }
}