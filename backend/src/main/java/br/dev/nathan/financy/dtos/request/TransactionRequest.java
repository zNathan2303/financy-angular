package br.dev.nathan.financy.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(

    @NotEmpty(message = "Description is required.")
    @Size(max = 100, message = "The maximum description size is 100 characters.")
    String description,

    LocalDate date,

    @NotNull(message = "Value is required.")
    BigDecimal value,

    @NotNull(message = "Income is required.")
    Boolean income,

    @NotNull(message = "CategoryId is required.")
    Long categoryId

) {}
