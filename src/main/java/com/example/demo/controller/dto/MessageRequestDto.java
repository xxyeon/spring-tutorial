package com.example.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MessageRequestDto {
    private String message;
    private LocalDateTime createAt;
}
