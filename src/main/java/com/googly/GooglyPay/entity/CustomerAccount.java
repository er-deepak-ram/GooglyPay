package com.googly.GooglyPay.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "customer_account")
@Data
public class CustomerAccount {

	@Id
	@Column(name = "account_no")
	private Long accountNo;
	
	@Column(name = "mobile_no")
	private String mobileNo;
}
