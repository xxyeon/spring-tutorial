package com.example.demo.service;

import com.example.demo.controller.dto.MessageResponseDto;
import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
        //TransactionSynchronizationManager 로 커넥션을 담아놓은 냉장고 생성 후 거기서 꺼내 사용
        TransactionSynchronizationManager.initSynchronization();
        Connection connection = DataSourceUtils.getConnection(dataSource); // dataSource를 사용해서 생성한 커넥션 사용
        try {

            connection.setAutoCommit(false); //auto commit false로 해주
            User user = userRepository.save(UserRequestDto.of(dto));
            String message = dto.getUsername() + "님 가입을 환영합니다.";
            Message resultMessage = messageRepository.save(user.getUserId(), message);
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
            DataSourceUtils.releaseConnection(connection, dataSource);
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

