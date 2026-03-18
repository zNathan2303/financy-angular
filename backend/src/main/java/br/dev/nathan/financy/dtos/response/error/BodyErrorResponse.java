package br.dev.nathan.financy.dtos.response.error;

import java.time.LocalDateTime;
import java.util.List;

public record BodyErrorResponse(
   Integer statusCode,
   String statusName,
   List<ErrorResponse> errors,
   String path,
   LocalDateTime timestamp
) {}
