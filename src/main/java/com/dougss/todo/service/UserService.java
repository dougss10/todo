package com.dougss.todo.service;

import com.dougss.todo.dto.UserInputDTO;
import com.dougss.todo.dto.UserOutputDTO;
import com.dougss.todo.exception.RegisterException;
import com.dougss.todo.model.User;
import com.dougss.todo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    final UserRepository userRepository;
    @Value("${limit.failed.attempts.login}")
    private Integer limitFailedAttemptsLogin;

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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public User updateFailedAttemptLogin(String username) {
        User user = findByUserName(username);

        if(user != null && user.isAccountNonLocked()) {
            if(user.getFailedAttempt() != null) {
                user.setFailedAttempt(user.getFailedAttempt() + 1);
            } else {
                user.setFailedAttempt(1);
            }
            if(user.getFailedAttempt() >= limitFailedAttemptsLogin ) {
                user.setAccountNonLocked(false);
                user.setLockTime(new Date());
            }
            save(user);
        }

        return user;
    }
}
