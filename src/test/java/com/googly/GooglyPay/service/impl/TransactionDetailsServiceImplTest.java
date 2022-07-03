package com.googly.GooglyPay.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
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

import com.googly.GooglyPay.dto.TransactionRequestDto;
import com.googly.GooglyPay.dto.TransactionResponseDto;
import com.googly.GooglyPay.dto.TransactionStatementDto;
import com.googly.GooglyPay.entity.Customer;
import com.googly.GooglyPay.entity.TransactionDetails;
import com.googly.GooglyPay.feignclient.BankClient;
import com.googly.GooglyPay.repository.CustomerRepository;
import com.googly.GooglyPay.repository.TransactionDetailsRepository;
import com.googly.GooglyPay.utils.Contants;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class TransactionDetailsServiceImplTest {

	@Mock
	TransactionDetailsRepository transactionDetailsRepository;
	
	@Mock
	CustomerRepository customerRepository;
	
	@Mock
	BankClient bankClient;
	
	@InjectMocks
	TransactionDetailsServiceImpl transactionDetailsService;
	
	static String mobileNo, ok;
	static TransactionRequestDto transactionRequestDto;
	static Optional<Customer> customerOp;
	static Customer customer;
	static TransactionDetails transactionDetails;
	
	@BeforeEach
	void setUp() throws Exception {
		mobileNo = "9881634256";
		ok = "Ok";
		transactionRequestDto = new TransactionRequestDto();
		transactionRequestDto.setFromMobileNo("9881634256");
		transactionRequestDto.setToMobileNo("9503526146");
		transactionRequestDto.setAmount(1000L);
		
		customer = new Customer();
		customer.setCustomerId(1);
		customer.setFirstName("Elon");
		customer.setLastName("Musk");
		customer.setDateOfBirth(LocalDate.of(1971, Month.JUNE, 28));
		customer.setEmailId("theelonmusk@gmail.com");
		customer.setMobileNo("9881634256");
		customerOp = Optional.of(customer);
		
		transactionDetails = new TransactionDetails();
		
		BeanUtils.copyProperties(transactionRequestDto, transactionDetails);
		transactionDetails.setCustomerId(customer);
		transactionDetails.setTransactionDate(LocalDateTime.now());
		
	}

	@Test
	@DisplayName("getTransactionStatement() : Positive scenario")
	void testGetTransactionStatement1() {
		
		// given or context
		when(transactionDetailsService.checkIfCustomerExist(mobileNo)).thenReturn(ok);
		when(transactionDetailsRepository.findTop10ByFromMobileNoOrToMobileNoOrderByTransactionDateDesc(mobileNo, mobileNo)).thenReturn(Mockito.anyList());
		
		// when or event
		ResponseEntity<List<TransactionStatementDto>> responseEntity = transactionDetailsService.getTransactionStatement(mobileNo);
		
		// then or outcome
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@DisplayName("performTransaction() : positive scenario")
	public void testPerformTransaction() {
		
		// given or context
		when(customerRepository.findByMobileNo(transactionRequestDto.getFromMobileNo())).thenReturn(customerOp);
		when(bankClient.performUpiTransaction(transactionRequestDto)).thenReturn(new ResponseEntity<String>(Contants.TRANSACTION_SUCCESS, HttpStatus.OK));
		when(transactionDetailsRepository.save(transactionDetails)).thenReturn(transactionDetails);
		
		// when or event
		TransactionResponseDto transactionResponseDto = transactionDetailsService.performTransaction(transactionRequestDto);
		
		// then or outcome
		verify(transactionDetailsRepository).save(transactionDetails);
		assertEquals(transactionResponseDto.getTransactionStatus(), Contants.TRANSACTION_SUCCESS);
	}

}
