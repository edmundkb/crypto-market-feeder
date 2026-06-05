package com.crypto.market.feeder;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MarketDataPublisher {

    private final RabbitTemplate rabbitTemplate;

    public MarketDataPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTicker(String symbol, Object pricePayload) {
        String routingKey = "market.data." + symbol.toLowerCase();

        rabbitTemplate.convertAndSend(
                RabbitConfig.MARKET_DATA_EXCHANGE,
                routingKey,
                pricePayload
        );

        System.out.println("Published to " + routingKey);
    }
}