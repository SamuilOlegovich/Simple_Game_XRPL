package com.samuilolegovich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Locale;

@SpringBootApplication
@EnableDiscoveryClient
public class SimpleGameApplication {

    // уюрать округление из логики, так как оно изначально внесено в формирование модели юзера

    public static void main(String[] args) throws InterruptedException {
           SpringApplication.run(SimpleGameApplication.class, args);
        // Обязательно стоит установить локаль иначе будет падать с ошибкой парсинга даты
        Locale.setDefault(Locale.ENGLISH);
    }
    /*
    * в данном сервисе должна быть реализована вообще незамыславатая логика -
    * сервис должен отслеживать изменение баланса на кошельке, и делать "ветеор"
    * а именно сделать лотерею доступной с любого кошелька,
    * без какого либо визуального интерфейса - почти децентрализация,
    * где и что будет обрабатывать этот кошелек (адресс) ни кто знать не будет
    * отправили на него средства система сама решила выиграли что-то или нет
    * и дала ответ на кошелек в виде выигрыша или может быть супер приза и т д
    */
}
