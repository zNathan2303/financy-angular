package br.dev.nathan.financy.services;

import br.dev.nathan.financy.entities.Transaction;
import br.dev.nathan.financy.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getRecentTransactionsFromAUser(UUID userId) {
        return transactionRepository.findTop5ByUserIdOrderByIdDesc(userId);
    }
}
