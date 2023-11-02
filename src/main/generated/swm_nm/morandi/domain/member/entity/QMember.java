package swm_nm.morandi.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1560027114L;

    public static final QMember member = new QMember("member1");

    public final swm_nm.morandi.domain.common.QBaseEntity _super = new swm_nm.morandi.domain.common.QBaseEntity(this);

    public final StringPath blogUrl = createString("blogUrl");

    public final StringPath bojId = createString("bojId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> currentTestId = createNumber("currentTestId", Long.class);

    public final StringPath email = createString("email");

    public final StringPath githubUrl = createString("githubUrl");

    public final StringPath introduceInfo = createString("introduceInfo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifedDate = _super.lastModifedDate;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> rating = createNumber("rating", Long.class);

    public final EnumPath<SocialType> socialInfo = createEnum("socialInfo", SocialType.class);

    public final StringPath thumbPhoto = createString("thumbPhoto");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

