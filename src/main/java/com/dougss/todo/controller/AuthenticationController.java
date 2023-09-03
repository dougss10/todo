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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Value("${time.lock.account.minutes}")
    private Integer timeLockAccountMinutes;
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
        Authentication auth;
        String token = "";
        try {
            auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            token = tokenService.generateToken((User) auth.getPrincipal());
        }
        catch (LockedException ex) {

            User user = userService.findByUserName(usernamePasswordAuthenticationToken.getName());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, (timeLockAccountMinutes * -1));
            Date dateLock = calendar.getTime();

            if(user != null && !user.isAccountNonLocked() && user.getLockTime().before(dateLock)) {
                user.setLockTime(null);
                user.setAccountNonLocked(true);
                user.setFailedAttempt(0);
                userService.save(user);
            } else {
                Calendar calendarTimeLock = Calendar.getInstance();
                calendarTimeLock.setTime(user.getLockTime());
                calendarTimeLock.add(Calendar.MINUTE, timeLockAccountMinutes);
                Date dateToUnlock = calendarTimeLock.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                throw new LockedException("Blocked account, wait until: " + dateFormat.format(dateToUnlock) + " and try again.");
            }
            auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            token = tokenService.generateToken((User) auth.getPrincipal());
        } catch (BadCredentialsException ex) {
            userService.updateFailedAttemptLogin(usernamePasswordAuthenticationToken.getName());
            throw new BadCredentialsException("Username does not exist or password is invalid");
        }

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
