package com.example.demo.service;

import com.example.demo.controller.dto.MessageResponseDto;
import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor //클래스 기반 프록시를 생성하는 CGLIB 는 타켓 클래스에 기본 생성자가 있어야함
 public class UserService {

    private final UserJdbcTemplateDao userRepository;
    private final MessageJdbcTemplateDao messageRepository;

    public UserResponseDto findById(Integer userId) {
        User user = userRepository.findById(userId);
        return UserResponseDto.from(user);
    }

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

