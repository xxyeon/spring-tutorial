package com.example.demo.common;

import com.example.demo.service.UserService;
import com.example.demo.service.proxy.CglibProxyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CglibProxyConfig {
    private final UserService userService;
    private final PlatformTransactionManager transactionManger;

    @Bean //인터페이스 대신 구현체를 반환
    public UserService userServiceCglibProxy() {
        UserService userServiceProxy = (UserService) Enhancer.create(UserService.class,
        new CglibProxyHandler(userService, transactionManger)); //(superClass, methodInterceptor(부가기능수행할 클래스))
        return userServiceProxy;
    }
}
