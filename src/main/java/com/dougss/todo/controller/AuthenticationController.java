package com.dougss.todo.controller;

import com.dougss.todo.dto.LoginResponseDTO;
import com.dougss.todo.dto.UserInputDTO;
import com.dougss.todo.dto.UserOutputDTO;
import com.dougss.todo.exception.RegisterException;
import com.dougss.todo.model.User;
import com.dougss.todo.service.TokenService;
import com.dougss.todo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final AuthenticationManager authenticationManager;
    final UserService userService;
    final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }


    @Operation(summary = "Login User", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login User performed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal error"),
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserInputDTO userInputDTO) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userInputDTO.getUsername(), userInputDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Save User", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save User performed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal error"),
    })
    @PostMapping("/register")
    public ResponseEntity<UserOutputDTO> register(@RequestBody @Valid UserInputDTO userInputDTO) throws RegisterException {
        UserOutputDTO userOutputDTO = userService.createNewUser(userInputDTO);
        return new ResponseEntity<>(userOutputDTO, HttpStatus.CREATED);
    }
}
