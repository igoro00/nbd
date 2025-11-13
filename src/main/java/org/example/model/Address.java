package org.example.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Address {
    @BsonProperty("city")
    private String city;

    @BsonProperty("zip_code")
    private String zipCode;

    @BsonProperty("street")
    private String street;

    @BsonProperty("number")
    private String number;

    @BsonCreator
    public Address(
            @BsonProperty("city") String city,
            @BsonProperty("zip_code") String zipCode,
            @BsonProperty("street") String street,
            @BsonProperty("number") String number
    ) {
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        if (!city.equals(address.city)) return false;
        if (!zipCode.equals(address.zipCode)) return false;
        if (!street.equals(address.street)) return false;
        if (!number.equals(address.number)) return false;
        return true;
    }
}
