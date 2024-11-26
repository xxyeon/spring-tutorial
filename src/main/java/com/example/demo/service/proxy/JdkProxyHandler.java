package com.example.demo.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public class JdkProxyHandler implements InvocationHandler {

    private final Object target; //IUserService, IMessageService,... 어떤 객체가 올지 모 -> 동적메서드 주입
    //수행할 부가기능 -> 트랜잭션을 수행할 것임
    private final PlatformTransactionManager transactionManger;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TransactionStatus status = transactionManger.getTransaction(new DefaultTransactionDefinition());
        try {
            Object result = method.invoke(target, args);
            transactionManger.commit(status);
            return result;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "[JdkProxyHandler] 트랜잭션 실패");
        }
    }
}
