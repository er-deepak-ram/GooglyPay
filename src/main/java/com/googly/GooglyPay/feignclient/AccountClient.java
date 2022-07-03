package com.googly.GooglyPay.feignclient;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "http://BANK-SERVICE/codersbank/customers")
public interface AccountClient {

	@GetMapping("/account")
	public Optional<Long> getAccountNumber(@RequestParam @NotNull Long mobileNo);
}
