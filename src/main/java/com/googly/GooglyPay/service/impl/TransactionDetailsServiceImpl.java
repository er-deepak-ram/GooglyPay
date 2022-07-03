package com.googly.GooglyPay.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.googly.GooglyPay.dto.TransactionRequestDto;
import com.googly.GooglyPay.dto.TransactionResponseDto;
import com.googly.GooglyPay.dto.TransactionStatementDto;
import com.googly.GooglyPay.entity.Customer;
import com.googly.GooglyPay.entity.CustomerAccount;
import com.googly.GooglyPay.entity.TransactionDetails;
import com.googly.GooglyPay.exception.ResourceNotFoundException;
import com.googly.GooglyPay.feignclient.BankClient;
import com.googly.GooglyPay.repository.CustomerAccountRepository;
import com.googly.GooglyPay.repository.CustomerRepository;
import com.googly.GooglyPay.repository.TransactionDetailsRepository;
import com.googly.GooglyPay.service.TransactionDetailsService;
import com.googly.GooglyPay.utils.Contants;

@Service
public class TransactionDetailsServiceImpl implements TransactionDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionDetailsServiceImpl.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionDetailsRepository transactionDetailsRepository;
	
	@Autowired
	private CustomerAccountRepository customerAccountRepository;

	@Autowired
	private BankClient bankClient;

	@Override
	public ResponseEntity<List<TransactionStatementDto>> getTransactionStatement(@NotNull String mobileNo) {
		checkIfCustomerExist(mobileNo);
		List<TransactionStatementDto> transactionStatementDtoList = new ArrayList<>();
		List<TransactionDetails> transactionDetailsList = transactionDetailsRepository
				.findTop10ByFromMobileNoOrToMobileNoOrderByTransactionDateDesc(mobileNo, mobileNo);
		transactionDetailsList.forEach(transactionDetail -> {
			TransactionStatementDto transactionStatementDto = new TransactionStatementDto();
			BeanUtils.copyProperties(transactionDetail, transactionStatementDto);
			transactionStatementDto
					.setType(mobileNo.equals(transactionDetail.getFromMobileNo()) ? Contants.CREDIT : Contants.DEBIT);
			transactionStatementDto.setMobileNo(
					mobileNo.equals(transactionDetail.getFromMobileNo()) ? transactionDetail.getToMobileNo()
							: transactionDetail.getFromMobileNo());
			transactionStatementDtoList.add(transactionStatementDto);
		});
		HttpHeaders responseHeaders = new HttpHeaders();
		if (transactionStatementDtoList.size() > 0) {
			responseHeaders.set("message", Contants.STATEMENT_GENERATED);
			return new ResponseEntity<>(transactionStatementDtoList, responseHeaders, HttpStatus.OK);
		} else {
			responseHeaders.set("message", Contants.NO_STATEMENT + mobileNo);
			logger.error(Contants.NO_STATEMENT + mobileNo);
			return new ResponseEntity<>(transactionStatementDtoList, responseHeaders, HttpStatus.NO_CONTENT);
		}
	}

	public String checkIfCustomerExist(String mobileNo) {
		long count = customerRepository.countByMobileNo(mobileNo);
		if (count > 0)
			return "Ok";
		logger.error(Contants.CUSTOMER_NOT_FOUND + mobileNo);
		throw new ResourceNotFoundException(Contants.CUSTOMER_NOT_FOUND + mobileNo);
	}

	@Override
	public TransactionResponseDto performTransaction(TransactionRequestDto transactionRequestDto) {
		TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
		Optional<Customer> customerOp = customerRepository.findByMobileNo(transactionRequestDto.getFromMobileNo());
		Optional<CustomerAccount> fromCustomerOp = customerAccountRepository.findByMobileNo(transactionRequestDto.getFromMobileNo());
		Optional<CustomerAccount> toCustomerOp = customerAccountRepository.findByMobileNo(transactionRequestDto.getToMobileNo());
		fromCustomerOp.ifPresentOrElse(fromCustomer -> {
			toCustomerOp.ifPresentOrElse(toCustomer -> {
				ResponseEntity<String> response = bankClient.performUpiTransaction(transactionRequestDto);
				if (response.getStatusCode().equals(HttpStatus.OK)) {
					TransactionDetails transactionDetails = new TransactionDetails();
					TransactionDetails savedTransactionDetails = new TransactionDetails();
					BeanUtils.copyProperties(transactionRequestDto, transactionDetails);
					transactionDetails.setTransactionDate(LocalDateTime.now());
					transactionDetails.setCustomerId(customerOp.get());
					savedTransactionDetails = transactionDetailsRepository.save(transactionDetails);
					BeanUtils.copyProperties(savedTransactionDetails, transactionResponseDto);
					transactionResponseDto.setTransactionStatus(Contants.TRANSACTION_SUCCESS);
				} else {
					logger.error(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getToMobileNo());
					throw new ResourceNotFoundException(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getToMobileNo());
				}
			}, () -> {
				logger.error(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getToMobileNo());
				throw new ResourceNotFoundException(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getToMobileNo());
			});
		}, () -> {
			logger.error(Contants.ACCOUNT_NOT_ADDED);
			throw new ResourceNotFoundException(Contants.ACCOUNT_NOT_ADDED);
		});
		
//		customerOp.ifPresentOrElse(customer -> {
//			ResponseEntity<String> response = bankClient.performUpiTransaction(transactionRequestDto);
//			if (response.getStatusCode().equals(HttpStatus.OK)) {
//				TransactionDetails transactionDetails = new TransactionDetails();
//				TransactionDetails savedTransactionDetails = new TransactionDetails();
//				BeanUtils.copyProperties(transactionRequestDto, transactionDetails);
//				transactionDetails.setTransactionDate(LocalDateTime.now());
//				transactionDetails.setCustomerId(customer);
//				savedTransactionDetails = transactionDetailsRepository.save(transactionDetails);
//				BeanUtils.copyProperties(savedTransactionDetails, transactionResponseDto);
//				transactionResponseDto.setTransactionStatus(Contants.TRANSACTION_SUCCESS);
//			} else {
//				logger.error(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getToMobileNo());
//				throw new ResourceNotFoundException(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getToMobileNo());
//			}
//		}, () -> {
//			logger.error(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getFromMobileNo());
//			throw new ResourceNotFoundException(Contants.CUSTOMER_NOT_FOUND + transactionRequestDto.getFromMobileNo());
//		});
		return transactionResponseDto;
	}
}
