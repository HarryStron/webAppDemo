package com.cs391.ejb;

import com.cs391.jpa.Project;
import com.cs391.jpa.ProjectTopic;
import com.cs391.jpa.Supervisor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class ProjectManagement {

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
            projects.setParameter("supervisorID", getSuperFromID(supervisorID));
        }
        
        return projects.getResultList();
    }
    
    public List<Project> getProposedProjects(String supervisorID) {
        TypedQuery<Project> projects;
        projects = em.createQuery("SELECT p FROM Project p WHERE p.supervisor = :supervisorID AND p.status = :status", Project.class);
        projects.setParameter("supervisorID", getSuperFromID(supervisorID));
        projects.setParameter("status", Project.Status.PROPOSED);
        
        return projects.getResultList();
    }
    
    public void editProjectStatus(int id, Project.Status status) {
        Project p = em.find(Project.class, id);
        p.setStatus(status);
        
        em.merge(p);
    }
    
    public List<Supervisor> getSupervisors() {
        TypedQuery<Supervisor> supervisor = em.createQuery("SELECT p.sussexID FROM Supervisor p", Supervisor.class);
        List<Supervisor> result = supervisor.getResultList();
        
        return result;
    }
    
    public List<ProjectTopic> getTopics() {
        TypedQuery<ProjectTopic> topic = em.createQuery("SELECT t FROM ProjectTopic t", ProjectTopic.class);
        List<ProjectTopic> result = topic.getResultList();
        
        return result;
    }
    
    public ProjectTopic getTopicByID(int id) {
        return em.find(ProjectTopic.class, id);
    }
    
    private Supervisor getSuperFromID(String id){
        TypedQuery<Supervisor> su = em.createQuery("SELECT s FROM Supervisor s WHERE s.sussexID = :id", Supervisor.class);
        su.setParameter("id", id);
        
        try {
            return su.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
 