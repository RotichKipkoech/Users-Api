package com.safcom.users.repositories;

import com.safcom.users.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUserId(String userId);
}
