package swm_nm.morandi.domain.testInfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTestType is a Querydsl query type for TestType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTestType extends EntityPathBase<TestType> {

    private static final long serialVersionUID = -140421650L;

    public static final QTestType testType = new QTestType("testType");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    public final NumberPath<Long> averageCorrectAnswerRate = createNumber("averageCorrectAnswerRate", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ListPath<swm_nm.morandi.domain.problem.dto.DifficultyRange, swm_nm.morandi.domain.problem.dto.QDifficultyRange> difficultyRanges = this.<swm_nm.morandi.domain.problem.dto.DifficultyRange, swm_nm.morandi.domain.problem.dto.QDifficultyRange>createList("difficultyRanges", swm_nm.morandi.domain.problem.dto.DifficultyRange.class, swm_nm.morandi.domain.problem.dto.QDifficultyRange.class, PathInits.DIRECT2);

    public final EnumPath<swm_nm.morandi.domain.problem.dto.DifficultyLevel> endDifficulty = createEnum("endDifficulty", swm_nm.morandi.domain.problem.dto.DifficultyLevel.class);

    public final StringPath frequentTypes = createString("frequentTypes");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final NumberPath<Integer> numberOfTestTrial = createNumber("numberOfTestTrial", Integer.class);

    public final NumberPath<Integer> problemCount = createNumber("problemCount", Integer.class);

    public final EnumPath<swm_nm.morandi.domain.problem.dto.DifficultyLevel> startDifficulty = createEnum("startDifficulty", swm_nm.morandi.domain.problem.dto.DifficultyLevel.class);

    public final NumberPath<Long> testTime = createNumber("testTime", Long.class);

    public final NumberPath<Long> testTypeId = createNumber("testTypeId", Long.class);

    public final StringPath testTypename = createString("testTypename");

    public QTestType(String variable) {
        super(TestType.class, forVariable(variable));
    }

    public QTestType(Path<? extends TestType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTestType(PathMetadata metadata) {
        super(TestType.class, metadata);
    }

}

