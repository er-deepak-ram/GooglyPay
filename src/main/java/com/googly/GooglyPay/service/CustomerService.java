package com.googly.GooglyPay.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;

import com.googly.GooglyPay.dto.CustomerRegistrationDto;

public interface CustomerService {

	ResponseEntity<String> customerRegistration(@Valid CustomerRegistrationDto customerRegistrationDto);

	ResponseEntity<String> addAccount(@NotNull String mobileNo);

}
