package br.dev.nathan.financy.services;

import br.dev.nathan.financy.dtos.request.CategoryRequest;
import br.dev.nathan.financy.dtos.response.CategoryResponse;
import br.dev.nathan.financy.dtos.response.CategoryWithDetailsResponse;
import br.dev.nathan.financy.dtos.response.dashboard.CategoryDTO;
import br.dev.nathan.financy.entities.Category;
import br.dev.nathan.financy.entities.User;
import br.dev.nathan.financy.exceptions.ResourceOwnershipException;
import br.dev.nathan.financy.repositories.CategoryRepository;
import br.dev.nathan.financy.repositories.TransactionRepository;
import br.dev.nathan.financy.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<CategoryDTO> getTop5Categories(UUID userId) {
        return categoryRepository.findTop(userId, PageRequest.of(0, 5));
    }

    public List<CategoryWithDetailsResponse> getCategoriesByUserId(UUID userId) {
        return categoryRepository.findCategoriesWithTransactionCount(userId);
    }

    public CategoryResponse getCategoryById(UUID userId, Long id) {

        Category category = categoryRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        CategoryResponse response = new CategoryResponse(category);

        return response;
    }

    public void deleteCategory(UUID userId, Long id) {

        Category categoryToDelete = categoryRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        categoryRepository.delete(categoryToDelete);
    }

    public CategoryResponse createCategory(UUID userId, CategoryRequest request) {

        User user = userRepository.findById(userId).orElseThrow();

        Category category = new Category(request, user, null);

        Category categoryCreated = categoryRepository.save(category);

        return new CategoryResponse(categoryCreated);
    }

    public CategoryResponse updateCategory(UUID userId, CategoryRequest request, Long id) {

        Category category = categoryRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceOwnershipException());

        category.setTitle(request.title());
        category.setDescription(request.description());
        category.setColor(request.color());
        category.setIcon(request.icon());

        Category categoryUpdated = categoryRepository.save(category);

        return new CategoryResponse(categoryUpdated);
    }
}
