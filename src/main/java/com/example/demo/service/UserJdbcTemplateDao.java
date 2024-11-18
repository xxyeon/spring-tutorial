package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserJdbcTemplateDao {
    private final JdbcTemplate jdbcTemplate;

    //단일 조회: jdbcTemplate.queryForObject
    public User findById(int userId) {
        String getUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        int getUserParams = userId;
        return this.jdbcTemplate.queryForObject(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                getUserParams
        );
    }

    //다수 조회: jdbcTemplate.queryForStream
    public List<User> findAll() {
        String getUserQuery = "SELECT * FROM \"user\"";
        return this.jdbcTemplate.queryForStream(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                )
        ).toList();
    }

    //저장: jdbcTemplate.update
    public User save(String name, Integer age, String job, String specialty) {
        // (A) INSERT USER
        String createUserQuery = "INSERT INTO \"user\" (name, age, job, specialty, created_at) VALUES (?, ?, ?, ?, ?)";
        Object[] createUserParams = new Object[]{
                name,
                age,
                job,
                specialty,
                LocalDateTime.now()
        };
        this.jdbcTemplate.update(
                createUserQuery,
                createUserParams
        );
        // (B) SELECT id - MySQL:last_insert_id()->id / PostgresQL:currval()->lastval/lastval()->lastval
        String lastInsertIdQuery = "SELECT lastval()";
        int createdUserId = this.jdbcTemplate.queryForObject(
                lastInsertIdQuery,
                int.class
        );
        // (C) SELECT USER
        String getUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        int getUserParams = createdUserId;
        return this.jdbcTemplate.queryForObject(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                getUserParams
        );
    }

    //수정: jdbcTemplate.update
    public User update(int id, String name, Integer age, String job, String specialty) {
        // (A) UPDATE USER
        String updateUserQuery = "UPDATE \"user\" SET name = ?, age = ?, job = ?, specialty = ? WHERE id = ?";
        Object[] updateUserParams = new Object[]{
                name,
                age,
                job,
                specialty,
                id,
        };
        int updatedUserId = this.jdbcTemplate.update(
                updateUserQuery,
                updateUserParams
        );
        // (B) SELECT USER
        String getUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        int getUserParams = updatedUserId;
        return this.jdbcTemplate.queryForObject(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                getUserParams
        );
    }

    //삭제: jdbcTemplate.update
    public void delete(int userId) {
        String deleteUserQuery = "DELETE FROM \"user\" WHERE id = ? ";
        Object[] deleteUserParams = new Object[]{
                userId
        };
        this.jdbcTemplate.update(
                deleteUserQuery,
                deleteUserParams
        );
    }
}
