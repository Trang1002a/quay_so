package com.example.quay_so.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_votes")
public class VotesEntity {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "BOOK_ID")
    private String bookId;
    @Column(name = "ACCOUNT_ID")
    private String accountId;
    @Column(name = "VALUE")
    private Float value;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updatedAt;
}
