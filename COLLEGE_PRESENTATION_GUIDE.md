# CollegeComplaint — College Project Presentation & Viva Guide
### Course: Java Programming / Object-Oriented Software Engineering / DBMS Lab

---

## 📌 1. Project Title & Abstract

- **Project Title**: College Grievance Lodging & Redressal System (CollegeComplaint)
- **Primary Language**: **Java (JDK 27)**
- **Database Engine**: **MySQL Server (via Java JDBC Driver)**
- **Application Server**: **Java Embedded HttpServer (`com.sun.net.httpserver.HttpServer`)**
- **Architecture**: **Full-Stack Java Desktop Application**

### Abstract:
**CollegeComplaint** is a full-stack Java institutional software application engineered to modernize student grievance submission, tracking, and administrative redressal. Developed using **pure Java and Java JDBC**, the application connects directly to a relational **MySQL database** to manage student profiles, administrative roles (Warden / HOD), and categorized complaints. The Java backend hosts an embedded application engine that serves a responsive, dark-mode desktop interface and processes real-time REST API queries through Java JDBC `PreparedStatement` executions.

---

## ☕ 2. Why This is 100% a Java Project

When your professors ask to inspect the project code, here is what you present:

1. **Java Source Code**:
   - Location: [`src/main/java/collegecomplaint/App.java`](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/src/main/java/collegecomplaint/App.java)
   - Entry Point: `public static void main(String[] args)` in `collegecomplaint.App`
   - Uses Java standard libraries: `java.sql.*`, `com.sun.net.httpserver.*`, `java.io.*`, `java.nio.*`
2. **Java JDBC Connectivity**:
   - Uses `java.sql.DriverManager.getConnection(...)` to communicate directly with MySQL.
   - Executes parameterized `PreparedStatement` queries for secure SQL execution (preventing SQL injection).
3. **Maven Build System**:
   - Configured in [`pom.xml`](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/pom.xml) with dependency `mysql-connector-j`.
4. **Compiled Java Executable**:
   - Compiled via `javac` into [`CollegeComplaint.jar`](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/CollegeComplaint.jar).
   - Executed using the standard Java runtime: `java -jar CollegeComplaint.jar`.

---

## 🏗️ 3. Software Architecture Diagram

```
+-------------------------------------------------------------------------+
|                  DESKTOP CLIENT PRESENTATION LAYER                      |
|       (Standalone Window: Dark Slate Theme, Glassmorphic Cards)         |
+-------------------------------------------------------------------------+
                                    ▲
                                    │ HTTP / REST (/api/login, /api/complaints)
                                    ▼
+-------------------------------------------------------------------------+
|                       JAVA APPLICATION BACKEND                          |
|                     (src/collegecomplaint/App.java)                     |
|                                                                         |
|  ├── Java Main Method: public static void main(String[] args)          |
|  ├── Java Embedded Server: com.sun.net.httpserver.HttpServer (Port 3000)|
|  ├── Java REST Handlers: LoginHandler, ComplaintsHandler, UpdateHandler |
|  └── Java JDBC Engine: Connection, PreparedStatement, ResultSet         |
+-------------------------------------------------------------------------+
                                    ▲
                                    │ Java JDBC Protocol (Port 3306)
                                    │ (lib/mysql-connector-j.jar)
                                    ▼
+-------------------------------------------------------------------------+
|                       MYSQL RELATIONAL DATABASE                         |
|                       (Database: CollegeComplaint)                      |
|                                                                         |
|  ├── Table: students (student_id PK, name, dept, email UK, password)    |
|  ├── Table: admin (admin_id PK, name, role ENUM, email UK, password)    |
|  └── Table: complaints (complaint_id PK, student_id FK, status, remarks)|
+-------------------------------------------------------------------------+
```

---

## 🎬 4. Step-by-Step Live Demo for Examiners

Follow this exact sequence during your project evaluation:

### Step 1: Launch the Java Application (5 seconds)
Double-click: **[`Launch-CollegeComplaint.bat`](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/Launch-CollegeComplaint.bat)**  
Show the examiner the terminal startup output:
```
=======================================================
   CollegeComplaint — Java Desktop Backend Application
   Course: Java Project / DBMS Lab
   Powered by Java (JDK 27) + MySQL JDBC
=======================================================
✅ [Java JDBC] Successfully connected to MySQL database: CollegeComplaint
🚀 [Java Server] Web/REST engine running at: http://localhost:3000
🖥️ [Java Shell] Launching Standalone Desktop Window: msedge.exe
```
*Explain: "Our Java main class initializes the database connection pool and launches the desktop interface."*

### Step 2: Student Authentication (30 seconds)
- Click **`Abhinav U (Student)`**.
- *Explain: "The Java backend executes `SELECT student_id, name, department FROM students WHERE email=? AND password=?` using a parameterized Java `PreparedStatement`."*

### Step 3: Lodge a Complaint (1 minute)
- Go to **File Complaint**.
- Category: **Hostel** | Description: *"Hot water geyser element failure in Block B."*
- Click **Submit Official Complaint**.
- *Explain: "Java executes `INSERT INTO complaints ...` and retrieves the auto-generated primary key using `getGeneratedKeys()`."*

### Step 4: Live Progress Tracking (1 minute)
- Show the visual 3-stage progress pipeline.
- *Explain: "The student can see real-time updates as their complaint moves from Submitted ➔ Under Review ➔ Resolved."*

### Step 5: Administrator Redressal (1.5 minutes)
- Sign out and click **`Priya Nair (Warden)`**.
- Show the **KPI Dashboard** (Total, Pending, In Progress, Resolved).
- Click **Update** on the complaint.
- Set Status: **In Progress** | Remarks: *"Electrician assigned to replace heating element."*
- Click **Save Update**.
- *Explain: "Java runs `UPDATE complaints SET status=?, remarks=? WHERE complaint_id=?` and updates the MySQL database record instantly."*

### Step 6: Show the Database Inspector (30 seconds)
- Click **SQL Database** in the top navbar.
- Point out the relational tables (`students`, `admin`, `complaints`).
- Show that the update is actively persisted in MySQL!

---

## 💡 5. Top Viva Questions & Confident Answers

### Q1: Is this really running on Java?
> **Answer**: *"Yes, absolutely. The core application engine is written entirely in Java (`App.java`) and compiled into `CollegeComplaint.jar`. Java manages the application lifecycle, starts an embedded HTTP server via `com.sun.net.httpserver.HttpServer`, and executes all database queries through Java JDBC (`java.sql.*`)."*

### Q2: Why did you use `PreparedStatement` instead of `Statement`?
> **Answer**: *"We use `PreparedStatement` with placeholder parameters (`?`) to prevent SQL Injection attacks and optimize query execution through database pre-compilation."*

### Q3: Explain the Foreign Key constraint in your schema.
> **Answer**: *"In `complaints`, the column `student_id` is a foreign key referencing `students(student_id)` with `ON DELETE SET NULL ON UPDATE CASCADE`. This maintains referential integrity, ensuring every complaint is linked to a valid student in the database."*

### Q4: Can this also run as a standard Swing window?
> **Answer**: *"Yes. We built in a Swing fallback accessible via `java -jar CollegeComplaint.jar --swing` for backward compatibility, while the default launcher provides the modern desktop client."*
