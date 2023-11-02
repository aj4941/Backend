package swm_nm.morandi.domain.problem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlgorithm is a Querydsl query type for Algorithm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlgorithm extends EntityPathBase<Algorithm> {

    private static final long serialVersionUID = -1684962760L;

    public static final QAlgorithm algorithm = new QAlgorithm("algorithm");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    public final NumberPath<Long> algorithmId = createNumber("algorithmId", Long.class);

    public final StringPath algorithmName = createString("algorithmName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public QAlgorithm(String variable) {
        super(Algorithm.class, forVariable(variable));
    }

    public QAlgorithm(Path<? extends Algorithm> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlgorithm(PathMetadata metadata) {
        super(Algorithm.class, metadata);
    }

}

