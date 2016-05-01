package com.cs391.web;

import com.cs391.service.ProjectManagement;
import com.cs391.service.UserManagement;
import com.cs391.data.Project;
import com.cs391.data.ProjectTopic;
import com.cs391.data.Student;
import com.cs391.data.Supervisor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
 
@Named
@ViewScoped
public class StudentView implements Serializable{
    private String projectTitle;
    private String projectDescription;
    private String projectSkills;
    private List<ProjectTopic> projectTopics;
    private String supervisor;

    @EJB
    private ProjectManagement projectManagement;
    
    @EJB
    private UserManagement userManagement;
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        MessageController.displayMessage("User logged out");
        return "index?faces-redirect=true";
    }
    
    public List<Project> getProjects() {
        return projectManagement.getAvailableProjects(null);
    }
    
    public Project getMyProject() {
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");    
        return projectManagement.getSelectedProject(student);
    }
    
    public void selectProject(int projectId) {
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");        
        if(!projectManagement.hasSelectedProject(student)) {            
            projectManagement.selectProject(projectId, student);
            MessageController.displayMessage("Project selected");
        } else {
            MessageController.displayMessage("You have already selected a project");
        }
    }
    
    public boolean isSelected() {
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");   
        return projectManagement.hasSelectedProject(student);
    }
    
    public void proposeProject() {
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");   
        if(!projectManagement.hasSelectedProject(student)) {            
            projectManagement.proposeProject(projectTitle, projectDescription, projectSkills, projectTopics, (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"), userManagement.getSuperFromID(supervisor));
            MessageController.displayMessage("Project proposed");
        } else {
            MessageController.displayMessage("You have already selected a project");
        }
        clearForm();
    }
    
    private void clearForm() {
        projectTitle = null;
        projectDescription = null;
        projectSkills = null;
        projectTopics = null;
        supervisor = null;
    }
    
    public List<Supervisor> getSupervisors() {
        return userManagement.getSupervisors();
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

    public String getSupervisor() {
            return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public List<ProjectTopic> getProjectTopics() {
        return projectManagement.getTopics();
    }

    public void setProjectTopics(List<String> projectTopicIds) {
        projectTopics = new ArrayList<>();
        for (String s : projectTopicIds) {
            projectTopics.add(projectManagement.getTopicByID(Integer.parseInt(s)));
        } 
    }

    public ProjectManagement getProjectManagement() {
        return projectManagement;
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

    public void setProjectManagement(ProjectManagement projectManagement) {
        this.projectManagement = projectManagement;
    }
}