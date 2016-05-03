package com.cs391.service;

import com.cs391.data.Project;
import com.cs391.data.ProjectTopic;
import com.cs391.data.Student;
import com.cs391.data.Supervisor;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
    @RolesAllowed({"supervisor"})
    public void removeProject (Project project) {
        em.remove(em.merge(project));
        em.flush();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"administrator", "supervisor"})
    public boolean addNewTopic(String name, String desc){
        if(!topicExists(name)) {
            ProjectTopic projectTopic = new ProjectTopic();
            projectTopic.setTopic(name);
            projectTopic.setDescription(desc);

            em.persist(projectTopic);
            em.flush();
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
        em.flush();
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
        em.flush();
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
    @RolesAllowed({"supervisor"})
    public void editProjectStatus(int id, Project.Status status) {
        Project p = em.find(Project.class, id);
        p.setStatus(status);
        
        em.merge(p);
        em.flush();
    }
    
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed({"supervisor"})
    public void removeOwner(int id) {
        Project p = em.find(Project.class, id);
        p.setOwner(null);
        
        em.merge(p);
        em.flush();
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
}
 