package com.complaint.controller;

import com.complaint.entity.Staff;
import com.complaint.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<?> registerStaff(@RequestBody Staff staff) {
        try {
            Staff registeredStaff = staffService.registerStaff(staff);
            return new ResponseEntity<>(registeredStaff, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStaff(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            if (email == null || email.trim().isEmpty()) {
                return new ResponseEntity<>(Map.of("error", "Email is required"), HttpStatus.BAD_REQUEST);
            }
            Staff staff = staffService.getStaffByEmail(email.trim());
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Long id) {
        try {
            Staff staff = staffService.getStaffById(id);
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllStaff() {
        try {
            return new ResponseEntity<>(staffService.getAllStaff(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
