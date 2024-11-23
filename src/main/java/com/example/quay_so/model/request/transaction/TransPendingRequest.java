package com.example.quay_so.model.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransPendingRequest {
    private List<String> requestType;
    private String createdBy;
    private PageRequest pageRequest;
}
