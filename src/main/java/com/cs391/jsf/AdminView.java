package com.cs391.jsf;

import com.cs391.ejb.ProjectManagement;
import com.cs391.ejb.UserManagement;
import com.cs391.jpa.Project;
import com.cs391.jpa.ProjectTopic;
import com.cs391.jpa.Supervisor;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ConversationScoped
public class AdminView implements Serializable {
    private String supervisor;
    private String role;
    private boolean admin;
    private boolean supr;
    private boolean stud;
    private String registerSussexId;
    private String registerName;
    private String registerSurname;
    private String registerDepartment;
    private String registerEmail;
    private String registerPhoneNum;
    private String registerCourse;
    private String registerPassword;
    private String topicTitle;
    private String topicDescription;   
    
    @EJB
    private ProjectManagement projectManagement;
    
    @EJB
    private UserManagement userManagement;
    
    @Inject
    private Conversation conversation;
    
    @PostConstruct
    public void postCon() {
        conversation.begin();
    }
    
    @PreDestroy
    public void preDest() {
        conversation.end();
    }
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        return "index";
    }
     
    public void registerUser() {
        System.out.println(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user").toString());
role= "Administrator";
        switch (role) {
            case "Administrator":
                userManagement.registerAdmin(registerSussexId, registerName, registerSurname, registerEmail, registerPhoneNum, registerPassword);                             
                break;
            case "Supervisor":
                userManagement.registerSupervisor(registerSussexId, registerName, registerSurname, registerEmail, registerPhoneNum, registerDepartment, registerPassword);    
                break;
            case "Student":
                userManagement.registerStudent(registerSussexId, registerName, registerSurname, registerEmail, registerCourse, registerPassword);    
                break;
            default:
                break;
        }
    }
    
    public void registerTopic() {
        projectManagement.addNewTopic(topicTitle, topicDescription);
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public void setRole(String role) {
        admin = false;
        supr = false;
        stud = false;
        switch (role) {
            case "Administrator":
                admin = true;
                break;
            case "Supervisor":
                supr = true;
                break;
            case "Student":
                stud = true;
                break;
            default:
                break;
        }             
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isSupr() {
        return supr;
    }

    public boolean isStud() {
        return stud;
    }
    
    public void setRegisterSussexId(String registerSussexId) {
        this.registerSussexId = registerSussexId;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public void setRegisterSurname(String registerSurname) {
        this.registerSurname = registerSurname;
    }

    public void setRegisterDepartment(String registerDepartment) {
        this.registerDepartment = registerDepartment;
    }

    public void setRegisterEmail(String registerEmail) {
        this.registerEmail = registerEmail;
    }

    public void setRegisterPhoneNum(String registerPhoneNum) {
        this.registerPhoneNum = registerPhoneNum;
    }

    public void setRegisterCourse(String registerCourse) {
        this.registerCourse = registerCourse;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }

    public String getRole() {
        return role;
    }

    public List<Project> getProjects() {
        return projectManagement.getProjects(supervisor);
    }
     
    public List<Supervisor> getSupervisors() {
        return projectManagement.getSupervisors();
    }
    
    public List<ProjectTopic> getTopics() {
        return projectManagement.getTopics();
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getRegisterSussexId() {
        return registerSussexId;
    }

    public String getRegisterName() {
        return registerName;
    }

    public String getRegisterSurname() {
        return registerSurname;
    }

    public String getRegisterDepartment() {
        return registerDepartment;
    }

    public String getRegisterEmail() {
        return registerEmail;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public String getRegisterPhoneNum() {
        return registerPhoneNum;
    }

    public String getRegisterCourse() {
        return registerCourse;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }
    
}