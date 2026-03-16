package br.dev.nathan.financy.dtos.response;

import br.dev.nathan.financy.entities.Category;

public record CategoryResponse(
    Long id,
    String title,
    String description,
    String color,
    String icon
) {
    public CategoryResponse(Category category) {
        this(
            category.getId(),
            category.getTitle(),
            category.getDescription(),
            category.getColor(),
            category.getIcon()
        );
    }
}
