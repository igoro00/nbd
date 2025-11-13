package org.example.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;

public class Client extends AbstractEntity {
    @BsonProperty("first_name")
    private String firstName;

    @BsonProperty("last_name")
    private String lastName;

    @BsonProperty("date_of_birth")
    private Date dateOfBirth;

    @BsonProperty("email")
    private String email;

    @BsonProperty("address")
    private Address address;

    public Client(String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        super(new ObjectId());
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.address = address;
    }

    @BsonCreator
    public Client(
            @BsonProperty("_id") ObjectId entityId,
            @BsonProperty("first_name") String firstName,
            @BsonProperty("last_name") String lastName,
            @BsonProperty("email") String email,
            @BsonProperty("date_of_birth") Date dateOfBirth,
            @BsonProperty("address") Address address) {
        super(new ObjectId());
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        if (!address.equals(client.address)) return false;

        return true;
    }
}
