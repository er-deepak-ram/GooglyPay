package com.googly.GooglyPay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.googly.GooglyPay.entity.TransactionDetails;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {

	List<TransactionDetails> findTop10ByFromMobileNoOrToMobileNoOrderByTransactionDateDesc(String fromMobileNo, String toMobileNo);
}
