package com.back.controller;

import com.back.dto.TodoCreateRequestDto;
import com.back.dto.TodoResponseDto;
import com.back.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
            @Valid
            @RequestBody
            TodoCreateRequestDto requestDto
    ) {
        TodoResponseDto createDto = todoService.createTodo(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createDto);
    }
}
