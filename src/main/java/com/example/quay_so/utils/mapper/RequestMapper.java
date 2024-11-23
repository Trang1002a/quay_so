package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.dto.TransDetailDto;
import com.example.quay_so.model.entity.RequestEntity;

public class RequestMapper {
    public static TransDetailDto mapRequestEntityToTransDetailDto(RequestEntity requestEntity) {
        TransDetailDto transDetailDto = new TransDetailDto();
        transDetailDto.setId(requestEntity.getId());
        transDetailDto.setRequestType(requestEntity.getRequestType());
        transDetailDto.setCreatedAt(requestEntity.getCreatedAt().toString());
        transDetailDto.setCreatedBy(requestEntity.getCreatedBy());
        transDetailDto.setStatus(requestEntity.getStatus());
        return transDetailDto;
    }
}
