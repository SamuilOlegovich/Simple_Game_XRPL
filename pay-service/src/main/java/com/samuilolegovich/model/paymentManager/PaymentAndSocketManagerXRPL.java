package com.samuilolegovich.model.paymentManager;

import com.samuilolegovich.enums.BooleanEnum;
import com.samuilolegovich.enums.StringEnum;
import com.samuilolegovich.model.paymentManager.interfaces.PaymentManager;
import com.samuilolegovich.model.paymentManager.interfaces.Presets;
import com.samuilolegovich.model.paymentManager.interfaces.SocketManager;
import com.samuilolegovich.model.sockets.SocketXRP;
import com.samuilolegovich.model.sockets.SocketXRPTest;
import com.samuilolegovich.model.sockets.enums.StreamSubscriptionEnum;
import com.samuilolegovich.model.sockets.exceptions.InvalidStateException;
import com.samuilolegovich.model.sockets.interfaces.CommandListener;
import com.samuilolegovich.model.subscribers.interfaces.StreamSubscriber;
import com.samuilolegovich.model.wallets.WalletXRP;
import com.samuilolegovich.model.wallets.WalletXRPTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class PaymentAndSocketManagerXRPL implements PaymentManager, SocketManager, Presets {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentAndSocketManagerXRPL.class);

    @Autowired
    private WalletXRPTest walletTest;
    @Autowired
    private SocketXRPTest socketTest;
    @Autowired
    private SocketXRP socket;
    @Autowired
    private WalletXRP wallet;
    @Autowired
    @Qualifier("streamSubscriberToAccountBalanceChanges")
    private StreamSubscriber streamSubscriberToAccountBalanceChanges;


    @PostConstruct
    public void init() {
        connectBlocking();
        subscribeToAccountBalanceChanges();
    }




    // Payment *********************************************************************************************************

    @Override
    public void sendPayment(String address, Integer tag, BigDecimal numberOfXRP, boolean isReal) {
        if (isReal && wallet != null) { wallet.sendPaymentToAddressXRP(address, tag, numberOfXRP);
        } else if (walletTest != null) { walletTest.sendPaymentToAddressXRP(address, tag, numberOfXRP); }
    }

    @Override
    public Map<String, String> connectAnExistingWallet(String seed, boolean isReal) {
        if (isReal && wallet != null) {
            StringEnum.setValue(StringEnum.SEED_REAL, seed);
            return wallet.restoreWallet();
        } else if (walletTest != null) {
            StringEnum.setValue(StringEnum.SEED_TEST, seed);
            return walletTest.restoreWallet();
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, String> createNewWallet(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.createNewWallet();
        } else if (walletTest != null) {
            return walletTest.createNewWallet();
        }
        return new HashMap<>();
    }

    @Override
    public void updateWallet(boolean isReal) {
        if (isReal && wallet != null) {
            wallet.restoreWallet();
        } else if (walletTest != null) {
            walletTest.restoreWallet();
        }
    }

    @Override
    public void setterWallet(boolean isReal) {

    }

    @Override
    public String getClassicAddress(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.classicAddress().toString();
        } else if (walletTest != null) {
            return walletTest.classicAddress().toString();
        }
        return StringEnum.WALLET_NOT_ACTIVATED.getValue();
    }

    @Override
    public String getPrivateKey(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.privateKey().get();
        } else if (walletTest != null) {
            return walletTest.privateKey().get();
        }
        return StringEnum.WALLET_NOT_ACTIVATED.getValue();
    }

    @Override
    public String getXAddress(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.xAddress().toString();
        } else if (walletTest != null) {
            return walletTest.xAddress().toString();
        }
        return StringEnum.WALLET_NOT_ACTIVATED.getValue();
    }

    @Override
    public String getPublicKey(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.publicKey();
        } else  if (walletTest != null) {
            return walletTest.publicKey();
        }
        return StringEnum.WALLET_NOT_ACTIVATED.getValue();
    }

    @Override
    public String getSeed(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.getSeed();
        } else  if (walletTest != null) {
            return walletTest.getSeed();
        }
        return StringEnum.WALLET_NOT_ACTIVATED.getValue();
    }

    @Override
    public boolean isTest(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.isTest();
        } else  if (walletTest != null) {
            return walletTest.isTest();
        }
        return false;
    }

    @Override
    public BigDecimal getBalance(boolean isReal) {
        BigDecimal allBalance = getAllBalance(isReal);
        BigDecimal activationPayment = new BigDecimal(StringEnum.ACTIVATION_PAYMENT.getValue());
        int compareTo = allBalance.compareTo(activationPayment);
        if (compareTo <= 0) { return new BigDecimal("0.000000"); }
        return allBalance.subtract(activationPayment);
    }

    @Override
    public BigDecimal getAllBalance(boolean isReal) {
        if (isReal && wallet != null) {
            return wallet.getBalance();
        } else if (walletTest != null) {
            return walletTest.getBalance();
        }
        return BigDecimal.ZERO;
    }



    // Socket **********************************************************************************************************

    @Override
    public String sendCommand(String command, CommandListener listener, boolean isReal) throws InvalidStateException {
        if (isReal && socket != null) {
            return socket.sendCommand(command, null, listener);
        } else if (socketTest != null) {
            return socketTest.sendCommand(command, null, listener);
        }
        return StringEnum.WALLET_NOT_ACTIVATED.getValue();
    }

    @Override
    public String sendCommand(String command, Map<String, Object> parameters, CommandListener listener, boolean isReal)
            throws InvalidStateException {
        if (isReal && socket != null) {
            return socket.sendCommand(command, parameters, listener);
        } else if (socketTest != null) {
            socketTest.sendCommand(command, parameters, listener);
        }
        return StringEnum.WALLET_NOT_ACTIVATED.getValue();
    }

    @Override
    public void subscribe(EnumSet<StreamSubscriptionEnum> streams, StreamSubscriber subscriber, boolean isReal)
            throws InvalidStateException {
        if (isReal && socket != null) {
            socket.subscribe(streams, subscriber);
        } else if (socketTest != null) {
            socketTest.subscribe(streams, subscriber);
        }
    }

    @Override
    public void subscribe(EnumSet<StreamSubscriptionEnum> streams, Map<String, Object> parameters,
                          StreamSubscriber subscriber, boolean isReal) throws InvalidStateException {
        if (isReal && socket != null) {
            socket.subscribe(streams, parameters, subscriber);
        } else if (socketTest != null) {
            socketTest.subscribe(streams, parameters, subscriber);
        }
    }

    @Override
    public void unsubscribe(EnumSet<StreamSubscriptionEnum> streams, boolean isReal) throws InvalidStateException {
        if (isReal) {
            socket.unsubscribe(streams);
        } else if (socketTest != null) {
            socketTest.unsubscribe(streams);
        }
    }

    @Override
    public EnumSet<StreamSubscriptionEnum> getActiveSubscriptions(boolean isReal) {
        if (isReal && socket != null) {
            return socket.getActiveSubscriptions();
        } else if (socketTest != null) {
            return socketTest.getActiveSubscriptions();
        }
        return null;
    }

    @Override
    public void closeWhenComplete(boolean isReal) {
        if (isReal) {
            socket.closeWhenComplete();
        } else if (socketTest != null) {
            socketTest.closeWhenComplete();
        }
    }



    // Presets *********************************************************************************************************


    @Override
    public void setPresets(BooleanEnum enums, boolean b) {
        BooleanEnum.setValue(enums, b);
    }

    @Override
    public void setPresets(StringEnum enums, String s) {
        StringEnum.setValue(enums, s);
    }



    // *****************************************************************************************************************

    private void connectBlocking() {
        if (socket != null) {
            try {
                socket.connectBlocking(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (socketTest != null) {
            try {
                socketTest.connectBlocking(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void subscribeToAccountBalanceChanges() {
        if (socket != null) {
            subscribeTo(true);
        }
        if (socketTest != null) {
            subscribeTo(false);
        }
    }

    private void subscribeTo(boolean b) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("accounts", List.of(getClassicAddress(b)));
        try {
            socket.subscribe(EnumSet.of(StreamSubscriptionEnum.ACCOUNT_CHANNELS), parameters, (subscription, message) -> {
                if (message.getJSONObject("transaction").has("DestinationTag")) {
                    LOG.info("Получил сообщение об изменении баланса на счету от подписки {}: {}", subscription.getMessageType(), message);
                }
                LOG.info("Получил сообщение от подписки {}: {}", subscription.getMessageType(), message);
            });
        } catch (InvalidStateException e) {
            e.printStackTrace();
        }
    }
}
