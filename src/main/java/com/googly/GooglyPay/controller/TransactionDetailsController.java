package com.googly.GooglyPay.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.googly.GooglyPay.dto.TransactionRequestDto;
import com.googly.GooglyPay.dto.TransactionResponseDto;
import com.googly.GooglyPay.dto.TransactionStatementDto;
import com.googly.GooglyPay.service.TransactionDetailsService;

@RestController
@RequestMapping("/transactions")
public class TransactionDetailsController {
	
	@Autowired
	TransactionDetailsService transactionDetailsService;

	@GetMapping
	public ResponseEntity<List<TransactionStatementDto>> getTransactionStatement(@RequestParam("mobileNo") @NotNull String mobileNo) {
		return transactionDetailsService.getTransactionStatement(mobileNo);
	}
	
	@PostMapping
	public TransactionResponseDto performTransaction(@RequestBody @Valid TransactionRequestDto transactionRequestDto) {
		return transactionDetailsService.performTransaction(transactionRequestDto);
	}
}
