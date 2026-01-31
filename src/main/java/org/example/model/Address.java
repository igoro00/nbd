package org.example.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Address {
    @BsonProperty("city")
    private String city;

    @BsonProperty("zip_code")
    private String zipCode;

    @BsonProperty("street")
    private String street;

    @BsonProperty("number")
    private String number;
}
