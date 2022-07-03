package com.googly.GooglyPay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.googly.GooglyPay.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	long countByMobileNo(String mobileNo);
	
	@Query(value = "SELECT customer_id FROM customer WHERE mobile_no = :mobileNo", nativeQuery = true)
	public Optional<Long> getCustomerId(@Param("mobileNo") String mobileNo);
	
	public Optional<Customer> findByMobileNo(String mobileNo);
}
