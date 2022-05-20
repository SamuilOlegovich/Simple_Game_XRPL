package com.samuilolegovich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Locale;

@SpringBootApplication
@EnableDiscoveryClient
public class PayApplication {

    // запустить сокеты и подписаться на нужные обновления.
    // сделать класс таймер который будет переодически проверять работоспособностть сокетов и решать проблему если он отпадет.
    // ну или же сделать это через ошибку -  плдумать как лучше и надежнее

    // протестить работоспособность реббита

    // использовать тег как перенос информации первые две цифры будут означать - что произошло - остальные нести количество доступного джекпота
    // 2 147 483 647
    // 21-47 483 647
    // сделать при запуске вычитывание оставшихся платежей из базы и выплату их
    // когда это сделаем добавить проверку на совместимость с очередью и удаление отработаных данных из базы
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
        // Обязательно стоит установить локаль иначе будет падать с ошибкой парсинга даты
        Locale.setDefault(Locale.ENGLISH);
    }
}
