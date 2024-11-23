package com.example.quay_so.model.request.rate;

import com.example.quay_so.model.response.TotalResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentsResponse {
    private List<CommentResponse> commentResponses;
    private TotalResponse totalResponse;
}
