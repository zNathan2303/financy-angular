package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.dtos.request.CategoryRequest;
import br.dev.nathan.financy.dtos.request.RegisterUserRequest;
import br.dev.nathan.financy.dtos.request.TransactionRequest;
import br.dev.nathan.financy.dtos.response.CategoryWithDetailsResponse;
import br.dev.nathan.financy.dtos.response.dashboard.CategoryDTO;
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
class CategoryRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should return top categories ordered by total value desc with correct count and sum")
    void findTopCase1() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        Category food = createCategory(new CategoryRequest(
            "Alimentação", "", "orange", "utensils"
        ), user);

        Category transport = createCategory(new CategoryRequest(
            "Transporte", "", "blue", "car"
        ), user);

        Category leisure = createCategory(new CategoryRequest(
            "Lazer", "", "purple", "gamepad"
        ), user);

        List<TransactionRequest> transactions = List.of(
            new TransactionRequest("Mercado", LocalDate.now(), new BigDecimal("200"), false, food.getId()),
            new TransactionRequest("Restaurante", LocalDate.now(), new BigDecimal("100"), false, food.getId()),
            new TransactionRequest("Uber", LocalDate.now(), new BigDecimal("150"), false, transport.getId()),
            new TransactionRequest("Cinema", LocalDate.now(), new BigDecimal("50"), false, leisure.getId())
        );

        for (TransactionRequest request : transactions) {
            createTransaction(request, user);
        }

        List<CategoryDTO> result =
            categoryRepository.findTop(user.getId(), PageRequest.of(0, 5));

        assertEquals(3, result.size());

        assertEquals("Alimentação", result.get(0).title());
        assertEquals("Transporte", result.get(1).title());
        assertEquals("Lazer", result.get(2).title());

        assertEquals(2L, result.get(0).itemsCount());
        assertEquals(0, result.get(0).totalValue().compareTo(new BigDecimal("300")));

        assertEquals(1L, result.get(1).itemsCount());
        assertEquals(0, result.get(1).totalValue().compareTo(new BigDecimal("150")));

        assertEquals(1L, result.get(2).itemsCount());
        assertEquals(0, result.get(2).totalValue().compareTo(new BigDecimal("50")));
    }

    @Test
    @DisplayName("Should return categories with correct transaction count ordered by title")
    void findCategoriesWithTransactionCountCase1() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        CategoryRequest foodRequest = new CategoryRequest(
            "Alimentação",
            "Gastos com comida",
            "orange",
            "utensils"
        );

        CategoryRequest salaryRequest = new CategoryRequest(
            "Salário",
            "Entradas de salário",
            "green",
            "wallet"
        );

        Category food = createCategory(foodRequest, user);
        Category salary = createCategory(salaryRequest, user);

        List<TransactionRequest> transactions = List.of(
            new TransactionRequest("Mercado", LocalDate.of(2026, 3, 1), new BigDecimal("200"), false, food.getId()),
            new TransactionRequest("Restaurante", LocalDate.of(2026, 3, 2), new BigDecimal("80"), false, food.getId()),
            new TransactionRequest("Pagamento", LocalDate.of(2026, 3, 5), new BigDecimal("5000"), true, salary.getId())
        );

        for (TransactionRequest request : transactions) {
            createTransaction(request, user);
        }

        List<CategoryWithDetailsResponse> result =
            categoryRepository.findCategoriesWithTransactionCount(user.getId());

        assertEquals(2, result.size());

        assertEquals("Alimentação", result.get(0).title());
        assertEquals("Salário", result.get(1).title());

        assertEquals(2L, result.get(0).itemsCount());
        assertEquals(1L, result.get(1).itemsCount());
    }

    @Test
    @DisplayName("Should return zero transaction count for categories without transactions")
    void findCategoriesWithTransactionCountCase2() {

        RegisterUserRequest userRequest = new RegisterUserRequest(
            "Nathan",
            "nathan@example.com",
            "minhaSenhaSegura123"
        );
        User user = createUser(userRequest);

        CategoryRequest categoryRequest = new CategoryRequest(
            "Lazer",
            "Gastos com entretenimento",
            "purple",
            "gamepad"
        );

        createCategory(categoryRequest, user);

        List<CategoryWithDetailsResponse> result =
            categoryRepository.findCategoriesWithTransactionCount(user.getId());

        assertEquals(1, result.size());
        assertEquals(0L, result.getFirst().itemsCount());
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