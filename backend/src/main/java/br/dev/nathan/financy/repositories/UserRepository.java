package br.dev.nathan.financy.repositories;

import br.dev.nathan.financy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
