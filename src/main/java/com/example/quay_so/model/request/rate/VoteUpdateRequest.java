package com.example.quay_so.model.request.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteUpdateRequest {
    private String id;
    private String status;
}