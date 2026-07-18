package com.complaint.service;

import com.complaint.entity.Staff;
import com.complaint.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff registerStaff(Staff staff) {
        if (staffRepository.findByEmail(staff.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Staff member with this email already exists");
        }
        return staffRepository.save(staff);
    }

    public Staff getStaffByEmail(String email) {
        return staffRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No staff member found with email: " + email));
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found with id: " + id));
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }
}
