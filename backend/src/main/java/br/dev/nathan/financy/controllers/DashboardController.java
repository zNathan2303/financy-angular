package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.config.JWTUserData;
import br.dev.nathan.financy.dtos.response.dashboard.DashboardResponse;
import br.dev.nathan.financy.services.CategoryService;
import br.dev.nathan.financy.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/financy/v1/dashboard")
@Tag(name = "Dashboard", description = "Manage the data on the Dashboard screen.")
public class DashboardController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public DashboardController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "Get data from the Dashboard screen."
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
    public ResponseEntity<DashboardResponse> getDashboardData(@AuthenticationPrincipal JWTUserData userData) {

        UUID userId = userData.userId();

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
