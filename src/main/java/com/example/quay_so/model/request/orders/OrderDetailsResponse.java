package com.example.quay_so.model.request.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponse {
    private String id;
    private String accountName;
    private String totalPrice;
    private String quantity;
    private String status;
    private String phoneNumber;
    private String address;
    private String createdAt;
    private String updatedAt;
    private String paymentMethods;
    private String note;
    private String discount;
    private List<OrderDetailResponse> detailResponseList;
}
