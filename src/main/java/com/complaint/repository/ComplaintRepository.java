package com.complaint.repository;

import com.complaint.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Complaint> findByAssignedStaffIdOrderByCreatedAtDesc(Long staffId);
    List<Complaint> findAllByOrderByCreatedAtDesc();
}
