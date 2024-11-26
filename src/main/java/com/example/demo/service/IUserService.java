package com.example.demo.service;

import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import java.util.List;

public interface IUserService {

    UserResponseDto findById(Integer id);

    List<UserResponseDto> findAll();
    UserResponseDto save(UserRequestDto dto);
    UserResponseDto update(Integer userId, UserRequestDto userDto);
    void deleteById(Integer id);
}
