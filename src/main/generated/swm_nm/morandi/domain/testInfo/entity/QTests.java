package swm_nm.morandi.domain.testInfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTests is a Querydsl query type for Tests
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTests extends EntityPathBase<Tests> {

    private static final long serialVersionUID = -1952065953L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTests tests = new QTests("tests");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    public final ListPath<AttemptProblem, QAttemptProblem> attemptProblems = this.<AttemptProblem, QAttemptProblem>createList("attemptProblems", AttemptProblem.class, QAttemptProblem.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final EnumPath<swm_nm.morandi.domain.problem.dto.DifficultyLevel> endDifficulty = createEnum("endDifficulty", swm_nm.morandi.domain.problem.dto.DifficultyLevel.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final swm_nm.morandi.domain.member.entity.QMember member;

    public final NumberPath<Long> originRating = createNumber("originRating", Long.class);

    public final NumberPath<Integer> problemCount = createNumber("problemCount", Integer.class);

    public final NumberPath<Long> remainingTime = createNumber("remainingTime", Long.class);

    public final EnumPath<swm_nm.morandi.domain.problem.dto.DifficultyLevel> startDifficulty = createEnum("startDifficulty", swm_nm.morandi.domain.problem.dto.DifficultyLevel.class);

    public final DateTimePath<java.time.LocalDateTime> testDate = createDateTime("testDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> testId = createNumber("testId", Long.class);

    public final NumberPath<Long> testRating = createNumber("testRating", Long.class);

    public final EnumPath<swm_nm.morandi.domain.testDuring.dto.TestStatus> testStatus = createEnum("testStatus", swm_nm.morandi.domain.testDuring.dto.TestStatus.class);

    public final NumberPath<Long> testTime = createNumber("testTime", Long.class);

    public final StringPath testTypename = createString("testTypename");

    public QTests(String variable) {
        this(Tests.class, forVariable(variable), INITS);
    }

    public QTests(Path<? extends Tests> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTests(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTests(PathMetadata metadata, PathInits inits) {
        this(Tests.class, metadata, inits);
    }

    public QTests(Class<? extends Tests> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new swm_nm.morandi.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

