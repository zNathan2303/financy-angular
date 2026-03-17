package br.dev.nathan.financy.dtos.response;

import br.dev.nathan.financy.entities.Category;

public record CategoryWithDetailsResponse(
    Long id,
    String title,
    String description,
    String color,
    String icon,
    Long itemsCount
) {
    public CategoryWithDetailsResponse(Category category, Long itemsCount) {
        this(
            category.getId(),
            category.getTitle(),
            category.getDescription(),
            category.getColor(),
            category.getIcon(),
            itemsCount
        );
    }
}
