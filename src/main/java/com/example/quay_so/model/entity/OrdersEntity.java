package com.example.quay_so.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_orders")
public class OrdersEntity {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "ACCOUNT_ID")
    private String accountId;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "TOTAL_PRICE")
    private Float totalPrice;
    @Column(name = "PAYMENT_METHODS")
    private String paymentMethods;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "DISCOUNT_CODE")
    private String discountCode;
    @Column(name = "DISCOUNT")
    private Float discount;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updatedAt;
}
