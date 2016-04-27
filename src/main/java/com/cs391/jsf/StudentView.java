package com.cs391.jsf;

import com.cs391.jpa.Project;
import com.cs391.jpa.ProjectTopic;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
 
@Named
@ConversationScoped
public class StudentView implements Serializable{
    private List<ProjectTopic> topics;
    private List<Project> projects;
    private List<String> supervisors;
    private String supervisor;
    private String notifications;
    private List<ProjectTopic> selectedTopics;

    @PostConstruct
    public void init() {
    }
    
    public void onSuperChange() {
        if(supervisor !=null && !supervisor.equals("")){
            
        }
    }
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        return "index";
    }
    public void submitProject() {}
    
    public List<ProjectTopic> getSelectedTopics() {
        return selectedTopics;
    }
 
    public void setSelectedTopics(List<ProjectTopic> selectedTopics) {
        this.selectedTopics = selectedTopics;
    }
 
    public List<ProjectTopic> getTopics() {
        return topics;
    }

    public List<Project> getProjects() {
        return projects;
    }
    
    public List<String> getSupervisors() {
        return supervisors;
    }

    public void setTopics(List<ProjectTopic> topics) {
        this.topics = topics;
    }
    
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setSupervisors(List<String> supervisors) {
        this.supervisors = supervisors;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSupervisor() {
        return supervisor;
    }
}