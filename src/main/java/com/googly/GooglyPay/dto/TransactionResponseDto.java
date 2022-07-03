package com.googly.GooglyPay.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionResponseDto {

	private long transactionId;
	private long amount;
	private LocalDateTime transactionDate;
	private String transactionStatus;
}
