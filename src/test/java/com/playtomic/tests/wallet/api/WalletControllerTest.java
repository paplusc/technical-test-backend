package com.playtomic.tests.wallet.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.playtomic.tests.wallet.Wallet;
import com.playtomic.tests.wallet.WalletException;
import com.playtomic.tests.wallet.WalletRepository;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;

public class WalletControllerTest {
  private final WalletRepository repositoryMock = mock(WalletRepository.class);
  private final StripeService stripeMock = mock(StripeService.class);
  private final WalletController controller = new WalletController(repositoryMock, stripeMock);

  @Nested
  class RechargeBalance {
    @Test
    void thenSuccess() throws StripeServiceException {
      // given
      final RechargeRequest request = new RechargeRequest(101L, "1234", BigDecimal.valueOf(100));
      when(repositoryMock.findById(any())).thenReturn(Optional.of(new Wallet(101L,BigDecimal.valueOf(123.33))));
      doNothing().when(stripeMock).charge(any(), any());
      when(repositoryMock.save(any())).thenReturn(new Wallet(101L,BigDecimal.valueOf(223.33)));
      // when
      final Wallet response = controller.rechargeBalance(request);
      // then
      assertThat(response.id(), is(101L));
      assertThat(response.balance(), is(BigDecimal.valueOf(223.33)));
    }
    @Test
    void thenNotFoundException() {
      // given
      final RechargeRequest request = new RechargeRequest(123L, "1234", BigDecimal.valueOf(100));
      when(repositoryMock.findById(any())).thenReturn(Optional.empty());
      // when
      try {
        controller.rechargeBalance(request);
      } catch (WalletException | StripeServiceException ex){
        // then
        assertThat(ex, instanceOf(WalletException.class));
        assertThat(ex.getMessage(), is("Wallet was not found for parameters {id=123}"));
      }
    }
    @Test
    void thenStripeServiceException() throws StripeServiceException {
      // given
      final RechargeRequest request = new RechargeRequest(123L, "1234", BigDecimal.valueOf(100));
      when(repositoryMock.findById(any())).thenReturn(Optional.of(new Wallet(101L,BigDecimal.valueOf(123.33))));
      doThrow(StripeServiceException.class).when(stripeMock).charge(any(),any());
      // when
      try {
        controller.rechargeBalance(request);
      } catch (StripeServiceException ex){
        // then
        assertThat(ex, instanceOf(StripeServiceException.class));
      }
    }
  }
}
