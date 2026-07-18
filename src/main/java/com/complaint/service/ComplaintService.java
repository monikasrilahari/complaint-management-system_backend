package com.complaint.service;

import com.complaint.entity.Complaint;
import com.complaint.entity.Staff;
import com.complaint.entity.User;
import com.complaint.repository.ComplaintRepository;
import com.complaint.repository.StaffRepository;
import com.complaint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository,
                            UserRepository userRepository,
                            StaffRepository staffRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
    }

    public Complaint createComplaint(String title, String description, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Complaint complaint = new Complaint();
        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setUser(user);
        complaint.setStatus("Pending");

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Complaint> getComplaintsByUser(Long userId) {
        return complaintRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Complaint> getComplaintsByStaff(Long staffId) {
        return complaintRepository.findByAssignedStaffIdOrderByCreatedAtDesc(staffId);
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found with ID: " + id));
    }

    public Complaint assignStaff(Long complaintId, Long staffId) {
        Complaint complaint = getComplaintById(complaintId);
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found with ID: " + staffId));

        complaint.setAssignedStaff(staff);
        // Automatically set status to In Progress when staff is assigned, if it was Pending
        if ("Pending".equalsIgnoreCase(complaint.getStatus())) {
            complaint.setStatus("In Progress");
        }
        return complaintRepository.save(complaint);
    }

    public Complaint updateStatus(Long complaintId, String status) {
        Complaint complaint = getComplaintById(complaintId);
        
        String cleanStatus = formatStatus(status);
        if (!isValidStatus(cleanStatus)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Allowed values: Pending, In Progress, Resolved");
        }
        
        if ("Resolved".equals(cleanStatus) && (complaint.getResolutionNotes() == null || complaint.getResolutionNotes().trim().isEmpty())) {
            throw new IllegalArgumentException("Resolution notes are required to transition status to Resolved");
        }
        
        if (!"Resolved".equals(cleanStatus)) {
            complaint.setResolutionNotes(null);
        }
        
        complaint.setStatus(cleanStatus);
        return complaintRepository.save(complaint);
    }

    public Complaint resolveComplaint(Long complaintId, String resolutionNotes) {
        Complaint complaint = getComplaintById(complaintId);
        complaint.setResolutionNotes(resolutionNotes);
        complaint.setStatus("Resolved");
        return complaintRepository.save(complaint);
    }

    private String formatStatus(String status) {
        if (status == null) return null;
        String normalized = status.trim().toLowerCase();
        if (normalized.equals("pending")) return "Pending";
        if (normalized.equals("in progress") || normalized.equals("inprogress") || normalized.equals("in_progress")) return "In Progress";
        if (normalized.equals("resolved")) return "Resolved";
        return status;
    }

    private boolean isValidStatus(String status) {
        return "Pending".equals(status) || "In Progress".equals(status) || "Resolved".equals(status);
    }
}
