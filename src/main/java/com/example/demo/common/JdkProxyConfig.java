package com.example.demo.common;

import com.example.demo.service.IUserService;
import com.example.demo.service.proxy.JdkProxyHandler;
import java.lang.reflect.Proxy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JdkProxyConfig {
    //프록시를 직접 구현해주지 않아도된다. Java Reflection API 활용
    //UserServiced(target), PlatfromTransactionManager(부가기능) 만 명시해서 이 둘을 가지고 프록시 생성해주도록
    private final IUserService userService;
    private final PlatformTransactionManager transactionManager;

    @Bean //IUserService 타입으로 userServiceJdkProxy 이름의 빈이 등록 -> 컨트롤러에서 사용
    public IUserService userServiceJdkProxy() {
        IUserService userServiceProxy = (IUserService) Proxy.newProxyInstance(
                IUserService.class.getClassLoader(),
                new Class[]{IUserService.class},
                new JdkProxyHandler(userService, transactionManager) //개발자가 구현한 InvocationHandler 사용
        );
        return userServiceProxy;
    }
}
