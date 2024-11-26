package com.example.demo.service;

import com.example.demo.controller.dto.MessageResponseDto;
import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserJdbcTemplateDao userRepository;
    private final MessageJdbcTemplateDao messageRepository;

    public UserResponseDto findById(Integer userId) {
        User user = userRepository.findById(userId);
        return UserResponseDto.from(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> result = userList.stream().map(UserResponseDto::from).toList();
        return result;
    }

    public UserResponseDto save(UserRequestDto dto) {

        User user = userRepository.save(UserRequestDto.of(dto));
        String message = dto.getUsername() + "님 가입을 환영합니다.";
        Message resultMessage = messageRepository.save(user.getUserId(), message);

        UserResponseDto result = UserResponseDto.from(user);
        List<MessageResponseDto> messageList = new ArrayList<>();
        messageList.add(MessageResponseDto.from(resultMessage));
        result.setMessage(messageList);
        return result;

    }

    public UserResponseDto update(Integer userId, UserRequestDto userDto) {
        User user = userRepository.update(userId, UserRequestDto.of(userDto));
        return UserResponseDto.from(user);
    }

    public void deleteById(Integer userId) {
        userRepository.delete(userId);
    }

}

