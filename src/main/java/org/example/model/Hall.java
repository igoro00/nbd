package org.example.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Hall extends AbstractEntity{

    @BsonProperty("name")
    private String name;

    @BsonProperty("columns")
    private int columns;

    @BsonProperty("rows")
    private int rows;

    public Hall(String name, int seatsColumn, int seatsRow) {
        super(new ObjectId());
        this.name = name;
        this.columns = seatsColumn;
        this.rows = seatsRow;
    }

    @BsonCreator
    public Hall(
            @BsonProperty("_id") ObjectId entityId,
            @BsonProperty("name") String name,
            @BsonProperty("columns") int columns,
            @BsonProperty("rows") int rows) {
        super(entityId);
        this.name = name;
        this.columns = columns;
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
