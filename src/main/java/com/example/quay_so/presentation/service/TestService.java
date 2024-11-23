package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.rate.PostVoteRequest;
import com.example.quay_so.model.response.ResStatus;

public interface TestService {
    ResStatus postTestDownloadFile(PostVoteRequest request);

    ResStatus generatorPDF();
}
