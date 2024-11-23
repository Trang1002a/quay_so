package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.entity.OrderDetailsEntity;
import com.example.quay_so.model.entity.OrdersEntity;
import com.example.quay_so.model.request.orders.OrderDetailResponse;
import com.example.quay_so.model.request.orders.OrderResponse;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;

public class OrderMapper {
    public static OrderResponse mapOrderEntityToOrderResponse(OrdersEntity entity) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(entity.getId());
        orderResponse.setAccountName(entity.getAccountName());
        orderResponse.setTotalPrice(entity.getTotalPrice().toString());
        orderResponse.setQuantity(entity.getQuantity().toString());
        orderResponse.setStatus(entity.getStatus());
        orderResponse.setCreatedAt(entity.getCreatedAt().toString());
        orderResponse.setUpdatedAt(ObjectUtils.isEmpty(entity.getUpdatedAt()) ? null : entity.getUpdatedAt().toString());
        return orderResponse;
    }

    public static OrderDetailResponse mapToOrderDetailResponse(OrderDetailsEntity entity, Map<String,String> bookImage) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.setBookId(entity.getBookId());
        response.setBookName(entity.getBookName());
        response.setPrice(entity.getPrice().toString());
        response.setQuantity(entity.getQuantity().toString());
        response.setStatus(entity.getStatus());
        response.setImage(bookImage.get(entity.getBookId()));
        return response;
    }
}
