-- Insert Users
INSERT INTO users (id, name, email, role, roll_number) VALUES 
(1, 'John Doe', 'john@example.com', 'user', 'STU202601'),
(2, 'Jane Smith', 'jane@example.com', 'user', 'STU202602'),
(3, 'Admin User', 'admin@example.com', 'admin', NULL);

-- Insert Staff
INSERT INTO staff (id, name, department, email) VALUES
(1, 'Robert Johnson', 'IT Support', 'robert@company.com'),
(2, 'Sarah Davis', 'Facilities & Maintenance', 'sarah@company.com'),
(3, 'Michael Wilson', 'Human Resources', 'michael@company.com');

-- Insert Complaints
-- Complaint 1: Pending (No Staff Assigned)
INSERT INTO complaints (id, title, description, status, created_at, updated_at, user_id, assigned_staff_id, resolution_notes) VALUES
(1, 'Slow Internet Connection', 'The office Wi-Fi is extremely slow today, causing delays in work. Please look into it.', 'Pending', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, NULL, NULL);

-- Complaint 2: In Progress (Sarah Assigned)
INSERT INTO complaints (id, title, description, status, created_at, updated_at, user_id, assigned_staff_id, resolution_notes) VALUES
(2, 'Broken AC in Conference Room B', 'The air conditioning in Conference Room B is blowing hot air instead of cold air. It is too hot for meetings.', 'In Progress', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2, NULL);

-- Complaint 3: Resolved (Robert Assigned and Notes updated)
INSERT INTO complaints (id, title, description, status, created_at, updated_at, user_id, assigned_staff_id, resolution_notes) VALUES
(3, 'Missing Access Card Badge', 'I lost my building access card badge and need a replacement issued.', 'Resolved', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 'Reissued a new access card badge and disabled the old one in the security system.');
