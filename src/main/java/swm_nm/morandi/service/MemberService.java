package swm_nm.morandi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.Member;
import swm_nm.morandi.dto.MemberDto;
import swm_nm.morandi.repository.MemberRepository;

import javax.transaction.Transactional;
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