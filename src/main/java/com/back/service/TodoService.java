package com.back.service;

import com.back.dto.TodoCreateRequestDto;
import com.back.dto.TodoResponseDto;
import com.back.model.Todo;
import com.back.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public TodoResponseDto toggleTodoCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo를 찾을 수 없습니다."));

        todo.toggleCompleted();

        return new TodoResponseDto(todo);
    }
}
