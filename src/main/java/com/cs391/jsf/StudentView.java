package com.cs391.jsf;

import com.cs391.ejb.ProjectManagement;
import com.cs391.ejb.UserManagement;
import com.cs391.jpa.Project;
import com.cs391.jpa.ProjectTopic;
import com.cs391.jpa.Student;
import com.cs391.jpa.Supervisor;
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
        return "index";
    }
    
    public List<Project> getProjects() {
        return projectManagement.getProjects(null);
    }
    
    public void selectProject(int projectId) {
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");        
        if(!projectManagement.hasSelectedProject(student)) {            
            projectManagement.selectProject(projectId, student);
        }
    }
    
    public void proposeProject() {
        projectManagement.proposeProject(projectTitle, projectDescription, projectSkills, projectTopics, (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"), userManagement.getSuperFromID(supervisor));
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