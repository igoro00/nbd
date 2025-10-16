package org.example.model.persons;

import jakarta.persistence.*;

import org.example.model.Address;

import java.util.Date;

@Entity
@DiscriminatorValue("client")
public class Client extends Person{
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "email", unique = true)
    private String email;

    @Embedded
    private Address address;


    public Client(String firstName, String lastName, Date dateOfBirth, String email) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.email = email;
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

}
