package com.bookitaka.NodeulProject.sheet;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSheetCreateDto is a Querydsl query type for SheetCreateDto
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSheetCreateDto extends EntityPathBase<SheetCreateDto> {

    private static final long serialVersionUID = 181065657L;

    public static final QSheetCreateDto sheetCreateDto = new QSheetCreateDto("sheetCreateDto");

    public final StringPath sheetAgegroupName = createString("sheetAgegroupName");

    public final StringPath sheetBookauthor = createString("sheetBookauthor");

    public final StringPath sheetBookimgename = createString("sheetBookimgename");

    public final StringPath sheetBookimguuid = createString("sheetBookimguuid");

    public final StringPath sheetBookisbn = createString("sheetBookisbn");

    public final StringPath sheetBooktitle = createString("sheetBooktitle");

    public final StringPath sheetContent = createString("sheetContent");

    public final StringPath sheetFilename = createString("sheetFilename");

    public final StringPath sheetFileuuid = createString("sheetFileuuid");

    public final StringPath sheetGenre = createString("sheetGenre");

    public final NumberPath<Integer> sheetNo = createNumber("sheetNo", Integer.class);

    public final NumberPath<Integer> sheetPrice = createNumber("sheetPrice", Integer.class);

    public final StringPath sheetPublisher = createString("sheetPublisher");

    public final DateTimePath<java.util.Date> sheetRegdate = createDateTime("sheetRegdate", java.util.Date.class);

    public QSheetCreateDto(String variable) {
        super(SheetCreateDto.class, forVariable(variable));
    }

    public QSheetCreateDto(Path<? extends SheetCreateDto> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSheetCreateDto(PathMetadata metadata) {
        super(SheetCreateDto.class, metadata);
    }

}

