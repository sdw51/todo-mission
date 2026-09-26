package com.back.controller;

import com.back.dto.TodoCreateRequestDto;
import com.back.dto.TodoResponseDto;
import com.back.dto.TodoUpdateDto;
import com.back.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/todos")
public class TodoController {

    private final TodoService todoService;

    //Todo 생성
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

    //완료/미완료 토글
    @PatchMapping("/{id}/completed")
    public ResponseEntity<TodoResponseDto> toggleCompleted(
            @PathVariable Long id
    ) {
        TodoResponseDto responseDto = todoService.toggleTodoCompleted(id);

        return ResponseEntity.ok(responseDto);
    }

    //Todo 수정
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long id,
            @Valid@RequestBody TodoUpdateDto updateDto
    ) {
        TodoResponseDto updateTodo = todoService.updateTodo(id, updateDto);

        return ResponseEntity.ok(updateTodo);
    }
}
