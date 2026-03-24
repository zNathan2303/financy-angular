package br.dev.nathan.financy.controllers;

import br.dev.nathan.financy.config.TokenConfig;
import br.dev.nathan.financy.dtos.request.LoginRequest;
import br.dev.nathan.financy.dtos.request.RegisterUserRequest;
import br.dev.nathan.financy.dtos.response.LoginResponse;
import br.dev.nathan.financy.dtos.response.RegisterUserResponse;
import br.dev.nathan.financy.dtos.response.error.BodyErrorResponse;
import br.dev.nathan.financy.entities.User;
import br.dev.nathan.financy.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financy/v1/auth")
@Tag(name = "Auth", description = "Authentication to access endpoints.")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    @Operation(
        summary = "Log in."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success!"),
        @ApiResponse(
            responseCode = "400",
            description = "Some of the fields are invalid.",
            content = @Content(schema = @Schema(implementation = BodyErrorResponse.class))
        )
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        try {
            UsernamePasswordAuthenticationToken userAndPass =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

            Authentication authentication = authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();
            String token = tokenConfig.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new LoginResponse(null));
        }
    }

    @PostMapping("/register")
    @Operation(
        summary = "Register."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created!"),
        @ApiResponse(
            responseCode = "400",
            description = "Some of the fields are invalid.",
            content = @Content(schema = @Schema(implementation = BodyErrorResponse.class))
        )
    })
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setName(request.name());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(newUser.getName(), newUser.getEmail()));
    }
}
