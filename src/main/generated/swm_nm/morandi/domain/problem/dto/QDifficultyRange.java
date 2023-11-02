package swm_nm.morandi.domain.problem.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDifficultyRange is a Querydsl query type for DifficultyRange
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDifficultyRange extends BeanPath<DifficultyRange> {

    private static final long serialVersionUID = 31103327L;

    public static final QDifficultyRange difficultyRange = new QDifficultyRange("difficultyRange");

    public final EnumPath<DifficultyLevel> end = createEnum("end", DifficultyLevel.class);

    public final EnumPath<DifficultyLevel> start = createEnum("start", DifficultyLevel.class);

    public QDifficultyRange(String variable) {
        super(DifficultyRange.class, forVariable(variable));
    }

    public QDifficultyRange(Path<? extends DifficultyRange> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDifficultyRange(PathMetadata metadata) {
        super(DifficultyRange.class, metadata);
    }

}

