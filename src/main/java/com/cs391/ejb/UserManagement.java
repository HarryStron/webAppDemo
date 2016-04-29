package com.cs391.ejb;

import com.cs391.jpa.Administrator;
import com.cs391.jpa.Credentials;
import com.cs391.jpa.Student;
import com.cs391.jpa.Supervisor;
import com.cs391.jpa.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class UserManagement {

    @PersistenceContext(unitName = "WebappsDB")
    private EntityManager em;

    public void addNewUser(User user) {
        em.persist(user);
    }

    public void registerAdmin(String sussexId, String name, String surname, String email, String phoneNum, String password) {
        Administrator admin = new Administrator();
        admin.setSussexID(sussexId);
        admin.setName(name);
        admin.setSurname(surname);
        admin.setEmail(email);
        admin.setPhoneNum(phoneNum);
       
        em.persist(admin);
        registerCredentials(sussexId, password, "Ad");
    }
    
    public void registerSupervisor(String sussexId, String name, String surname, String email, String phoneNum, String department, String password) {
        Supervisor supervisor = new Supervisor();
        supervisor.setSussexID(sussexId);
        supervisor.setName(name);
        supervisor.setSurname(surname);
        supervisor.setEmail(email);
        supervisor.setPhoneNum(phoneNum);
        supervisor.setDepartment(department);
       
        em.persist(supervisor);
        registerCredentials(sussexId, password, "Su");
    }
    
    public void registerStudent(String sussexId, String name, String surname, String email, String course, String password) {
        Student student = new Student();
        student.setSussexID(sussexId);
        student.setName(name);
        student.setSurname(surname);
        student.setEmail(email);
        student.setCourse(course);
       
        em.persist(student);
        registerCredentials(sussexId, password, "St");
    }
    
    private void registerCredentials(String id, String pass, String role) {
        Credentials credentials = new Credentials();
        credentials.setSussexID(id);
        credentials.setPass(pass);
        credentials.setRole(role);
        
        em.persist(credentials);
    }
    
    public String getUserRole(String id) {
        TypedQuery<Credentials> roleQuery = em.createQuery("SELECT c FROM Credentials c WHERE c.sussexID = :sussexID", Credentials.class);
        Credentials c = roleQuery.setParameter("sussexID", id).getSingleResult();
        
        return c.getRole();
    }
    
    public User getUserByID(String id) {
        User u = null;
        try{
            TypedQuery<User> adminQuery = em.createQuery("SELECT c FROM Administrator c WHERE c.sussexID = :sussexID", User.class);
            u = adminQuery.setParameter("sussexID", id).getSingleResult();
        } catch(NoResultException e) {}
        if (u==null) {
            try {
            TypedQuery<User> superQuery = em.createQuery("SELECT c FROM Supervisor c WHERE c.sussexID = :sussexID", User.class);
                u = superQuery.setParameter("sussexID", id).getSingleResult();
            } catch(NoResultException e) {}
        }
        if (u==null) {
            try {
                TypedQuery<User> studentQuery = em.createQuery("SELECT c FROM Student c WHERE c.sussexID = :sussexID", User.class);
                u = studentQuery.setParameter("sussexID", id).getSingleResult();
            } catch(NoResultException e) {}
        }
        
        return u;
    }
    
    private List<User> getUsers() {
        TypedQuery<User> admins = em.createQuery("SELECT u FROM Administrator u", User.class);
        TypedQuery<User> supervisors = em.createQuery("SELECT u FROM Supervisor u", User.class);
        TypedQuery<User> students = em.createQuery("SELECT u FROM Student u", User.class);
        List<User> result = admins.getResultList();
        result.addAll(supervisors.getResultList());
        result.addAll(students.getResultList());
        
        return result;
    }

    public boolean userExists(String username) {
        TypedQuery<Credentials> query = em.createQuery("SELECT c FROM Credentials c WHERE c.sussexID = :sussexID", Credentials.class);
        Credentials c = query.setParameter("sussexID", username).getSingleResult();
        if (c!=null) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean verifyPass(String username, String password) {
        TypedQuery<Credentials> query = em.createQuery("SELECT c FROM Credentials c WHERE c.sussexID = :sussexID", Credentials.class);
        String pass = query.setParameter("sussexID", username).getSingleResult().getPass();
        if (password.equals(pass)) {
            return true;
        } else {
            return false;
        }
    }
        
    public Supervisor getSuperFromID(String id){
        TypedQuery<Supervisor> su = em.createQuery("SELECT s FROM Supervisor s WHERE s.sussexID = :id", Supervisor.class);
        su.setParameter("id", id);
        
        try {
            return su.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<Supervisor> getSupervisors() {
        TypedQuery<Supervisor> supervisor = em.createQuery("SELECT p.sussexID FROM Supervisor p", Supervisor.class);
        List<Supervisor> result = supervisor.getResultList();
        
        return result;
    }
}
