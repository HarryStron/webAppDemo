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
import javax.faces.context.FacesContext;
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
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        return "index";
    }
    
    public void registerProject() {
        Supervisor projectSupervisor = (Supervisor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        projectManagement.addNewProject(projectTitle, projectDescription, projectSkills, projectTopics, projectSupervisor);
    }
    
    public void registerTopic() {
        projectManagement.addNewTopic(topicTitle, topicDescription);
    }
    
    public List<Project> getProposals() {
        return projectManagement.getProposedProjects(((Supervisor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user")).getSussexID());
    }
    
    public void acceptProposal(int id) {
        projectManagement.editProjectStatus(id, Project.Status.ACCEPTED);
    }
    
    public void declineProposal(Project proposal) {
        projectManagement.removeProject(proposal);
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
    
    public void setTopics(List<String> projectTopics){
        this.projectTopics = new ArrayList<>();
        for (String id : projectTopics) {
            this.projectTopics.add(projectManagement.getTopicByID(Integer.parseInt(id)));
        }
    }

    public String getNotifications() {
        if(getProposals().size()<1)
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
        return projectManagement.getProjects(((Supervisor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user")).getSussexID());
    }
     
    public List<Supervisor> getSupervisors() {
        return projectManagement.getSupervisors();
    }
    
    public List<ProjectTopic> getTopics() {
        return projectManagement.getTopics();
    }
}