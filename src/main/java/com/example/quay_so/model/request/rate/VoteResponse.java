package com.example.quay_so.model.request.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponse {
    private String id;
    private String accountName;
    private String bookName;
    private String value;
    private String status;
    private String createdAt;
    private String updatedAt;
}
