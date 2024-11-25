package com.example.quay_so.presentation.controller.admin;

import com.example.quay_so.model.request.rate.*;
import com.example.quay_so.model.request.rate.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.presentation.service.RateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/rate")
public class RateController {

    @Resource
    RateService rateService;

    @PostMapping("/getAllVotes")
    public GetAllVotesResponse getAllVotes(@RequestBody GetAllVotesRequest request) {
        return rateService.getAllVotes(request);
    }

    @PostMapping("/getAllComments")
    public GetAllCommentsResponse getAllComments(@RequestBody GetAllCommentsRequest request) {
        return rateService.getAllComments(request);
    }

    @GetMapping("/vote/{id}")
    public VoteResponse findVoteById(@PathVariable("id") String id) {
        return rateService.findVoteById(id);
    }

    @GetMapping("/comment/{id}")
    public CommentResponse findCommentById(@PathVariable("id") String id) {
        return rateService.findCommentById(id);
    }

    @PostMapping("/vote/update")
    public ResStatus voteUpdate(@RequestBody VoteUpdateRequest request) {
        return rateService.voteUpdate(request);
    }

    @PostMapping("/comment/update")
    public ResStatus commentUpdate(@RequestBody CommentUpdateRequest request) {
        return rateService.commentUpdate(request);
    }
}
