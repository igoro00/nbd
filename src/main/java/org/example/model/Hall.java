package org.example.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "hall")
public class Hall extends ModelEntity{

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "columns")
    private int columns;

    @Column(name = "rows")
    private int rows;

    public Hall(String name, int seatsColumn, int seatsRow) {
        this.name = name;
        this.columns = seatsColumn;
        this.rows = seatsRow;
    }

    public Hall(){}

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
