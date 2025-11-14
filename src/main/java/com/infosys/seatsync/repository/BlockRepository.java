package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
