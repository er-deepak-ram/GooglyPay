package com.googly.GooglyPay.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionStatementDto {

	private long transactionId;
	private LocalDateTime transactionDate;
	private long amount;
	private String type;
	private String mobileNo;
}
