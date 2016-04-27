package com.cs391.jsf;

import com.cs391.ejb.UserManagement;
import com.cs391.jpa.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@ConversationScoped
public class IndexView implements Serializable {

    @EJB
    private UserManagement userManagement;

    public String login() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String username = request.getParameter("form:username");
        String password = request.getParameter("form:password");
        
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
}
