package com.metrics_board.persistence.repository;

import com.metrics_board.persistence.entity.Demo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends CrudRepository<Demo, Long> {
}