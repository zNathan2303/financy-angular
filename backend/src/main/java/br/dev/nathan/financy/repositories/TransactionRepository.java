package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.dtos.response.dashboard.TransactionDTO;
import br.dev.nathan.financy.entities.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
        SELECT new br.dev.nathan.financy.dtos.response.dashboard.TransactionDTO(
            t.id,
            t.description,
            t.date,
            t.value,
            t.income,
            new br.dev.nathan.financy.dtos.response.dashboard.TransactionCategoryDTO(
                c.id,
                c.title,
                c.color,
                c.icon
            )
        )
        FROM Transaction t
        LEFT JOIN t.category c
        WHERE t.user.id = :userId
        ORDER BY t.date DESC
    """)
    List<TransactionDTO> findTopRecent(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(t.value), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
          AND t.income = true
          AND MONTH(t.date) = :month
          AND YEAR(t.date) = :year
    """)
    BigDecimal sumMonthlyIncome(UUID userId, int month, int year);

    @Query("""
        SELECT COALESCE(SUM(t.value), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
          AND t.income = false
          AND MONTH(t.date) = :month
          AND YEAR(t.date) = :year
    """)
    BigDecimal sumMonthlyExpenses(UUID userId, int month, int year);

    @Query("""
        SELECT COALESCE(SUM(
            CASE
                WHEN t.income = true THEN t.value
                ELSE -t.value
            END
        ), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
    """)
    BigDecimal getTotalBalance(UUID userId);
}
