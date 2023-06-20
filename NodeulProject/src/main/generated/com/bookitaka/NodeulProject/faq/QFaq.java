package com.bookitaka.NodeulProject.faq;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFaq is a Querydsl query type for Faq
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFaq extends EntityPathBase<Faq> {

    private static final long serialVersionUID = -2113394728L;

    public static final QFaq faq = new QFaq("faq");

    public final StringPath faqAnswer = createString("faqAnswer");

    public final NumberPath<Integer> faqBest = createNumber("faqBest", Integer.class);

    public final StringPath faqCategory = createString("faqCategory");

    public final DateTimePath<java.util.Date> faqModdate = createDateTime("faqModdate", java.util.Date.class);

    public final NumberPath<Long> faqNo = createNumber("faqNo", Long.class);

    public final StringPath faqQuestion = createString("faqQuestion");

    public final DateTimePath<java.util.Date> faqRegdate = createDateTime("faqRegdate", java.util.Date.class);

    public QFaq(String variable) {
        super(Faq.class, forVariable(variable));
    }

    public QFaq(Path<? extends Faq> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFaq(PathMetadata metadata) {
        super(Faq.class, metadata);
    }

}

