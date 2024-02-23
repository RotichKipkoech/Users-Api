package com.safcom.users.controller;

import com.safcom.users.entities.Users;
import com.safcom.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        user.setCreatedAt(LocalDateTime.now());
        Users savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawFunds(@RequestBody WithdrawalRequest withdrawalRequest) {
        String userId = withdrawalRequest.getUserId();
        double amount = withdrawalRequest.getAmount();

        Users user = userRepository.findByUserId(userId);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }

        if (!user.isActive()) {
            return new ResponseEntity<>("User is not active", HttpStatus.BAD_REQUEST);
        }

        if (amount % 100 != 0) {
            return new ResponseEntity<>("Withdrawal amount must be a multiple of 100", HttpStatus.BAD_REQUEST);
        }

        if (user.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.BAD_REQUEST);
        }

        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        return new ResponseEntity<>("Withdrawal successful", HttpStatus.OK);
    }
}
}
