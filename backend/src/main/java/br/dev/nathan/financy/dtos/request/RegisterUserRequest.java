package br.dev.nathan.financy.dtos.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(

    @NotEmpty(message = "Name is required.")
    String name,

    @NotEmpty(message = "Email is required.")
    String email,

    @NotEmpty(message = "A password is required.")
    String password
) {}
