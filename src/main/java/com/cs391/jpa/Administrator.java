package com.cs391.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Administrator  extends User implements Serializable {
    @NotNull
    String phoneNum;
    
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}
