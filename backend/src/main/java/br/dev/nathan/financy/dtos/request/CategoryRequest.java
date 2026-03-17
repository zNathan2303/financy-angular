package br.dev.nathan.financy.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

    @NotEmpty(message = "Title is required.")
    @Size(max = 50, message = "The maximum title size is 50 characters.")
    String title,

    @Size(max = 100, message = "The maximum description size is 100 characters.")
    String description,

    @NotEmpty(message = "Color is required.")
    @Size(max = 50, message = "The maximum color size is 50 characters.")
    String color,

    @NotEmpty(message = "Icon is required.")
    @Size(max = 100, message = "The maximum icon size is 100 characters.")
    String icon
) {}
