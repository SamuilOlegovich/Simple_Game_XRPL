package com.samuilolegovich.configuration;

import com.samuilolegovich.enums.StringEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


public class RabbitConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitConfiguration.class);
    public static final String OTHER_CHANGES_ROUTING_KEY = "other-changes";
    public static final String BALANCE_ROUTING_KEY = "rebalancing";
    public static final String TOPIC_EXCHANGE = "topic.changes";


//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }

//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//
//
//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }

    //настраиваем соединение с RabbitMQ
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(StringEnum.RABBIT_MQ_NET_TEST.getValue());
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

    // объявляем очередь с именем queueToAccountBalanceChanges
    @Bean
    public Queue queueBalance() {
        return new Queue("queue-account-balance-changes");
    }

    @Bean
    public Queue queueOtherChanges() {
        return new Queue("other-changes");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingBalance() {
        return BindingBuilder.bind(queueBalance()).to(topicExchange()).with(BALANCE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOtherChanges() {
        return BindingBuilder.bind(queueOtherChanges()).to(topicExchange()).with(OTHER_CHANGES_ROUTING_KEY);
    }

}
