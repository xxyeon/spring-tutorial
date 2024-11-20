package com.example.demo.controller.dto;

import com.example.demo.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserRequestDto {
    private String username;
    private String password;

    public static User of(UserRequestDto dto) {
        return new User(dto.getUsername(), dto.getPassword());
    }
}
