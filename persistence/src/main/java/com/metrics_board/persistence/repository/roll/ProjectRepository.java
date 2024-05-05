package com.metrics_board.persistence.repository.roll;

import com.metrics_board.persistence.entity.roll.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findAllByOwnerId(UUID ownerID);
}