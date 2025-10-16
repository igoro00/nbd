package org.example.model.persons;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import org.example.model.Address;

import java.util.Date;

@Entity
@DiscriminatorValue("client")
public class Client extends Person{
    @NotNull
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Embedded
    private Address address;

    public Client(String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        super(firstName, lastName);
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
}
