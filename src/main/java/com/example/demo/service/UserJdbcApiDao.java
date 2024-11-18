package com.example.demo.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserJdbcApiDao {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    private final DataSource dataSource;

    public User findById(int userId) throws SQLException {
        Connection connection = null;   // 1
        Statement statement = null;     // 2
        ResultSet resultSet = null;     // 3
        try {
            connection = dataSource.getConnection(); // 1 pool에서 커넥션을 가져오므로 url, username, password를 매번 넣지 않아도됨
            statement = connection.createStatement();   // 2
            resultSet = statement.executeQuery(         // 3
                    "SELECT * FROM \"user\" WHERE id = " + userId //쿼리 결과가 resultSet에 담김
            );
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                );
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 존재하지 않습니다 - id : " + userId);
        } catch (SQLException e) { //checked exception -> 컴파일 에러
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원에 대한 접근에 문제가 있습니다.");
        } finally {
            // 자원반납
            if (resultSet != null) resultSet.close();   // 1
            if (statement != null) statement.close();   // 2
            if (connection != null) connection.close(); // 3
        }
    }

    public List<User> findAll() throws SQLException {
        Connection connection = null;   // 1
        Statement statement = null;     // 2
        ResultSet resultSet = null;     // 3
        try {
            connection = dataSource.getConnection();    // 1
            statement = connection.createStatement();   // 2
            resultSet = statement.executeQuery(         // 3
                    "SELECT * FROM \"user\""
            );
            List<User> results = new ArrayList();
            while (resultSet.next()) {
                results.add(
                        new User(
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
                );
            }
            return results;
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원에 대한 접근에 문제가 있습니다.");
        } finally {
            // 자원반납
            if (resultSet != null) resultSet.close();   // 1
            if (statement != null) statement.close();   // 2
            if (connection != null) connection.close(); // 3
        }
    }
}
