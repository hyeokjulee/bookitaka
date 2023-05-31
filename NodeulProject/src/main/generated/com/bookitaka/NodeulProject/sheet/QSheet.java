package com.bookitaka.NodeulProject.sheet;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSheet is a Querydsl query type for Sheet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSheet extends EntityPathBase<Sheet> {

    private static final long serialVersionUID = 875340458L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSheet sheet = new QSheet("sheet");

    public final QSheetAgegroup sheetAgegroup;

    public final StringPath sheetBookauthor = createString("sheetBookauthor");

    public final StringPath sheetBookimgname = createString("sheetBookimgname");

    public final StringPath sheetBookimguuid = createString("sheetBookimguuid");

    public final StringPath sheetBookisbn = createString("sheetBookisbn");

    public final StringPath sheetBookpublisher = createString("sheetBookpublisher");

    public final StringPath sheetBooktitle = createString("sheetBooktitle");

    public final NumberPath<Integer> sheetBuycnt = createNumber("sheetBuycnt", Integer.class);

    public final StringPath sheetContent = createString("sheetContent");

    public final StringPath sheetFilename = createString("sheetFilename");

    public final StringPath sheetFileuuid = createString("sheetFileuuid");

    public final QSheetGenre sheetGenre;

    public final NumberPath<Integer> sheetHit = createNumber("sheetHit", Integer.class);

    public final DateTimePath<java.util.Date> sheetModdate = createDateTime("sheetModdate", java.util.Date.class);

    public final NumberPath<Integer> sheetNo = createNumber("sheetNo", Integer.class);

    public final NumberPath<Integer> sheetPrice = createNumber("sheetPrice", Integer.class);

    public final DateTimePath<java.util.Date> sheetRegdate = createDateTime("sheetRegdate", java.util.Date.class);

    public QSheet(String variable) {
        this(Sheet.class, forVariable(variable), INITS);
    }

    public QSheet(Path<? extends Sheet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSheet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSheet(PathMetadata metadata, PathInits inits) {
        this(Sheet.class, metadata, inits);
    }

    public QSheet(Class<? extends Sheet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sheetAgegroup = inits.isInitialized("sheetAgegroup") ? new QSheetAgegroup(forProperty("sheetAgegroup")) : null;
        this.sheetGenre = inits.isInitialized("sheetGenre") ? new QSheetGenre(forProperty("sheetGenre")) : null;
    }

}

