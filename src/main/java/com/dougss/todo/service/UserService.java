package com.dougss.todo.service;

import com.dougss.todo.dto.UserInputDTO;
import com.dougss.todo.dto.UserOutputDTO;
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

    public UserOutputDTO save(User userParam) {
        User user = userRepository.save(userParam);
        return new UserOutputDTO(user);
    }

    public User findByUserName(String username) {
        return (User) userRepository.findByUsername(username);
    }

    public UserOutputDTO createNewUser(UserInputDTO userInputDTO) {

        if(findByUserName(userInputDTO.getUsername()) != null) {
            //TODO valid user already exists
            System.out.println("Validate here.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userInputDTO.getPassword());
        User user = new User(userInputDTO.getUsername(), encryptedPassword);
        return save(user);
    }
}
