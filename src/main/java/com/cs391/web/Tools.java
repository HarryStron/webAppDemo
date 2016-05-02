package com.cs391.web;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@ViewScoped
public class Tools implements Serializable {
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("user");
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            MessageController.displayMessage("User logged out");
        } catch (ServletException ex) {
            MessageController.displayMessage("User log out failed");            
        }
        return "index?faces-redirect=true";
    }
}
