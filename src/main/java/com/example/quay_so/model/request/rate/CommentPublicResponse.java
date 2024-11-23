package com.example.quay_so.model.request.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPublicResponse {
    private String id;
    private String value;
    private String accountName;
    private String status;
    private String createdAt;
    private List<CommentResponse> children;
}
