package com.dougss.todo.controller;

import com.dougss.todo.dto.TodoDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static com.dougss.todo.common.TodoConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class TodoControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private String token = null;

    @BeforeAll
    void getUser(){
        ResponseEntity<Object> objectRetorno = restTemplate.postForEntity("/auth/register", USER, Object.class );
        if(objectRetorno.getBody() != null) {
            ResponseEntity<HashMap> hashRetorno = restTemplate.postForEntity("/auth/login", USER, HashMap.class );
            if(hashRetorno.getStatusCode() == HttpStatus.OK) {
                token = hashRetorno.getBody().get("token").toString();
            }
        }
    }

    @Test
    void testCreateTodoSuccess() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(TODODTO, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntity = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntity.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTemp = todoDTOResponseEntity.getBody();
        assertNotNull(todoDTOTemp);
        assertNotNull(todoDTOTemp.getId());
        assertEquals(todoDTOTemp.getDescription(), TODODTO.getDescription());
    }

    @Test
    void testDeleteTodoSuccess() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();

        TodoDTO todoDTODelete = new TodoDTO("todo remove", false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(todoDTODelete, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntity = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntity.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTemp = todoDTOResponseEntity.getBody();
        assertNotNull(todoDTOTemp);
        assertNotNull(todoDTOTemp.getId());
        assertEquals(todoDTOTemp.getDescription(), todoDTODelete.getDescription());

        HttpEntity<String> entityDelete = new HttpEntity<>("", headers);
        ResponseEntity<Object> todoDeletResponseEntity = restTemplate.exchange("/todo/" + todoDTOTemp.getId(), HttpMethod.DELETE, entityDelete, Object.class);
        assertEquals(HttpStatus.NO_CONTENT, todoDeletResponseEntity.getStatusCode());
    }

    @Test
    void testGetTodoSuccess() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(TODODTO, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntityPost = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntityPost.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTempPost = todoDTOResponseEntityPost.getBody();
        assertNotNull(todoDTOTempPost);
        assertNotNull(todoDTOTempPost.getId());
        assertEquals(todoDTOTempPost.getDescription(), TODODTO.getDescription());

        ResponseEntity<Object> todoGetResponseEntity = restTemplate.exchange("/todo/" + todoDTOTempPost.getId(), HttpMethod.GET, entity, Object.class);
        assertEquals(HttpStatus.OK, todoGetResponseEntity.getStatusCode());
    }


    @Test
    void testPutTodoSuccess() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(TODODTO, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntityPost = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntityPost.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTempPost = todoDTOResponseEntityPost.getBody();
        assertNotNull(todoDTOTempPost);
        assertNotNull(todoDTOTempPost.getId());
        assertEquals(todoDTOTempPost.getDescription(), TODODTO.getDescription());

        TODODTO.setDescription(TODODTO.getDescription() + "_UPDATE");

        HttpEntity<TodoDTO> entityPut = new HttpEntity<>(TODODTO, headers);
        ResponseEntity<Object> todoPutResponseEntity = restTemplate.exchange("/todo/" + todoDTOTempPost.getId(), HttpMethod.PUT, entityPut, Object.class);
        assertEquals(HttpStatus.CREATED, todoPutResponseEntity.getStatusCode());
    }

    @Test
    public void testCreateTodoFailure() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(INVALID_TODODTO, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntity = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }


    @Test
    void testDeleteTodoFailure() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();

        TodoDTO todoDTODelete = new TodoDTO("todo remove", false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(todoDTODelete, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntity = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntity.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTemp = todoDTOResponseEntity.getBody();
        assertNotNull(todoDTOTemp);
        assertNotNull(todoDTOTemp.getId());
        assertEquals(todoDTOTemp.getDescription(), todoDTODelete.getDescription());

        HttpEntity<String> entityDelete = new HttpEntity<>("", headers);
        ResponseEntity<Object> todoDeletResponseEntity = restTemplate.exchange("/todo/-1", HttpMethod.DELETE, entityDelete, Object.class);
        assertEquals(HttpStatus.NOT_FOUND, todoDeletResponseEntity.getStatusCode());
    }

    @Test
    void testGetTodoFailure() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(TODODTO, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntityPost = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntityPost.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTempPost = todoDTOResponseEntityPost.getBody();
        assertNotNull(todoDTOTempPost);
        assertNotNull(todoDTOTempPost.getId());
        assertEquals(todoDTOTempPost.getDescription(), TODODTO.getDescription());

        ResponseEntity<Object> todoGetResponseEntity = restTemplate.exchange("/todo/-1", HttpMethod.GET, entity, Object.class);
        assertEquals(HttpStatus.NOT_FOUND, todoGetResponseEntity.getStatusCode());
    }

    @Test
    void testPutTodoFailure() {
        //@BeforeAll not runs in mvn test the code below is to fix this
        getUser();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(TODODTO, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntityPost = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntityPost.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTempPost = todoDTOResponseEntityPost.getBody();
        assertNotNull(todoDTOTempPost);
        assertNotNull(todoDTOTempPost.getId());
        assertEquals(todoDTOTempPost.getDescription(), TODODTO.getDescription());

        HttpEntity<TodoDTO> entityPut = new HttpEntity<>(INVALID_TODODTO, headers);
        ResponseEntity<Object> todoPutResponseEntity = restTemplate.exchange("/todo/" + todoDTOTempPost.getId(), HttpMethod.PUT, entityPut, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, todoPutResponseEntity.getStatusCode());
    }

    @Test
    void testAccessWithoutLogin() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TodoDTO> entity = new HttpEntity<>(TODODTO, headers);

        ResponseEntity<TodoDTO> todoDTOResponseEntityPost = restTemplate.postForEntity("/todo", entity, TodoDTO.class );

        assertEquals(todoDTOResponseEntityPost.getStatusCode(), HttpStatus.CREATED);
        TodoDTO todoDTOTempPost = todoDTOResponseEntityPost.getBody();
        assertNotNull(todoDTOTempPost);
        assertNotNull(todoDTOTempPost.getId());
        assertEquals(todoDTOTempPost.getDescription(), TODODTO.getDescription());

        ResponseEntity<Object> todoGetResponseEntity = restTemplate.exchange("/todo/" + todoDTOTempPost.getId(), HttpMethod.GET, entity, Object.class);
        assertEquals(HttpStatus.OK, todoGetResponseEntity.getStatusCode());

        ResponseEntity<Object> todoGeWithoutLoginResponseEntity = restTemplate.getForEntity("/todo/" + todoDTOTempPost.getId(), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, todoGeWithoutLoginResponseEntity.getStatusCode());
    }
}
