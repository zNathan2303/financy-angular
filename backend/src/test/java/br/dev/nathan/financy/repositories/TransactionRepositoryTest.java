package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.dtos.request.CategoryRequest;
import br.dev.nathan.financy.dtos.request.RegisterUserRequest;
import br.dev.nathan.financy.dtos.request.TransactionRequest;
import br.dev.nathan.financy.dtos.response.dashboard.TransactionDTO;
import br.dev.nathan.financy.entities.Category;
import br.dev.nathan.financy.entities.Transaction;
import br.dev.nathan.financy.entities.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should return a maximum of 5 transactions ordered by most recent")
    void findTopRecentCase1() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        CategoryRequest categoryRequest = new CategoryRequest(
            "Alimentação",
            "Refeições, mercado e delivery",
            "orange",
            "utensils"
        );
        Category category = createCategory(categoryRequest, user);

        Long categoryId = category.getId();

        List<TransactionRequest> requests = List.of(
            new TransactionRequest("Salário", LocalDate.of(2026, 3, 1), new BigDecimal("5000.00"), true, categoryId),
            new TransactionRequest("Mercado", LocalDate.of(2026, 3, 2), new BigDecimal("250.50"), false, categoryId),
            new TransactionRequest("Freelance", LocalDate.of(2026, 3, 5), new BigDecimal("1200.00"), true, categoryId),
            new TransactionRequest("Restaurante", LocalDate.of(2026, 3, 6), new BigDecimal("80.75"), false, categoryId),
            new TransactionRequest("Investimento", LocalDate.of(2026, 3, 10), new BigDecimal("2000.00"), true, categoryId),
            new TransactionRequest("Transporte", LocalDate.of(2026, 3, 12), new BigDecimal("150.00"), false, categoryId),
            new TransactionRequest("Bônus", LocalDate.of(2026, 3, 15), new BigDecimal("500.00"), true, categoryId),
            new TransactionRequest("Cinema", LocalDate.of(2026, 3, 18), new BigDecimal("60.00"), false, categoryId)
        );

        for (TransactionRequest request : requests) {
            createTransaction(request, user);
        }

        List<TransactionDTO> result = transactionRepository.findTopRecent(user.getId(), PageRequest.of(0, 5));

        assertEquals(5, result.size());

        LocalDate previousDate = result.getFirst().date();

        for (TransactionDTO transaction : result) {
            assertFalse(transaction.date().isAfter(previousDate),
                "Transactions are not ordered correctly");
            previousDate = transaction.date();
        }
    }

    @Test
    @DisplayName("Should return an empty list")
    void findTopRecentCase2() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        List<TransactionDTO> result = transactionRepository.findTopRecent(user.getId(), PageRequest.of(0, 5));

        assertTrue(result.isEmpty(), "The list should be empty for a user with no transactions");
    }

    @Test
    @DisplayName("Should return the correct sum of monthly income")
    void sumMonthlyIncomeCase1() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        CategoryRequest categoryRequest = new CategoryRequest(
            "Renda",
            "Entradas de dinheiro",
            "green",
            "money"
        );
        Category category = createCategory(categoryRequest, user);

        Long categoryId = category.getId();

        List<TransactionRequest> requests = List.of(
            new TransactionRequest("Salário", LocalDate.of(2026, 3, 1), new BigDecimal("5000.00"), true, categoryId),
            new TransactionRequest("Freelance", LocalDate.of(2026, 3, 5), new BigDecimal("1200.00"), true, categoryId),
            new TransactionRequest("Mercado", LocalDate.of(2026, 3, 10), new BigDecimal("300.00"), false, categoryId),
            new TransactionRequest("Bônus", LocalDate.of(2026, 4, 1), new BigDecimal("800.00"), true, categoryId)
        );

        for (TransactionRequest request : requests) {
            createTransaction(request, user);
        }

        BigDecimal result = transactionRepository.sumMonthlyIncome(
            user.getId(), 3, 2026
        );

        assertEquals(new BigDecimal("6200.00"), result);
    }

    @Test
    @DisplayName("Should return zero when no income is found for the month")
    void sumMonthlyIncomeCase2() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        BigDecimal result = transactionRepository.sumMonthlyIncome(
            user.getId(), 3, 2026
        );

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should return the correct sum of monthly expenses")
    void sumMonthlyExpensesCase1() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        CategoryRequest categoryRequest = new CategoryRequest(
            "Despesas",
            "Saídas de dinheiro",
            "red",
            "credit-card"
        );
        Category category = createCategory(categoryRequest, user);

        Long categoryId = category.getId();

        List<TransactionRequest> requests = List.of(
            new TransactionRequest("Mercado", LocalDate.of(2026, 3, 2), new BigDecimal("250.50"), false, categoryId),
            new TransactionRequest("Restaurante", LocalDate.of(2026, 3, 6), new BigDecimal("80.75"), false, categoryId),
            new TransactionRequest("Salário", LocalDate.of(2026, 3, 1), new BigDecimal("5000.00"), true, categoryId),
            new TransactionRequest("Transporte", LocalDate.of(2026, 4, 1), new BigDecimal("150.00"), false, categoryId)
        );

        for (TransactionRequest request : requests) {
            createTransaction(request, user);
        }

        BigDecimal result = transactionRepository.sumMonthlyExpenses(
            user.getId(), 3, 2026
        );

        // 250.50 + 80.75 = 331.25
        assertEquals(0, result.compareTo(new BigDecimal("331.25")));
    }

    @Test
    @DisplayName("Should return zero when no expenses are found for the month")
    void sumMonthlyExpensesCase2() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        BigDecimal result = transactionRepository.sumMonthlyExpenses(
            user.getId(), 3, 2026
        );

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should return the correct total balance (income - expenses)")
    void getTotalBalanceCase1() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        CategoryRequest categoryRequest = new CategoryRequest(
            "Geral",
            "Categoria padrão",
            "blue",
            "wallet"
        );
        Category category = createCategory(categoryRequest, user);

        Long categoryId = category.getId();

        List<TransactionRequest> requests = List.of(
            // Income
            new TransactionRequest("Salário", LocalDate.of(2026, 3, 1), new BigDecimal("5000.00"), true, categoryId),
            new TransactionRequest("Freelance", LocalDate.of(2026, 3, 5), new BigDecimal("1200.00"), true, categoryId),

            // Expenses
            new TransactionRequest("Mercado", LocalDate.of(2026, 3, 10), new BigDecimal("300.00"), false, categoryId),
            new TransactionRequest("Restaurante", LocalDate.of(2026, 3, 12), new BigDecimal("100.00"), false, categoryId)
        );

        for (TransactionRequest request : requests) {
            createTransaction(request, user);
        }

        BigDecimal result = transactionRepository.getTotalBalance(user.getId());

        // 5000 + 1200 - 300 - 100 = 5800
        assertEquals(0, result.compareTo(new BigDecimal("5800.00")));
    }

    @Test
    @DisplayName("Should return zero when user has no transactions")
    void getTotalBalanceCase2() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        BigDecimal result = transactionRepository.getTotalBalance(user.getId());

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should return negative balance when only expenses exist")
    void getTotalBalanceCase3() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        CategoryRequest categoryRequest = new CategoryRequest(
            "Despesas",
            "Saídas",
            "red",
            "credit-card"
        );
        Category category = createCategory(categoryRequest, user);

        Long categoryId = category.getId();

        List<TransactionRequest> requests = List.of(
            new TransactionRequest("Mercado", LocalDate.of(2026, 3, 10), new BigDecimal("300.00"), false, categoryId),
            new TransactionRequest("Transporte", LocalDate.of(2026, 3, 12), new BigDecimal("200.00"), false, categoryId)
        );

        for (TransactionRequest request : requests) {
            createTransaction(request, user);
        }

        BigDecimal result = transactionRepository.getTotalBalance(user.getId());

        // -(300 + 200) = -500
        assertEquals(0, result.compareTo(new BigDecimal("-500.00")));
    }

    private User createUser(RegisterUserRequest request) {
        User user = new User(request);
        entityManager.persist(user);
        return user;
    }

    private Transaction createTransaction(TransactionRequest request, User user) {

        Category category = categoryRepository.findById(request.categoryId()).orElseThrow();
        Transaction transaction = new Transaction(request, category, user, null);

        entityManager.persist(transaction);

        return transaction;
    }

    private Category createCategory(CategoryRequest request, User user) {
        Category category = new Category(request, user, null);
        entityManager.persist(category);
        return category;
    }
}