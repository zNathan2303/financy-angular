package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.services.TransactionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financy/v1/dashboard")
public class DashboardController {
    private final TransactionService transactionService;

    public DashboardController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
