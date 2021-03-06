package com.cs391.service;

import com.cs391.data.Administrator;
import com.cs391.data.Credentials;
import com.cs391.data.Log;
import com.cs391.data.Project;
import com.cs391.data.Student;
import com.cs391.data.Supervisor;
import com.cs391.data.User;
import com.cs391.data.UserGroup;
import com.cs391.thrift.TimestampService;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

@Stateless
public class UserManagement {

    @PersistenceContext(unitName = "WebappsDB")
    private EntityManager em;

    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public boolean registerAdmin(String sussexId, String name, String surname, String email, String phoneNum, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if(!userExists(sussexId)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);
            
            Administrator admin = new Administrator();
            admin.setSussexID(sussexId);
            admin.setName(name);
            admin.setSurname(surname);
            admin.setEmail(email);
            admin.setPhoneNum(phoneNum);

            em.persist(admin);
            registerCredentials(sussexId, paswdToStoreInDB, "administrator");
            writeTolog("Registered a new administrator with ID: "+sussexId);
            return true;
        } else {
            return false;
        }
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public boolean registerSupervisor(String sussexId, String name, String surname, String email, String phoneNum, String department, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if(!userExists(sussexId)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);
            
            Supervisor supervisor = new Supervisor();
            supervisor.setSussexID(sussexId);
            supervisor.setName(name);
            supervisor.setSurname(surname);
            supervisor.setEmail(email);
            supervisor.setPhoneNum(phoneNum);
            supervisor.setDepartment(department);

            em.persist(supervisor);
            registerCredentials(sussexId, paswdToStoreInDB, "supervisor");            
            writeTolog("Registered a new supervisor with ID: "+sussexId);
         return true;
        } else {
            return false;
        }
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public boolean registerStudent(String sussexId, String name, String surname, String email, String course, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if(!userExists(sussexId)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);
            
            Student student = new Student();
            student.setSussexID(sussexId);
            student.setName(name);
            student.setSurname(surname);
            student.setEmail(email);
            student.setCourse(course);

            em.persist(student);
            registerCredentials(sussexId, paswdToStoreInDB, "student");
            writeTolog("Registered a new student with ID: "+sussexId);
            return true;
        } else {
            return false;
        }
    }
    
    @TransactionAttribute(MANDATORY)
    private void registerCredentials(String id, String pass, String role) {
        Credentials credentials = new Credentials();
        credentials.setSussexID(id);
        credentials.setPass(pass);
        em.persist(credentials);

        UserGroup userGroup = new UserGroup();
        userGroup.setSussexID(id);
        userGroup.setGroupName(role);
        em.persist(userGroup);
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor", "student"})
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
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public List<User> getUsers() {
        List<User> users = null;
        TypedQuery<User> adminQuery = em.createQuery("SELECT c FROM Administrator c", User.class);
        users = adminQuery.getResultList();
        TypedQuery<User> superQuery = em.createQuery("SELECT c FROM Supervisor c", User.class);
        users.addAll(superQuery.getResultList());
        TypedQuery<User> studentQuery = em.createQuery("SELECT c FROM Student c", User.class);
        users.addAll(studentQuery.getResultList());
        
        return users;
    }

    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public boolean userExists(String username) {
        TypedQuery<Credentials> query = em.createQuery("SELECT c FROM Credentials c WHERE c.sussexID = :sussexID", Credentials.class);
        try {
            query.setParameter("sussexID", username).getSingleResult();        
            return true;
        } catch (NoResultException e){
            return false;
        }
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    public Supervisor getSuperOfStudent(String id) {
        TypedQuery<Supervisor> su = em.createQuery("SELECT s.supervisor FROM Project s WHERE s.owner.sussexID = :id", Supervisor.class);
        su.setParameter("id", id);
        
        try {
            return su.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
      
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor", "student"})
    public Supervisor getSuperFromID(String id){
        TypedQuery<Supervisor> su = em.createQuery("SELECT s FROM Supervisor s WHERE s.sussexID = :id", Supervisor.class);
        su.setParameter("id", id);
        
        try {
            return su.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    public List<Supervisor> getSupervisors() {
        TypedQuery<Supervisor> supervisor = em.createQuery("SELECT p FROM Supervisor p", Supervisor.class);
        List<Supervisor> result = supervisor.getResultList();
        
        return result;
    }
     
    @TransactionAttribute(REQUIRES_NEW)
    public List<Student> getStudents() {
        TypedQuery<Student> student = em.createQuery("SELECT s FROM Student s", Student.class);
        List<Student> result = student.getResultList();

        return result;
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    public List<Student> getStudentsBySupervisorId(String id) {
        TypedQuery<Student> student;
        student = em.createQuery("SELECT p.owner FROM Project p WHERE p.supervisor.sussexID = :id AND p.status = :status", Student.class);
        student.setParameter("id", id);
        student.setParameter("status", Project.Status.ACCEPTED);
        
        return student.getResultList();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor", "student"})
    public void logLastLogin(){
        writeTolog("Login");
    }
    
    @TransactionAttribute(MANDATORY)
    private void writeTolog(String info) {
        try {
            TTransport transport;
            transport = new TSocket("localhost", 10000);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TimestampService.Client client = new TimestampService.Client(protocol);            
            String timestamp = client.stamp();
            transport.close();
            
            Log log = new Log();
            log.setSussexID(((User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user")).getSussexID());
            log.setEventDate(timestamp);
            log.setInfo(info);
            em.persist(log); 
        } catch (TException e){
            System.err.println("Failed to receive timestamp"+e);
        } 
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public List<Student> getStudentsByStatus(boolean hasChosen) {
        List<Student> studs = em.createQuery("SELECT p.owner FROM Project p WHERE p.status = :status", Student.class).setParameter("status", Project.Status.ACCEPTED).getResultList();
        studs.addAll(em.createQuery("SELECT p.owner FROM Project p WHERE p.status = :status", Student.class).setParameter("status", Project.Status.SELECTED).getResultList());
        studs.addAll(em.createQuery("SELECT p.owner FROM Project p WHERE p.status = :status", Student.class).setParameter("status", Project.Status.PROPOSED).getResultList());
        
        if(!hasChosen){
            List<Student> allStudents = getStudents();
            allStudents.removeAll(studs);
            studs = allStudents;
        }        
        return studs;
    }
}
