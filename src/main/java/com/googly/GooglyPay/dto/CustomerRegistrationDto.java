package com.googly.GooglyPay.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerRegistrationDto {

	@NotNull(message = "firstName is required")
	@Size(min = 2, message = "firstName should have atleast 2 charecters")
	private String firstName;
	
	@NotNull(message = "lastName is required")
	@Size(min = 2, message = "lastName should have atleast 2 charecters")
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	@NotNull(message = "Mobile number is required")
	@Size(min = 10, max = 10, message = "mobile number should have 10 digits")
	private String mobileNo;
	
	@NotBlank
	@Email
	private String emailId;
}