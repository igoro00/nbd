package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@CqlName("clients")
@PropertyStrategy(mutable = true)
public class Client {
    @PartitionKey
    @CqlName("client_id")
    private UUID clientId;

    @CqlName("first_name")
    private String firstName;

    @CqlName("last_name")
    private String lastName;

    @CqlName("date_of_birth")
    private Date dateOfBirth;

    @CqlName("email")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return new EqualsBuilder()
                .append(firstName, client.firstName)
                .append(lastName, client.lastName)
                .append(email, client.email)
                .append(dateOfBirth, client.dateOfBirth)
                .isEquals();
    }
}
