package com.cs391.jsf;

import com.cs391.ejb.UserManagement;
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
                context.getExternalContext().getSessionMap().put("user", userManagement.getUserByID(username));
                return "admin";
            }
            //wrong password
        }
        //wrong username
        return "index";
    }
}
