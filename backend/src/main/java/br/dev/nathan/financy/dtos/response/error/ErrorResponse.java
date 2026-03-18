package br.dev.nathan.financy.dtos.response.error;

public record ErrorResponse(
   String field,
   String message
) {}
