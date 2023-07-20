package swm_nm.morandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
