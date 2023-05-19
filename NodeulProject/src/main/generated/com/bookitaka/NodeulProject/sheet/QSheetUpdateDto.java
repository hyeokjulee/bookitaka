package com.bookitaka.NodeulProject.sheet;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSheetUpdateDto is a Querydsl query type for SheetUpdateDto
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSheetUpdateDto extends EntityPathBase<SheetUpdateDto> {

    private static final long serialVersionUID = 1928466636L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSheetUpdateDto sheetUpdateDto = new QSheetUpdateDto("sheetUpdateDto");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QSheetAgegroup sheetAgegroup;

    public final StringPath sheetBookauthor = createString("sheetBookauthor");

    public final StringPath sheetBookimgename = createString("sheetBookimgename");

    public final StringPath sheetBookimguuid = createString("sheetBookimguuid");

    public final StringPath sheetBookisbn = createString("sheetBookisbn");

    public final StringPath sheetBooktitle = createString("sheetBooktitle");

    public final StringPath sheetContent = createString("sheetContent");

    public final StringPath sheetFilename = createString("sheetFilename");

    public final StringPath sheetFileuuid = createString("sheetFileuuid");

    public final QSheetGenre sheetGenre;

    public final DateTimePath<java.util.Date> sheetModdate = createDateTime("sheetModdate", java.util.Date.class);

    public final NumberPath<Integer> sheetNo = createNumber("sheetNo", Integer.class);

    public final StringPath sheetPublisher = createString("sheetPublisher");

    public QSheetUpdateDto(String variable) {
        this(SheetUpdateDto.class, forVariable(variable), INITS);
    }

    public QSheetUpdateDto(Path<? extends SheetUpdateDto> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSheetUpdateDto(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSheetUpdateDto(PathMetadata metadata, PathInits inits) {
        this(SheetUpdateDto.class, metadata, inits);
    }

    public QSheetUpdateDto(Class<? extends SheetUpdateDto> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sheetAgegroup = inits.isInitialized("sheetAgegroup") ? new QSheetAgegroup(forProperty("sheetAgegroup")) : null;
        this.sheetGenre = inits.isInitialized("sheetGenre") ? new QSheetGenre(forProperty("sheetGenre")) : null;
    }

}

