package com.example.demo.controller.dto;

import com.example.demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String password;
    @Setter
    private List<MessageResponseDto> message;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getUsername(), user.getPassword());
    }

    public UserResponseDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
