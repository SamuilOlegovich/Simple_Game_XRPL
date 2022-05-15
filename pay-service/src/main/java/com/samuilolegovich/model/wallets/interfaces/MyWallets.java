package com.samuilolegovich.model.wallets.interfaces;

import org.xrpl.xrpl4j.wallet.Wallet;

import java.math.BigDecimal;
import java.util.Map;

public interface MyWallets extends Wallet {
    Map<String, String> createNewWallet();
    BigDecimal getBalance();
    String getSeed();
}
