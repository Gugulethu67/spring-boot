-- Insert sample employees
INSERT INTO employees (name, email, is_manager, vacation_days_used, total_vacation_days) VALUES
('John Doe', 'john.doe@company.com', false, 5, 30),
('Jane Smith', 'jane.smith@company.com', true, 3, 30),
('Bob Johnson', 'bob.johnson@company.com', false, 10, 30),
('Alice Brown', 'alice.brown@company.com', true, 0, 30),
('Charlie Wilson', 'charlie.wilson@company.com', false, 15, 30);

-- Insert sample vacation requests
INSERT INTO vacation_requests (author, status, resolved_by, request_created_at, vacation_start_date, vacation_end_date) VALUES
(1, 'APPROVED', 2, '2024-11-01T10:00:00', '2024-12-20T00:00:00', '2024-12-25T00:00:00'),
(3, 'PENDING', null, '2024-11-05T14:30:00', '2024-12-15T00:00:00', '2024-12-22T00:00:00'),
(5, 'REJECTED', 4, '2024-11-03T09:15:00', '2024-11-25T00:00:00', '2024-11-29T00:00:00'),
(1, 'PENDING', null, '2024-11-08T11:45:00', '2025-01-15T00:00:00', '2025-01-20T00:00:00');