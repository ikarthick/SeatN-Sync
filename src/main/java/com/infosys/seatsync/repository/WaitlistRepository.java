package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.booking.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<WaitList, Long> {

    // Fetch highest-priority WL entry for a wing
    Optional<WaitList> findTopByWing_WingIdAndStatusOrderByPriorityAsc(
            Long wingId,
            WaitList.WaitlistStatus status
    );

    // Get all active waitlist entries for a wing ordered by priority
    List<WaitList> findByWing_WingIdAndStatusOrderByPriorityAsc(
            Long wingId,
            WaitList.WaitlistStatus status
    );

    // Count WL entries for a wing (to assign next priority WL number)
    int countByWing_WingIdAndStatus(Long wingId, WaitList.WaitlistStatus status);

}
