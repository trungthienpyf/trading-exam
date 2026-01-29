package com.trading.scheduler;

import com.trading.client.BinanceClient;
import com.trading.client.HuobiClient;
import com.trading.entity.Price;
import com.trading.enums.TradingPair;
import com.trading.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceAggregationJobService {
    
    private final BinanceClient binanceClient;
    private final HuobiClient huobiClient;
    private final PriceRepository priceRepository;
    
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void aggregatePrices() {
        for (TradingPair tradingPair : TradingPair.values()) {
            try {
                aggregatePriceForPair(tradingPair);
            } catch (Exception e) {
                log.error("Error aggregating price {}: {}", tradingPair, e.getMessage());
            }
        }
    }
    
    private void aggregatePriceForPair(TradingPair tradingPair) {
        Map<String, BigDecimal> binancePrices = binanceClient.getPrice(tradingPair);
        Map<String, BigDecimal> huobiPrices = huobiClient.getPrice(tradingPair);
        
        BigDecimal bestBid = null;
        BigDecimal bestAsk = null;
        
        if (binancePrices != null && huobiPrices != null) {
            bestBid = binancePrices.get("bid").max(huobiPrices.get("bid"));
            bestAsk = binancePrices.get("ask").min(huobiPrices.get("ask"));
        } else if (binancePrices != null) {
            bestBid = binancePrices.get("bid");
            bestAsk = binancePrices.get("ask");
        } else if (huobiPrices != null) {
            bestBid = huobiPrices.get("bid");
            bestAsk = huobiPrices.get("ask");
        }
        
        if (bestBid != null && bestAsk != null) {
            Price price = priceRepository.findByTradingPair(tradingPair)
                .orElse(new Price());
            
            price.setTradingPair(tradingPair);
            price.setBidPrice(bestBid);
            price.setAskPrice(bestAsk);

            priceRepository.save(price);
            log.info("pair {} - best bid: {}, best ask: {}",
                tradingPair, bestBid, bestAsk);
        }
    }
}
