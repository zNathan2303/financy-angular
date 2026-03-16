package br.dev.nathan.financy.dtos.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
    BigDecimal totalBalance,
    BigDecimal monthlyIncome,
    BigDecimal monthlyExpenses,
    List<TransactionDTO> recentTransactions,
    List<CategoryDTO> topCategories
) {}
