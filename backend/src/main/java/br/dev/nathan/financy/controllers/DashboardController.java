package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.dtos.dashboard.DashboardResponse;
import br.dev.nathan.financy.services.CategoryService;
import br.dev.nathan.financy.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/financy/v1/dashboard")
public class DashboardController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public DashboardController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DashboardResponse> getDashboardData(@PathVariable UUID userId) {
        DashboardResponse response = new DashboardResponse(
            transactionService.getTotalBalance(userId),
            transactionService.getMonthlyIncome(userId),
            transactionService.getMonthlyExpenses(userId),
            transactionService.getRecentTransactions(userId),
            categoryService.getTop5Categories(userId)
        );

        return ResponseEntity.ok(response);
    }
}
