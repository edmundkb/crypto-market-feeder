package com.crypto.market.feeder.service.clients;

import com.crypto.market.feeder.MarketDataPublisher;
import com.crypto.alerts.market.feeder.model.TickerPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class CoinbaseFeederClient extends TextWebSocketHandler implements CommandLineRunner {

    private final MarketDataPublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CoinbaseFeederClient(MarketDataPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void run(String... args) throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();

        client.execute(this, "wss://ws-feed.exchange.coinbase.com");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected to Coinbase WebSocket! Sending subscription packet...");

        // Coinbase subscription format for public ticker channels
        String subscribeJson = """
            {
                "type": "subscribe",
                "product_ids": ["BTC-USD", "ETH-USD"],
                "channels": ["ticker"]
            }
            """;

        session.sendMessage(new TextMessage(subscribeJson));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        TickerPayload payload = objectMapper.readValue(message.getPayload(), TickerPayload.class);

        if (payload.productId() != null && payload.price() != null) {

            String symbol = payload.productId().split("-")[0].toLowerCase();

            publisher.publishTicker(symbol, payload);
        }
    }
}