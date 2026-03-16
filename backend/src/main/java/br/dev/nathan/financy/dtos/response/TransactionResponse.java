package br.dev.nathan.financy.dtos.response;

import br.dev.nathan.financy.entities.Category;
import br.dev.nathan.financy.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
    Long id,
    String description,
    LocalDate date,
    BigDecimal value,
    Boolean income,
    CategoryResponse category
) {
    public TransactionResponse(Transaction transaction) {
        this(
            transaction.getId(),
            transaction.getDescription(),
            transaction.getDate(),
            transaction.getValue(),
            transaction.getIncome(),
            new CategoryResponse(transaction.getCategory())
        );
    }
}
