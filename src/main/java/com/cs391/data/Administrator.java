package com.cs391.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

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
