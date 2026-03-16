package br.dev.nathan.financy.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotEmpty(message = "Email is required.")
    @Size(max = 300, message = "The maximum email size is 300 characters.")
    String email,

    @NotEmpty(message = "A password is required.")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
    String password
) {}
