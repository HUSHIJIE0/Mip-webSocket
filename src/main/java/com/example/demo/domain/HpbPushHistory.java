package com.example.demo.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 *@Author: Michael
 *@Date: Created in 16:14 2018/6/14
 *@Desciption: 
*/
@Entity
@Data
@Table(name = "HPB_PUSH_HISTORY")
public class HpbPushHistory{

    @Column(name = "COME_FROM")
    private String comeFrom;

    @Column(name = "PUSH_TO")
    private String pushTo;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CREATE_AT")
    private Long createDate = System.currentTimeMillis();

    @Column(name = "DATA",length = 2000)
    private String data;

    @Column(name = "READED")
    private Number readed;

    @Id
    @Column(nullable = false)
    private String id = UUID.randomUUID().toString().replace("-","");


}


