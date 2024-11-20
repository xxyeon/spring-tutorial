package com.example.demo.service;

import com.example.demo.controller.dto.MessageResponseDto;
import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJdbcApiDao userRepository;
    private final MessageJdbcApiDoa messageRepository;

    public UserResponseDto findById(Integer userId) throws SQLException {
        User user = userRepository.findById(userId);
        return UserResponseDto.from(user);
    }

    public UserResponseDto save(UserRequestDto dto) throws SQLException {
        User user = userRepository.save(UserRequestDto.of(dto));
        String message = dto.getUsername() + "님 가입을 환영합니다.";
        Message resultMessage = messageRepository.save(user.getUserId(), message);
        UserResponseDto result = UserResponseDto.from(user);
        List<MessageResponseDto> messageList = new ArrayList<>();
        messageList.add(MessageResponseDto.from(resultMessage));
        result.setMessage(messageList);
        return result;
    }

    public UserResponseDto update(Integer userId, UserRequestDto userDto) throws SQLException {
        User user = userRepository.update(userId, UserRequestDto.of(userDto));
        return UserResponseDto.from(user);
    }

    public void deleteById(Integer userId) throws SQLException {
        userRepository.delete(userId);
    }

}

