package com.playtomic.tests.wallet;

import java.util.Map;

import org.springframework.util.StringUtils;

public class WalletException extends RuntimeException {
  public WalletException(Class clazz, String id, String value) {
    super(WalletException.generateMessage(clazz.getSimpleName(), Map.of(id, value)));
  }

  private static String generateMessage(String entity, Map<String, String> searchParams) {
    return StringUtils.capitalize(entity) +
               " was not found for parameters " +
               searchParams;
  }
}
