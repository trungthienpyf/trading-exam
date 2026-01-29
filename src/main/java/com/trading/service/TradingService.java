package com.trading.service;

import com.trading.dto.TradeRequest;
import com.trading.dto.TradeResponse;
import com.trading.entity.Price;
import com.trading.entity.Transaction;
import com.trading.entity.User;
import com.trading.enums.TransactionType;
import com.trading.enums.TradingPair;
import com.trading.exception.InsufficientBalanceException;
import com.trading.exception.PriceNotFoundException;
import com.trading.exception.UserNotFoundException;
import com.trading.repository.PriceRepository;
import com.trading.repository.TransactionRepository;
import com.trading.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradingService {

    private final UserRepository userRepository;
    private final PriceRepository priceRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TradeResponse trade(TradeRequest request) {
        log.info("Executing trade: {}", request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getUserId()));

        Price price = priceRepository.findByTradingPair(request.getTradingPair())
                .orElseThrow(() -> new PriceNotFoundException("Price not found for " + request.getTradingPair()));

        BigDecimal executionPrice = request.getType() == TransactionType.BUY
                ? price.getAskPrice()
                : price.getBidPrice();

        // cal USDT
        BigDecimal total = request.getAmount().multiply(executionPrice);

        if (request.getType() == TransactionType.BUY) {
            executeBuy(user, request.getTradingPair(), request.getAmount(), total);
        } else {
            executeSell(user, request.getTradingPair(), request.getAmount(), total);
        }


        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setTradingPair(request.getTradingPair());
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setPrice(executionPrice);
        transaction.setTotalUsdt(total);


        transaction = transactionRepository.save(transaction);
        userRepository.save(user);

        log.info("Trade executed successfully: {} {} {} at price {}",
                request.getType(), request.getAmount(), request.getTradingPair(), executionPrice);

        return new TradeResponse(
                transaction.getId(),
                true,
                "Trade executed successfully",
                executionPrice,
                total
        );
    }

    private void executeBuy(User user, TradingPair tradingPair, BigDecimal amount, BigDecimal totalUsdt) {
        if (user.getUsdtBalance().compareTo(totalUsdt) < 0) {
            throw new InsufficientBalanceException("Insufficient USDT balance. Required: "
                    + totalUsdt + ", Available: " + user.getUsdtBalance());
        }

        // Deduct USDT
        user.setUsdtBalance(user.getUsdtBalance().subtract(totalUsdt));

        if (tradingPair == TradingPair.ETHUSDT) {
            user.setEthBalance(user.getEthBalance().add(amount));
        } else if (tradingPair == TradingPair.BTCUSDT) {
            user.setBtcBalance(user.getBtcBalance().add(amount));
        }
    }

    private void executeSell(User user, TradingPair tradingPair, BigDecimal amount, BigDecimal totalUsdt) {
        BigDecimal currentBalance = tradingPair == TradingPair.ETHUSDT
                ? user.getEthBalance()
                : user.getBtcBalance();

        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient " +
                    tradingPair + " balance. Required: " + amount + ", Available: " + currentBalance);
        }

        if (tradingPair == TradingPair.ETHUSDT) {
            user.setEthBalance(user.getEthBalance().subtract(amount));
        } else if (tradingPair == TradingPair.BTCUSDT) {
            user.setBtcBalance(user.getBtcBalance().subtract(amount));
        }

        user.setUsdtBalance(user.getUsdtBalance().add(totalUsdt));
    }
}
