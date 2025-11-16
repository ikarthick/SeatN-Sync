package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.infra.Wing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WingRepository extends JpaRepository<Wing, Long> {
   List<Wing> findByBlockBlockId(Long blockId);
}
