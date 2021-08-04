package com.playtomic.tests.wallet.api;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.playtomic.tests.wallet.Wallet;
import com.playtomic.tests.wallet.WalletException;
import com.playtomic.tests.wallet.WalletRepository;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;

@RestController
public class WalletController {
    private final Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletRepository repository;
    private final StripeService stripe;

    @Autowired
    public WalletController(WalletRepository repository, StripeService stripe) {
        this.repository = repository;
        this.stripe = stripe;
    }

    @GetMapping("/wallet/{id}")
    public Wallet getWallet(@PathVariable("id") final long id) {
        final Optional<Wallet> walletOptional = repository.findById(id);
        if (!walletOptional.isPresent()) {
            throw new WalletException(Wallet.class, "id", String.valueOf(id));
        }
        return walletOptional.get();
    }

    @PostMapping("/wallet/recharge")
    public Wallet rechargeBalance(@Valid @RequestBody final RechargeRequest request) throws StripeServiceException {
        final Optional<Wallet> walletOptional = repository.findById(request.walletId());
        if (!walletOptional.isPresent()) {
            throw new WalletException(Wallet.class, "id", String.valueOf(request.walletId()));
        }
        final Wallet wallet = walletOptional.get();
        stripe.charge(request.creditCardNumber(), request.amount());
        wallet.addBalance(request.amount());
        return repository.save(wallet);
    }

    @PostMapping("/wallet/charge")
    public Wallet chargeAmount(@Valid @RequestBody final ChargeRequest request) {
        final Optional<Wallet> walletOptional = repository.findById(request.walletId());
        if (!walletOptional.isPresent()) {
            throw new WalletException(Wallet.class, "id", String.valueOf(request.walletId()));
        }
        final Wallet wallet = walletOptional.get();
        wallet.charge(request.amount());
        return repository.save(wallet);
    }
}
