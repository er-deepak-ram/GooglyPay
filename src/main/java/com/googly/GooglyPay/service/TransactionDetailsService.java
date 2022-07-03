package com.googly.GooglyPay.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;

import com.googly.GooglyPay.dto.TransactionRequestDto;
import com.googly.GooglyPay.dto.TransactionResponseDto;
import com.googly.GooglyPay.dto.TransactionStatementDto;

public interface TransactionDetailsService {

	ResponseEntity<List<TransactionStatementDto>> getTransactionStatement(@NotNull String mobileNo);

	TransactionResponseDto performTransaction(TransactionRequestDto transactionRequestDto);

}
