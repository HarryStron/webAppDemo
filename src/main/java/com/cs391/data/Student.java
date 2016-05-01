package com.cs391.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Student extends User implements Serializable{
    @NotNull
    String course;
    
    public Student() {}

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }
}
