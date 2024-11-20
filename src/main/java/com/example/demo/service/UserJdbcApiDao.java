package com.example.demo.service;


import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class UserJdbcApiDao {

    private final DataSource dataSource;
    //jdbc api 3요소
    //커넥션 -> datasource 사용하므로 풀에서 꺼내서 사용
    //statement
    //resultset

    public User findById(Integer userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE user_id = ?"
            );
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 가져오는데 문제가 발생했습니다 id:" + userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }
    public User save(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, password) VALUES (?, ?)"
            );
            //prepareStatement에 파라미터 설정
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            //statement 파라미터 다 넣었다면 실행
            //executeQuery()
            statement.executeUpdate(); //select만 executQuery, insert, delete, update로 사용하면 아무것도 반환하지 않음

            //결과 가져오기 위함
            //저장된 유저의 id 가져오기 위함
            resultSet = connection.prepareStatement(
                    "SELECT lastval()"
            ).executeQuery();

            Integer createUserId = null;
            if (resultSet.next()) {
                createUserId = resultSet.getInt("lastval");
            }

            statement = connection
                    .prepareStatement("SELECT *  FROM \"user\" WHERE user_id = ?");
            statement.setInt(1, createUserId);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 저장하는데 문제가 발생했습니다 id:" + createUserId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //자원 수거
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    public User update(Integer userId, User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(
                    "UPDATE \"user\" SET username = ?, password = ? WHERE user_id = ?"
            );
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, userId);

            statement.executeUpdate(); //이 결과를 바탕으로 유저 객체 생성 후 반환하는건? no 실행 결과로 영향받은 레코드 수를 반환하므로 안됨

            statement = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE user_id = ?"
            );
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }

            //update 유저가 잘 수정되었는지 lastval로 id 꺼내서 업데이트된 유저 반환 -> update는 lastval 이 세션에 정의 되어 있지 않다고 오류
            //LASTVAL은 현재 세션에서 생성된 가장 최근의 값을 가져오는 데 사용-> update는 생성이 아니므로 latval  사용 못함
//            Integer updatedUserId = null;
//            statement = connection.prepareStatement(
//                    "SELECT lastval()"
//            );


//            resultSet = statement.executeQuery();

//            if (resultSet.next()) {
//                updatedUserId = resultSet.getInt("lastval");
//                System.out.println(updatedUserId);
//            }

//            statement = connection.prepareStatement(
//                    "SELECT * FROM \"user\" WHERE user_id = ?"
//            );
//            statement.setInt(1, updatedUserId);
//            resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                return new User(
//                        resultSet.getInt("user_id"),
//                        resultSet.getString("username"),
//                        resultSet.getString("password")
//                );
//            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 수정하는데 문제가 발생했습니다 id: " + userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    public void delete(Integer userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(
                    "DELETE FROM \"user\" WHERE user_id = ?"
            );
            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }
}

