package com.example.demo.service.proxy;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public class CglibProxyHandler implements MethodInterceptor {
    private final Object target;
    private final PlatformTransactionManager transactionManger; //부가기능

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        TransactionStatus status = transactionManger.getTransaction(
                new DefaultTransactionDefinition());
        try {
            Object result = method.invoke(target, args);
            transactionManger.commit(status);
            return result;
        } catch (Exception e) {
            transactionManger.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "[CglibProxyHandler] 트랜잭션 실패");
        }
    }
}
