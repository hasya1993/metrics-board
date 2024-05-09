package com.metrics_board.persistence.repository.roll;

import com.metrics_board.persistence.entity.roll.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
}