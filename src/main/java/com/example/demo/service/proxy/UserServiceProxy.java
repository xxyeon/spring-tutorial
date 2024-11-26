package com.example.demo.service.proxy;

import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.service.IUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public class UserServiceProxy implements IUserService {

    private final IUserService userService;
    private final PlatformTransactionManager transactionManager;

    @Override
    public UserResponseDto findById(Integer id) {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
        try {
            UserResponseDto result = userService.findById(id);
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "[UserProxy] 트랜잭션 실패");
        }
    }

    @Override
    public List<UserResponseDto> findAll() {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
        try {
            List<UserResponseDto> result = userService.findAll();
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "[UserProxy] 트랜잭션 실패");
        }

    }

    @Override
    public UserResponseDto save(UserRequestDto dto) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserResponseDto result = userService.save(dto);
            M
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "[UserProxy] 트랜잭션 실패");
        }
    }

    @Override
    public UserResponseDto update(Integer userId, UserRequestDto userDto) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserResponseDto result = userService.update(userId, userDto);
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "[UserProxy] 트랜잭션 실패");
        }
    }

    @Override
    public void deleteById(Integer id) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.deleteById(id);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "[UserProxy] 트랜잭션 실패");
        }
    }
}
