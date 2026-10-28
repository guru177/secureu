# CollegeComplaint — Grievance Redressal System (Java Desktop & Web)

Educational starter project featuring student/admin login, grievance submission, live tracking, history list, and administrative redressal status/remarks updates.

---

## 🖥️ 1. How to Run as Desktop Java Application

Your machine is now fully configured to run the native Java Swing desktop app:
- **JDK 27** located at `C:\Program Files\Java\jdk-27.0.1.0.3\bin`
- **XAMPP MySQL** running on port `3306` with database `CollegeComplaint` imported
- **MySQL Connector/J** downloaded in `lib/mysql-connector-j.jar`
- Compiled into `CollegeComplaint.jar`

### Quick 1-Click Launch:
👉 **Double-click [run-desktop-java.bat](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/run-desktop-java.bat)** in File Explorer.

### Or From Terminal:
```powershell
# Using the compiled executable JAR:
& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -jar CollegeComplaint.jar

# Or running the compiled classes directly:
& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -cp "bin;lib\mysql-connector-j.jar" collegecomplaint.App
```

---

## 🌐 2. How to Run as Web Application (Browser)

The project also includes a complete modern browser version:
- **Local Server**: Running at **[http://localhost:3000](http://localhost:3000)**
- **1-Click Launch**: Double-click [start-browser-app.bat](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/start-browser-app.bat)
- **Offline Direct View**: Double-click [index.html](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/index.html)

---

## 🔑 Demo Logins (Both Desktop & Web)

| Role | Name | Department / Role | Email | Password |
| :--- | :--- | :--- | :--- | :--- |
| **Student** | Abhinav U | Computer Science (CSE) | `abhinav@example.com` | `student@123` |
| **Student** | Sneha S | Electronics (ECE) | `sneha@example.com` | `student@123` |
| **Admin** | Priya Nair | Campus Warden | `warden@example.com` | `admin@123` |
| **Admin** | Suresh Babu | Head of Department (HOD) | `hod@example.com` | `admin@123` |

---

## 📁 Project Structure

```
c:\Users\Hariprasad\Videos\JAVA PROJECT\
├── run-desktop-java.bat            # 1-Click Desktop Java App Launcher
├── CollegeComplaint.jar            # Compiled executable desktop JAR
├── bin/                            # Compiled Java bytecode (.class files)
├── lib/
│   └── mysql-connector-j.jar       # MySQL JDBC Connector library
├── src/main/java/collegecomplaint/
│   └── App.java                    # Java Swing Desktop Source Code
├── sql/
│   └── CollegeComplaint.sql        # MySQL Schema & Seeds
├── web/                            # Modern Web Browser Version
├── server.js                       # Local Web Server
└── start-browser-app.bat           # 1-Click Browser Launcher
```
