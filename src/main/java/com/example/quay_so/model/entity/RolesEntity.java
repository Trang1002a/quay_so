package com.example.quay_so.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_roles")
public class RolesEntity {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "ROLE_NAME")
    private String role_name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updatedAt;
}
