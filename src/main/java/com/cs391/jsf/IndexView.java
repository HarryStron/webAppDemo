package com.cs391.jsf;

import com.cs391.ejb.UserManagement;
import com.cs391.jpa.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class IndexView implements Serializable {
    private String username;
    private String password;
    
    @EJB
    private UserManagement userManagement;

    public String login() {     
        if(userManagement.userExists(username)) {
            if(userManagement.verifyPass(username, password)){
                FacesContext context = FacesContext.getCurrentInstance();
                User currentUser = userManagement.getUserByID(username);
                context.getExternalContext().getSessionMap().put("user", currentUser);
                if(userManagement.getUserRole(currentUser.getSussexID()).equals("Ad")) {
                    return "admin";
                } else if(userManagement.getUserRole(currentUser.getSussexID()).equals("Su")) {
                    return "super";
                } else if(userManagement.getUserRole(currentUser.getSussexID()).equals("St")) {
                    return "student";
                } 
            }
            //wrong password
            return "index";                
        }
        //wrong username
        return "index";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }    
}
