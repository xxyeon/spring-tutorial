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
public class UserService {

    private final UserJdbcTemplateDao userRepository;
    private final MessageJdbcTemplateDao messageRepository;
    private final DataSource dataSource;

    public UserResponseDto findById(Integer userId) {
        User user = userRepository.findById(userId);
        return UserResponseDto.from(user);
    }

    public UserResponseDto save(UserRequestDto dto) {
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource); //빈으로 주입하지 않고 직접 주입
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        try {
            User user = userRepository.save(UserRequestDto.of(dto));
            String message = dto.getUsername() + "님 가입을 환영합니다.";
            Message resultMessage = messageRepository.save(user.getUserId(), message);
            transactionManager.commit(status);

            UserResponseDto result = UserResponseDto.from(user);
            List<MessageResponseDto> messageList = new ArrayList<>();
            messageList.add(MessageResponseDto.from(resultMessage));
            result.setMessage(messageList);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 실패");
        }
    }

    public UserResponseDto update(Integer userId, UserRequestDto userDto) {
        User user = userRepository.update(userId, UserRequestDto.of(userDto));
        return UserResponseDto.from(user);
    }

    public void deleteById(Integer userId) {
        userRepository.delete(userId);
    }

}

