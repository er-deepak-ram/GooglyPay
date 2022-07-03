package com.googly.GooglyPay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.googly.GooglyPay.entity.CustomerAccount;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
	
	Optional<CustomerAccount> findByMobileNo(String mobileNo);

}
