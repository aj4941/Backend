package swm_nm.morandi.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swm_nm.morandi.auth.response.GoogleUserDto;
import swm_nm.morandi.auth.response.TokenDto;
import swm_nm.morandi.auth.security.JwtProvider;
import swm_nm.morandi.auth.security.SecurityUtils;
import swm_nm.morandi.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.dto.MemberInfoDto;
import swm_nm.morandi.member.dto.RegisterInfoDto;
import swm_nm.morandi.member.dto.ThumbURLDto;
import swm_nm.morandi.member.repository.MemberRepository;

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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    public TokenDto loginOrRegisterMember(GoogleUserDto googleUserDto){
        Member member = memberRepository.findByEmail(googleUserDto.getEmail())
                .orElseGet(() -> memberRepository.save(
                                Member.builder()
                                        .email(googleUserDto.getEmail())
                                        .nickname(googleUserDto.getName())
                                        .thumbPhoto(googleUserDto.getPicture())
                                        .socialInfo(googleUserDto.getType())
                                        .rating(1000L)
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
        memberInfoDto.setNickname(member.getNickname());
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
}