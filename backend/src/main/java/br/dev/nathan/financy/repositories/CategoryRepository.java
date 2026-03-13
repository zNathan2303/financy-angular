package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
