package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.config.JWTUserData;
import br.dev.nathan.financy.dtos.request.CategoryRequest;
import br.dev.nathan.financy.dtos.response.CategoryResponse;
import br.dev.nathan.financy.dtos.response.CategoryWithDetailsResponse;
import br.dev.nathan.financy.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/financy/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryWithDetailsResponse>> getCategories(@AuthenticationPrincipal JWTUserData userData) {

        UUID userId = userData.userId();

        List<CategoryWithDetailsResponse> categories = categoryService.getCategoriesByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@AuthenticationPrincipal JWTUserData userData,
                                                            @PathVariable Long id) {

        UUID userId = userData.userId();

        CategoryResponse response = categoryService.getCategoryById(userId, id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@AuthenticationPrincipal JWTUserData userData,
                                               @PathVariable Long id) {

        UUID userId = userData.userId();

        categoryService.deleteCategory(userId, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@AuthenticationPrincipal JWTUserData userData,
                                               @Valid @RequestBody CategoryRequest request) {

        UUID userId = userData.userId();

        CategoryResponse categoryCreated = categoryService.createCategory(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@AuthenticationPrincipal JWTUserData userData,
                                               @PathVariable Long id,
                                               @Valid @RequestBody CategoryRequest request) {

        UUID userId = userData.userId();

        CategoryResponse categoryUpdated = categoryService.updateCategory(userId, request, id);

        return ResponseEntity.status(HttpStatus.OK).body(categoryUpdated);
    }
}
