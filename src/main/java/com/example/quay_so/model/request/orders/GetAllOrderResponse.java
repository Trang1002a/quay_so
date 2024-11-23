package com.example.quay_so.model.request.orders;

import com.example.quay_so.model.response.TotalResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllOrderResponse {
    private List<OrderResponse> orderResponses;
    private TotalResponse totalResponse;
}
