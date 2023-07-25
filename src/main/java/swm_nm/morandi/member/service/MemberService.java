package swm_nm.morandi.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swm_nm.morandi.auth.response.GoogleUserDto;
import swm_nm.morandi.auth.response.TokenDto;
import swm_nm.morandi.auth.security.JwtProvider;
import swm_nm.morandi.auth.security.SecurityUtils;
import swm_nm.morandi.exception.LoginAppException;
import swm_nm.morandi.exception.LoginErrorCode;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.dto.MemberInfoDto;
import swm_nm.morandi.member.dto.RegisterInfoDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    public TokenDto registerMember(GoogleUserDto googleUserDto){
    //TODO
        //이부분에 oidc 검증로직 넣기





        // 이메일이나 아이디가 이미 존재하는지 검증하는 로직도 넣기
        Optional<Member> findMember = memberRepository.findByEmail(googleUserDto.getEmail());
        // 이메일이나 아이디가 존재하지 않으면 회원가입 진행
        if(findMember.isEmpty()) {
            findMember = Optional.of(Member.builder()
                    .email(googleUserDto.getEmail())
                    .nickname(googleUserDto.getName())
                    .thumbPhoto(googleUserDto.getPicture())
                    .socialInfo(googleUserDto.getType())
                    .build());
            memberRepository.save(findMember.get());
            TokenDto tokenDto = jwtProvider.getTokens(findMember.get());
            return tokenDto;
        }
        //토큰 발급 로직 넣기
        TokenDto tokenDto = jwtProvider.getTokens(findMember.get());

        return tokenDto;

    }
    private String userHome = System.getProperty("user.home");
    private String uploadFolder = userHome + "/SWM/morandi-backend/morandi-backend/uploads";
    public String editThumbPhoto(Long memberId, MultipartFile thumbPhotoFile) throws IOException {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        String thumbPhoto = member.getThumbPhoto();
        String fileName = thumbPhotoFile.getOriginalFilename();
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

    public MemberInfoDto getMemberInfo(Long memberId) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        String thumbPhoto = member.getThumbPhoto();
        MemberInfoDto memberDto = new MemberInfoDto();
        memberDto.setNickname(member.getNickname());
        memberDto.setBojId(member.getBojId());
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
            memberDto.setThumbPhoto(resizedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return memberDto;
    }
    @Transactional
    public void editProfile(Long memberId, String nickname, String bojId, String thumbPhoto) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        member.editProfile(nickname, bojId, thumbPhoto);
    }
    public String getBojId(Long memberId) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        return member.getBojId();
    }

    public RegisterInfoDto memberInitialize(RegisterInfoDto registerInfoDto) {
        Long userId = SecurityUtils.getCurrentUserId();
        Member member = memberRepository.findById(userId).orElseThrow(()-> new LoginAppException(LoginErrorCode.USERNAME_NOT_FOUND,"Username not found"));
        member.setBojId(registerInfoDto.getBojId());

        return registerInfoDto;
    }
}