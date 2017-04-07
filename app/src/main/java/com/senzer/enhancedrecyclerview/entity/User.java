package com.senzer.enhancedrecyclerview.entity;

import java.io.Serializable;

/**
 * ProjectName: User
 * Description:
 * 
 * review by chenpan, wangkang, wangdong 2017/4/7
 * edit by JeyZheng 2017/4/7
 * author: JeyZheng
 * version: 4.0
 * created at: 2017/4/7 14:51
 */
public class User implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
