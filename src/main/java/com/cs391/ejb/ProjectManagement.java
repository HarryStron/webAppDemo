package com.cs391.ejb;

import com.cs391.jpa.Project;
import com.cs391.jpa.ProjectTopic;
import com.cs391.jpa.Student;
import com.cs391.jpa.Supervisor;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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

    public void addNewProject(String title, String description, String requiredSkills, List<ProjectTopic> topics, Supervisor supervisor) {
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setRequiredSkills(requiredSkills);
        project.setTopic(topics);
        project.setSupervisor(supervisor);
        project.setStatus(Project.Status.AVAILABLE);
        
        em.persist(project);
    }
    
    public void removeProject (Project project) {
        em.remove(em.merge(project));
    }
    
    public void addNewTopic(String name, String desc){
        ProjectTopic projectTopic = new ProjectTopic();
        projectTopic.setTopic(name);
        projectTopic.setDescription(desc);
        
        em.persist(projectTopic);
    }

    public List<Project> getProjects(String supervisorID) {
        TypedQuery<Project> projects;
        if(supervisorID == null) {
            projects = em.createQuery("SELECT p FROM Project p", Project.class);
        } else {
            projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor = :supervisorID", Project.class);
            projects.setParameter("supervisorID", userManagement.getSuperFromID(supervisorID));
        }
        
        return projects.getResultList();
    }
    
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
    
    public void selectProject(int projectId, Student student) {
        Project p = em.find(Project.class, projectId);
        p.setOwner(student);
        p.setStatus(Project.Status.SELECTED);
        
        em.merge(p);
    }
    
    public void proposeProject(String title, String desc, String skills, List<ProjectTopic> topics, Supervisor supervisor) {
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(desc);
        project.setRequiredSkills(skills);
        project.setTopic(topics);
        project.setSupervisor(supervisor);
        project.setStatus(Project.Status.PROPOSED);
        
        em.persist(project);
    }
    
    public List<Project> getSelectedProjects(String supervisorID) {
        TypedQuery<Project> projects;
        projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor = :supervisorID AND p.status = :status", Project.class);
        projects.setParameter("supervisorID", userManagement.getSuperFromID(supervisorID));     
        projects.setParameter("status", Project.Status.SELECTED);
        
        return projects.getResultList();
    }
    
    public List<Project> getProposedProjects(String supervisorID) {
        TypedQuery<Project> projects;
        projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor = :supervisorID AND p.status = :status", Project.class);
        projects.setParameter("supervisorID", userManagement.getSuperFromID(supervisorID));
        projects.setParameter("status", Project.Status.PROPOSED);
        
        return projects.getResultList();
    }
    
    public void editProjectStatus(int id, Project.Status status) {
        Project p = em.find(Project.class, id);
        p.setStatus(status);
        
        em.merge(p);
    }
    
    public void removeOwner(int id) {
        Project p = em.find(Project.class, id);
        p.setOwner(null);
        
        em.merge(p);
    }
    
    public List<ProjectTopic> getTopics() {
        TypedQuery<ProjectTopic> topic = em.createQuery("SELECT t FROM ProjectTopic t", ProjectTopic.class);
        List<ProjectTopic> result = topic.getResultList();
        
        return result;
    }
    
    public ProjectTopic getTopicByID(int id) {
        return em.find(ProjectTopic.class, id);
    }
}
 