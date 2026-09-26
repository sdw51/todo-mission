package com.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoCreateRequestDto {
    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 200, message = "제목은 최대 200글자까지 입력가능합니다.")
    private String title;

    @Size(max = 1000, message = "설명은 최대 1000글자까지 입력가능합니다.")
    private String description;
}
