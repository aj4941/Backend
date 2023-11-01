package swm_nm.morandi.domain.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.practice.entity.PracticeProblem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PracticeProblemRepository extends JpaRepository<PracticeProblem, Long> {
    List<PracticeProblem> findAllByMember_MemberIdAndPracticeDateAfterAndIsSolved
            (Long memberId, LocalDate oneYearAgo, boolean isSolved);

    Optional<PracticeProblem> findByMember_MemberIdAndProblem_BojProblemId(Long memberId, Long bojProblemId);
}
