package com.example.demo.service;

import com.example.demo.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Repository
@RequiredArgsConstructor
public class MessageJdbcTemplateDao {

    private final JdbcTemplate jdbcTemplate;

    public Message save(Integer userId, String message) {
        String query = "INSERT INTO \"message\" (message, create_at, user_id) VALUES(?, ?, ?)";
        this.jdbcTemplate.update(
                query,
                message,
                Timestamp.valueOf(LocalDateTime.now()),
                userId
        );
        String createdMessageId = "SELECT lastval()";
        Integer getMessageId = this.jdbcTemplate.queryForObject(
                createdMessageId,
                Integer.class
        );
        String findMessageQuery = "SELECT * FROM \"message\" WHERE message_id = ?";
        return this.jdbcTemplate.queryForObject(
                findMessageQuery,
                (resultSet, rowNum) -> new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("create_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime(),
                        resultSet.getInt("user_id")
                ),
                getMessageId

        );
    }

}
