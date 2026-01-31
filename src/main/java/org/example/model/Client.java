package org.example.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
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
}
