package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.dtos.response.CategoryWithDetailsResponse;
import br.dev.nathan.financy.dtos.response.dashboard.CategoryDTO;
import br.dev.nathan.financy.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
        SELECT new br.dev.nathan.financy.dtos.response.dashboard.CategoryDTO(
            c.id,
            c.title,
            c.color,
            COUNT(t.id),
            COALESCE(SUM(t.value), 0)
        )
        FROM Category c
        LEFT JOIN Transaction t
            ON t.category.id = c.id
            AND t.user.id = :userId
        GROUP BY c.id, c.title, c.color
        ORDER BY
            COALESCE(SUM(t.value), 0) DESC,
            c.title ASC
    """)
    List<CategoryDTO> findTop(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT new br.dev.nathan.financy.dtos.response.CategoryWithDetailsResponse(
            c.id,
            c.title,
            c.description,
            c.color,
            c.icon,
            (
                SELECT COUNT(t)
                FROM Transaction t
                WHERE t.category = c
                    AND t.user.id = :userId
            )
        )
        FROM Category c
        WHERE c.user.id = :userId
        ORDER BY c.title ASC
    """)
    List<CategoryWithDetailsResponse> findCategoriesWithTransactionCount(@Param("userId") UUID userId);

    Optional<Category> findByIdAndUserId(Long id, UUID userId);
}
