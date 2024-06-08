package edu.bethlehem.user_service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Boolean existsByUsername(String username);

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

}
