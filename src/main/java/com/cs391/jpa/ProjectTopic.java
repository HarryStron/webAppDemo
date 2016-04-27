package com.cs391.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class ProjectTopic implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer topicId;
    
    @NotNull
    String topic;
    
    @NotNull
    String description;
    
    @JoinTable
    @ManyToMany(targetEntity=Project.class)
    List<Project> projects;

    public int getTopicId() {
        return topicId;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.topicId);
        hash = 31 * hash + Objects.hashCode(this.topic);
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
        final ProjectTopic other = (ProjectTopic) obj;
        if (!Objects.equals(this.topic, other.topic)) {
            return false;
        }
        if (!Objects.equals(this.topicId, other.topicId)) {
            return false;
        }
        return true;
    }

       
}
