package com.samuilolegovich;

import com.samuilolegovich.model.paymentManager.PaymentAndSocketManagerXRPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Locale;

@SpringBootApplication
@EnableDiscoveryClient
public class PayApplication {
    @Autowired
    private PaymentAndSocketManagerXRPL paymentAndSocketManagerXRPL;

    // запустить сокеты и подписаться на нужные обновления.
    // сделать класс таймер который будет переодически проверять работоспособностть сокетов и решать проблему если он отпадет.
    // ну или же сделать это через ошибку -  плдумать как лучше и надежнее
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
        // Обязательно стоит установить локаль иначе будет падать с ошибкой парсинга даты
        Locale.setDefault(Locale.ENGLISH);
    }
}
