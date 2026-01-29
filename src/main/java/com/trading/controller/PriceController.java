package com.trading.controller;

import com.trading.dto.PriceResponse;
import com.trading.entity.Price;
import com.trading.enums.TradingPair;
import com.trading.exception.PriceNotFoundException;
import com.trading.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceRepository priceRepository;

    @GetMapping("/{tradingPair}")
    public ResponseEntity<PriceResponse> getLatestPrice(@PathVariable TradingPair tradingPair) {

        Price price = priceRepository.findByTradingPair(tradingPair)
                .orElseThrow(() -> new PriceNotFoundException("Price not found for " + tradingPair));

        PriceResponse response = new PriceResponse(
                price.getTradingPair(),
                price.getBidPrice(),
                price.getAskPrice(),
                price.getUpdatedAt()
        );

        return ResponseEntity.ok(response);
    }
}
