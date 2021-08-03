package com.playtomic.tests.wallet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.playtomic.tests.wallet.api.ChargeRequest;
import com.playtomic.tests.wallet.api.RechargeRequest;
import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

	@Autowired
	private WalletRepository repository;

	@Autowired
	private WalletController controller;

	@Test
	void getWalletThenSuccess() {
		// given
		final long id = 100L;
		// when
		final Wallet response = controller.getWallet(id);
		// then
		assertThat(response.id(), is(100L));
		assertThat(response.balance(), is(BigDecimal.valueOf(999.99)));
	}
	@Test
	void rechargeBalanceThenSuccess() throws StripeServiceException {
		// given
		final RechargeRequest request = new RechargeRequest(101L,
							"4222 1111 1111 1111", BigDecimal.valueOf(234.56));
		// when
		final Wallet response = controller.rechargeBalance(request);
		// then
		assertThat(response.id(), is(101L));
		assertThat(response.balance(), is(BigDecimal.valueOf(1234.56)));
	}

	@Test
	void chargeAmountThenSuccess() {
		// given
		final ChargeRequest request = new ChargeRequest(102L, BigDecimal.valueOf(100.01));
		// when
		final Wallet response = controller.chargeAmount(request);
		// then
		assertThat(response.id(), is(102L));
		assertThat(response.balance(), is(BigDecimal.valueOf(420.06)));
	}
}
