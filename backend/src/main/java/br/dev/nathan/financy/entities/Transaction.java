package br.dev.nathan.financy.entities;

import br.dev.nathan.financy.dtos.request.TransactionRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private Boolean income;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Transaction(TransactionRequest request, Category category, User user, Long id) {
        this.id = id;
        this.description = request.description();
        this.date = request.date();
        this.value = request.value();
        this.income = request.income();
        this.category = category;
        this.user = user;
    }
}
