package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop5ByUserIdOrderByIdDesc(UUID userId);
}
