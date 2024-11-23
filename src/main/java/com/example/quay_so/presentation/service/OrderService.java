package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.orders.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.order.CreateOrderResponse;

public interface OrderService {
    GetAllOrderResponse getAll(GetAllOrderRequest request);

    OrderDetailsResponse findById(String id);

    ResStatus updateStatus(UpdateStatusOrderRequest request);

    GetAllOrderResponse getAllCustomerOrder();

    OrderDetailsResponse getCustomerOrderDetails(String id);

    CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest);

    OrderTotalResponse total();
}
