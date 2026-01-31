package org.example.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ticket extends AbstractEntity {
    @Setter
    @BsonProperty("client")
    private Client client;

    @BsonProperty("screening")
    private Screening screening;

    @BsonProperty("seat_row")
    private int seatRow;

    @BsonProperty("seat_column")
    private int seatColumn;

    public void setSeatColumn(int seatColumn) {
        if (seatColumn < 0 || seatColumn >= this.getScreening().getHall().getColumns()) {
            throw new IllegalArgumentException("Seat column value is outside Hall bounds");
        }

        this.seatColumn = seatColumn;
    }

    public void setSeatRow(int seatRow) {
        if (seatRow < 0 || seatRow >= this.getScreening().getHall().getRows()) {
            throw new IllegalArgumentException("Seat row value is outside Hall bounds");
        }

        this.seatRow = seatRow;
    }

    public void setScreening(Screening screening, int seatRow, int seatColumn) {
        this.screening = screening;
        this.setSeatRow(seatRow);
        this.setSeatColumn(seatColumn);
    }

    public Ticket(Client client, Screening screening, int seatRow, int seatColumn) {
        super(new ObjectId());
        this.setClient(client);
        this.setScreening(screening, seatRow, seatColumn);
    }

    @BsonCreator
    public Ticket(
            @BsonProperty("_id") ObjectId id,
            @BsonProperty("client") Client client,
            @BsonProperty("screening") Screening screening,
            @BsonProperty("seat_row") int seatRow,
            @BsonProperty("seat_column") int seatColumn
    ) {
        this.setEntityId(id);
        this.setClient(client);
        this.setScreening(screening, seatRow, seatColumn);
    }
}
