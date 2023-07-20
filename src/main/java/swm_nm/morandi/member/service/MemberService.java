package swm_nm.morandi.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.dto.MemberDto;
import swm_nm.morandi.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void editProfile(Long memberId, MemberDto memberDto) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        member.editProfile(memberDto.getNickname(), memberDto.getBojId(), memberDto.getThumbPhoto());
        // memberRepository.save(member);
    }
    public String getBojId(Long memberId) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.get();
        return member.getBojId();
    }
}