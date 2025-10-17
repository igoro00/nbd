package org.example.model.persons;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("director")
public class Director extends Person{
    public Director(String firstName, String lastName) {
        super(firstName, lastName);
    }
    public Director(){}
}
