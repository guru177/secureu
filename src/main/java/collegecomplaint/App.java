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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CollegeComplaint — Complete Java Application
 * 
 * Architecture:
 * 1. Pure Java Backend Server (com.sun.net.httpserver.HttpServer)
 * 2. Real MySQL Database Connectivity via Java JDBC (java.sql.*)
 * 3. Java REST API Endpoints (/api/login, /api/complaints, /api/update, /api/db)
 * 4. Java Desktop Shell Launcher (Native frameless desktop window)
 * 5. Java Swing UI Fallback included
 */
public class App {
    public static final int PORT = 3000;
    static final String DB_URL = "jdbc:mysql://localhost:3306/CollegeComplaint?serverTimezone=UTC";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";

    // Project root directory
    static final File ROOT_DIR = new File(System.getProperty("user.dir"));
    static final File WEB_DIR = new File(ROOT_DIR, "web");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void main(String[] args) {
        // If --swing flag is passed, launch traditional Swing window
        if (args.length > 0 && "--swing".equalsIgnoreCase(args[0])) {
            System.out.println("[Java] Starting in Swing GUI Mode...");
            SwingUtilities.invokeLater(() -> new SwingLogin().setVisible(true));
            return;
        }

        System.out.println("=======================================================");
        System.out.println("   SecureU — Student Safety & Grievance Application");
        System.out.println("   Course: Java Project / DBMS Lab");
        System.out.println("   Powered by Java (JDK " + System.getProperty("java.version") + ") + MySQL JDBC");
        System.out.println("=======================================================");

        // 1. Verify MySQL Connection
        try (Connection c = connect()) {
            System.out.println("✅ [Java JDBC] Successfully connected to MySQL database: CollegeComplaint");
            seedSampleData(c);
        } catch (SQLException e) {
            System.err.println("⚠️ [Java JDBC] MySQL connection notice: " + e.getMessage());
            System.err.println("   (Ensure XAMPP MySQL is running on port 3306)");
        }

        // 2. Start Built-in Java HTTP Server
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
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

            System.out.println("🚀 [Java Server] Web/REST engine running at: http://localhost:" + PORT);
            System.out.println("📁 [Java Server] Serving frontend from: " + WEB_DIR.getAbsolutePath());

            // 3. Launch Desktop Window
            launchDesktopWindow("http://localhost:" + PORT);

            // 4. Keep server running indefinitely until terminated
            System.out.println("⚡ [Java Server] Server active. Press Ctrl+C in terminal to stop.");
            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("❌ [Java Server] Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================================================================
    // SAMPLE DATA SEEDER (JDBC) — runs once if the complaints table is small
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

            // {student_id, category, description, evidence, status, days_ago, remarks}
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
                {"12","Other","Street lights near parking area not working.","parking_dark.jpg","Pending","7",null},
                {"1","Food","Insects found in breakfast sambar.","sambar.jpg","Resolved","34","Kitchen deep cleaned; pest control done."},
                {"2","Hostel","Bathroom taps leaking in Block A, room 112.",null,"Resolved","38","Plumber fixed leakage."},
                {"3","Academic","Faculty frequently absent for Machine Design lectures.",null,"Resolved","41","HOD discussed with faculty; extra classes arranged."},
                {"4","Verbal Harassment","Lab assistant shouts at students during practicals.",null,"Resolved","45","Counselling given to staff member."},
                {"5","Hostel","Room fan making loud noise and slow.",null,"Resolved","48","Fan replaced."},
                {"6","Academic","Projector in Seminar Hall not working.","projector.jpg","In Progress","50","New projector purchase approved."},
                {"7","Other","Bus route 7 consistently late by 30 minutes.",null,"Pending","52",null},
                {"8","Food","Canteen overcharging for snacks.","bill.jpg","Resolved","55","Price list displayed; vendor warned."},
                {"9","Hostel","Mosquito problem in hostel rooms.",null,"Resolved","63","Fogging done and window nets installed."},
                {"10","Academic","Exam timetable clash between two electives.",null,"Resolved","66","Exam rescheduled by controller."},
                {"11","Physical Harassment","Student pushed during inter-department match.",null,"Resolved","70","Sports committee issued warning."},
                {"12","Food","No vegetarian option on Sunday dinner.",null,"Resolved","72","Menu updated."},
                {"1","Academic","Lab manual not provided for Compiler Design lab.",null,"Resolved","76","Manuals distributed."},
                {"2","Hostel","Hostel gate closes before library closing time.",null,"In Progress","80","Proposal sent to Dean."},
                {"3","Other","Drinking water cooler in Mech block not cooling.",null,"Resolved","84","Cooler serviced."},
                {"4","Academic","Attendance wrongly marked absent for 3 days.","attendance.png","Resolved","88","Attendance corrected."},
                {"5","Food","Mess timings too short for lab students.",null,"Pending","92",null},
                {"6","Hostel","Laundry machine broken in Girls Hostel.",null,"Resolved","96","Machine repaired."},
                {"7","Verbal Harassment","Derogatory comments on department WhatsApp group.","screenshot.png","Resolved","101","Students counselled; group moderated."},
                {"8","Academic","Internship NOC delayed by department office.",null,"Resolved","105","NOC issued."},
                {"9","Other","Gym equipment damaged.",null,"In Progress","110","Quotation requested for replacement."},
                {"10","Hostel","Power cuts every night in Block D.",null,"Resolved","115","Wiring fixed; inverter installed."},
                {"11","Academic","Question paper had out-of-syllabus questions.",null,"Resolved","120","Grace marks awarded."},
                {"12","Food","Juice stall hygiene issues.",null,"Resolved","126","Stall licence reviewed."},
                {"1","Hostel","Water tank not cleaned for months.",null,"Resolved","132","Tank cleaned."},
                {"2","Academic","Delay in publishing semester results.",null,"Resolved","138","Results published."},
                {"3","Other","Parking space insufficient for two-wheelers.",null,"Pending","142",null},
                {"4","Physical Harassment","Fight between students outside canteen.",null,"Resolved","148","Both parties suspended for 3 days."},
                {"5","Food","Mess food too spicy every day.",null,"Resolved","152","Spice levels reduced."},
                {"6","Academic","Lab computers lack required software (MATLAB).",null,"Resolved","158","Licences installed."},
                {"7","Hostel","Broken window in room 305.",null,"Resolved","164","Glass replaced."},
                {"8","Other","Medical room closed during evening hours.",null,"In Progress","170","Nurse duty extension under review."},
                {"9","Academic","Assignment deadlines clashing across subjects.",null,"Resolved","175","Faculty coordinated new schedule."}
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
            System.out.println("🌱 [Java JDBC] Seeded " + rows.length + " sample complaints for dashboard analytics.");
        } catch (SQLException e) {
            System.err.println("⚠️ [Java JDBC] Sample seeding skipped: " + e.getMessage());
        }
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
                        System.out.println("🖥️ [Java Shell] Launching Standalone Desktop Window: " + f.getName());
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
    // JAVA REST API HANDLERS (JDBC)
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
                        String userJson = isAdmin
                            ? String.format("{\"role\":\"admin\",\"data\":{\"admin_id\":%d,\"name\":\"%s\",\"role\":\"%s\",\"email\":\"%s\"}}",
                                r.getInt(1), escape(r.getString(2)), escape(r.getString(3)), escape(r.getString(4)))
                            : String.format("{\"role\":\"student\",\"data\":{\"student_id\":%d,\"name\":\"%s\",\"department\":\"%s\",\"email\":\"%s\"}}",
                                r.getInt(1), escape(r.getString(2)), escape(r.getString(3)), escape(r.getString(4)));

                        sendJsonResponse(ex, 200, "{\"success\":true,\"user\":" + userJson + "}");
                        return;
                    }
                }
                sendJsonResponse(ex, 401, "{\"success\":false,\"message\":\"Invalid email or password.\"}");
            } catch (SQLException e) {
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
                    while (r.next()) {
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
                    sendJsonResponse(ex, 200, sb.toString());
                } catch (SQLException e) {
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
                    sendJsonResponse(ex, 200, "{\"success\":true,\"complaint_id\":" + newId + "}");
                } catch (SQLException e) {
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
                    sendJsonResponse(ex, 200, "{\"success\":true,\"message\":\"Complaint #" + cid + " updated.\"}");
                } else {
                    sendJsonResponse(ex, 404, "{\"success\":false,\"message\":\"Complaint not found.\"}");
                }
            } catch (SQLException e) {
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

                String full = String.format("{\"students\":%s,\"admin\":%s,\"complaints\":%s}", studentsJson, adminJson, complaintsJson);
                sendJsonResponse(ex, 200, full);
            } catch (SQLException e) {
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
