package br.dev.nathan.financy.dtos.response.dashboard;

public record TransactionCategoryDTO(
    Long id,
    String title,
    String color,
    String icon
) {}
