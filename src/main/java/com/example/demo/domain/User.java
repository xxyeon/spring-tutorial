package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    @Id //이걸로 pk 설정해주고
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 이건 id 채번해주늕 전략을 db에게 준걸로 했습니다
    Integer userId; //이걸 pk로 설정했고

    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
