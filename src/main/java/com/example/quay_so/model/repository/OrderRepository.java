package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.OrdersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrdersEntity, String> {

    @Query("SELECT o FROM OrdersEntity o " +
            "WHERE ((COALESCE(:status , '0') = '0' OR o.status IN :status)) " +
            "AND (:accountName IS NULL OR o.accountName LIKE %:accountName%)")
    Page<OrdersEntity> findOrdersWithRequest(String accountName, List<String> status, Pageable pageable);

    @Query("SELECT o FROM OrdersEntity o WHERE o.status = :status AND o.createdAt >= :oneMonthAgo")
    List<OrdersEntity> findOrdersOneMonthAgoWithSuccessStatus(String status, Date oneMonthAgo);

    List<OrdersEntity> findAllByAccountIdOrderByCreatedAtDesc(String accountId);
}
