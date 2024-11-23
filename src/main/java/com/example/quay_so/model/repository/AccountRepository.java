package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountsEntity, String> {
    Optional<AccountsEntity> findByUserNameAndStatus(String userName, String status);

    Optional<AccountsEntity> findByUserName(String userName);

    Optional<AccountsEntity> findByPhoneNumber(String phoneNumber);

    Optional<AccountsEntity> findByEmail(String email);

    List<AccountsEntity> findAllByUserNameContaining(String userName);
}
