package com.bookitaka.NodeulProject.sheet;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSheetAgegroup is a Querydsl query type for SheetAgegroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSheetAgegroup extends EntityPathBase<SheetAgegroup> {

    private static final long serialVersionUID = 1726284202L;

    public static final QSheetAgegroup sheetAgegroup = new QSheetAgegroup("sheetAgegroup");

    public final StringPath sheetAgegroupName = createString("sheetAgegroupName");

    public final NumberPath<Long> sheetAgegroupNo = createNumber("sheetAgegroupNo", Long.class);

    public QSheetAgegroup(String variable) {
        super(SheetAgegroup.class, forVariable(variable));
    }

    public QSheetAgegroup(Path<? extends SheetAgegroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSheetAgegroup(PathMetadata metadata) {
        super(SheetAgegroup.class, metadata);
    }

}

