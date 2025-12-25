package com.nht.demorestapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDaoService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findOne(Integer id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s not found", id)));
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
