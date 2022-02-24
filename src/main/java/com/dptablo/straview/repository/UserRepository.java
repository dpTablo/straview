package com.dptablo.straview.repository;

import com.dptablo.straview.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserIdAndPassword(String userId, String password);
}
