package com.trading.controller;

import com.trading.dto.WalletBalanceResponse;
import com.trading.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(@PathVariable Long userId) {

        WalletBalanceResponse response = walletService.getWalletBalance(userId);

        return ResponseEntity.ok(response);
    }
}
