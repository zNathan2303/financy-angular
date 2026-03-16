package br.dev.nathan.financy.services;

import br.dev.nathan.financy.dtos.response.TransactionResponse;
import br.dev.nathan.financy.dtos.response.dashboard.TransactionDTO;
import br.dev.nathan.financy.entities.Transaction;
import br.dev.nathan.financy.repositories.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionDTO> getRecentTransactions(UUID userId) {
        return transactionRepository.findTopRecent(userId, PageRequest.of(0, 5));
    }

    public BigDecimal getMonthlyIncome(UUID userId) {
        LocalDate now = LocalDate.now();

        return transactionRepository.sumMonthlyIncome(
            userId,
            now.getMonthValue(),
            now.getYear()
        );
    }

    public BigDecimal getMonthlyExpenses(UUID userId) {
        LocalDate now = LocalDate.now();

        return transactionRepository.sumMonthlyExpenses(
            userId,
            now.getMonthValue(),
            now.getYear()
        );
    }

    public BigDecimal getTotalBalance(UUID userId) {
        return transactionRepository.getTotalBalance(userId);
    }

    public List<TransactionResponse> getTransactionsByUserId(UUID userId) {
        return transactionRepository.findByUserIdOrderByIdDesc(userId)
            .stream()
            .map(entity -> new TransactionResponse(entity))
            .toList();
    }
}
