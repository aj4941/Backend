package swm_nm.morandi.domain.testInfo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testRecord.dto.TestHistoryCondition;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static swm_nm.morandi.domain.member.entity.QMember.member;
import static swm_nm.morandi.domain.problem.entity.QProblem.problem;
import static swm_nm.morandi.domain.testInfo.entity.QAttemptProblem.attemptProblem;
import static swm_nm.morandi.domain.testInfo.entity.QTests.tests;

@Repository
@RequiredArgsConstructor
public class TestHistoryRepositoryImpl implements TestHistoryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Tests> findAllTestHistoryByCondition(TestHistoryCondition testHistoryCondition) {
        Pageable pageable = PageRequest.of(testHistoryCondition.getPage() - 1, testHistoryCondition.getSize(), Sort.by(DESC, "testDate"));
        return findAllTestHistoryByCondition(testHistoryCondition.getTestTypename(), testHistoryCondition.getBojProblemId(), testHistoryCondition.getBojId(), pageable);
    }
    @Override
    public Page<Tests> findAllTestHistoryByCondition(String testTypename, Long bojProblemId, String bojId, Pageable pageable) {

        JPQLQuery<Tests> query =  queryFactory
                .selectFrom(tests)
                .join(tests.member, member)
                .join(tests.attemptProblems, attemptProblem)
                .join(attemptProblem.problem, problem)
                .where(bojIdEq(bojId),
                        testTypenameEq(testTypename),
                        bojProblemIdEq(bojProblemId),
                        tests.testStatus.eq(TestStatus.COMPLETED));

        JPQLQuery<Tests> paginatedQuery = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Tests> testList = paginatedQuery.fetch();
        return new PageImpl<>(testList, pageable, query.fetchCount());
    }

    private BooleanExpression bojIdEq(String bojId) {
        return StringUtils.hasText(bojId) ? tests.member.bojId.eq(bojId) : null;
    }

    private BooleanExpression testTypenameEq(String testTypename) {
        return StringUtils.hasText(testTypename) ? tests.testTypename.eq(testTypename) : null;
    }

    private BooleanExpression bojProblemIdEq(Long bojProblemId) {
        return bojProblemId != null ? attemptProblem.problem.bojProblemId.eq(bojProblemId) : null;
    }

}
