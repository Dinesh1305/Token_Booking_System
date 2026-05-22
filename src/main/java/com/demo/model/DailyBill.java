package com.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dailybill")
public class DailyBill {

    @Id
    private String email;
    
    private Integer count;
    
    private Integer ott;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public Integer getOtt() { return ott; }
    public void setOtt(Integer ott) { this.ott = ott; }
}