package com.example.demo.controller;

import com.example.demo.controller.dto.UserRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService; //ProxyFactory 빈으로 주입된 UserService DI

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() throws SQLException {
        List<UserResponseDto> result = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    //crud
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Integer userId) throws SQLException {
        UserResponseDto result = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> save(@RequestBody UserRequestDto userDto) throws SQLException {
        UserResponseDto user = userService.save(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Integer userId, @RequestBody UserRequestDto userDto) throws SQLException {
        UserResponseDto user = userService.update(userId, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping ("/{userId}")
    public ResponseEntity<String> delete(@PathVariable Integer userId) throws SQLException {
        userService.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
}

