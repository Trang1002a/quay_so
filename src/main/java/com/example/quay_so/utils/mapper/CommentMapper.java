package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.entity.CommentsEntity;
import com.example.quay_so.model.request.rate.CommentResponse;
import org.apache.commons.lang3.ObjectUtils;

public class CommentMapper {
    public static CommentResponse mapCommentEntityToCommentResponses(CommentsEntity entity, String bookName, String accountName) {
        CommentResponse response = new CommentResponse();
        response.setId(entity.getId());
        response.setAccountName(accountName);
        response.setBookName(bookName);
        response.setValue(entity.getValue());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(ObjectUtils.isEmpty(entity.getUpdatedAt()) ? null : entity.getUpdatedAt().toString());
        return response;
    }

    public static CommentResponse mapCommentEntityToCommentResponse(CommentsEntity entity, String accountName) {
        CommentResponse response = new CommentResponse();
        response.setId(entity.getId());
        response.setValue(entity.getValue());
        response.setAccountName(accountName);
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(ObjectUtils.isEmpty(entity.getUpdatedAt()) ? null : entity.getUpdatedAt().toString());
        return response;
    }
}
