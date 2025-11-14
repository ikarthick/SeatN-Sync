package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    List<Waitlist> findBySeatIdOrderByWaitlistRankAsc(Long seatId);
}
