package com.example.demo.service;

import com.example.demo.controller.dto.MessageResponseDto;
import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJdbcApiDao userRepository;
    private final MessageJdbcApiDoa messageRepository;
    private final DataSource dataSource;

    public UserResponseDto findById(Integer userId) throws SQLException {
        User user = userRepository.findById(userId);
        return UserResponseDto.from(user);
    }

    public UserResponseDto save(UserRequestDto dto) throws SQLException {
        Connection connection = null; //final Connection connection = dataSource.getConnection();?
        //null로 하는 이유 나중에 finally에서 자원 수거할때 null인지확인하기 위햬?
        try {
            connection = dataSource.getConnection(); //connection이 생성되징 않을 경우 어떤 오류를 잡아야하나? SQLException?
            connection.setAutoCommit(false); //auto commit false로 해주
            User user = userRepository.save(connection, UserRequestDto.of(dto));
            String message = dto.getUsername() + "님 가입을 환영합니다.";
            Message resultMessage = messageRepository.save(connection, user.getUserId(), message);
            connection.commit();

            UserResponseDto result = UserResponseDto.from(user);
            List<MessageResponseDto> messageList = new ArrayList<>();
            messageList.add(MessageResponseDto.from(resultMessage));
            result.setMessage(messageList);
            return result;
        } catch (SQLException e) { //ResponseException이 SQLException에 포함되는가?
            try {
                connection.rollback(); //rollback이 실패하는 원인에는 뭐가 있는가?
            } catch (final SQLException ignored) { //final로 해주는 이유는?
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원 반납 시 문제가 있습니다.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (final SQLException ignored) {
            }
        }
    }

    public UserResponseDto update(Integer userId, UserRequestDto userDto) throws SQLException {
        User user = userRepository.update(userId, UserRequestDto.of(userDto));
        return UserResponseDto.from(user);
    }

    public void deleteById(Integer userId) throws SQLException {
        userRepository.delete(userId);
    }

}

