package com.bookitaka.NodeulProject.sheet;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSheetGenre is a Querydsl query type for SheetGenre
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSheetGenre extends EntityPathBase<SheetGenre> {

    private static final long serialVersionUID = 513315065L;

    public static final QSheetGenre sheetGenre = new QSheetGenre("sheetGenre");

    public final StringPath SheetGenreName = createString("SheetGenreName");

    public final NumberPath<Integer> SheetGenreNo = createNumber("SheetGenreNo", Integer.class);

    public QSheetGenre(String variable) {
        super(SheetGenre.class, forVariable(variable));
    }

    public QSheetGenre(Path<? extends SheetGenre> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSheetGenre(PathMetadata metadata) {
        super(SheetGenre.class, metadata);
    }

}

