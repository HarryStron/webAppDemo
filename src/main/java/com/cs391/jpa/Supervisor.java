package com.cs391.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Supervisor  extends User implements Serializable {
    @NotNull
    String department;
    
    @NotNull
//    @Pattern(regexp = "^\\(?(\\d{4})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
//            message = "{invalid.phonenumber}")
    String phoneNum;
    
    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}
