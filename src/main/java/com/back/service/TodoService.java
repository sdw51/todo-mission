package com.back.service;

import com.back.dto.TodoCreateRequestDto;
import com.back.dto.TodoResponseDto;
import com.back.model.Todo;
import com.back.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoResponseDto createTodo(TodoCreateRequestDto requestDto) {
        Todo todo = new Todo(
                requestDto.getTitle(),
                requestDto.getDescription()
        );

        Todo savedTodo = todoRepository.save(todo);

        return new TodoResponseDto(savedTodo);
    }
}
