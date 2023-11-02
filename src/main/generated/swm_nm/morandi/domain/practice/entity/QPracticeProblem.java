package swm_nm.morandi.domain.practice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPracticeProblem is a Querydsl query type for PracticeProblem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPracticeProblem extends EntityPathBase<PracticeProblem> {

    private static final long serialVersionUID = -1527013273L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPracticeProblem practiceProblem = new QPracticeProblem("practiceProblem");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final BooleanPath isSolved = createBoolean("isSolved");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final swm_nm.morandi.domain.member.entity.QMember member;

    public final DatePath<java.time.LocalDate> practiceDate = createDate("practiceDate", java.time.LocalDate.class);

    public final NumberPath<Long> practiceProblemId = createNumber("practiceProblemId", Long.class);

    public final swm_nm.morandi.domain.problem.entity.QProblem problem;

    public QPracticeProblem(String variable) {
        this(PracticeProblem.class, forVariable(variable), INITS);
    }

    public QPracticeProblem(Path<? extends PracticeProblem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPracticeProblem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPracticeProblem(PathMetadata metadata, PathInits inits) {
        this(PracticeProblem.class, metadata, inits);
    }

    public QPracticeProblem(Class<? extends PracticeProblem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new swm_nm.morandi.domain.member.entity.QMember(forProperty("member")) : null;
        this.problem = inits.isInitialized("problem") ? new swm_nm.morandi.domain.problem.entity.QProblem(forProperty("problem")) : null;
    }

}

