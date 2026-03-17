package br.dev.nathan.financy.services;

import br.dev.nathan.financy.dtos.request.TransactionRequest;
import br.dev.nathan.financy.dtos.response.TransactionResponse;
import br.dev.nathan.financy.dtos.response.dashboard.TransactionDTO;
import br.dev.nathan.financy.entities.Category;
import br.dev.nathan.financy.entities.Transaction;
import br.dev.nathan.financy.entities.User;
import br.dev.nathan.financy.repositories.CategoryRepository;
import br.dev.nathan.financy.repositories.TransactionRepository;
import br.dev.nathan.financy.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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
        return transactionRepository.findByUserIdOrderByDateDescIdDesc(userId)
            .stream()
            .map(entity -> new TransactionResponse(entity))
            .toList();
    }

    public void createTransaction(UUID userId, TransactionRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();

        Transaction transaction = new Transaction(request, category, user, null);

        transactionRepository.save(transaction);
    }

    public TransactionResponse getTransactionById(Long transactionId) {

        Transaction transactionEntity = transactionRepository.findById(transactionId).orElseThrow();

        return new TransactionResponse(transactionEntity);
    }

    public void deleteTransactionById(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public void updateTransaction(UUID userId, TransactionRequest request, Long id) {
        User user = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();

        Transaction transaction = new Transaction(request, category, user, id);

        transactionRepository.save(transaction);
    }
}
