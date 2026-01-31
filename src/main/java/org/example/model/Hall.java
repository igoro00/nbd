package org.example.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Hall extends AbstractEntity{
    @BsonProperty("name")
    private String name;

    @BsonProperty("columns")
    private int columns;

    @BsonProperty("rows")
    private int rows;
}
