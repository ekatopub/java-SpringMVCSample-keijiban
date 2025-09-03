package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")//テーブル名
public class User {

    @Id
    @Column(name = "user_id", length = 4, nullable = false)
    private String userId;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "user_name", length = 10, nullable = false)
    private String userName;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setUserNamed(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return this.userId;
    }
    public String getPassword() {
        return this.password;
    }
    
    public String getUserName() {
        return this.userName;
    }
}