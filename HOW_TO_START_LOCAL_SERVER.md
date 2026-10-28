# SecureU — Complete Local Server Setup & Execution Guide

This document provides step-by-step instructions for running the **SecureU Student Safety & Grievance Redressal System** on your local machine.

---

## 📋 System Requirements & Prerequisites

Before starting, ensure the following tools are installed:

| Component | Required Version | Verification Command | Notes |
| :--- | :--- | :--- | :--- |
| **Java JDK** | JDK 17, 21, or 27 | `& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -version` | Installed at `C:\Program Files\Java\jdk-27.0.1.0.3` |
| **MySQL Server** | MySQL 5.7+ / 8.0+ | Port `3306` check | Recommended via **XAMPP Control Panel** |
| **Web Browser** | Any modern browser | — | Google Chrome, Microsoft Edge, Brave, Firefox |
| **Node.js** *(Optional)* | v18+ | `node -v` | Only needed if running the alternative Node server |

---

## 🚀 Step 1: Start the MySQL Database (XAMPP)

The application uses **Java JDBC** to connect directly to MySQL.

1. Open the **XAMPP Control Panel**.
2. Locate the **MySQL** module row.
3. Click the **Start** button. Ensure the status turns **green** and shows port `3306`.
4. *(First-time setup only)*:
   - Open your browser and go to: [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
   - Click **SQL** tab and paste the contents of `sql/CollegeComplaint.sql`, then click **Go**.
   - *(Note: If skipped, the Java application automatically verifies and auto-seeds the tables on first startup).*

---

## ⚡ Step 2: Start the Local Java Server

### Method A: One-Click Launcher (Recommended)
1. Open the project root folder:
   ```text
   C:\Users\Hariprasad\Videos\JAVA PROJECT
   ```
2. Double-click the launcher script:
   - **`Launch-CollegeComplaint.bat`** (or `run-desktop-java.bat`)
3. The script now automatically:
   - Verifies MySQL is running on port `3306`.
   - Automatically frees port `3000` if an old background process was left running.
   - Starts the Java application using the installed JDK 27 binary.
   - Opens the standalone window / web portal.

---

### Method B: Start via PowerShell Terminal
If `java` is not recognized in your PowerShell terminal, use the full path to JDK 27:

```powershell
& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -jar CollegeComplaint.jar
```

*(Tip: To make `java` recognized in your current terminal session, run:)*
```powershell
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
```

---

### Method C: Alternative Node.js Server
If you encounter PowerShell script restriction with `npm start`, run `node server.js` or `npm.cmd start`:

```powershell
# Directly launch Node server (bypasses PowerShell script execution policy):
node server.js

# Or using the Windows batch command:
npm.cmd start
```

---

## 🌐 Step 3: Open the Web Portal

Once the server is running, open any browser and navigate to:
```text
http://localhost:3000
```

---

## 🔑 Step 4: Login Credentials (Pre-seeded)

Use any of the following accounts or use the **Quick 1-Click Demo Buttons** on the login page:

### 1. Student Portal
- **Email:** `abhinav@example.com`
- **Password:** `student@123`
- *Capabilities:* File grievances, upload evidence attachments (PNG, JPG, PDF, DOC), preview files in real-time, track grievance progress through live step timeline.

### 2. Warden Portal (Hostel Grievances)
- **Email:** `warden@example.com`
- **Password:** `warden@123`
- *Capabilities:* Review hostel grievances, change status (`Pending` ➔ `Under Review` ➔ `Resolved`), view submitted evidence attachments, add formal administrative remarks.

### 3. HOD Portal (Department & Academic Grievances)
- **Email:** `hod.cse@example.com`
- **Password:** `hod@123`
- *Capabilities:* Oversee departmental complaints, review analytics & graphs, update status and redressal notes, view attached evidence files.

---

## 🛠️ Step 5: How to Recompile After Code Changes

If you modify any Java source files in `src/main/java/collegecomplaint/App.java`:

1. Open PowerShell in the project directory.
2. Compile the Java source files:
   ```powershell
   & "C:\Program Files\Java\jdk-27.0.1.0.3\bin\javac.exe" -cp "lib/mysql-connector-j.jar" -d bin src/main/java/collegecomplaint/*.java
   ```
3. Re-package the executable JAR file:
   ```powershell
   & "C:\Program Files\Java\jdk-27.0.1.0.3\bin\jar.exe" cvfm CollegeComplaint.jar manifest.txt -C bin .
   ```
4. Restart the server by running `Launch-CollegeComplaint.bat`.

---

## 🔍 Step 6: Database Inspector Tool

Inside the Warden or HOD portals:
1. Click the **Database Inspector** button in the top navigation bar.
2. This sends a live query through the Java REST API (`/api/db`) to inspect:
   - `students` table
   - `admin` table
   - `complaints` table
3. You can click **Export JSON** or **Reset to Original SQL Seed Data** at any time.

---

## ❓ Troubleshooting Guide

| Error in Terminal | Root Cause | Solution |
| :--- | :--- | :--- |
| **`Address already in use: bind`** | An old Java or Node process is already running on port 3000 | Double-click `Launch-CollegeComplaint.bat` (now auto-cleans old processes), or run `taskkill /F /IM java.exe` |
| **`The term 'java' is not recognized`** | Java JDK is not loaded in current terminal session PATH | Run: `& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -jar CollegeComplaint.jar` |
| **`npm.ps1 cannot be loaded because running scripts is disabled`** | Windows PowerShell blocks `.ps1` scripts by default | Run `node server.js` or `npm.cmd start` instead |
| **`MySQL Communications link failure`** | MySQL is not running on port 3306 | Start MySQL in XAMPP Control Panel |

---

*College Complaint & Grievance Redressal System — Local Server Setup Guide*
