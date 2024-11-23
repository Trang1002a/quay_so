package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.RequestCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestCustomerRepository extends JpaRepository<RequestCustomerEntity, String> {
    Optional<RequestCustomerEntity> findByIdAndStatus(String id, String status);
}
