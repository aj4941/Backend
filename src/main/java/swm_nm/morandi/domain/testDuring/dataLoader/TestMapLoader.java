package swm_nm.morandi.domain.testDuring.dataLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testDuring.scheduler.TestMapManager;
import swm_nm.morandi.domain.testInfo.entity.Tests;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class TestMapLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    private final TestMapManager testMapManager;
    @Override
    public void run(String... args) {
        List<Member> members = memberRepository.findAll();
        members.stream().filter(member -> member.getCurrentTestId() != null)
                .filter(member -> member.getCurrentTestId() != -1)
                .map(member -> TestCheckDto.builder()
                .testId(member.getCurrentTestId())
                .bojId(member.getBojId())
                .build()).forEach(testMapManager::addTest);
    }
}
