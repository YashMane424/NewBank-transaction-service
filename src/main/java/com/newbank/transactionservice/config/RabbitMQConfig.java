package com.newbank.transactionservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TXN_EXCHANGE = "transaction-exchange";
    public static final String TRANSACTION_EVENTS_QUEUE = "transaction-events-queue";
    public static final String DLQ = "transaction-events-dlq";

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(TXN_EXCHANGE, true, false);
    }

    @Bean
    public Queue transactionEventsQueue() {
        return QueueBuilder.durable(TRANSACTION_EVENTS_QUEUE)
                .withArgument("x-dead-letter-exchange", TXN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "transaction.dlq")
                .build();
    }

    @Bean
    public Queue transactionDLQ() {
        return new Queue(DLQ, true);
    }

    @Bean
    public Binding transactionBinding(Queue transactionEventsQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionEventsQueue).to(transactionExchange).with("transaction.*");
    }

    @Bean
    public Binding transactionDLQBinding(Queue transactionDLQ, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionDLQ).to(transactionExchange).with("transaction.dlq");
    }
}
