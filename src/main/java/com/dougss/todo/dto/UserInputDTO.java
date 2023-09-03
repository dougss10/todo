package com.dougss.todo.dto;

import com.dougss.todo.config.validators.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserInputDTO {

    @Email(message = "username must be a valid e-mail.")
    private String username;
    @ValidPassword
    private String password;
}
