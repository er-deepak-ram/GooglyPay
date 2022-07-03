package com.googly.GooglyPay.feignclient;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.googly.GooglyPay.dto.TransactionRequestDto;

//@FeignClient(value = "bank-client", url = "http://localhost:8082/codersbank/transactions")
@FeignClient(name = "http://BANK-SERVICE/codersbank/transactions")
public interface BankClient {

	@PostMapping("/upi")
	public ResponseEntity<String> performUpiTransaction(@RequestBody @Valid TransactionRequestDto transactionRequestDto);
}
