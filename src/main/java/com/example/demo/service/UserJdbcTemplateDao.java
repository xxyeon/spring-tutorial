package com.example.demo.service;

import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJdbcTemplateDao {

    private final JdbcTemplate jdbcTemplate;

    //단일 조회: jdbcTemplate.queryForObject
    //다수 조회: jdbcTemplate.queryForStream
    public User findById(Integer userId) {
        //query
        String query = "SELECT * FROM \"user\" WHERE user_id = ?"; //먼저 쿼리로 어떤 유저 조회할지 쿼리 작성하고
        //쿼리 결과로 어떤 객체 반환할지

        return this.jdbcTemplate.queryForObject(
                query,
                (resultSet, rowNum) -> new User( // 여기서 유저 객체로 반환하기 위해 유저 객체 설정하고
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ),
                //파라미터에 들어갈 쿼리 파라미터
                userId //이건 위에 쿼리에서 ?에 들어가는 값 설정해준것입니다. 네넨
        );
    }

    public User save(User user) {
        String query = "INSERT INTO \"user\" (username, password) VALUES (?, ?)";
        //update(query, param)
        this.jdbcTemplate.update(query, user.getUsername(), user.getPassword()); //쿼
        String lastInsertQuery = "SELECT lastval()"; //insert한 후 db가 채번한 id를 받기위함
        //lastVal() 로 db가 채번해준 id 갸져와서 user 객체에 세팅 후 결과로 반환
        Integer createdUserId =  this.jdbcTemplate.queryForObject(
                lastInsertQuery,
                Integer.class //반환값을 설정하기 위해서 설정했습니다 네네 아하넵
        );
        String findUserQuery = "SELECT * FROM \"user\" WHERE user_id = ?";
        return this.jdbcTemplate.queryForObject(
                findUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ),
                createdUserId
        );
    }

    public User update(Integer userId, User user) {
        String query = "UPDATE \"user\" SET username = ?, password = ? WHERE user_id = ?";
        this.jdbcTemplate.update(
                //쿼리
                //반환 타입 ? -> 이게 질문인데, 그 save에서는 반환 타입을 명시했는데
//                 여기서는 명시하지 ㅏ않아도 잘 동작하는 이유가 궁금합니다 네

                //파라미터
                query,
                user.getUsername(),
                user.getPassword(),
                userId

        );

        String lastInsertQuery = "SELECT lastval()";
        //lastVal() 로 db가 채번해준 id 갸져와서 user 객체에 세팅 후 결과로 반환
        Integer getUserId = this.jdbcTemplate.queryForObject(
                lastInsertQuery,
                Integer.class
        );
        //아뇨


        String findUserQuery = "SELECT * FROM \"user\" WHERE user_id = ?";
        return this.jdbcTemplate.queryForObject(
                findUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ),
                getUserId
        );
    }

    public void delete(Integer userId) {
        String query = "DELETE FROM \"user\" WHERE user_id = ?";
        this.jdbcTemplate.update(
                query,
                userId //넵 넵네

        );
    }
}
