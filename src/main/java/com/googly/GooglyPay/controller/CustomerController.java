package com.googly.GooglyPay.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.googly.GooglyPay.dto.CustomerRegistrationDto;
import com.googly.GooglyPay.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	@PostMapping
	public ResponseEntity<String> customerRegistration(@Valid @RequestBody CustomerRegistrationDto customerRegistrationDto) {
		return customerService.customerRegistration(customerRegistrationDto);
	}
	
	@PostMapping("/account")
	public ResponseEntity<String> addAccount(@RequestParam("mobileNo") @NotNull String mobileNo) {
		return customerService.addAccount(mobileNo);
	}
}
