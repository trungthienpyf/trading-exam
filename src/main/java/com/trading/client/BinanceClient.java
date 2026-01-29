package com.trading.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.trading.enums.TradingPair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BinanceClient {

    private final RestTemplate restTemplate;

    public BinanceClient() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, BigDecimal> getPrice(TradingPair tradingPair) {
        try {
            String url = "https://api.binance.com/api/v3/ticker/bookTicker" + "?symbol=" + tradingPair.name();

            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null) {
                BigDecimal bidPrice = new BigDecimal(response.get("bidPrice").asText());
                BigDecimal askPrice = new BigDecimal(response.get("askPrice").asText());

                Map<String, BigDecimal> prices = new HashMap<>();
                prices.put("bid", bidPrice);
                prices.put("ask", askPrice);

                return prices;
            }
        } catch (Exception e) {
            log.error("Error fetching price from Binance for {}: {}", tradingPair, e.getMessage());

        }
        return null;
    }
}
