package com.example.quay_so.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_order_details")
public class OrderDetailsEntity {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "ORDER_ID")
    private String orderId;
    @Column(name = "BOOK_ID")
    private String bookId;
    @Column(name = "BOOK_Name")
    private String bookName;
    @Column(name = "PRICE")
    private Float price;
    @Column(name = "QUANTITY")
    private Integer quantity;
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
