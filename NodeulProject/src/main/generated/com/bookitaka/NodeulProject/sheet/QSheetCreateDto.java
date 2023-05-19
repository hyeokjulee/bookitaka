package com.bookitaka.NodeulProject.sheet;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSheetCreateDto is a Querydsl query type for SheetCreateDto
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSheetCreateDto extends EntityPathBase<SheetCreateDto> {

    private static final long serialVersionUID = 181065657L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSheetCreateDto sheetCreateDto = new QSheetCreateDto("sheetCreateDto");

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

    public final NumberPath<Integer> sheetNo = createNumber("sheetNo", Integer.class);

    public final StringPath sheetPublisher = createString("sheetPublisher");

    public final DateTimePath<java.util.Date> sheetRegdate = createDateTime("sheetRegdate", java.util.Date.class);

    public QSheetCreateDto(String variable) {
        this(SheetCreateDto.class, forVariable(variable), INITS);
    }

    public QSheetCreateDto(Path<? extends SheetCreateDto> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSheetCreateDto(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSheetCreateDto(PathMetadata metadata, PathInits inits) {
        this(SheetCreateDto.class, metadata, inits);
    }

    public QSheetCreateDto(Class<? extends SheetCreateDto> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sheetAgegroup = inits.isInitialized("sheetAgegroup") ? new QSheetAgegroup(forProperty("sheetAgegroup")) : null;
        this.sheetGenre = inits.isInitialized("sheetGenre") ? new QSheetGenre(forProperty("sheetGenre")) : null;
    }

}

