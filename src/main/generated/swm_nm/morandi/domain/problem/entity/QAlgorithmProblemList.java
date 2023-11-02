package swm_nm.morandi.domain.problem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlgorithmProblemList is a Querydsl query type for AlgorithmProblemList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlgorithmProblemList extends EntityPathBase<AlgorithmProblemList> {

    private static final long serialVersionUID = 1984926341L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlgorithmProblemList algorithmProblemList = new QAlgorithmProblemList("algorithmProblemList");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    public final QAlgorithm algorithm;

    public final NumberPath<Long> algorithmProblemListId = createNumber("algorithmProblemListId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final QProblem problem;

    public QAlgorithmProblemList(String variable) {
        this(AlgorithmProblemList.class, forVariable(variable), INITS);
    }

    public QAlgorithmProblemList(Path<? extends AlgorithmProblemList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlgorithmProblemList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlgorithmProblemList(PathMetadata metadata, PathInits inits) {
        this(AlgorithmProblemList.class, metadata, inits);
    }

    public QAlgorithmProblemList(Class<? extends AlgorithmProblemList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.algorithm = inits.isInitialized("algorithm") ? new QAlgorithm(forProperty("algorithm")) : null;
        this.problem = inits.isInitialized("problem") ? new QProblem(forProperty("problem")) : null;
    }

}

