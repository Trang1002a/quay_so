package com.example.quay_so.presentation.controller.customer;

import com.example.quay_so.model.request.rate.PostVoteRequest;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.presentation.service.TestService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test/")
public class TestController {
    @Resource
    TestService testService;

    @PostMapping("/postVotes")
    public ResStatus postVotes(@RequestBody PostVoteRequest request) {
        return testService.postTestDownloadFile(request);
    }

    @GetMapping("/generatorPDF")
    public ResStatus generatorPDF() {
        return testService.generatorPDF();
    }
}
