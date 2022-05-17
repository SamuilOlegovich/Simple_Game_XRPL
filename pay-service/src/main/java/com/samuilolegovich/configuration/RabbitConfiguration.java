package com.samuilolegovich.configuration;

import com.samuilolegovich.enums.StringEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitConfiguration.class);
    public static final String BALANCE_NOT_TAG_ROUTING_KEY = "balance-not-tag";
    public static final String OTHER_CHANGES_ROUTING_KEY = "other-changes";
    public static final String BALANCE_ROUTING_KEY = "balance";
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
    public Queue queueAccountBalanceChanges() {
        return new Queue("queue-account-balance-changes");
    }

    @Bean
    public Queue queueAccountOtherChanges() {
        return new Queue("queue-account-other-changes");
    }

    @Bean
    public Queue queueAccountBalanceChangeNotTag() { return new Queue("queue-account-balance-change-not-tag"); }



    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }



    @Bean
    public Binding bindingBalance() {
        return BindingBuilder.bind(queueAccountBalanceChanges()).to(topicExchange()).with(BALANCE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingQueueAccountBalanceChangeNotTag() {
        return BindingBuilder.bind(queueAccountBalanceChangeNotTag()).to(topicExchange()).with(BALANCE_NOT_TAG_ROUTING_KEY);
    }

    @Bean
    public Binding bindingQueueAccountOtherChanges() {
        return BindingBuilder.bind(queueAccountOtherChanges()).to(topicExchange()).with(OTHER_CHANGES_ROUTING_KEY);
    }

}
