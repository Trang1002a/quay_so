package com.example.quay_so.model.request.orders;

import com.example.quay_so.model.request.transaction.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllOrderRequest {
    private String accountName;
    private List<String> status;
    private PageRequest pageRequest;
}
