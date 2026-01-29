package com.trading.service;

import com.trading.dto.WalletBalanceResponse;
import com.trading.entity.User;
import com.trading.exception.UserNotFoundException;
import com.trading.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public WalletBalanceResponse getWalletBalance(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return new WalletBalanceResponse(
                user.getId(),
                user.getUsername(),
                user.getUsdtBalance(),
                user.getEthBalance(),
                user.getBtcBalance()
        );
    }
}
