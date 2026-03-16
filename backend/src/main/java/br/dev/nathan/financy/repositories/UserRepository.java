package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<UserDetails> findUserByEmail(String username);
}
