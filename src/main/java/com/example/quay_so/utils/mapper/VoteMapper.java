package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.entity.VotesEntity;
import com.example.quay_so.model.request.rate.VoteResponse;
import org.apache.commons.lang3.ObjectUtils;

public class VoteMapper {
    public static VoteResponse mapVoteEntityToVoteResponses(VotesEntity entity, String bookName, String accountName) {
        VoteResponse response = new VoteResponse();
        response.setId(entity.getId());
        response.setAccountName(accountName);
        response.setBookName(bookName);
        response.setValue(entity.getValue().toString());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(ObjectUtils.isEmpty(entity.getUpdatedAt()) ? null : entity.getUpdatedAt().toString());
        return response;
    }

    public static VoteResponse mapVoteEntityToVoteResponses(VotesEntity entity, String accountName) {
        VoteResponse response = new VoteResponse();
        response.setId(entity.getId());
        response.setAccountName(accountName);
        response.setValue(entity.getValue().toString());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(ObjectUtils.isEmpty(entity.getUpdatedAt()) ? null : entity.getUpdatedAt().toString());
        return response;
    }
}
