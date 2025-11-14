package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
}
