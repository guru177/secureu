# SecureU — Teacher Terminal Output & Lab Presentation Guide

This guide explains how to show the **Java Terminal Output** directly in the command prompt/terminal when your teacher asks to see the Java console output.

---

## ⚡ Method 1: One-Click Terminal Launcher (Recommended)

1. Open your project folder: `c:\Users\Hariprasad\Videos\JAVA PROJECT`
2. Double-click **`Launch-Terminal-Console.bat`**
3. A Command Prompt terminal window will immediately open and display:
   * **Application Banner** with JDK version and JDBC connection confirmation.
   * **Live MySQL Metrics** (Total students, total complaints, breakdown of Pending / In Progress / Resolved).
   * **Formatted ASCII Complaints Table** showing real records from MySQL.
   * **Interactive Console Menu** ready for your commands.

---

## 💻 Method 2: From PowerShell or Command Prompt

Open Command Prompt or PowerShell inside the project directory and run:

```powershell
# Direct Terminal Mode:
java -jar CollegeComplaint.jar --terminal

# Or with full JDK path:
& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -jar CollegeComplaint.jar --terminal
```

---

## 🎯 What to Demonstrate to Your Teacher

When the terminal is open, you can demonstrate the following commands:

### 1. Show All Complaints Table (Type `1` and press Enter)
Displays a formatted ASCII table of all complaints in the database with columns:
* `ID`
* `Student Name`
* `Dept`
* `Category`
* `Status`
* `Filed Date`
* `Description`

### 2. Filter Complaints by Status (Type `2` and press Enter)
Prompt will ask for status filter:
* Enter `P` for **Pending**
* Enter `I` for **In Progress**
* Enter `R` for **Resolved**

### 3. Show Registered Students Table (Type `3` and press Enter)
Displays the complete list of students registered in MySQL with Student ID, Name, Department, Email, and Phone number.

### 4. File a Complaint via Terminal (Type `4` and press Enter)
Demonstrates a live JDBC `INSERT` query:
1. Enter Student ID (e.g. `1` or `2`).
2. Enter Category (e.g. `Hostel`, `Food`, `Academic`).
3. Enter Description (e.g. `Water dispenser filter issue`).
4. Java executes `PreparedStatement` and prints `[SUCCESS] New complaint logged with ID #... in MySQL database!`.

### 5. Update Complaint Status via Terminal (Type `5` and press Enter)
Demonstrates a live JDBC `UPDATE` query:
1. Enter Complaint ID (e.g. `48`).
2. Select new status (`1` for Pending, `2` for In Progress, `3` for Resolved).
3. Enter Administrative remarks (e.g. `Maintenance team inspected and resolved`).
4. Java updates the record in MySQL and prints confirmation.

### 6. Show Live Database Metrics (Type `6` and press Enter)
Recalculates and displays live counts from the MySQL database.

### 7. Launch Web GUI from Terminal (Type `7` and press Enter)
Opens the browser interface at `http://localhost:3000` while keeping the terminal console active.

---

## 🔴 Live JDBC Streaming to Terminal (Very Impressive for Viva!)

While the terminal is running, any action performed in the web browser (or mobile browser) will **stream live SQL logs into the terminal window**:

| Action in Browser | Output in Terminal Window |
| :--- | :--- |
| Student Sneha logs in | `[HH:mm:ss] [JDBC LOGIN   ] SUCCESS: Student 'Sneha S' (sneha@example.com) authenticated.` |
| Admin logs in | `[HH:mm:ss] [JDBC LOGIN   ] SUCCESS: Admin 'Dr. K. Raman' authenticated.` |
| Loading dashboard | `[HH:mm:ss] [JDBC SELECT  ] Fetched 50 complaints from MySQL for Dashboard view.` |
| Student submits complaint | `[HH:mm:ss] [JDBC INSERT  ] New complaint filed! ID #51 \| Student ID: 2 \| Category: Academic` |
| Admin updates status | `[HH:mm:ss] [JDBC UPDATE  ] Complaint #51 status updated to 'In Progress' \| Remarks: '...'` |
| Database Inspector opened | `[HH:mm:ss] [JDBC INSPECT ] Database inspector accessed: Students, Admin, and Complaints tables dumped.` |

---

## 🎓 Teacher / Viva Questions & Answers

**Q1: Which Java packages are used for the database connection?**
* **Answer**: `java.sql.*` — specifically `DriverManager`, `Connection`, `PreparedStatement`, `Statement`, and `ResultSet`.

**Q2: What driver is used to connect to MySQL?**
* **Answer**: MySQL Connector/J (`com.mysql.cj.jdbc.Driver`), located in the `lib/` directory (`lib/mysql-connector-j.jar`).

**Q3: How does the application prevent SQL Injection?**
* **Answer**: By using parameterized queries via `PreparedStatement` with placeholder `?` markers for student input, categories, descriptions, and remarks.

**Q4: Can this Java program run on both command-line and web?**
* **Answer**: Yes. The application is a dual-mode system:
  1. A standalone **Terminal Console (CLI)** that runs directly in Command Prompt / Terminal.
  2. A built-in **Java HTTP Server** (`com.sun.net.httpserver.HttpServer`) that serves the modern responsive web portal while logging all database activity live to the console.
