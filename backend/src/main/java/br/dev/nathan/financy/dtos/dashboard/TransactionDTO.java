package br.dev.nathan.financy.dtos.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(
    Long id,
    String description,
    LocalDate date,
    BigDecimal value,
    Boolean income,
    TransactionCategoryDTO category
) {}
