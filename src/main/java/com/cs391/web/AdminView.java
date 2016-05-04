package com.cs391.web;

import com.cs391.data.Log;
import com.cs391.service.ProjectManagement;
import com.cs391.service.UserManagement;
import com.cs391.data.Project;
import com.cs391.data.ProjectTopic;
import com.cs391.data.Student;
import com.cs391.data.Supervisor;
import com.cs391.data.User;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class AdminView implements Serializable {
    private String supervisor;
    private String role;
    private String user;
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
    private String userStatus;
    
    @EJB
    private ProjectManagement projectManagement;
    
    @EJB
    private UserManagement userManagement;
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        User currentUser = userManagement.getUserByID(context.getExternalContext().getUserPrincipal().toString());
        context.getExternalContext().getSessionMap().put("user", currentUser);
    }
    
    public void registerUser() {
        boolean done = false;
        try {
            switch (role) {
                case "Administrator":
                    done = userManagement.registerAdmin(registerSussexId, registerName, registerSurname, registerEmail, registerPhoneNum, registerPassword);                             
                    break;
                case "Supervisor":
                    done = userManagement.registerSupervisor(registerSussexId, registerName, registerSurname, registerEmail, registerPhoneNum, registerDepartment, registerPassword);    
                    break;
                case "Student":
                    done = userManagement.registerStudent(registerSussexId, registerName, registerSurname, registerEmail, registerCourse, registerPassword);    
                    break;
                default:
                    break;
            }   
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            MessageController.displayMessage("Something went wrong");
        }
        if(done) {
            MessageController.displayMessage("User has been registered");
        } else {
            MessageController.displayMessage("User already exists");
        }
        resetForm();
    }
    
    public List<User> getUsers() {
        return userManagement.getUsers();
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getUser() {
        return user;
    }
    
    public List<Log> getLogs() {
        return projectManagement.getLogs(user);
    }
    
    public List<Project> getAcceptedProjects() {
        return projectManagement.getAcceptedProjects();
    }
    
    public void releaseProject(int id) {
        projectManagement.releaseProject(id);
    }
    
    public void removeProject(Project p) {
        projectManagement.removeProject(p);
    }
    
    public void resetForm() {
        registerSussexId = null;
        registerName = null;
        registerSurname = null;
        registerEmail = null;
        registerPhoneNum = null;
        registerCourse = null;
        registerDepartment = null;
        registerPassword = null;
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
        this.role = role;
    }
    
    public List<Student> getStudentsByStatus() {
        if(userStatus!=null && userStatus.equals("chosen")){
            return userManagement.getStudentsByStatus(true);
        } else if(userStatus!=null && userStatus.equals("not_chosen")) {
            return userManagement.getStudentsByStatus(false);
        } else {
            return userManagement.getStudents();
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

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
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
        return projectManagement.getProjectsBySupervisorId(supervisor);
    }
     
    public List<Supervisor> getSupervisors() {
        return userManagement.getSupervisors();
    }
    
    public List<ProjectTopic> getTopics() {
        return projectManagement.getTopics();
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getUserStatus() {
        return userStatus;
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