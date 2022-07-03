package com.googly.GooglyPay.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.googly.GooglyPay.dto.CustomerRegistrationDto;
import com.googly.GooglyPay.entity.Customer;
import com.googly.GooglyPay.feignclient.AccountClient;
import com.googly.GooglyPay.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class CustomerServiceImplTest {

	@Mock
	CustomerRepository customerRepository;
	
	@Mock
	AccountClient accountClient;
	
	@InjectMocks
	CustomerServiceImpl customerService;
	
	static CustomerRegistrationDto customerRegistrationDto;
	
	static Customer customer, savedCustomer;
	static String mobileNo;
	Optional<Long> accountNo;
	
	@BeforeEach
	void setUp() throws Exception {
		customerRegistrationDto = new CustomerRegistrationDto();
		customerRegistrationDto.setFirstName("Elon");
		customerRegistrationDto.setLastName("Musk");
		customerRegistrationDto.setDateOfBirth(LocalDate.of(1971, Month.JUNE, 28));
		customerRegistrationDto.setEmailId("theelonmusk@gmail.com");
		customerRegistrationDto.setMobileNo("9999999999");
		
		customer = new Customer();
		savedCustomer = new Customer();
		BeanUtils.copyProperties(customerRegistrationDto, customer);
		BeanUtils.copyProperties(customer, savedCustomer);
		
//		mobileNo = "9999999999";
//		accountNo = Optional.of(123000L);
	}

	@Test
	@DisplayName("Testing customerRegistration(): positive scenario")
	void testCustomerRegistration() {
		
		// given or context
		when(customerRepository.save(customer)).thenReturn(savedCustomer);
		
		// when or event
		ResponseEntity<String> registered = customerService.customerRegistration(customerRegistrationDto);
		
		// then or outcome
		verify(customerRepository).save(customer);
		assertEquals(registered.getStatusCode(), HttpStatus.CREATED);
	}
	
//	@Test
//	@DisplayName("Testing addAccount(): positive scenario")
//	void testAddAccount() {
//		
//		// given or context
//		when(accountClient.getAccountNumber(Long.parseLong(Mockito.anyString()))).thenReturn(Optional.of(Mockito.anyLong()));
//	}

}
