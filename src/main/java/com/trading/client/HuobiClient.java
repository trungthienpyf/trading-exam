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
public class HuobiClient {

    private final RestTemplate restTemplate;

    public HuobiClient() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, BigDecimal> getPrice(TradingPair tradingPair) {
        try {

            String url = "https://api.huobi.pro/market/tickers" + "?symbol=" + tradingPair.name();

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
            log.error("Error fetching price from Huobi for {}: {}", tradingPair, e.getMessage());
        }
        return null;
    }
}
