package br.dev.nathan.financy.services;

import br.dev.nathan.financy.dtos.request.TransactionRequest;
import br.dev.nathan.financy.dtos.response.TransactionResponse;
import br.dev.nathan.financy.dtos.response.dashboard.TransactionDTO;
import br.dev.nathan.financy.entities.Category;
import br.dev.nathan.financy.entities.Transaction;
import br.dev.nathan.financy.entities.User;
import br.dev.nathan.financy.exceptions.ResourceOwnershipException;
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

    public List<TransactionResponse> getTransactions(UUID userId) {
        return transactionRepository.findByUserIdOrderByDateDescIdDesc(userId)
            .stream()
            .map(entity -> new TransactionResponse(entity))
            .toList();
    }

    public TransactionResponse createTransaction(UUID userId, TransactionRequest request) {

        User user = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findByIdAndUserId(request.categoryId(), userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        Transaction transaction = new Transaction(request, category, user, null);

        Transaction transactionCreated = transactionRepository.save(transaction);

        return new TransactionResponse(transactionCreated);
    }

    public TransactionResponse getTransactionById(UUID userId, Long id) {

        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        TransactionResponse response = new TransactionResponse(transaction);

        return response;
    }

    public void deleteTransactionById(UUID userId, Long id) {

        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        transactionRepository.delete(transaction);
    }

    public TransactionResponse updateTransaction(UUID userId, TransactionRequest request, Long id) {

        Category category = categoryRepository.findByIdAndUserId(request.categoryId(), userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        transaction.setDescription(request.description());
        transaction.setDate(request.date());
        transaction.setValue(request.value());
        transaction.setIncome(request.income());
        transaction.setCategory(category);

        Transaction transactionUpdated = transactionRepository.save(transaction);

        return new TransactionResponse(transactionUpdated);
    }
}
