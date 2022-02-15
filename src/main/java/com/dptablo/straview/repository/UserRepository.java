package com.dptablo.straview.repository;

import com.dptablo.straview.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserIdAndPassword(String userId, String password);
}
