package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.config.JWTUserData;
import br.dev.nathan.financy.dtos.request.TransactionRequest;
import br.dev.nathan.financy.dtos.response.TransactionResponse;
import br.dev.nathan.financy.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/financy/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactionsFromAUser(@AuthenticationPrincipal JWTUserData userData) {

        UUID userId = userData.userId();

        List<TransactionResponse> transactions = transactionService.getTransactionsByUserId(userId);

        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@AuthenticationPrincipal JWTUserData userData,
                                                  @Valid @RequestBody TransactionRequest request) {
        UUID userId = userData.userId();

        transactionService.createTransaction(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {

        TransactionResponse transaction = transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable Long transactionId) {

        transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(@PathVariable Long transactionId,
                                                  @AuthenticationPrincipal JWTUserData userData,
                                                  @Valid @RequestBody TransactionRequest request) {
        UUID userId = userData.userId();

        transactionService.updateTransaction(userId, request, transactionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
