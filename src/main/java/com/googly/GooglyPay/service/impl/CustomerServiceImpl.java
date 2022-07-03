package com.googly.GooglyPay.service.impl;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.googly.GooglyPay.dto.CustomerRegistrationDto;
import com.googly.GooglyPay.entity.Customer;
import com.googly.GooglyPay.entity.CustomerAccount;
import com.googly.GooglyPay.exception.IncorrectDataException;
import com.googly.GooglyPay.feignclient.AccountClient;
import com.googly.GooglyPay.repository.CustomerAccountRepository;
import com.googly.GooglyPay.repository.CustomerRepository;
import com.googly.GooglyPay.service.CustomerService;
import com.googly.GooglyPay.utils.Contants;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	private CustomerAccountRepository customerAccountRepository;

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Override
	public ResponseEntity<String> customerRegistration(@Valid CustomerRegistrationDto customerRegistrationDto) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerRegistrationDto, customer);
		Customer savedCustomer = customerRepository.save(customer);
		if (savedCustomer == null) {
			logger.error(Contants.REGISTRATION_FAILED);
			return new ResponseEntity<>(Contants.REGISTRATION_FAILED, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(Contants.REGISTRATION_SUCCESSFULL, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> addAccount(@NotNull String mobileNo) {
		Optional<Long> accountOp = accountClient.getAccountNumber(Long.parseLong(mobileNo));
		accountOp.ifPresentOrElse(accountNo -> {
			CustomerAccount customerAccount = new CustomerAccount();
			customerAccount.setAccountNo(accountNo);
			customerAccount.setMobileNo(mobileNo);
			customerAccountRepository.save(customerAccount);
		}, () -> {
			throw new IncorrectDataException(Contants.NO_BANK_ACCOUNT + mobileNo);
		});
		return new ResponseEntity<>(Contants.ACCOUNT_ADDED, HttpStatus.OK);
	}
}
