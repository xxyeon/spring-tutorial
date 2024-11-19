package com.example.demo.controller.dto;

import com.example.demo.service.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageResponseDto {
    private LocalDateTime createAt;
    private String message;

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(message.getCreatedAt(), message.getMessage());
    }
}
