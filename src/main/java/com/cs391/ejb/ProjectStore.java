package com.cs391.ejb;

import com.cs391.jpa.Project;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;

@Singleton
public class ProjectStore {

    ArrayList<Project> projectList;

    public ProjectStore() {
        projectList = new ArrayList<>();
    }

    public synchronized List<Project> getProjectList() {
        return projectList;
    }
    
    public synchronized void insertComment() {
        Project project = new Project();
        projectList.add(project);
    }

    @PostConstruct
    public void postConstruct() {
    }

    @PreDestroy
    public void preDestroy() {
    }
}
