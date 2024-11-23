package com.example.quay_so.model.request.books;

import com.example.quay_so.model.request.rate.VoteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotesByIdResponse {
    private List<VoteResponse> voteResponses;
}
