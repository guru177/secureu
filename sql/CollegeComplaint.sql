CREATE DATABASE IF NOT EXISTS CollegeComplaint;
USE CollegeComplaint;

CREATE TABLE IF NOT EXISTS students (
  student_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  department VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS admin (
  admin_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  role ENUM('Warden','HOD') NOT NULL,
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS complaints (
  complaint_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NULL,
  category VARCHAR(100) NOT NULL,
  description TEXT NOT NULL,
  evidence VARCHAR(255),
  status VARCHAR(30) NOT NULL DEFAULT 'Pending',
  date_filed DATE NOT NULL,
  remarks TEXT,
  CONSTRAINT fk_complaint_student FOREIGN KEY(student_id) REFERENCES students(student_id)
  ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT IGNORE INTO students(student_id,name,department,email,password,phone) VALUES
(1,'Abhinav U','CSE','abhinav@example.com','student@123','9876543210'),
(2,'Sneha S','ECE','sneha@example.com','student@123','8765432109'),
(3,'Rahul V','MECH','rahul@example.com','student@123','9123456780'),
(4,'Priya S','ECE','priya.s@example.com','student@123','9988776655'),
(5,'Vignesh R','CIVIL','vignesh@example.com','student@123','9090909090'),
(6,'Anjali K','CSE','anjali@example.com','student@123','9811122233'),
(7,'Mohammed F','EEE','farhan@example.com','student@123','9822233344'),
(8,'Divya M','IT','divya@example.com','student@123','9833344455'),
(9,'Karthik N','MECH','karthik@example.com','student@123','9844455566'),
(10,'Fathima Z','CSE','fathima@example.com','student@123','9855566677'),
(11,'Arjun P','EEE','arjun@example.com','student@123','9866677788'),
(12,'Meera J','IT','meera@example.com','student@123','9877788899');

INSERT IGNORE INTO admin(admin_id,name,role,email,password) VALUES
(1,'Priya Nair','Warden','warden@example.com','admin@123'),
(2,'Suresh Babu','HOD','hod@example.com','admin@123');

INSERT IGNORE INTO complaints(complaint_id, student_id, category, description, evidence, status, date_filed, remarks) VALUES
(101, 1, 'Hostel', 'Hot water geyser on 2nd floor of Block B not working for 3 days.', 'geyser_photo.jpg', 'In Progress', DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'Electrician assigned. Heating coil being replaced.'),
(102, 2, 'Academic', 'DSP lab workstation #14 crashes repeatedly during practicals.', 'lab_bsod.png', 'Pending', DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), NULL),
(103, 3, 'Academic', 'Internal assessment marks for Thermodynamics not updated on portal.', NULL, 'Pending', DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), NULL),
(104, 4, 'Food', 'Mess dinner served stale rice on Wednesday night.', 'mess_rice.jpg', 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'Food committee inspected. Vendor warned and menu revised.'),
(105, 5, 'Other', 'Classroom 204 air conditioner not working.', 'ac_room204.jpg', 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'AC compressor repaired by maintenance.'),
(106, 6, 'Verbal Harassment', 'Senior students using abusive language in hostel corridor.', NULL, 'In Progress', DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'Anti-ragging committee investigating.'),
(107, 7, 'Academic', 'Revaluation request for Circuits paper not processed.', 'reval_receipt.pdf', 'Pending', DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), NULL),
(108, 8, 'Hostel', 'Wi-Fi in Girls Hostel Block C down for a week.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Router replaced; bandwidth upgraded.'),
(109, 9, 'Food', 'Drinking water dispenser in canteen is unhygienic.', 'dispenser.jpg', 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Filter replaced and weekly cleaning scheduled.'),
(110, 10, 'Physical Harassment', 'Ragging incident reported near hostel mess.', NULL, 'In Progress', DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), 'Statements recorded. Disciplinary committee meeting scheduled.'),
(111, 11, 'Academic', 'Library not open on weekends during exam season.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'Weekend library hours extended till 8 PM.'),
(112, 12, 'Other', 'Street lights near parking area not working.', 'parking_dark.jpg', 'Pending', DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), NULL),
(113, 1, 'Food', 'Insects found in breakfast sambar.', 'sambar.jpg', 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 34 DAY), 'Kitchen deep cleaned; pest control done.'),
(114, 2, 'Hostel', 'Bathroom taps leaking in Block A, room 112.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 38 DAY), 'Plumber fixed leakage.'),
(115, 3, 'Academic', 'Faculty frequently absent for Machine Design lectures.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 41 DAY), 'HOD discussed with faculty; extra classes arranged.'),
(116, 4, 'Verbal Harassment', 'Lab assistant shouts at students during practicals.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 45 DAY), 'Counselling given to staff member.'),
(117, 5, 'Hostel', 'Room fan making loud noise and slow.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 48 DAY), 'Fan replaced.'),
(118, 6, 'Academic', 'Projector in Seminar Hall not working.', 'projector.jpg', 'In Progress', DATE_SUB(CURRENT_DATE(), INTERVAL 50 DAY), 'New projector purchase approved.'),
(119, 7, 'Other', 'Bus route 7 consistently late by 30 minutes.', NULL, 'Pending', DATE_SUB(CURRENT_DATE(), INTERVAL 52 DAY), NULL),
(120, 8, 'Food', 'Canteen overcharging for snacks.', 'bill.jpg', 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 55 DAY), 'Price list displayed; vendor warned.'),
(121, 9, 'Hostel', 'Mosquito problem in hostel rooms.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 63 DAY), 'Fogging done and window nets installed.'),
(122, 10, 'Academic', 'Exam timetable clash between two electives.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 66 DAY), 'Exam rescheduled by controller.'),
(123, 11, 'Physical Harassment', 'Student pushed during inter-department match.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 70 DAY), 'Sports committee issued warning.'),
(124, 12, 'Food', 'No vegetarian option on Sunday dinner.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 72 DAY), 'Menu updated.'),
(125, 1, 'Academic', 'Lab manual not provided for Compiler Design lab.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 76 DAY), 'Manuals distributed.'),
(126, 2, 'Hostel', 'Hostel gate closes before library closing time.', NULL, 'In Progress', DATE_SUB(CURRENT_DATE(), INTERVAL 80 DAY), 'Proposal sent to Dean.'),
(127, 3, 'Other', 'Drinking water cooler in Mech block not cooling.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 84 DAY), 'Cooler serviced.'),
(128, 4, 'Academic', 'Attendance wrongly marked absent for 3 days.', 'attendance.png', 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 88 DAY), 'Attendance corrected.'),
(129, 5, 'Food', 'Mess timings too short for lab students.', NULL, 'Pending', DATE_SUB(CURRENT_DATE(), INTERVAL 92 DAY), NULL),
(130, 6, 'Hostel', 'Laundry machine broken in Girls Hostel.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 96 DAY), 'Machine repaired.'),
(131, 7, 'Verbal Harassment', 'Derogatory comments on department WhatsApp group.', 'screenshot.png', 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 101 DAY), 'Students counselled; group moderated.'),
(132, 8, 'Academic', 'Internship NOC delayed by department office.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 105 DAY), 'NOC issued.'),
(133, 9, 'Other', 'Gym equipment damaged.', NULL, 'In Progress', DATE_SUB(CURRENT_DATE(), INTERVAL 110 DAY), 'Quotation requested for replacement.'),
(134, 10, 'Hostel', 'Power cuts every night in Block D.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 115 DAY), 'Wiring fixed; inverter installed.'),
(135, 11, 'Academic', 'Question paper had out-of-syllabus questions.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 120 DAY), 'Grace marks awarded.'),
(136, 12, 'Food', 'Juice stall hygiene issues.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 126 DAY), 'Stall licence reviewed.'),
(137, 1, 'Hostel', 'Water tank not cleaned for months.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 132 DAY), 'Tank cleaned.'),
(138, 2, 'Academic', 'Delay in publishing semester results.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 138 DAY), 'Results published.'),
(139, 3, 'Other', 'Parking space insufficient for two-wheelers.', NULL, 'Pending', DATE_SUB(CURRENT_DATE(), INTERVAL 142 DAY), NULL),
(140, 4, 'Physical Harassment', 'Fight between students outside canteen.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 148 DAY), 'Both parties suspended for 3 days.'),
(141, 5, 'Food', 'Mess food too spicy every day.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 152 DAY), 'Spice levels reduced.'),
(142, 6, 'Academic', 'Lab computers lack required software (MATLAB).', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 158 DAY), 'Licences installed.'),
(143, 7, 'Hostel', 'Broken window in room 305.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 164 DAY), 'Glass replaced.'),
(144, 8, 'Other', 'Medical room closed during evening hours.', NULL, 'In Progress', DATE_SUB(CURRENT_DATE(), INTERVAL 170 DAY), 'Nurse duty extension under review.'),
(145, 9, 'Academic', 'Assignment deadlines clashing across subjects.', NULL, 'Resolved', DATE_SUB(CURRENT_DATE(), INTERVAL 175 DAY), 'Faculty coordinated new schedule.');
