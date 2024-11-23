package com.example.quay_so.utils;

import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.model.entity.RequestEntity;
import com.example.quay_so.model.repository.RequestRepository;
import com.example.quay_so.model.response.transaction.CreateTransResponse;
import com.example.quay_so.utils.validate.ConvertUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Component
public class RequestUtils {
    @Resource
    RequestRepository requestRepository;

    public CreateTransResponse responseCreateTrans(UserInfoDto userInfoDto, FunctionCode functionCode, Object data) {
        RequestEntity requestEntity = saveRequest(data, userInfoDto, functionCode);
        CreateTransResponse createTransResponse = new CreateTransResponse();
        createTransResponse.setRequestId(requestEntity.getId());
        createTransResponse.setOtpId(requestEntity.getOtpId());
        createTransResponse.setPhoneNumber(UserInfoService.maskPhoneNumber(userInfoDto.getPhoneNumber()));
        return createTransResponse;
    }

    public RequestEntity saveRequest(Object data, UserInfoDto userInfoDto, FunctionCode functionCode) {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(UUID.randomUUID().toString());
        requestEntity.setUserId(userInfoDto.getUserId());
        requestEntity.setRequestType(functionCode.name());
        requestEntity.setRequestBody(ConvertUtils.toJson(data));
        requestEntity.setStatus(TransactionStatus.INIT.name());
//        requestEntity.setOtpId(UUID.randomUUID().toString());
        requestEntity.setCreatedAt(new Date(System.currentTimeMillis()));
        requestEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
        requestEntity.setCreatedBy(userInfoDto.getUserName());
        requestRepository.save(requestEntity);
        return requestEntity;
    }
}
