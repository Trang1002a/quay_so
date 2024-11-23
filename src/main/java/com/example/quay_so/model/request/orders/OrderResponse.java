package com.example.quay_so.model.request.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;
    private String accountName;
    private String totalPrice;
    private String quantity;
    private String status;
    private String createdAt;
    private String updatedAt;
}
