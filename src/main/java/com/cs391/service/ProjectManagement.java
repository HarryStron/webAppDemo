package com.cs391.service;

import com.cs391.data.Log;
import com.cs391.data.Project;
import com.cs391.data.ProjectTopic;
import com.cs391.data.Student;
import com.cs391.data.Supervisor;
import com.cs391.data.User;
import com.cs391.thrift.TimestampService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
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
public class ProjectManagement {

    @EJB
    UserManagement userManagement;
    
    @PersistenceContext(unitName = "WebappsDB")
    private EntityManager em;

    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"supervisor"})
    public boolean addNewProject(String title, String description, String requiredSkills, List<ProjectTopic> topics, Supervisor supervisor) {
        if(!projectExists(title)) {
            Project project = new Project();
            project.setTitle(title);
            project.setDescription(description);
            project.setRequiredSkills(requiredSkills);
            project.setTopic(topics);
            project.setSupervisor(supervisor);
            project.setStatus(Project.Status.AVAILABLE);

            em.persist(project);
            writeTolog("Registered project with title: "+title);
            return true;
        } else {
            return false;
        }
    }
    
    @TransactionAttribute(MANDATORY)
    @RolesAllowed({"supervisor", "student"})
    private boolean projectExists(String title) {
        TypedQuery<Project> project;
        project = em.createQuery("SELECT p FROM Project p WHERE p.title = :title", Project.class);
        project.setParameter("title", title);
        
        try{
            project.getSingleResult();
            return true;
        } catch(NoResultException e){
            return false;
        }
    }
       
    @TransactionAttribute(REQUIRES_NEW)
    public List<Project> getProjectsBySupervisorId(String id) {
        TypedQuery<Project> projects;
        if(id == null) {
            projects = em.createQuery("SELECT p FROM Project p", Project.class);
        } else {
            projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor.sussexID = :supervisorID", Project.class);
            projects.setParameter("supervisorID", id);
        }
        
        return projects.getResultList();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor"})
    public void removeProject (Project project) {
        em.remove(em.merge(project));
        writeTolog("Deleted project with title: "+project.getTitle());
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor"})
    public boolean addNewTopic(String name, String desc){
        if(!topicExists(name)) {
            ProjectTopic projectTopic = new ProjectTopic();
            projectTopic.setTopic(name);
            projectTopic.setDescription(desc);

            em.persist(projectTopic);
            writeTolog("Added topic with title: "+projectTopic.getTopic());
            return true;
        } else {
            return false;
        }
    }
    
    @TransactionAttribute(MANDATORY)
    @RolesAllowed({"administrator", "supervisor"})
    private boolean topicExists(String topic) {
        TypedQuery<ProjectTopic> projectTopic;
        projectTopic = em.createQuery("SELECT p FROM ProjectTopic p WHERE p.topic = :topic", ProjectTopic.class);
        projectTopic.setParameter("topic", topic);
        
        try{
            projectTopic.getSingleResult();
            return true;
        } catch(NoResultException e){
            return false;
        }
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"student"})
    public List<Project> getAvailableProjects(String supervisorID) {
        TypedQuery<Project> projects;
        if(supervisorID == null) {
            projects = em.createQuery("SELECT p FROM Project p WHERE p.status = :status", Project.class);
        } else {
            projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor = :supervisorID AND p.status = :status", Project.class);
            projects.setParameter("supervisorID", userManagement.getSuperFromID(supervisorID));
        }
        projects.setParameter("status", Project.Status.AVAILABLE);
        
        return projects.getResultList();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"student"})
    public boolean hasSelectedProject(Student student) {
        TypedQuery<Project> project = em.createQuery("SELECT p FROM Project p WHERE p.owner = :student", Project.class);
        project.setParameter("student", student);
        
        try{
            project.getSingleResult(); //if there are no results this line will crash
            return true;
        } catch(NoResultException e){
            return false;
        }
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"student"})
    public Project getSelectedProject(Student student) {
        TypedQuery<Project> project = em.createQuery("SELECT p FROM Project p WHERE p.owner = :student", Project.class);
        project.setParameter("student", student);
        
        return project.getSingleResult(); 
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"student"})
    public void selectProject(int projectId, Student student) {
        Project p = em.find(Project.class, projectId);
        p.setOwner(student);
        p.setStatus(Project.Status.SELECTED);
        
        em.merge(p);
        writeTolog("Selected a project with title: "+p.getTitle());
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"student"})
    public void proposeProject(String title, String desc, String skills, List<ProjectTopic> topics, Student owner, Supervisor supervisor) {
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(desc);
        project.setRequiredSkills(skills);
        project.setTopic(topics);
        project.setOwner(owner);
        project.setSupervisor(supervisor);
        project.setStatus(Project.Status.PROPOSED);
        
        em.persist(project);        
        writeTolog("Proposed a project with title: "+title);
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"supervisor"})
    public List<Project> getSelectedProjects(String supervisorID) {
        TypedQuery<Project> projects;
        projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor = :supervisorID AND p.status = :status", Project.class);
        projects.setParameter("supervisorID", userManagement.getSuperFromID(supervisorID));     
        projects.setParameter("status", Project.Status.SELECTED);
        
        return projects.getResultList();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"supervisor"})
    public List<Project> getProposedProjects(String supervisorID) {
        TypedQuery<Project> projects;
        projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor = :supervisorID AND p.status = :status", Project.class);
        projects.setParameter("supervisorID", userManagement.getSuperFromID(supervisorID));
        projects.setParameter("status", Project.Status.PROPOSED);
        
        return projects.getResultList();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public List<Project> getAcceptedProjects() {
        TypedQuery<Project> projects;
        projects = em.createQuery("SELECT p FROM Project p WHERE p.status = :status", Project.class);
        projects.setParameter("status", Project.Status.ACCEPTED);
        
        return projects.getResultList();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"supervisor"})
    public void acceptProposal(int id) {
        Project p = em.find(Project.class, id);
        p.setStatus(Project.Status.ACCEPTED);
        em.merge(p);
        
        writeTolog("Accepted proposed/selected project with title: "+p.getTitle());
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator"})
    public void releaseProject(int id) {
        Project p = em.find(Project.class, id);
        p.setStatus(Project.Status.AVAILABLE);
        p.setOwner(null);
        em.merge(p);
        
        writeTolog("Released accepted project with title: "+p.getTitle());
    }
    
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"supervisor"})
    public void declineSelected(int id) {
        Project p = em.find(Project.class, id);
        p.setStatus(Project.Status.AVAILABLE);
        p.setOwner(null);
        em.merge(p);
        
        writeTolog("Declined selected project with title: "+p.getTitle());
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor", "student"})
    public List<ProjectTopic> getTopics() {
        TypedQuery<ProjectTopic> topic = em.createQuery("SELECT t FROM ProjectTopic t", ProjectTopic.class);
        List<ProjectTopic> result = topic.getResultList();
        
        return result;
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor", "student"})
    public ProjectTopic getTopicByID(int id) {
        return em.find(ProjectTopic.class, id);
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
    public List<Log> getLogs(String userID) {
        TypedQuery<Log> query;
        if(userID != null) {
            query = em.createQuery("SELECT l FROM Log l WHERE l.sussexID = :sussexID", Log.class);
            query.setParameter("sussexID", userID);
        } else {
            query = em.createQuery("SELECT l FROM Log l", Log.class);
        }
        return query.getResultList();
    }
}
 