package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RolesEntity, String> {
    Optional<RolesEntity> findByIdAndStatus(String id, String status);
}
