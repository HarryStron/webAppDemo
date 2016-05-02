package com.cs391.web;

import com.cs391.service.UserManagement;
import com.cs391.data.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class IndexView implements Serializable {
    private String username;
    
    @EJB
    private UserManagement userManagement;

    public String getUsername() {
        return "";
    }

    public void setUsername(String name) {
        username = name; 
System.out.println(username);        
        FacesContext context = FacesContext.getCurrentInstance();
        User currentUser = userManagement.getUserByID(username);
        context.getExternalContext().getSessionMap().put("user", currentUser);            
    }
}