package com.example.demo.service;

import com.example.demo.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Repository
@RequiredArgsConstructor
public class MessageJdbcApiDoa {
    private final DataSource dataSource;

    public Message save(Integer userId, String message) throws SQLException {
        if(true) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메시지를 저장하는데 문제가 발생했습니다");
        }
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            statement = connection.prepareStatement(
                    "INSERT INTO \"message\" (message, create_at, user_id) VALUES(?, ?, ?)"
            );
            statement.setString(1, message);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, userId);
            statement.executeUpdate();

            statement = connection.prepareStatement(
                    "SELECT lastval()"
            );
            resultSet = statement.executeQuery();
            Integer createdMessageId = null;
            while(resultSet.next()) {
                createdMessageId = resultSet.getInt("lastval");
            }
            statement = connection
                    .prepareStatement("SELECT * FROM \"message\" WHERE message_id = ?");
            statement.setInt(1, createdMessageId);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("create_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime(),
                        resultSet.getInt("user_id")

                );
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메시지를 저장하는데 문제가 발생했습니다 id:" + createdMessageId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(resultSet != null) resultSet.close();
            if(statement != null) statement.close();
        }
    }
}
