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
    public ResponseEntity<List<TransactionResponse>> getTransactions(@AuthenticationPrincipal JWTUserData userData) {

        UUID userId = userData.userId();

        List<TransactionResponse> transactions = transactionService.getTransactions(userId);

        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@AuthenticationPrincipal JWTUserData userData,
                                                  @Valid @RequestBody TransactionRequest request) {

        UUID userId = userData.userId();

        TransactionResponse transactionCreated = transactionService.createTransaction(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@AuthenticationPrincipal JWTUserData userData,
                                                                  @PathVariable Long id) {

        UUID userId = userData.userId();

        TransactionResponse transaction = transactionService.getTransactionById(userId, id);

        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionById(@AuthenticationPrincipal JWTUserData userData,
                                                      @PathVariable Long id) {

        UUID userId = userData.userId();

        transactionService.deleteTransactionById(userId, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id,
                                                  @AuthenticationPrincipal JWTUserData userData,
                                                  @Valid @RequestBody TransactionRequest request) {

        UUID userId = userData.userId();

        TransactionResponse transactionUpdated = transactionService.updateTransaction(userId, request, id);

        return ResponseEntity.status(HttpStatus.OK).body(transactionUpdated);
    }
}
