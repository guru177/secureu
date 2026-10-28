package collegecomplaint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * SecureU — Java Backend & Terminal DBMS Redressal System
 * 
 * Architecture:
 * 1. Interactive Java Terminal Console & ASCII Table Reports (System.out)
 * 2. Real MySQL Database Connectivity via Java JDBC (java.sql.*)
 * 3. Pure Java Backend HTTP Server (com.sun.net.httpserver.HttpServer)
 * 4. Real-time Live JDBC Logging in Terminal for all API queries
 * 5. Java Desktop Shell Launcher & Swing UI Fallback
 */
public class App {
    public static final int PORT = 3000;
    static final String DB_URL = "jdbc:mysql://localhost:3306/CollegeComplaint?serverTimezone=UTC";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";

    // Project root directory
    static final File ROOT_DIR = new File(System.getProperty("user.dir"));
    static final File WEB_DIR = new File(ROOT_DIR, "web");

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static synchronized void log(String tag, String message) {
        String time = LocalTime.now().format(TIME_FMT);
        System.out.println(String.format("[%s] [%-13s] %s", time, tag, message));
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void main(String[] args) {
        boolean isCliOnly = false;
        boolean isSwing = false;

        for (String arg : args) {
            if ("--swing".equalsIgnoreCase(arg)) isSwing = true;
            if ("--cli".equalsIgnoreCase(arg) || "--terminal".equalsIgnoreCase(arg) || "--console".equalsIgnoreCase(arg) || "--no-gui".equalsIgnoreCase(arg)) {
                isCliOnly = true;
            }
        }

        if (isSwing) {
            System.out.println("[Java] Starting in Swing GUI Mode...");
            SwingUtilities.invokeLater(() -> new SwingLogin().setVisible(true));
            return;
        }

        // 1. Print Master Banner in Terminal
        System.out.println("========================================================================================");
        System.out.println("            SECURE U — STUDENT SAFETY & GRIEVANCE REDRESSAL SYSTEM                      ");
        System.out.println("            Course: Java Programming / DBMS Lab (Live Terminal Output)                  ");
        System.out.println("            Powered by Java (JDK " + System.getProperty("java.version") + ") + MySQL JDBC (java.sql.*)          ");
        System.out.println("========================================================================================");

        // 2. Connect to MySQL & Seed Sample Data
        try (Connection c = connect()) {
            System.out.println("[OK] [Java JDBC] Successfully connected to MySQL database: CollegeComplaint");
            seedSampleData(c);
            
            // Print Terminal Summary & Initial ASCII Table
            printDatabaseSummary(c);
            System.out.println("\n>>> RECENT GRIEVANCES / COMPLAINTS FETCHED VIA JDBC (TERMINAL OUTPUT):");
            printComplaintsTable(c, null, 10);

        } catch (SQLException e) {
            System.err.println("[WARN] [Java JDBC] MySQL notice: " + e.getMessage());
            System.err.println("       (Make sure XAMPP / MySQL service is started on port 3306)");
        }

        // 3. Start Built-in Java HTTP Server
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // API Handlers (Pure Java JDBC)
            server.createContext("/api/status", new StatusHandler());
            server.createContext("/api/login", new LoginHandler());
            server.createContext("/api/complaints", new ComplaintsHandler());
            server.createContext("/api/update", new UpdateHandler());
            server.createContext("/api/db", new DatabaseHandler());
            
            // Static File Handler (serves HTML/CSS/JS directly from Java)
            server.createContext("/", new StaticFileHandler());
            
            server.setExecutor(null); // default executor
            server.start();

            log("SERVER", "HTTP & REST API active at http://localhost:" + PORT);
            log("SERVER", "Frontend directory: " + WEB_DIR.getAbsolutePath());

            if (!isCliOnly) {
                // Launch Desktop Window in Edge/Chrome
                launchDesktopWindow("http://localhost:" + PORT);
            } else {
                log("CONSOLE", "Running in pure Terminal Console Mode (GUI auto-launch skipped).");
            }

        } catch (Exception e) {
            System.err.println("[ERROR] [Java Server] Could not bind server to port " + PORT + ": " + e.getMessage());
        }

        // 4. Start Interactive Terminal CLI Loop
        runInteractiveTerminalCli();
    }

    // =========================================================================
    // TERMINAL REPORTING & ASCII TABLES (Direct System.out Output)
    // =========================================================================

    public static void printDatabaseSummary(Connection c) {
        try {
            int totalStudents = 0, totalComplaints = 0;
            int pending = 0, inProgress = 0, resolved = 0;

            try (Statement st = c.createStatement(); ResultSet r = st.executeQuery("SELECT COUNT(*) FROM students")) {
                if (r.next()) totalStudents = r.getInt(1);
            }
            try (Statement st = c.createStatement(); ResultSet r = st.executeQuery("SELECT COUNT(*) FROM complaints")) {
                if (r.next()) totalComplaints = r.getInt(1);
            }
            try (Statement st = c.createStatement(); ResultSet r = st.executeQuery("SELECT status, COUNT(*) FROM complaints GROUP BY status")) {
                while (r.next()) {
                    String s = r.getString(1);
                    int cnt = r.getInt(2);
                    if ("Pending".equalsIgnoreCase(s)) pending = cnt;
                    else if ("In Progress".equalsIgnoreCase(s)) inProgress = cnt;
                    else if ("Resolved".equalsIgnoreCase(s)) resolved = cnt;
                }
            }

            System.out.println("----------------------------------------------------------------------------------------");
            System.out.println("  DATABASE METRICS OVERVIEW (LIVE MYSQL COUNTS):");
            System.out.printf("  * Total Students Registered : %d\n", totalStudents);
            System.out.printf("  * Total Complaints Logged   : %d\n", totalComplaints);
            System.out.printf("  * Status Breakdown          : [ Pending: %d ] | [ In Progress: %d ] | [ Resolved: %d ]\n",
                    pending, inProgress, resolved);
            System.out.println("----------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.err.println("[WARN] Could not compute database summary: " + e.getMessage());
        }
    }

    public static void printComplaintsTable(Connection c, String statusFilter, int limit) {
        String sql = "SELECT c.complaint_id, COALESCE(s.name, 'Unknown') AS student_name, " +
                     "COALESCE(s.department, 'N/A') AS department, c.category, c.status, c.date_filed, c.description " +
                     "FROM complaints c LEFT JOIN students s ON c.student_id=s.student_id ";
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            sql += "WHERE c.status = '" + statusFilter.replace("'", "''") + "' ";
        }
        sql += "ORDER BY c.complaint_id DESC";
        if (limit > 0) {
            sql += " LIMIT " + limit;
        }

        try (Statement st = c.createStatement(); ResultSet r = st.executeQuery(sql)) {
            System.out.println("+-----+--------------------+-------+-------------------+-------------+------------+---------------------------------------+");
            System.out.println("| ID  | Student Name       | Dept  | Category          | Status      | Filed Date | Description                           |");
            System.out.println("+-----+--------------------+-------+-------------------+-------------+------------+---------------------------------------+");
            
            int count = 0;
            while (r.next()) {
                count++;
                int id = r.getInt("complaint_id");
                String name = pad(r.getString("student_name"), 18);
                String dept = pad(r.getString("department"), 5);
                String cat = pad(r.getString("category"), 17);
                String status = pad(r.getString("status"), 11);
                String date = pad(r.getString("date_filed"), 10);
                String desc = pad(r.getString("description"), 37);

                System.out.printf("| %-3d | %s | %s | %s | %s | %s | %s |\n",
                        id, name, dept, cat, status, date, desc);
            }
            System.out.println("+-----+--------------------+-------+-------------------+-------------+------------+---------------------------------------+");
            System.out.println("  (Total displayed: " + count + " records from MySQL database)");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch complaints table: " + e.getMessage());
        }
    }

    public static void printStudentsTable(Connection c) {
        String sql = "SELECT student_id, name, department, email, phone FROM students ORDER BY student_id ASC";
        try (Statement st = c.createStatement(); ResultSet r = st.executeQuery(sql)) {
            System.out.println("\n>>> ALL REGISTERED STUDENTS IN DATABASE (TERMINAL OUTPUT):");
            System.out.println("+----+--------------------+-------+---------------------------+------------+");
            System.out.println("| ID | Name               | Dept  | Email                     | Phone      |");
            System.out.println("+----+--------------------+-------+---------------------------+------------+");
            int count = 0;
            while (r.next()) {
                count++;
                int id = r.getInt("student_id");
                String name = pad(r.getString("name"), 18);
                String dept = pad(r.getString("department"), 5);
                String email = pad(r.getString("email"), 25);
                String phone = pad(r.getString("phone"), 10);

                System.out.printf("| %-2d | %s | %s | %s | %s |\n", id, name, dept, email, phone);
            }
            System.out.println("+----+--------------------+-------+---------------------------+------------+");
            System.out.println("  (Total registered students: " + count + ")");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch students: " + e.getMessage());
        }
    }

    private static String pad(String s, int width) {
        if (s == null) s = "-";
        s = s.replace("\n", " ").replace("\r", " ").trim();
        if (s.length() > width) {
            return s.substring(0, Math.max(0, width - 3)) + "...";
        }
        return String.format("%-" + width + "s", s);
    }

    // =========================================================================
    // INTERACTIVE TERMINAL COMMAND LOOP (CLI MODE)
    // =========================================================================

    private static void runInteractiveTerminalCli() {
        printMenuHelp();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n[SecureU Console] Enter command (1-8, menu, exit) > ");
            String line;
            try {
                if (!scanner.hasNextLine()) break;
                line = scanner.nextLine().trim();
            } catch (Exception e) {
                break;
            }

            if (line.isEmpty()) continue;

            if ("0".equals(line) || "exit".equalsIgnoreCase(line) || "quit".equalsIgnoreCase(line)) {
                System.out.println("\n[Java] Shutting down SecureU application. Goodbye!");
                System.exit(0);
                return;
            }

            try (Connection c = connect()) {
                switch (line.toLowerCase()) {
                    case "1":
                        System.out.println("\n>>> FULL COMPLAINTS TABLE (ALL RECORDS):");
                        printComplaintsTable(c, null, 100);
                        break;

                    case "2":
                        System.out.println("\nFilter options: [P] Pending  |  [I] In Progress  |  [R] Resolved");
                        System.out.print("Enter status filter (P/I/R): ");
                        String sf = scanner.nextLine().trim().toUpperCase();
                        String filter = null;
                        if (sf.startsWith("P")) filter = "Pending";
                        else if (sf.startsWith("I")) filter = "In Progress";
                        else if (sf.startsWith("R")) filter = "Resolved";

                        if (filter != null) {
                            System.out.println("\n>>> FILTERED COMPLAINTS (Status = " + filter + "):");
                            printComplaintsTable(c, filter, 100);
                        } else {
                            System.out.println("Invalid selection. Showing all.");
                            printComplaintsTable(c, null, 100);
                        }
                        break;

                    case "3":
                        printStudentsTable(c);
                        break;

                    case "4":
                        System.out.println("\n--- [FILE COMPLAINT VIA TERMINAL] ---");
                        System.out.print("Enter Student ID (1-12) [default 1]: ");
                        String sidStr = scanner.nextLine().trim();
                        int sid = sidStr.isEmpty() ? 1 : Integer.parseInt(sidStr);

                        System.out.println("Categories: Academic, Hostel, Food, Verbal Harassment, Physical Harassment, Other");
                        System.out.print("Enter Category [Hostel]: ");
                        String cat = scanner.nextLine().trim();
                        if (cat.isEmpty()) cat = "Hostel";

                        System.out.print("Enter Complaint Description: ");
                        String desc = scanner.nextLine().trim();
                        if (desc.isEmpty()) desc = "Issue reported via Java Terminal Console.";

                        String insertSql = "INSERT INTO complaints (student_id, category, description, status, date_filed) VALUES (?, ?, ?, 'Pending', CURRENT_DATE())";
                        try (PreparedStatement ps = c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                            ps.setInt(1, sid);
                            ps.setString(2, cat);
                            ps.setString(3, desc);
                            ps.executeUpdate();
                            int newId = -1;
                            try (ResultSet k = ps.getGeneratedKeys()) {
                                if (k.next()) newId = k.getInt(1);
                            }
                            System.out.println("[SUCCESS] New complaint logged with ID #" + newId + " in MySQL database!");
                            log("TERMINAL INSERT", "Complaint #" + newId + " filed for Student ID " + sid);
                        }
                        break;

                    case "5":
                        System.out.println("\n--- [UPDATE COMPLAINT STATUS VIA TERMINAL] ---");
                        System.out.print("Enter Complaint ID to update: ");
                        int cid = Integer.parseInt(scanner.nextLine().trim());

                        System.out.println("Available Statuses: [1] Pending  [2] In Progress  [3] Resolved");
                        System.out.print("Select Status (1/2/3) [3]: ");
                        String opt = scanner.nextLine().trim();
                        String newStatus = "Resolved";
                        if ("1".equals(opt)) newStatus = "Pending";
                        else if ("2".equals(opt)) newStatus = "In Progress";

                        System.out.print("Enter Administrative Remarks: ");
                        String remarks = scanner.nextLine().trim();
                        if (remarks.isEmpty()) remarks = "Status updated via Java Terminal Console.";

                        String updSql = "UPDATE complaints SET status=?, remarks=? WHERE complaint_id=?";
                        try (PreparedStatement ps = c.prepareStatement(updSql)) {
                            ps.setString(1, newStatus);
                            ps.setString(2, remarks);
                            ps.setInt(3, cid);
                            int rows = ps.executeUpdate();
                            if (rows > 0) {
                                System.out.println("[SUCCESS] Complaint #" + cid + " successfully updated to '" + newStatus + "'!");
                                log("TERMINAL UPDATE", "Complaint #" + cid + " set to " + newStatus);
                            } else {
                                System.out.println("[WARN] No complaint found with ID #" + cid);
                            }
                        }
                        break;

                    case "6":
                        printDatabaseSummary(c);
                        break;

                    case "7":
                        launchDesktopWindow("http://localhost:" + PORT);
                        System.out.println("[OK] Opened browser window to http://localhost:" + PORT);
                        break;

                    case "8":
                    case "menu":
                    case "help":
                        printMenuHelp();
                        break;

                    default:
                        System.out.println("Unknown option '" + line + "'. Type 'menu' or '8' for list of commands.");
                        break;
                }
            } catch (SQLException e) {
                System.err.println("[JDBC ERROR] " + e.getMessage());
            } catch (Exception e) {
                System.err.println("[INPUT ERROR] " + e.getMessage());
            }
        }
    }

    private static void printMenuHelp() {
        System.out.println("\n========================================================================================");
        System.out.println("                          SECURE U — TERMINAL CONSOLE COMMANDS                          ");
        System.out.println("========================================================================================");
        System.out.println("  [1] View All Complaints Table (Full terminal table with IDs & status)");
        System.out.println("  [2] Filter Complaints by Status (Pending / In Progress / Resolved)");
        System.out.println("  [3] View All Registered Students Table");
        System.out.println("  [4] Submit New Complaint (Interactive Terminal Input)");
        System.out.println("  [5] Update Complaint Status & Remarks (Interactive Terminal Input)");
        System.out.println("  [6] View Live Database Summary & Metrics");
        System.out.println("  [7] Open / Re-open Web GUI Browser (http://localhost:" + PORT + ")");
        System.out.println("  [8] Show This Menu Again (or type 'menu')");
        System.out.println("  [0] Exit Application (or type 'exit')");
        System.out.println("========================================================================================");
        System.out.println("  * Web GUI and REST endpoints are running simultaneously.");
        System.out.println("  * Any action in browser will be logged LIVE in this terminal below.");
        System.out.println("========================================================================================");
    }

    // Launch standalone frameless desktop window
    public static void launchDesktopWindow(String url) {
        new Thread(() -> {
            try {
                Thread.sleep(800); // Give server time to bind
                String[] possiblePaths = {
                    "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe",
                    "C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe",
                    "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                    "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
                };

                for (String path : possiblePaths) {
                    File f = new File(path);
                    if (f.exists()) {
                        log("DESKTOP SHELL", "Launching Standalone Desktop Window: " + f.getName());
                        new ProcessBuilder(path, "--app=" + url, "--start-maximized").start();
                        return;
                    }
                }

                // Fallback to default browser
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                }
            } catch (Exception e) {
                System.err.println("Could not launch desktop window automatically: " + e.getMessage());
            }
        }).start();
    }

    // =========================================================================
    // JAVA REST API HANDLERS (JDBC with Live Terminal Logging)
    // =========================================================================

    static class StatusHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            boolean dbOk = false;
            try (Connection c = connect()) { dbOk = true; } catch (Exception ignored) {}
            String json = "{\"status\":\"online\",\"engine\":\"Java " + System.getProperty("java.version") + "\",\"db_connected\":" + dbOk + "}";
            sendJsonResponse(ex, 200, json);
        }
    }

    static class LoginHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
                sendJsonResponse(ex, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            String body = readBody(ex);
            Map<String, String> params = parseJsonSimple(body);
            String email = params.getOrDefault("email", "").trim();
            String password = params.getOrDefault("password", "").trim();
            String role = params.getOrDefault("role", "student").trim();

            boolean isAdmin = "admin".equalsIgnoreCase(role);
            String sql = isAdmin
                ? "SELECT admin_id, name, role, email FROM admin WHERE email=? AND password=?"
                : "SELECT student_id, name, department, email FROM students WHERE email=? AND password=?";

            try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql)) {
                s.setString(1, email);
                s.setString(2, password);
                try (ResultSet r = s.executeQuery()) {
                    if (r.next()) {
                        String userName = r.getString(2);
                        log("JDBC LOGIN", "SUCCESS: " + (isAdmin ? "Admin " : "Student ") + "'" + userName + "' (" + email + ") authenticated.");
                        
                        String userJson = isAdmin
                            ? String.format("{\"role\":\"admin\",\"data\":{\"admin_id\":%d,\"name\":\"%s\",\"role\":\"%s\",\"email\":\"%s\"}}",
                                r.getInt(1), escape(userName), escape(r.getString(3)), escape(r.getString(4)))
                            : String.format("{\"role\":\"student\",\"data\":{\"student_id\":%d,\"name\":\"%s\",\"department\":\"%s\",\"email\":\"%s\"}}",
                                r.getInt(1), escape(userName), escape(r.getString(3)), escape(r.getString(4)));

                        sendJsonResponse(ex, 200, "{\"success\":true,\"user\":" + userJson + "}");
                        return;
                    }
                }
                log("JDBC LOGIN", "FAILED: Invalid credentials for " + email);
                sendJsonResponse(ex, 401, "{\"success\":false,\"message\":\"Invalid email or password.\"}");
            } catch (SQLException e) {
                log("JDBC LOGIN ERROR", e.getMessage());
                sendJsonResponse(ex, 500, "{\"success\":false,\"error\":\"" + escape(e.getMessage()) + "\"}");
            }
        }
    }

    static class ComplaintsHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String method = ex.getRequestMethod();

            if ("GET".equalsIgnoreCase(method)) {
                // Fetch all complaints with joined student details
                String sql = "SELECT c.complaint_id, c.student_id, COALESCE(s.name, 'Unknown') AS student_name, " +
                             "COALESCE(s.department, 'N/A') AS department, c.category, c.description, " +
                             "c.evidence, c.status, c.date_filed, c.remarks " +
                             "FROM complaints c LEFT JOIN students s ON c.student_id=s.student_id ORDER BY c.complaint_id DESC";

                try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
                    StringBuilder sb = new StringBuilder("[");
                    boolean first = true;
                    int rowCount = 0;
                    while (r.next()) {
                        rowCount++;
                        if (!first) sb.append(",");
                        first = false;
                        sb.append(String.format("{\"complaint_id\":%d,\"student_id\":%d,\"student_name\":\"%s\"," +
                                "\"department\":\"%s\",\"category\":\"%s\",\"description\":\"%s\",\"evidence\":%s," +
                                "\"status\":\"%s\",\"date_filed\":\"%s\",\"remarks\":%s}",
                                r.getInt("complaint_id"),
                                r.getInt("student_id"),
                                escape(r.getString("student_name")),
                                escape(r.getString("department")),
                                escape(r.getString("category")),
                                escape(r.getString("description")),
                                r.getString("evidence") == null ? "null" : "\"" + escape(r.getString("evidence")) + "\"",
                                escape(r.getString("status")),
                                r.getDate("date_filed"),
                                r.getString("remarks") == null ? "null" : "\"" + escape(r.getString("remarks")) + "\""
                        ));
                    }
                    sb.append("]");
                    log("JDBC SELECT", "Fetched " + rowCount + " complaints from MySQL for Dashboard view.");
                    sendJsonResponse(ex, 200, sb.toString());
                } catch (SQLException e) {
                    log("JDBC SELECT ERROR", e.getMessage());
                    sendJsonResponse(ex, 500, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
                }

            } else if ("POST".equalsIgnoreCase(method)) {
                // Submit new complaint
                String body = readBody(ex);
                Map<String, String> p = parseJsonSimple(body);

                int studentId = Integer.parseInt(p.getOrDefault("student_id", "1"));
                String category = p.getOrDefault("category", "Other");
                String desc = p.getOrDefault("description", "");
                String evidence = p.get("evidence");

                String sql = "INSERT INTO complaints (student_id, category, description, evidence, status, date_filed) VALUES (?, ?, ?, ?, 'Pending', CURRENT_DATE())";
                try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    s.setInt(1, studentId);
                    s.setString(2, category);
                    s.setString(3, desc);
                    s.setString(4, (evidence == null || evidence.trim().isEmpty() || "null".equals(evidence)) ? null : evidence.trim());
                    s.executeUpdate();

                    int newId = -1;
                    try (ResultSet k = s.getGeneratedKeys()) {
                        if (k.next()) newId = k.getInt(1);
                    }
                    log("JDBC INSERT", "New complaint filed! ID #" + newId + " | Student ID: " + studentId + " | Category: " + category);
                    sendJsonResponse(ex, 200, "{\"success\":true,\"complaint_id\":" + newId + "}");
                } catch (SQLException e) {
                    log("JDBC INSERT ERROR", e.getMessage());
                    sendJsonResponse(ex, 500, "{\"success\":false,\"error\":\"" + escape(e.getMessage()) + "\"}");
                }
            } else {
                sendJsonResponse(ex, 405, "{\"error\":\"Method Not Allowed\"}");
            }
        }
    }

    static class UpdateHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
                sendJsonResponse(ex, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            String body = readBody(ex);
            Map<String, String> p = parseJsonSimple(body);
            int cid = Integer.parseInt(p.getOrDefault("complaint_id", "0"));
            String status = p.getOrDefault("status", "Pending");
            String remarks = p.getOrDefault("remarks", "");

            String sql = "UPDATE complaints SET status=?, remarks=? WHERE complaint_id=?";
            try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql)) {
                s.setString(1, status);
                s.setString(2, remarks);
                s.setInt(3, cid);
                int count = s.executeUpdate();
                if (count > 0) {
                    log("JDBC UPDATE", "Complaint #" + cid + " status updated to '" + status + "' | Remarks: '" + remarks + "'");
                    sendJsonResponse(ex, 200, "{\"success\":true,\"message\":\"Complaint #" + cid + " updated.\"}");
                } else {
                    log("JDBC UPDATE", "Complaint #" + cid + " not found.");
                    sendJsonResponse(ex, 404, "{\"success\":false,\"message\":\"Complaint not found.\"}");
                }
            } catch (SQLException e) {
                log("JDBC UPDATE ERROR", e.getMessage());
                sendJsonResponse(ex, 500, "{\"success\":false,\"error\":\"" + escape(e.getMessage()) + "\"}");
            }
        }
    }

    static class DatabaseHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            // Returns raw tables for the Database Inspector
            try (Connection c = connect()) {
                String studentsJson = queryToJson(c, "SELECT student_id, name, department, email, password, phone FROM students");
                String adminJson = queryToJson(c, "SELECT admin_id, name, role, email, password FROM admin");
                String complaintsJson = queryToJson(c, "SELECT complaint_id, student_id, category, description, evidence, status, date_filed, remarks FROM complaints ORDER BY complaint_id DESC");

                log("JDBC INSPECT", "Database inspector accessed: Students, Admin, and Complaints tables dumped.");
                String full = String.format("{\"students\":%s,\"admin\":%s,\"complaints\":%s}", studentsJson, adminJson, complaintsJson);
                sendJsonResponse(ex, 200, full);
            } catch (SQLException e) {
                log("JDBC INSPECT ERROR", e.getMessage());
                sendJsonResponse(ex, 500, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
            }
        }

        private String queryToJson(Connection c, String sql) throws SQLException {
            try (PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
                ResultSetMetaData md = r.getMetaData();
                int count = md.getColumnCount();
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                while (r.next()) {
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{");
                    for (int i = 1; i <= count; i++) {
                        if (i > 1) sb.append(",");
                        String col = md.getColumnLabel(i);
                        Object val = r.getObject(i);
                        sb.append("\"").append(col).append("\":");
                        if (val == null) sb.append("null");
                        else if (val instanceof Number || val instanceof Boolean) sb.append(val);
                        else sb.append("\"").append(escape(val.toString())).append("\"");
                    }
                    sb.append("}");
                }
                sb.append("]");
                return sb.toString();
            }
        }
    }

    // =========================================================================
    // STATIC FILE HANDLER (Serves web assets directly from Java)
    // =========================================================================

    static class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String path = ex.getRequestURI().getPath();
            if ("/".equals(path) || path.isEmpty()) path = "/index.html";

            File file = new File(WEB_DIR, path.replace('/', File.separatorChar));
            if (!file.exists() || file.isDirectory()) {
                file = new File(WEB_DIR, "index.html");
            }

            String mime = "application/octet-stream";
            String lower = file.getName().toLowerCase();
            if (lower.endsWith(".html")) mime = "text/html; charset=UTF-8";
            else if (lower.endsWith(".css")) mime = "text/css; charset=UTF-8";
            else if (lower.endsWith(".js")) mime = "application/javascript; charset=UTF-8";
            else if (lower.endsWith(".json")) mime = "application/json; charset=UTF-8";
            else if (lower.endsWith(".svg")) mime = "image/svg+xml";
            else if (lower.endsWith(".png")) mime = "image/png";
            else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) mime = "image/jpeg";

            byte[] bytes = Files.readAllBytes(file.toPath());
            ex.getResponseHeaders().set("Content-Type", mime);
            ex.getResponseHeaders().set("Cache-Control", "no-cache");
            ex.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // =========================================================================
    // SAMPLE DATA SEEDER (JDBC)
    // =========================================================================
    static void seedSampleData(Connection c) {
        try {
            String[][] students = {
                {"1","Abhinav U","CSE","abhinav@example.com","9876543210"},
                {"2","Sneha S","ECE","sneha@example.com","8765432109"},
                {"3","Rahul V","MECH","rahul@example.com","9123456780"},
                {"4","Priya S","ECE","priya.s@example.com","9988776655"},
                {"5","Vignesh R","CIVIL","vignesh@example.com","9090909090"},
                {"6","Anjali K","CSE","anjali@example.com","9811122233"},
                {"7","Mohammed F","EEE","farhan@example.com","9822233344"},
                {"8","Divya M","IT","divya@example.com","9833344455"},
                {"9","Karthik N","MECH","karthik@example.com","9844455566"},
                {"10","Fathima Z","CSE","fathima@example.com","9855566677"},
                {"11","Arjun P","EEE","arjun@example.com","9866677788"},
                {"12","Meera J","IT","meera@example.com","9877788899"}
            };
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT IGNORE INTO students(student_id,name,department,email,password,phone) VALUES(?,?,?,?, 'student@123', ?)")) {
                for (String[] s : students) {
                    ps.setInt(1, Integer.parseInt(s[0]));
                    ps.setString(2, s[1]); ps.setString(3, s[2]); ps.setString(4, s[3]); ps.setString(5, s[4]);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            int existing = 0;
            try (Statement st = c.createStatement(); ResultSet r = st.executeQuery("SELECT COUNT(*) FROM complaints")) {
                if (r.next()) existing = r.getInt(1);
            }
            if (existing >= 25) return;

            String[][] rows = {
                {"1","Hostel","Hot water geyser on 2nd floor of Block B not working for 3 days.","geyser_photo.jpg","In Progress","4","Electrician assigned. Heating coil being replaced."},
                {"2","Academic","DSP lab workstation #14 crashes repeatedly during practicals.","lab_bsod.png","Pending","2",null},
                {"3","Academic","Internal assessment marks for Thermodynamics not updated on portal.",null,"Pending","1",null},
                {"4","Food","Mess dinner served stale rice on Wednesday night.","mess_rice.jpg","Resolved","9","Food committee inspected. Vendor warned and menu revised."},
                {"5","Other","Classroom 204 air conditioner not working.","ac_room204.jpg","Resolved","12","AC compressor repaired by maintenance."},
                {"6","Verbal Harassment","Senior students using abusive language in hostel corridor.",null,"In Progress","6","Anti-ragging committee investigating."},
                {"7","Academic","Revaluation request for Circuits paper not processed.","reval_receipt.pdf","Pending","3",null},
                {"8","Hostel","Wi-Fi in Girls Hostel Block C down for a week.",null,"Resolved","15","Router replaced; bandwidth upgraded."},
                {"9","Food","Drinking water dispenser in canteen is unhygienic.","dispenser.jpg","Resolved","20","Filter replaced and weekly cleaning scheduled."},
                {"10","Physical Harassment","Ragging incident reported near hostel mess.",null,"In Progress","5","Statements recorded. Disciplinary committee meeting scheduled."},
                {"11","Academic","Library not open on weekends during exam season.",null,"Resolved","25","Weekend library hours extended till 8 PM."},
                {"12","Other","Street lights near parking area not working.","parking_dark.jpg","Pending","7",null}
            };
            String sql = "INSERT INTO complaints (student_id, category, description, evidence, status, date_filed, remarks) " +
                         "VALUES (?, ?, ?, ?, ?, DATE_SUB(CURRENT_DATE(), INTERVAL ? DAY), ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                for (String[] r : rows) {
                    ps.setInt(1, Integer.parseInt(r[0]));
                    ps.setString(2, r[1]);
                    ps.setString(3, r[2]);
                    ps.setString(4, r[3]);
                    ps.setString(5, r[4]);
                    ps.setInt(6, Integer.parseInt(r[5]));
                    ps.setString(7, r[6]);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            log("JDBC SEED", "Seeded " + rows.length + " sample complaints into MySQL.");
        } catch (SQLException e) {
            log("JDBC SEED NOTICE", e.getMessage());
        }
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    private static void sendJsonResponse(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int n;
            while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
            return bos.toString(StandardCharsets.UTF_8);
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static Map<String, String> parseJsonSimple(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) return map;
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String k = kv[0].trim().replace("\"", "");
                String v = kv[1].trim();
                if (v.startsWith("\"") && v.endsWith("\"")) {
                    v = v.substring(1, v.length() - 1);
                }
                map.put(k, v);
            }
        }
        return map;
    }

    // Minimal Swing Fallback if invoked with --swing
    static class SwingLogin extends JFrame {
        SwingLogin() {
            setTitle("SecureU — Swing Interface");
            setSize(450, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JPanel p = new JPanel(new GridLayout(0, 1, 10, 10));
            p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            p.add(new JLabel("SecureU Swing Desktop Mode"));
            p.add(new JLabel("For modern UI, run without --swing to launch the Java Desktop Engine."));
            JButton btnLaunch = new JButton("Launch Modern Desktop Interface");
            btnLaunch.addActionListener(e -> {
                launchDesktopWindow("http://localhost:3000");
                dispose();
            });
            p.add(btnLaunch);
            add(p);
        }
    }
}
