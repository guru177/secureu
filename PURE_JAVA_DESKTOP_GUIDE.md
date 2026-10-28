# SecureU — Pure Java Desktop Application (Swing & JDBC)

This document details the **100% Pure Java Desktop GUI Application** developed using **Java Swing & AWT (`javax.swing.*`, `java.awt.*`)** with **MySQL JDBC (`java.sql.*`)**.

The application runs directly as a native Java window (`JFrame`) without using an embedded or external web browser.

---

## 🚀 How to Launch the Pure Java Desktop App

### Option A: One-Click Launcher (Recommended)
In the project folder, double-click:
### 👉 **[`Launch-CollegeComplaint.bat`](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/Launch-CollegeComplaint.bat)**
*(Or double-click [`run-desktop-java.bat`](file:///c:/Users/Hariprasad/Videos/JAVA%20PROJECT/run-desktop-java.bat))*

### Option B: From Command Prompt / PowerShell
```powershell
cd "c:\Users\Hariprasad\Videos\JAVA PROJECT"
java -jar CollegeComplaint.jar
```

---

## 🖥️ Pure Java Swing Architecture & Windows

### 1. Pure Java Login Window (`App.LoginWindow` extends `JFrame`)
* **Framework**: Built with `javax.swing.JFrame`, `JPanel` with `GridBagLayout`, and native system look & feel.
* **Role Selection**: Toggle between **Student** and **Administrator / Warden**.
* **Credential Fields**: Styled `JTextField` for email and `JPasswordField` for secure password input.
* **Quick-Fill Demo Buttons** (For Teacher / Viva Evaluation):
  * Click **"Student (Abhinav)"** &rarr; automatically fills `abhinav@example.com` / `student@123`.
  * Click **"Admin (Dr. Raman)"** &rarr; automatically fills `warden@example.com` / `admin@123`.
* **Database Authentication**: Executes SQL `SELECT` via `PreparedStatement` to verify credentials against MySQL `students` or `admin` table.

---

### 2. Pure Java Student Dashboard (`App.StudentDashboard` extends `JFrame`)
* **Header Bar**: Displays student's name, registered department, student ID, and a **Logout** button.
* **Tabbed Interface (`JTabbedPane`)**:
  1. **File New Grievance**:
     * Category dropdown (`JComboBox`): Hostel, Academic, Food, Verbal Harassment, Physical Harassment, Other.
     * Description text area (`JTextArea` inside `JScrollPane`).
     * Optional evidence reference field.
     * **Submit Button**: Executes JDBC `INSERT INTO complaints` and shows a dialog with the generated Complaint ID.
  2. **Track Status**:
     * Search grievance by Complaint ID.
     * Displays real-time status card with filing date, category, and administrative remarks.
  3. **My Grievance History**:
     * Live `JTable` driven by `DefaultTableModel`.
     * **Custom Status Badges (`DefaultTableCellRenderer`)**:
       * 🟡 **Pending**: Amber highlight
       * 🔵 **In Progress**: Blue highlight
       * 🟢 **Resolved**: Green highlight
     * **Refresh Table** button to reload live records from MySQL.

---

### 3. Pure Java Administrator Dashboard (`App.AdminDashboard` extends `JFrame`)
* **Header Bar**: Displays administrator credentials, designated role, and **Logout** button.
* **Live KPI Metric Cards**:
  * 📊 **Total Grievances**
  * ⏳ **Pending Review**
  * ⚙️ **In Progress**
  * ✅ **Resolved Cases**
* **Filter & Search Bar**:
  * Filter by Status dropdown (`All Statuses`, `Pending`, `In Progress`, `Resolved`).
  * Real-time search keyword field (filters description or student name).
  * **Apply Filter** and **Reset** buttons.
* **Complaints Master Table (`JTable`)**:
  * Displays ID, Student ID, Student Name, Department, Category, Description, Status, Date Filed, and Remarks.
  * Click any table row &rarr; automatically populates the update controls at the bottom!
* **Status Update Panel**:
  * Displays selected Complaint ID.
  * Status dropdown (`Pending`, `In Progress`, `Resolved`).
  * Administrative remarks text field.
  * **Save Update** button: Executes JDBC `UPDATE complaints SET status=?, remarks=? WHERE complaint_id=?` and instantly refreshes table data and KPI counts.

---

## 📋 Java Swing Components Used in Code

| Component | Class Used | Purpose in SecureU |
| :--- | :--- | :--- |
| **Top-level Windows** | `javax.swing.JFrame` | Login, Student Portal, Admin Portal windows |
| **Tabbed Views** | `javax.swing.JTabbedPane` | Student navigation (File, Track, History) |
| **Tables** | `javax.swing.JTable` | Rendering grievances with custom cell renderers |
| **Table Models** | `javax.swing.table.DefaultTableModel` | Dynamic data binding from JDBC `ResultSet` |
| **Input Fields** | `javax.swing.JTextField`, `JPasswordField` | Text input and masked passwords |
| **Text Areas** | `javax.swing.JTextArea`, `JScrollPane` | Multi-line complaint descriptions with scrolling |
| **Dropdowns** | `javax.swing.JComboBox` | Role, category, and status selectors |
| **Dialogs** | `javax.swing.JOptionPane` | Success, confirmation, and error alert dialogs |
| **Layout Managers** | `BorderLayout`, `GridBagLayout`, `GridLayout`, `FlowLayout` | Responsive Swing panel layouts |
| **Borders** | `EmptyBorder`, `LineBorder`, `CompoundBorder`, `MatteBorder` | Clean modern card styling |

---

## 🎓 Teacher / Viva Evaluation Answers

**Q1: Is this an electron app or web wrapper?**
* **Answer**: No. This is 100% native Java Swing using `javax.swing.JFrame`, `JTable`, and `JTabbedPane` compiled into `CollegeComplaint.jar` running on the Java Virtual Machine (JVM).

**Q2: How does the Java GUI communicate with the database?**
* **Answer**: Through Java Database Connectivity (JDBC) using `java.sql.DriverManager`, `Connection`, `PreparedStatement`, and `ResultSet` connecting to MySQL at `jdbc:mysql://localhost:3306/CollegeComplaint`.

**Q3: Can the application also run on the command line?**
* **Answer**: Yes. Running with `--terminal` launches the interactive CLI console with ASCII tables, allowing full functionality even on headless servers.
