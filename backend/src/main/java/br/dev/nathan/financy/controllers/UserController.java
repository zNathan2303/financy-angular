package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.config.JWTUserData;
import br.dev.nathan.financy.dtos.request.UserNameRequest;
import br.dev.nathan.financy.dtos.response.UserResponse;
import br.dev.nathan.financy.dtos.response.error.BodyErrorResponse;
import br.dev.nathan.financy.services.UserService;
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

import java.util.UUID;

@RestController
@RequestMapping("/financy/v1/user")
@Tag(name = "User", description = "Manages the user info of the authenticated user.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "Update name of user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content."),
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
    @PatchMapping("/name")
    public ResponseEntity<Void> changeName(@AuthenticationPrincipal JWTUserData userData, @Valid @RequestBody UserNameRequest request) {

        UUID userId = userData.userId();
        String name = request.name();

        userService.changeName(userId, name);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
        summary = "Get user info."
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
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal JWTUserData userData) {

        UUID userId = userData.userId();

        UserResponse response = userService.getUserInfo(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
