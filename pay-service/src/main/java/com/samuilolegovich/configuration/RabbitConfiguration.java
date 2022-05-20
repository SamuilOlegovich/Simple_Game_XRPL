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
    public Queue queueAccountBalanceChangesTest() {
        return new Queue("queue-account-balance-changes-test");
    }

    @Bean
    public Queue queueAccountOtherChangesTest() {
        return new Queue("queue-account-other-changes-test");
    }

    @Bean
    public Queue queueAccountBalanceChangeNotTagTest() { return new Queue("queue-account-balance-change-not-tag-test"); }



    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(StringEnum.TOPIC_EXCHANGE.getValue(), true, false);
    }



    @Bean
    public Binding bindingBalance() {
        return BindingBuilder.bind(queueAccountBalanceChanges()).to(topicExchange()).with(StringEnum.BALANCE_ROUTING_KEY.getValue());
    }

    @Bean
    public Binding bindingQueueAccountBalanceChangeNotTag() {
        return BindingBuilder.bind(queueAccountBalanceChangeNotTag()).to(topicExchange()).with(StringEnum.BALANCE_NOT_TAG_ROUTING_KEY.getValue());
    }

    @Bean
    public Binding bindingQueueAccountOtherChanges() {
        return BindingBuilder.bind(queueAccountOtherChanges()).to(topicExchange()).with(StringEnum.OTHER_CHANGES_ROUTING_KEY.getValue());
    }

    @Bean
    public Binding bindingBalanceTest() {
        return BindingBuilder.bind(queueAccountBalanceChangesTest()).to(topicExchange()).with(StringEnum.BALANCE_ROUTING_KEY_TEST.getValue());
    }

    @Bean
    public Binding bindingQueueAccountBalanceChangeNotTagTest() {
        return BindingBuilder.bind(queueAccountBalanceChangeNotTagTest()).to(topicExchange()).with(StringEnum.BALANCE_NOT_TAG_ROUTING_KEY_TEST.getValue());
    }

    @Bean
    public Binding bindingQueueAccountOtherChangesTest() {
        return BindingBuilder.bind(queueAccountOtherChangesTest()).to(topicExchange()).with(StringEnum.OTHER_CHANGES_ROUTING_KEY_TEST.getValue());
    }

}
