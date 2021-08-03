package com.playtomic.tests.wallet;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;

@Entity
public class Wallet {
  @Id
  @NotNull
  private long id;
  @NotNull
  private BigDecimal balance;
  @Version
  @NotNull
  private long version;

  public Wallet() {}

  public Wallet(long id, BigDecimal balance) {
    this.id = id;
    this.balance = balance;
  }

  @JsonGetter("id")
  public long id() {
    return id;
  }

  @JsonGetter("balance")
  public BigDecimal balance() {
    return balance;
  }

  public void addBalance(final BigDecimal amount) {
    balance = balance.add(amount);
  }

  public void charge(final BigDecimal amount) {
    balance = balance.subtract(amount);
  }
}
