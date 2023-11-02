package swm_nm.morandi.domain.testInfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttemptProblem is a Querydsl query type for AttemptProblem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttemptProblem extends EntityPathBase<AttemptProblem> {

    private static final long serialVersionUID = -715909996L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttemptProblem attemptProblem = new QAttemptProblem("attemptProblem");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    public final NumberPath<Long> attemptProblemId = createNumber("attemptProblemId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> executionTime = createNumber("executionTime", Long.class);

    public final BooleanPath isSolved = createBoolean("isSolved");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final swm_nm.morandi.domain.member.entity.QMember member;

    public final swm_nm.morandi.domain.problem.entity.QProblem problem;

    public final StringPath submitCode = createString("submitCode");

    public final QTests test;

    public final DatePath<java.time.LocalDate> testDate = createDate("testDate", java.time.LocalDate.class);

    public QAttemptProblem(String variable) {
        this(AttemptProblem.class, forVariable(variable), INITS);
    }

    public QAttemptProblem(Path<? extends AttemptProblem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttemptProblem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttemptProblem(PathMetadata metadata, PathInits inits) {
        this(AttemptProblem.class, metadata, inits);
    }

    public QAttemptProblem(Class<? extends AttemptProblem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new swm_nm.morandi.domain.member.entity.QMember(forProperty("member")) : null;
        this.problem = inits.isInitialized("problem") ? new swm_nm.morandi.domain.problem.entity.QProblem(forProperty("problem")) : null;
        this.test = inits.isInitialized("test") ? new QTests(forProperty("test"), inits.get("test")) : null;
    }

}

