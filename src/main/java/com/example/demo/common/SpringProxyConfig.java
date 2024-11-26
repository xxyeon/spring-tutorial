package com.example.demo.common;

import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SpringProxyConfig {
    private final UserService userService;
    private final PlatformTransactionManager transactionManger;

    @Bean
    @Primary
    public ProxyFactoryBean userServiceProxyFactoryBean() {
        ProxyFactoryBean userServiceProxyFactoryBean = new ProxyFactoryBean();
        userServiceProxyFactoryBean.setTarget(userService); //타켓을 proxyFactoryBean 넣기, userService는 이제 ProxyFactoryBean으주 주입됨
        userServiceProxyFactoryBean.setInterceptorNames("springProxyHandler"); //컴포넌트 스캔으로 등록된 빈 주입
        return userServiceProxyFactoryBean;
    }
}
