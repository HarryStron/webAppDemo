package com.cs391.service;

import com.cs391.data.Administrator;
import com.cs391.data.Credentials;
import com.cs391.data.Student;
import com.cs391.data.Supervisor;
import com.cs391.data.User;
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

    public boolean registerAdmin(String sussexId, String name, String surname, String email, String phoneNum, String password) {
        if(!userExists(sussexId)) {
            Administrator admin = new Administrator();
            admin.setSussexID(sussexId);
            admin.setName(name);
            admin.setSurname(surname);
            admin.setEmail(email);
            admin.setPhoneNum(phoneNum);

            em.persist(admin);
            registerCredentials(sussexId, password, "Ad");
            return true;
        } else {
            return false;
        }
    }
    
    public boolean registerSupervisor(String sussexId, String name, String surname, String email, String phoneNum, String department, String password) {
        if(!userExists(sussexId)) {
            Supervisor supervisor = new Supervisor();
            supervisor.setSussexID(sussexId);
            supervisor.setName(name);
            supervisor.setSurname(surname);
            supervisor.setEmail(email);
            supervisor.setPhoneNum(phoneNum);
            supervisor.setDepartment(department);

            em.persist(supervisor);
            registerCredentials(sussexId, password, "Su");
         return true;
        } else {
            return false;
        }
    }
    
    public boolean registerStudent(String sussexId, String name, String surname, String email, String course, String password) {
        if(!userExists(sussexId)) {
            Student student = new Student();
            student.setSussexID(sussexId);
            student.setName(name);
            student.setSurname(surname);
            student.setEmail(email);
            student.setCourse(course);

            em.persist(student);
            registerCredentials(sussexId, password, "St");
            return true;
        } else {
            return false;
        }
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

    public boolean userExists(String username) {
        TypedQuery<Credentials> query = em.createQuery("SELECT c FROM Credentials c WHERE c.sussexID = :sussexID", Credentials.class);
        try {
            query.setParameter("sussexID", username).getSingleResult();        
            return true;
        } catch (NoResultException e){
            return false;
        }
    }
    
    public Supervisor getSuperOfStudent(String id) {
        TypedQuery<Supervisor> su = em.createQuery("SELECT s.supervisor FROM Project s WHERE s.owner.sussexID = :id", Supervisor.class);
        su.setParameter("id", id);
        
        try {
            return su.getSingleResult();
        } catch (NoResultException e) {
            return null;
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
    
    public List<Supervisor> getSupervisorIDs() {
        TypedQuery<Supervisor> supervisor = em.createQuery("SELECT p.sussexID FROM Supervisor p", Supervisor.class);
        List<Supervisor> result = supervisor.getResultList();
        
        return result;
    }
    
    public List<Supervisor> getSupervisors() {
        TypedQuery<Supervisor> supervisor = em.createQuery("SELECT p FROM Supervisor p", Supervisor.class);
        List<Supervisor> result = supervisor.getResultList();
        
        return result;
    }
     
    public List<Student> getStudents() {
        TypedQuery<Student> student = em.createQuery("SELECT s FROM Student s", Student.class);
        List<Student> result = student.getResultList();

        return result;
    }
    
    public List<Student> getStudentsBySupervisorId(String id) {
        TypedQuery<Student> student;
        student = em.createQuery("SELECT p.owner FROM Project p WHERE p.supervisor.sussexID = :id", Student.class);
        student.setParameter("id", id);
        
        return student.getResultList();
    }
}
