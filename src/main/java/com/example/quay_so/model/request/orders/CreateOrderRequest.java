package com.example.quay_so.model.request.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String phoneNumber;
    private String address;
    private String paymentMethods;
    private String note;
    private String discount;
    List<BookOrder> bookOrders;
}
