package com.example.quay_so.presentation.controller.customer;

import com.bookstore.quay_so.model.request.rate.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.presentation.service.RateService;
import com.example.quay_so.model.request.rate.PostCommentRequest;
import com.example.quay_so.model.request.rate.PostVoteRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/customer/rate")
public class RateCustomerController {
    @Resource
    RateService rateService;

    @PostMapping("/postVotes")
    public ResStatus postVotes(@RequestBody PostVoteRequest request) {
        return rateService.postVotes(request);
    }

    @PostMapping("/postComments")
    public ResStatus postComments(@RequestBody PostCommentRequest request) {
        return rateService.postComments(request);
    }
}
