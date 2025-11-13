package org.example.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Ticket extends AbstractEntity {
    @BsonProperty("client")
    private Client client;

    @BsonProperty("screening")
    private Screening screening;

    @BsonProperty("seat_column")
    private int seatColumn;

    @BsonProperty("seat_row")
    private int seatRow;

    @BsonCreator
    public Ticket(
            @BsonProperty("_id") ObjectId entityId,
            @BsonProperty("screening") Screening screening,
            @BsonProperty("client") Client client,
            @BsonProperty("seat_column") int seatColumn,
            @BsonProperty("seat_row") int seatRow) {
        super(entityId);
        this.setClient(client);
        this.setScreening(screening, seatRow, seatColumn);
    }

    public Ticket(Screening screening, Client client, int seatColumn, int seatRow) {
        super(new ObjectId());
        this.setClient(client);
        this.setScreening(screening, seatRow, seatColumn);
    }

    public int getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(int seatColumn) {
        if (seatColumn < 0 || seatColumn >= this.getScreening().getHall().getColumns()) {
            throw new IllegalArgumentException("Seat column value is outside Hall bounds");
        }

        this.seatColumn = seatColumn;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        if (seatRow < 0 || seatRow >= this.getScreening().getHall().getRows()) {
            throw new IllegalArgumentException("Seat row value is outside Hall bounds");
        }

        this.seatRow = seatRow;
    }

    public Screening getScreening() {
        return this.screening;
    }

    public void setScreening(Screening screening, int seatRow, int seatColumn) {
        this.screening = screening;
        this.setSeatRow(seatRow);
        this.setSeatColumn(seatColumn);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (seatColumn != ticket.seatColumn) return false;
        if (seatRow != ticket.seatRow) return false;
        if (!client.equals(ticket.client)) return false;
        if (!screening.equals(ticket.screening)) return false;

        return true;
    }
}
