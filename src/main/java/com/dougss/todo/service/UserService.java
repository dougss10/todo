package com.dougss.todo.service;

import com.dougss.todo.dto.UserInputDTO;
import com.dougss.todo.dto.UserOutputDTO;
import com.dougss.todo.exception.RegisterException;
import com.dougss.todo.model.User;
import com.dougss.todo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User userParam) {
        return userRepository.save(userParam);
    }

    public User findByUserName(String username) {
        return (User) userRepository.findByUsername(username);
    }

    public UserOutputDTO createNewUser(UserInputDTO userInputDTO) throws RegisterException {

        if(findByUserName(userInputDTO.getUsername()) != null) {
            throw new RegisterException("User already exists.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userInputDTO.getPassword());
        User user = new User(userInputDTO.getUsername(), encryptedPassword);
        return new UserOutputDTO(save(user));
    }
}
