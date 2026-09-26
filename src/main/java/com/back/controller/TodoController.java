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

import java.util.List;

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

    //Todo 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id
    ) {
        todoService.deleteTodo(id);

        return ResponseEntity.noContent().build();
    }

    //전체조회
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {
        List<TodoResponseDto> todos = todoService.getAllTodos();

        return ResponseEntity.ok(todos);
    }

    //단일조회
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodo(
            @PathVariable Long id
    ) {
        TodoResponseDto todo = todoService.getTodo(id);

        return ResponseEntity.ok(todo);
    }
}
