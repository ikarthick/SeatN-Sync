package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.QRCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCheckInRepository extends JpaRepository<QRCheckIn, Long> {
}
