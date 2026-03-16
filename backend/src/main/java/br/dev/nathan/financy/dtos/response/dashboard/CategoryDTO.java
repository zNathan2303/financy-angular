package br.dev.nathan.financy.dtos.response.dashboard;

import java.math.BigDecimal;

public record CategoryDTO(
    Long id,
    String title,
    String color,
    Long itemsCount,
    BigDecimal totalValue
) {}
