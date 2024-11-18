package com.example.demo.controller;

import com.example.demo.controller.dto.UserCreateRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.service.User;
import com.example.demo.service.UserJdbcApiDao;
import com.example.demo.service.UserJdbcTemplateDao;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {
    UserJdbcTemplateDao userService;

    @GetMapping("")
    public ResponseEntity<List<User>> users() {
        try {
            List<User> users = userService.findAll();
            return ResponseEntity
//                  .status(HttpStatusCode.valueOf(200))
                    .status(HttpStatus.OK)      // 1. HTTP Status Code
                    .body(users);               // 2. 결과 객체(List<User>)
        } catch (RuntimeException e) {
            return ResponseEntity
//                  .status(HttpStatusCode.valueOf(404))
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> user(@PathVariable Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity
//                  .status(HttpStatusCode.valueOf(200))
                    .status(HttpStatus.OK)      // 1. HTTP Status Code
                    .body(user);                // 2. 결과 객체(User)
        } catch (RuntimeException e) {
            return ResponseEntity
//                  .status(HttpStatusCode.valueOf(404))
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<User> save(@RequestBody @Valid UserCreateRequestDto request) throws SQLException {
        User user = userService.save(request.getName(), request.getAge(), request.getJob(), request.getSpecialty());
        return ResponseEntity
//              .status(HttpStatusCode.valueOf(201))
                .status(HttpStatus.CREATED) // 1. HTTP Status Code
                .body(user);                // 2. 결과 객체(User)
    }
}
