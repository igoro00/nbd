package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@CqlName("tickets_by_screening")
public class TicketByScreening {
    @PartitionKey()
    @CqlName("screening_id")
    private UUID screeningId;

    // tutaj moze zduplikowane dane ze screening i movie? nie wiem jeszcze

    @CqlName("client_id")
    private UUID clientId;


    @ClusteringColumn(0)
    @CqlName("seat_column")
    private int seatColumn;

    @ClusteringColumn(1)
    @CqlName("seat_row")
    private int seatRow;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        TicketByScreening ticket = (TicketByScreening) o;

        return new EqualsBuilder()
                .append(this.screeningId, ticket.screeningId)
                .append(this.clientId, ticket.clientId)
                .append(this.seatColumn, ticket.seatColumn)
                .append(this.seatRow, ticket.seatRow)
                .isEquals();
    }
}
