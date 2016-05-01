package com.cs391.data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Project implements Serializable {
    public enum Status {
        AVAILABLE, ACCEPTED, PROPOSED, SELECTED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    
    @NotNull
    String title;
    
    @JoinTable
    @ManyToMany(targetEntity=ProjectTopic.class)
    @NotNull
    List<ProjectTopic> topic;
    
    @NotNull
    String description;
    
    @NotNull
    String requiredSkills;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    Status status;
    
    @OneToOne(targetEntity = Student.class)
    Student owner;
    
    @OneToOne(targetEntity = Supervisor.class)
    @NotNull
    Supervisor supervisor;
    
    public Project() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<ProjectTopic> getTopic() {
        return topic;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public Status getStatus() {
        return status;
    }

    public Student getOwner() {
        return owner;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopic(List<ProjectTopic> topic) {
        this.topic = topic;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.title);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

        
}
