package com.example.quay_so.model.request.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String id;
    private String bookName;
    private String accountName;
    private String value;
    private String status;
    private String createdAt;
    private String updatedAt;
}
