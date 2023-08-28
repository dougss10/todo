package com.dougss.todo.dto;

import com.dougss.todo.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserOutputDTO {

    public UserOutputDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    private Long id;
    private String username;
}
