package com.example.demo.service;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class UserRepository implements IRepository<Integer, User> {
    private static final Map<Integer, User> users;

    static {
        users = new HashMap<>();
        users.put(1, new User(1, "Aaron", 10, "Developer", "Backend", LocalDateTime.now().plusMinutes(10)));
        users.put(2, new User(2, "Baron", 20, "Developer", "Frontend", LocalDateTime.now().plusMinutes(20)));
        users.put(3, new User(3, "Caron", 30, "Engineer", "DevOps/SRE", LocalDateTime.now().plusMinutes(30)));
    }

    public User findById(Integer id) {
        Optional<User> retrieved = Optional.ofNullable(users.get(id));
        return retrieved.orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다. id : " + id));
    }

    public List<User> findAll() {
        return users.values().stream().toList();
    }

    public User save(User entity) {
        int generatedId = users.size() + 1;
//        entity.setId(generatedId);
        users.put(generatedId, entity);
        return users.get(generatedId);
    }
}
