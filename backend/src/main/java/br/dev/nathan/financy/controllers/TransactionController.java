package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.config.JWTUserData;
import br.dev.nathan.financy.dtos.request.TransactionRequest;
import br.dev.nathan.financy.dtos.response.TransactionResponse;
import br.dev.nathan.financy.dtos.response.error.BodyErrorResponse;
import br.dev.nathan.financy.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/financy/v1/transactions")
@Tag(name = "Transaction", description = "Manages the transactions of the authenticated user.")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
        summary = "Get transactions."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success!"),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@AuthenticationPrincipal JWTUserData userData) {

        UUID userId = userData.userId();

        List<TransactionResponse> transactions = transactionService.getTransactions(userId);

        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }


    @Operation(
        summary = "Create transaction."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created!"),
        @ApiResponse(
            responseCode = "400",
            description = "Some of the fields are invalid.",
            content = @Content(schema = @Schema(implementation = BodyErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@AuthenticationPrincipal JWTUserData userData,
                                                  @Valid @RequestBody TransactionRequest request) {

        UUID userId = userData.userId();

        TransactionResponse transactionCreated = transactionService.createTransaction(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionCreated);
    }

    @Operation(
        summary = "Get transaction by Id."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success!"),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@AuthenticationPrincipal JWTUserData userData,
                                                                  @PathVariable Long id) {

        UUID userId = userData.userId();

        TransactionResponse transaction = transactionService.getTransactionById(userId, id);

        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @Operation(
        summary = "Delete transaction by Id."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content."),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionById(@AuthenticationPrincipal JWTUserData userData,
                                                      @PathVariable Long id) {

        UUID userId = userData.userId();

        transactionService.deleteTransactionById(userId, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
        summary = "Update transaction."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success!"),
        @ApiResponse(
            responseCode = "400",
            description = "Some of the fields are invalid.",
            content = @Content(schema = @Schema(implementation = BodyErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id,
                                                  @AuthenticationPrincipal JWTUserData userData,
                                                  @Valid @RequestBody TransactionRequest request) {

        UUID userId = userData.userId();

        TransactionResponse transactionUpdated = transactionService.updateTransaction(userId, request, id);

        return ResponseEntity.status(HttpStatus.OK).body(transactionUpdated);
    }
}
