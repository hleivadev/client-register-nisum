package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneDto {
    private String number;
    private String cityCode;
    private String countryCode;
}
