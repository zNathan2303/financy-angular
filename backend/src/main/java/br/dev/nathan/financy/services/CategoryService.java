package br.dev.nathan.financy.services;

import br.dev.nathan.financy.dtos.response.CategoryResponse;
import br.dev.nathan.financy.dtos.response.dashboard.CategoryDTO;
import br.dev.nathan.financy.repositories.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getTop5Categories(UUID userId) {
        return categoryRepository.findTop(userId, PageRequest.of(0, 5));
    }

    public List<CategoryResponse> getCategoriesByUserId(UUID userId) {
        return categoryRepository.findByUserIdOrderByTitleAsc(userId)
            .stream()
            .map(entity -> new CategoryResponse(entity))
            .toList();
    }
}
