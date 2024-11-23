package com.example.quay_so.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_request_customer")
public class RequestCustomerEntity {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "REQUEST_TYPE")
    private String requestType;
    @Column(name = "REQUEST_BODY")
    private String requestBody;
    @Column(name = "OTP_ID")
    private String otpId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "CREATED_AT")
    private Date createdAt;
}
