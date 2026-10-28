# SecureU — Complete Step-by-Step System Setup & Execution Guide

**Course:** Java Programming / DBMS Lab  
**Technology Stack:** 100% Pure Java Swing (`javax.swing.*`) + MySQL JDBC (`java.sql.*`) + Optional Web Portal

---

## 📑 Table of Contents
1. [System Prerequisites](#1-system-prerequisites)
2. [MySQL Database Setup (XAMPP)](#2-mysql-database-setup-xampp)
3. [Running as Pure Java Desktop Application (GUI Mode)](#3-running-as-pure-java-desktop-application-gui-mode)
4. [Running in Terminal Console Mode (CLI / Viva Mode)](#4-running-in-terminal-console-mode-cli--viva-mode)
5. [Demo Credentials & What to Test](#5-demo-credentials--what-to-test)
6. [Optional: Running the Web Portal](#6-optional-running-the-web-portal)
7. [Troubleshooting & Common Questions](#7-troubleshooting--common-questions)

---

## 1. System Prerequisites

Before running the project, verify that the following two software components are installed:

| Component | Recommended Version | Path on Your System |
| :--- | :--- | :--- |
| **Java Development Kit (JDK)** | JDK 17, 21, or 27 | `C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe` |
| **MySQL Database Server** | XAMPP MySQL or MySQL Server 8.x | `http://localhost/phpmyadmin` (Port 3306) |

---

## 2. MySQL Database Setup (XAMPP)

Follow these 4 simple steps to set up the database:

### Step 2.1: Start MySQL in XAMPP
1. Open the **XAMPP Control Panel** from your Start Menu.
2. Next to **MySQL**, click the **Start** button.
3. Verify that the MySQL module turns green and displays port **3306**.

### Step 2.2: Open phpMyAdmin
Open your web browser and navigate to:
```text
http://localhost/phpmyadmin
```

### Step 2.3: Create Database & Import SQL Schema
1. Click on the **"Databases"** tab in phpMyAdmin.
2. Under "Create database", enter the name: `CollegeComplaint` (or `collegecomplaint`).
3. Click **"Create"**.
4. Select the newly created `CollegeComplaint` database from the left sidebar.
5. Click on the **"Import"** tab at the top.
6. Click **"Choose File"** and select:
   ```text
   c:\Users\Hariprasad\Videos\JAVA PROJECT\sql\CollegeComplaint.sql
   ```
7. Scroll to the bottom and click **"Import"**.
8. You should see a green success banner showing tables `students`, `admin`, and `complaints` imported!

> **Note:** The Java application also includes an automatic JDBC data seeder. If the database is freshly created, Java will automatically populate sample students and complaints on its first run!

---

## 3. Running as Pure Java Desktop Application (GUI Mode)

In this mode, the program launches a **native Java desktop window (`JFrame`)** with zero browser dependencies.

### Option A: One-Click Desktop Launcher (Recommended)
Navigate to your project folder:
```text
c:\Users\Hariprasad\Videos\JAVA PROJECT
```
Double-click:
### 👉 **`Launch-CollegeComplaint.bat`**
*(or `run-desktop-java.bat`)*

A Command Prompt window will open, verify MySQL and port 3000, and launch the **SecureU Pure Java Login Window (`JFrame`)**!

---

### Option B: From PowerShell
Open PowerShell in the project directory and run:
```powershell
.\run.ps1
```

---

### Option C: Direct Java Command
If you want to run the JAR file with the exact Java binary path:
```powershell
& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -jar CollegeComplaint.jar
```

---

## 4. Running in Terminal Console Mode (CLI / Viva Mode)

If your teacher or lab examiner asks: **"Show me the Java output in the terminal itself!"**, use this mode.

### Option A: One-Click Terminal Launcher
In your project folder, double-click:
### 👉 **`Launch-Terminal-Console.bat`**

---

### Option B: From PowerShell / Command Prompt
```powershell
.\run.ps1 --terminal
```
*(Or `& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe" -jar CollegeComplaint.jar --terminal`)*

---

### What Appears in the Terminal:
1. **Banner**: JDK version, MySQL database name, and JDBC connection confirmation.
2. **Metrics Table**: Real-time SQL count of total students, complaints, and status breakdown.
3. **ASCII Table**: Formatted grid of recent complaints fetched directly from MySQL.
4. **Interactive Console Menu**:

```text
========================================================================================
                          SECURE U — TERMINAL CONSOLE COMMANDS                          
========================================================================================
  [1] View All Complaints Table (Full terminal table with IDs & status)
  [2] Filter Complaints by Status (Pending / In Progress / Resolved)
  [3] View All Registered Students Table
  [4] Submit New Complaint (Interactive Terminal Input)
  [5] Update Complaint Status & Remarks (Interactive Terminal Input)
  [6] View Live Database Summary & Metrics
  [7] Open Pure Java Swing Desktop Window (JFrame)
  [8] Show This Menu Again (or type 'menu')
  [0] Exit Application (or type 'exit')
========================================================================================
[SecureU Console] Enter command (1-8, menu, exit) > 
```

### Commands You Can Type in Front of the Teacher:
* Type `1` &rarr; Prints the complete 50+ complaint table in formatted ASCII columns.
* Type `2` &rarr; Type `P` for Pending, `I` for In Progress, or `R` for Resolved.
* Type `3` &rarr; Displays registered student details (ID, Name, Dept, Email, Phone).
* Type `4` &rarr; Interactively file a complaint in the console (shows JDBC `INSERT`).
* Type `5` &rarr; Update a complaint to Resolved with remarks (shows JDBC `UPDATE`).
* Type `6` &rarr; Displays live SQL `COUNT(*)` counts.

---

## 5. Demo Credentials & What to Test

### Student Account:
* **Email:** `abhinav@example.com`
* **Password:** `student@123`
* **Role:** Student
* **Quick Demo Button in GUI:** Click **"Student (Abhinav)"** to auto-fill.
* **Features to Test:**
  * **File Grievance tab:** Select Category, write a description, click **"Submit Grievance to Administration"**. A dialog will display your newly assigned Complaint ID!
  * **Track Status tab:** Enter your Complaint ID and click **"Search Grievance"** to see live status and admin remarks.
  * **My Grievance History tab:** View your complaints with color badges (Pending in Amber, In Progress in Blue, Resolved in Green).

### Administrator Account:
* **Email:** `warden@example.com`
* **Password:** `admin@123`
* **Role:** Administrator / Warden
* **Quick Demo Button in GUI:** Click **"Admin (Dr. Raman)"** to auto-fill.
* **Features to Test:**
  * View the 4 live KPI cards (**Total**, **Pending**, **In Progress**, **Resolved**).
  * Filter complaints by status or search keywords.
  * **Click any table row** &rarr; it auto-fills the bottom update form!
  * Change status to **Resolved**, enter remarks (e.g., `Repaired by maintenance committee`), and click **"Save Update"**.
  * The table and KPI counters refresh instantly!

---

## 6. Optional: Running the Web Portal

If you also want to demonstrate the modern responsive web application:

### Local Web Server:
Double-click **`start-browser-app.bat`** and visit:
```text
http://localhost:3000
```

### Free Hosted Cloud Version (No Setup Needed):
Give this link to your teacher or open on your smartphone/tablet:
👉 **[https://secureu.vercel.app](https://secureu.vercel.app)**

---

## 7. Troubleshooting & Common Questions

### Q1: When running `java -jar CollegeComplaint.jar`, I get `'java' is not recognized`
* **Cause:** The `java` command hasn't been added to your current terminal session's `PATH`.
* **Fix:** Use the provided launcher script:
  ```powershell
  .\run.ps1
  ```
  Or run the `.bat` file:
  ```powershell
  .\Launch-CollegeComplaint.bat
  ```

---

### Q2: `Address already in use: bind`
* **Cause:** A previous instance of the Java server or local server is still holding port 3000.
* **Fix:** Run **`Launch-CollegeComplaint.bat`** or **`Launch-Terminal-Console.bat`**. Both scripts automatically detect and terminate any lingering port 3000 processes before launching.

---

### Q3: `MySQL connection notice: Connection refused`
* **Cause:** MySQL service is not running on port 3306.
* **Fix:** Open **XAMPP Control Panel** and click **"Start"** next to MySQL.

---

### Q4: How do I recompile the project after making changes to `App.java`?
Run these two commands in PowerShell:
```powershell
# 1. Compile Java source code:
& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\javac.exe" -cp "lib/mysql-connector-j.jar" -d bin src/main/java/collegecomplaint/App.java

# 2. Package into JAR:
& "C:\Program Files\Java\jdk-27.0.1.0.3\bin\jar.exe" cfm CollegeComplaint.jar manifest.txt -C bin .
```
