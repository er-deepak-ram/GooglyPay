package com.googly.GooglyPay.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "transaction_details")
@Data
public class TransactionDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaction_id")
	private long transactionId;
	
	@Column(name = "amount")
	private long amount;
	
	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;
	
	@Column(name = "from_mobile_no")
	private String fromMobileNo;
	
	@Column(name = "to_mobile_no")
	private String toMobileNo;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customerId;
}
