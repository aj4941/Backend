package swm_nm.morandi.domain.problem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProblem is a Querydsl query type for Problem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProblem extends EntityPathBase<Problem> {

    private static final long serialVersionUID = -1545048408L;

    public static final QProblem problem = new QProblem("problem");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    public final NumberPath<Long> bojProblemId = createNumber("bojProblemId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final EnumPath<swm_nm.morandi.domain.problem.dto.DifficultyLevel> problemDifficulty = createEnum("problemDifficulty", swm_nm.morandi.domain.problem.dto.DifficultyLevel.class);

    public final NumberPath<Long> problemId = createNumber("problemId", Long.class);

    public QProblem(String variable) {
        super(Problem.class, forVariable(variable));
    }

    public QProblem(Path<? extends Problem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProblem(PathMetadata metadata) {
        super(Problem.class, metadata);
    }

}

