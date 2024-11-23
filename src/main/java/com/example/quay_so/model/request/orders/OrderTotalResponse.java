package com.example.quay_so.model.request.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTotalResponse {
    private Double totalBalance;
    private Integer totalOrder;
    private Integer totalBook;
}
