package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.OrderDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailsEntity, String> {
    List<OrderDetailsEntity> findAllByOrderId(String orderId);

    List<OrderDetailsEntity> findAllByStatusInAndCreatedAtAfter(List<String> status, Date oneMonthAgo);
}
