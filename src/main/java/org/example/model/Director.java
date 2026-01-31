package org.example.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class Director {
    @BsonProperty("first_name")
    private String firstName;

    @BsonProperty("last_name")
    private String lastName;
}
