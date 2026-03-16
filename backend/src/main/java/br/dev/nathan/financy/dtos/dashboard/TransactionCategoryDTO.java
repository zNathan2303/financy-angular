package br.dev.nathan.financy.dtos.dashboard;

public record TransactionCategoryDTO(
    Long id,
    String title,
    String color,
    String icon
) {}
