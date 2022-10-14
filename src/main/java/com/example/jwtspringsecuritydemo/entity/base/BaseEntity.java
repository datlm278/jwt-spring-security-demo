package com.example.jwtspringsecuritydemo.entity.base;

import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class BaseEntity {

    @Column(name = "status")
    private Long status;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name= "update_time")
    private Timestamp updateTime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "create_by")
    private String createBy;
}
