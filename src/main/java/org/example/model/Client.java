package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@CqlName("clients")
@PropertyStrategy(mutable = true)
public class Client {
    @PartitionKey
    @CqlName("client_id")
    private UUID id;

    @CqlName("first_name")
    private String firstName;

    @CqlName("last_name")
    private String lastName;

    @CqlName("date_of_birth")
    private Date dateOfBirth;

    @CqlName("email")
    private String email;

    @CqlName("address")
    private Address address;

    public Client(String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        this(UUID.randomUUID(), firstName, lastName, email, dateOfBirth, address);
    }

    public Client(UUID id, String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (!firstName.equals(client.firstName)) return false;
        if (!lastName.equals(client.lastName)) return false;
        if (!email.equals(client.email)) return false;
        if (!dateOfBirth.equals(client.dateOfBirth)) return false;
        return address.equals(client.address);
    }
}
