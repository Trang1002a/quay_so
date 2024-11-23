package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.RequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, String> {
    List<RequestEntity> findByIdInAndStatus(List<String> id, String status);

    List<RequestEntity> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, String status);

    List<RequestEntity> findAllByStatusOrderByCreatedAtDesc(String status);

    @Query("SELECT r FROM RequestEntity r " +
            "WHERE r.status = :status " +
            "AND ((COALESCE(:requestType , '0') = '0' OR r.requestType IN :requestType)) " +
            "AND (:createdBy IS NULL OR r.createdBy LIKE %:createdBy%) ")
    Page<RequestEntity> findTransPendingWithRequest(String status,
                                                    List<String> requestType,
                                                    String createdBy,
                                                    Pageable pageable);

}
