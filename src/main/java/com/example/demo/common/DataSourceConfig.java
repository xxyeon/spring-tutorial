package com.example.demo.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String dirver;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(dirver);

        //커넥션 풀에서 커넥션 가져오기
        //driver manager는 물리적 접속, datasource는 논리적 접속(커넥션 풀)
        //먼저 커넥션 풀 생성하고 리천
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        return hikariDataSource;
    }


}
