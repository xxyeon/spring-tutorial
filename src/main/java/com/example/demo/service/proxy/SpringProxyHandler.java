package com.example.demo.service.proxy;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class SpringProxyHandler implements MethodInterceptor {

    private final PlatformTransactionManager transactionManger;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = transactionManger.getTransaction(
                new DefaultTransactionDefinition());
        try {
            Object result = invocation.proceed(); //타겟 오브젝트를 내부적으로 실행
            transactionManger.commit(status);
            return result;
        }catch (Exception e) {
            transactionManger.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "[SpringProxyHandler] 트랜잭션 실패");
        }

    }
}
