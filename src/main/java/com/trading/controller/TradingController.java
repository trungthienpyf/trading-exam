package com.trading.controller;

import com.trading.dto.TradeRequest;
import com.trading.dto.TradeResponse;
import com.trading.service.TradingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradingController {
    
    private final TradingService tradingService;
    
    @PostMapping
    public ResponseEntity<TradeResponse> executeTrade(@Valid @RequestBody TradeRequest request) {

        TradeResponse response = tradingService.trade(request);
        
        return ResponseEntity.ok(response);
    }
}
