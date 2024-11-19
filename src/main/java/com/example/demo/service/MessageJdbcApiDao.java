package com.example.demo.service;

import com.example.demo.controller.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageJdbcApiDao {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    //post
    public List<Message> save(final Connection connection, Integer userId, String name) throws SQLException {
//        Connection connection = null;           // 1
        PreparedStatement statement = null;     // 2
        ResultSet resultSet = null;
        try {
            // (A) INSERT USER
            statement = connection.prepareStatement(    // (A)-2:Statement
                    "INSERT INTO \"message\" (user_id, create_at, message) VALUES (?, ?, ?)"
            );
            statement.setInt(1, userId);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, name + "님 가입을 환영합니다.");

            int executedNumberOfQuery = statement.executeUpdate();
            statement = connection.prepareStatement(    // (B)-2:Statement
                    "SELECT lastval()" //INDENTITY이면 디비에 id 채번
            );
            resultSet = statement.executeQuery();
            Integer createdUserId = null;
            if (resultSet.next()) {
                createdUserId = resultSet.getInt("lastval");
            }
            statement = connection.prepareStatement(    // (C)-2:Statement
                    "SELECT * FROM \"message\" WHERE id = ?"
            );
            statement.setInt(1, createdUserId);
            resultSet = statement.executeQuery();
            List<Message> results = new ArrayList<>();
            if (resultSet.next()) {
                results.add(new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("create_at").toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                    )
                );
                return results;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 저장되지 않았습니다 - id : " + createdUserId);

        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원에 대한 접근에 문제가 있습니다.");
        } finally {
            // 자원반납
            if (resultSet != null) resultSet.close();   // 1
            if (statement != null) statement.close();   // 2
//            if (connection != null) connection.close(); // 3
        }
    }

}
