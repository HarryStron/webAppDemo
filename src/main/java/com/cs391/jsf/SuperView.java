package com.cs391.jsf;

import com.cs391.ejb.ProjectManagement;
import com.cs391.ejb.UserManagement;
import com.cs391.jpa.Project;
import com.cs391.jpa.ProjectTopic;
import com.cs391.jpa.Supervisor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;


@Named
@ConversationScoped
public class SuperView implements Serializable {
    private String notifications;
    private String topicTitle;
    private String topicDescription; 
    private String projectTitle;
    private String projectDescription;
    private String projectSkills;
    private List<ProjectTopic> projectTopics;
    private Supervisor projectSupervisor;

    @EJB
    private ProjectManagement projectManagement;
    
    @EJB
    private UserManagement userManagement;
    
    @Inject
    private Conversation conversation;
    
    @PostConstruct
    public void init() {
    }
        
    public String logout() {
        return "index";
    }
    
    public void registerProject() {
        projectSupervisor = (Supervisor) userManagement.getUserByID("cs392");
        projectTopics = new ArrayList<>();
        projectManagement.addNewProject(projectTitle, projectDescription, projectSkills, projectTopics, projectSupervisor);
    }
    
    public void registerTopic() {
        projectManagement.addNewTopic(topicTitle, topicDescription);
    }
    
    public List<Project> getProposals() {
        return projectManagement.getProposedProjects("cs392"); //CHANGE TO CURRENT SUPERVISOR
    }
    
    public void acceptProposal(int id) {
        projectManagement.editProjectStatus(id, Project.Status.ACCEPTED);
    }
    
    public void declineProposal() {
        //remove project from db
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectSkills() {
        return projectSkills;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setProjectSkills(String projectSkills) {
        this.projectSkills = projectSkills;
    }

    public String getNotifications() {
        if((Math.random()*10)>5)
            return "No notifications";
        else
            return "You got an new proposal";
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }
    
    public List<Project> getProjects() {
        return projectManagement.getProjects(null); // CHANGE THIS TO CURRENTLY LOGGED IN SUPERVISOR
    }
     
    public List<Supervisor> getSupervisors() {
        return projectManagement.getSupervisors();
    }
    
    public List<ProjectTopic> getTopics() {
        return projectManagement.getTopics();
    }
}