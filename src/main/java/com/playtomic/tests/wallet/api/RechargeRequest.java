package com.playtomic.tests.wallet.api;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RechargeRequest {
  @NotNull
  private final long walletId;
  @NotNull
  private final String creditCardNumber;
  @Min(value = 10)
  @NotNull
  private final BigDecimal amount;

  public RechargeRequest(long walletId, String creditCardNumber, BigDecimal amount) {
    this.walletId = walletId;
    this.creditCardNumber = creditCardNumber;
    this.amount = amount;
  }

  public long walletId() {
    return walletId;
  }

  public String creditCardNumber() {
    return creditCardNumber;
  }

  public BigDecimal amount() {
    return amount;
  }
}
