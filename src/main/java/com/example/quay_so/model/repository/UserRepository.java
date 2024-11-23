package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, String> {
    Optional<UsersEntity> findByUserNameAndStatus(String userName, String status);

    Optional<UsersEntity> findByUserName(String userName);

    Optional<UsersEntity> findByPhoneNumber(String phoneNumber);

    Optional<UsersEntity> findByEmail(String email);
}
