package swm_nm.morandi.domain.problem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTypeProblemList is a Querydsl query type for TypeProblemList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTypeProblemList extends EntityPathBase<TypeProblemList> {

    private static final long serialVersionUID = -1716829364L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTypeProblemList typeProblemList = new QTypeProblemList("typeProblemList");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final QProblem problem;

    public final swm_nm.morandi.domain.testInfo.entity.QTestType testType;

    public final NumberPath<Long> typeProblemListId = createNumber("typeProblemListId", Long.class);

    public QTypeProblemList(String variable) {
        this(TypeProblemList.class, forVariable(variable), INITS);
    }

    public QTypeProblemList(Path<? extends TypeProblemList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTypeProblemList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTypeProblemList(PathMetadata metadata, PathInits inits) {
        this(TypeProblemList.class, metadata, inits);
    }

    public QTypeProblemList(Class<? extends TypeProblemList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.problem = inits.isInitialized("problem") ? new QProblem(forProperty("problem")) : null;
        this.testType = inits.isInitialized("testType") ? new swm_nm.morandi.domain.testInfo.entity.QTestType(forProperty("testType")) : null;
    }

}

