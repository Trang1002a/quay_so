package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.rate.*;
import com.example.quay_so.model.response.ResStatus;

public interface RateService {
    GetAllVotesResponse getAllVotes(GetAllVotesRequest request);

    GetAllCommentsResponse getAllComments(GetAllCommentsRequest request);

    VoteResponse findVoteById(String id);

    CommentResponse findCommentById(String id);

    ResStatus voteUpdate(VoteUpdateRequest request);

    ResStatus commentUpdate(CommentUpdateRequest request);

    ResStatus postComments(PostCommentRequest request);

    ResStatus postVotes(PostVoteRequest request);
}
