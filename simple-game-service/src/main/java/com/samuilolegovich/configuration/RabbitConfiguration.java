package com.samuilolegovich.configuration;

import com.samuilolegovich.enums.StringEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitConfiguration.class);


//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }

//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }



//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }

    //настраиваем соединение с RabbitMQ

    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory
                = new CachingConnectionFactory(StringEnum.RABBIT_MQ_NET_TEST.getValue());
        return connectionFactory;
    }



    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }



    @Bean
    public Queue queueToMakePayment() {
        return new Queue("to-make-a-payment");
    }

    @Bean
    public Queue queueToMakePaymentTest() {
        return new Queue("to-make-a-payment-test");
    }




    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(StringEnum.TOPIC_EXCHANGE.getValue(), true, false);
    }



    @Bean
    public Binding bindingBalance() {
        return BindingBuilder.bind(queueToMakePayment()).to(topicExchange()).with(StringEnum.MAKE_PAYMENT_ROUTING_KEY.getValue());
    }

    @Bean
    public Binding bindingBalanceTest() {
        return BindingBuilder.bind(queueToMakePayment()).to(topicExchange()).with(StringEnum.MAKE_PAYMENT_ROUTING_KEY_TEST.getValue());
    }
}
