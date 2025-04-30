package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO class for Phone.
 */
@Getter
@Setter
public class PhoneDto {

    private String number;
    private String cityCode;
    private String countryCode;

    // Default constructor
    public PhoneDto() {
    }

    // Constructor with parameters
    public PhoneDto(String number, String cityCode, String countryCode) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
    }

}
