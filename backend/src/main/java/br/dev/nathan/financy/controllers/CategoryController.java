package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.config.JWTUserData;
import br.dev.nathan.financy.dtos.request.CategoryRequest;
import br.dev.nathan.financy.dtos.response.CategoryResponse;
import br.dev.nathan.financy.dtos.response.CategoryWithDetailsResponse;
import br.dev.nathan.financy.dtos.response.error.BodyErrorResponse;
import br.dev.nathan.financy.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/financy/v1/categories")
@Tag(name = "Category", description = "Manages the categories of the authenticated user.")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "Get categories."
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
    public ResponseEntity<List<CategoryWithDetailsResponse>> getCategories(@AuthenticationPrincipal JWTUserData userData) {

        UUID userId = userData.userId();

        List<CategoryWithDetailsResponse> categories = categoryService.getCategoriesByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @Operation(
        summary = "Get category by Id."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success!"),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@AuthenticationPrincipal JWTUserData userData,
                                                            @PathVariable Long id) {

        UUID userId = userData.userId();

        CategoryResponse response = categoryService.getCategoryById(userId, id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
        summary = "Delete category by Id."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content."),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@AuthenticationPrincipal JWTUserData userData,
                                               @PathVariable Long id) {

        UUID userId = userData.userId();

        categoryService.deleteCategory(userId, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
        summary = "Create category."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created!"),
        @ApiResponse(
            responseCode = "400",
            description = "Some of the fields are invalid.",
            content = @Content(schema = @Schema(implementation = BodyErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@AuthenticationPrincipal JWTUserData userData,
                                               @Valid @RequestBody CategoryRequest request) {

        UUID userId = userData.userId();

        CategoryResponse categoryCreated = categoryService.createCategory(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryCreated);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update category."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success!"),
        @ApiResponse(
            responseCode = "400",
            description = "Some of the fields are invalid.",
            content = @Content(schema = @Schema(implementation = BodyErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Not authenticated.",
            content = @Content()
        )
    })
    public ResponseEntity<CategoryResponse> updateCategory(@AuthenticationPrincipal JWTUserData userData,
                                               @PathVariable Long id,
                                               @Valid @RequestBody CategoryRequest request) {

        UUID userId = userData.userId();

        CategoryResponse categoryUpdated = categoryService.updateCategory(userId, request, id);

        return ResponseEntity.status(HttpStatus.OK).body(categoryUpdated);
    }
}
