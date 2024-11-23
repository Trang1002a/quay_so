package com.example.quay_so.model.request.books;

import com.example.quay_so.model.request.rate.CommentPublicResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsByIdResponse {
    private List<CommentPublicResponse> commentPublicResponses;
}
