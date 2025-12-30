package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Address {
    @CqlName("city")
    private String city;

    @CqlName("zip_code")
    private String zipCode;

    @CqlName("street")
    private String street;

    @CqlName("number")
    private String number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return new EqualsBuilder()
                .append(city, address.city)
                .append(zipCode, address.zipCode)
                .append(street, address.street)
                .append(number, address.number)
                .isEquals();
    }
}
