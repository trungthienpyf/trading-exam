package com.trading.controller;

import com.trading.dto.TransactionResponse;
import com.trading.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionHistoryService transactionHistoryService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable Long userId) {

        List<TransactionResponse> response = transactionHistoryService.getTransactionHistory(userId);

        return ResponseEntity.ok(response);
    }
}
