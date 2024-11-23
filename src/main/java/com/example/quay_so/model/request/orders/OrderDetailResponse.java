package com.example.quay_so.model.request.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private String bookId;
    private String bookName;
    private String price;
    private String quantity;
    private String image;
    private String status;
}
