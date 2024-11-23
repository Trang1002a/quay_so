package com.example.quay_so.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_books")
public class BooksEntity {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "PUBLISH_YEAR")
    private String publishYear;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE")
    private Float price;
    @Column(name = "INVENTORY")
    private Integer inventory;
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
    @Column(name = "SOLD")
    private Integer sold;
    @Column(name = "IMAGE")
    private String image;
}
