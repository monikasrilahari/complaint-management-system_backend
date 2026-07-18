package com.complaint.controller;

import com.complaint.entity.Complaint;
import com.complaint.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<?> createComplaint(@RequestBody Map<String, Object> body) {
        try {
            String title = (String) body.get("title");
            String description = (String) body.get("description");
            
            Object userIdObj = body.get("userId");
            if (userIdObj == null) {
                return new ResponseEntity<>(Map.of("error", "userId is required"), HttpStatus.BAD_REQUEST);
            }
            Long userId = Long.valueOf(userIdObj.toString());

            if (title == null || title.trim().isEmpty()) {
                return new ResponseEntity<>(Map.of("error", "Title is required"), HttpStatus.BAD_REQUEST);
            }
            if (description == null || description.trim().isEmpty()) {
                return new ResponseEntity<>(Map.of("error", "Description is required"), HttpStatus.BAD_REQUEST);
            }

            Complaint complaint = complaintService.createComplaint(title, description, userId);
            return new ResponseEntity<>(complaint, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllComplaints() {
        try {
            return new ResponseEntity<>(complaintService.getAllComplaints(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getComplaintsByUser(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(complaintService.getComplaintsByUser(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<?> getComplaintsByStaff(@PathVariable Long staffId) {
        try {
            return new ResponseEntity<>(complaintService.getComplaintsByStaff(staffId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComplaintById(@PathVariable Long id) {
        try {
            Complaint complaint = complaintService.getComplaintById(id);
            return new ResponseEntity<>(complaint, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignStaff(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Object staffIdObj = body.get("staffId");
            if (staffIdObj == null) {
                return new ResponseEntity<>(Map.of("error", "staffId is required"), HttpStatus.BAD_REQUEST);
            }
            Long staffId = Long.valueOf(staffIdObj.toString());

            Complaint updatedComplaint = complaintService.assignStaff(id, staffId);
            return new ResponseEntity<>(updatedComplaint, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status");
            if (status == null || status.trim().isEmpty()) {
                return new ResponseEntity<>(Map.of("error", "status is required"), HttpStatus.BAD_REQUEST);
            }

            Complaint updatedComplaint = complaintService.updateStatus(id, status);
            return new ResponseEntity<>(updatedComplaint, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<?> resolveComplaint(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String resolutionNotes = body.get("resolutionNotes");
            if (resolutionNotes == null || resolutionNotes.trim().isEmpty()) {
                return new ResponseEntity<>(Map.of("error", "Resolution notes are required to resolve the complaint"), HttpStatus.BAD_REQUEST);
            }

            Complaint resolvedComplaint = complaintService.resolveComplaint(id, resolutionNotes);
            return new ResponseEntity<>(resolvedComplaint, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
