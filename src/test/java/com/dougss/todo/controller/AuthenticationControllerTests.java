package com.dougss.todo.controller;

import com.dougss.todo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static com.dougss.todo.common.TodoConstants.INVALID_USER;
import static com.dougss.todo.common.TodoConstants.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateUserSuccess() {
        User userNew = new User("douglasNew@dougss.com", "Afasdf123@");
        ResponseEntity<Object> objectRetorno = restTemplate.postForEntity("/auth/register", userNew, Object.class );
        assertEquals(objectRetorno.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void testLoginUserSuccess(){
        ResponseEntity<Object> objectRetorno = restTemplate.postForEntity("/auth/register", USER, Object.class );
        if(objectRetorno.getStatusCode() == HttpStatus.CREATED) {
            ResponseEntity<HashMap> hashRetorno = restTemplate.postForEntity("/auth/login", USER, HashMap.class );
            assertEquals(hashRetorno.getStatusCode(), HttpStatus.OK);
        }
    }

    @Test
    public void testCreateUserFailure() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "");
        HttpEntity<User> entity = new HttpEntity<>(INVALID_USER, headers);
        ResponseEntity<User> userResponseEntity = restTemplate.postForEntity("/auth/register", entity, User.class );
        assertEquals(userResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void testLoginUserFailure() {
        ResponseEntity<Object> objectRetorno = restTemplate.postForEntity("/auth/register", USER, Object.class);
        if (objectRetorno.getStatusCode() == HttpStatus.CREATED) {
            ResponseEntity<HashMap> hashRetorno = restTemplate.postForEntity("/auth/login", INVALID_USER, HashMap.class);
            assertEquals(hashRetorno.getStatusCode(), HttpStatus.FORBIDDEN);
        }
    }
}
