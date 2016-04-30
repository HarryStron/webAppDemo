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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class SuperView implements Serializable {
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
    
    @PostConstruct
    public void init() {
    }
        
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        MessageController.displayMessage("User logged out");
        return "index";
    }
    
    public void registerProject() {
        Supervisor projectSupervisor = (Supervisor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if(!projectManagement.addNewProject(projectTitle, projectDescription, projectSkills, projectTopics, projectSupervisor)){
            MessageController.displayMessage("Project title already exists");
        } else {
            MessageController.displayMessage("Project has been registered");
        }
    }
    
    public void registerTopic() {
        if(projectManagement.addNewTopic(topicTitle, topicDescription)) {
            MessageController.displayMessage("Topic has been registered");
            topicTitle = null;
            topicDescription = null;
        } else {
            MessageController.displayMessage("Topic title already exists");
        }
    }
    
    public List<Project> getProposals() {
        return projectManagement.getProposedProjects(((Supervisor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user")).getSussexID());
    }
    
    public void acceptProposal(int id) {
        projectManagement.editProjectStatus(id, Project.Status.ACCEPTED);
        MessageController.displayMessage("Project proposal has been accepted");
    }
    
    public void declineProposal(Project proposal) {
        projectManagement.removeProject(proposal);
        MessageController.displayMessage("Project proposal has been declined. Project has been removed from DB");
    }
    
    public void declineSelected(int selectedId) {
        projectManagement.editProjectStatus(selectedId, Project.Status.AVAILABLE);
        projectManagement.removeOwner(selectedId);
        MessageController.displayMessage("Project proposal has been declined. Project can now be requested from another student");
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
        if(getProposals().size()>0)
            return "You have a new proposal. Please revise it.";
        else if (getSelected().size()>0)
            return "A student has selected a project. Please revise it.";
        else
            return "No notifications";
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
    
    public List<Project> getSelected() {
        return projectManagement.getSelectedProjects(((Supervisor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user")).getSussexID());
    }
     
    public List<Supervisor> getSupervisors() {
        return userManagement.getSupervisors();
    }
    
    public List<ProjectTopic> getTopics() {
        return projectManagement.getTopics();
    }
}