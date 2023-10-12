package swm_nm.morandi.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.config.security.JwtProvider;
import swm_nm.morandi.domain.auth.response.TokenDto;
import swm_nm.morandi.domain.auth.response.UserDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    public TokenDto loginOrRegisterMember(UserDto userDto){
        Member member = memberRepository.findByEmail(userDto.getEmail())
                .orElseGet(() -> memberRepository.save(
                                Member.builder()
                                        .email(userDto.getEmail())
                                        .introduceInfo(userDto.getIntroduceInfo())
                                        .thumbPhoto(userDto.getPicture())
                                        .socialInfo(userDto.getType())
                                        .githubUrl(userDto.getGithubUrl())
                                        .rating(1000L)
                                        .currentTestId(-1L)
                                        .build()
                        )
                );

        return jwtProvider.getTokens(member);
    }
}
