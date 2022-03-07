package com.dptablo.straview.service;

import com.dptablo.straview.dto.entity.User;
import com.dptablo.straview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByUserId(String userId) {
        return userRepository.findById(userId).orElseThrow(NullPointerException::new);
    }
}
