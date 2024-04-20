package com.metrics_board.persistence.service.roll;

import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.repository.roll.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}