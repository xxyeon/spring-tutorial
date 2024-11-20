package com.example.demo.controller.dto;

import com.example.demo.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class MessageResponseDto {
    private String message;
    private LocalDateTime createAt;

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(message.getMessage(), message.getCreateAt());
    }
}
