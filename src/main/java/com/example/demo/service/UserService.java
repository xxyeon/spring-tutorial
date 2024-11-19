package com.example.demo.service;

import com.example.demo.common.DataSourceConfig;
import com.example.demo.controller.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserJdbcApiDao userJdbcApiDao;
    private final MessageJdbcApiDao messageJdbcApiDao;
    private final DataSource datasource;


    public UserResponseDto findById(Integer id) {
        User user = userRepository.findById(id);
        return UserResponseDto.from(user);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::from)
                .toList();
    }

    public UserResponseDto save(String name, Integer age, String job, String specialty) throws SQLException {

        Connection connection = datasource.getConnection();
        User user = userJdbcApiDao.save(connection, name, age, job, specialty);
        List<Message> result = messageJdbcApiDao.save(connection, user.getId(), name);

        connection.close();
        UserResponseDto dto = UserResponseDto.from(user);
        dto.setMessage(result);
        return dto;
    }
}
