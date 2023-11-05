package swm_nm.morandi.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.dto.MemberListRequest;
import swm_nm.morandi.domain.member.dto.MemberListResponse;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberListService {

    private final MemberRepository memberRepository;

    public List<MemberListResponse> findAll(MemberListRequest memberListRequest) {
        Integer page = memberListRequest.getPage();
        Integer size = memberListRequest.getSize();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(DESC, "rating"));
        Page<Member> memberPage = memberRepository.findAll(pageable);
        List<Member> members = memberPage.getContent();
        List<MemberListResponse> memberListResponseDtos = new ArrayList<>();
        long rankIndex = (page - 1) * size + 1;
        for (Member member : members) {
            MemberListResponse memberListResponseDto = MemberListResponse.builder()
                    .rank(rankIndex++)
                    .bojId(member.getBojId())
                    .rating(member.getRating())
                    .build();
            memberListResponseDtos.add(memberListResponseDto);
        }

        return memberListResponseDtos;
    }
}
