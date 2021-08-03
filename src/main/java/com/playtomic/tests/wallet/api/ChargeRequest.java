package com.playtomic.tests.wallet.api;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeRequest {
  @NotNull
  private final long walletId;
  @Min(value = 1)
  @NotNull
  private final BigDecimal amount;

  public ChargeRequest(long walletId, BigDecimal amount) {
    this.walletId = walletId;
    this.amount = amount;
  }

  @JsonGetter("walletId")
  public long walletId() {
    return walletId;
  }

  @JsonGetter("amount")
  public BigDecimal amount() {
    return amount;
  }
}
