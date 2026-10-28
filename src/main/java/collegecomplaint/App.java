package collegecomplaint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * SecureU — Pure Java Desktop Application & DBMS Redressal System
 * 
 * Features:
 * 1. 100% Pure Java Desktop GUI (Java Swing / AWT: JFrame, JTable, JTabbedPane, CardLayout)
 * 2. Native Windows Look & Feel with professional modern typography and color palettes
 * 3. Real MySQL Database Connectivity via Java JDBC (java.sql.*)
 * 4. Dual-mode Architecture: Pure Java GUI + Simultaneous Terminal Console & Live JDBC Logging
 * 5. Background REST Server for web/hybrid synchronization
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

    // Colors for modern Pure Java Swing UI
    public static final Color COLOR_PRIMARY = new Color(15, 23, 42);      // Slate 900
    public static final Color COLOR_PRIMARY_HOVER = new Color(30, 41, 59);
    public static final Color COLOR_ACCENT = new Color(37, 99, 235);      // Blue 600
    public static final Color COLOR_BG = new Color(248, 250, 252);        // Slate 50
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(226, 232, 240);    // Slate 200
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);

    public static synchronized void log(String tag, String message) {
        String time = LocalTime.now().format(TIME_FMT);
        System.out.println(String.format("[%s] [%-13s] %s", time, tag, message));
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void main(String[] args) {
        boolean isCliOnly = false;

        for (String arg : args) {
            if ("--cli".equalsIgnoreCase(arg) || "--terminal".equalsIgnoreCase(arg) || "--console".equalsIgnoreCase(arg) || "--no-gui".equalsIgnoreCase(arg)) {
                isCliOnly = true;
            }
        }

        // Apply Native System Look & Feel for Swing
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 12));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 12));
        } catch (Exception ignored) {}

        // 1. Terminal Banner
        System.out.println("========================================================================================");
        System.out.println("            SECURE U — PURE JAVA DESKTOP APPLICATION & DBMS PORTAL                      ");
        System.out.println("            Course: Java Programming / DBMS Lab                                        ");
        System.out.println("            Powered by Java Swing (JFrame) + MySQL JDBC (java.sql.*)                   ");
        System.out.println("========================================================================================");

        // 2. Test MySQL Connection & Seed Sample Data
        try (Connection c = connect()) {
            System.out.println("[OK] [Java JDBC] Connected to MySQL database: CollegeComplaint");
            seedSampleData(c);
            printDatabaseSummary(c);
        } catch (SQLException e) {
            System.err.println("[WARN] [Java JDBC] MySQL notice: " + e.getMessage());
            System.err.println("       (Make sure XAMPP / MySQL service is started on port 3306)");
        }

        // 3. Start Background HTTP Server (for hybrid API / browser access)
        startBackgroundServer();

        // 4. Launch Pure Java Swing Desktop GUI (Default Desktop Mode)
        if (!isCliOnly) {
            log("JAVA SWING", "Launching Pure Java Desktop GUI Window (JFrame)...");
            SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
        } else {
            log("CONSOLE", "Running in pure Terminal Console Mode (GUI skipped).");
        }

        // 5. Run Interactive Terminal Console in Background
        new Thread(App::runInteractiveTerminalCli).start();
    }

    private static void startBackgroundServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/api/status", new StatusHandler());
            server.createContext("/api/login", new LoginHandler());
            server.createContext("/api/complaints", new ComplaintsHandler());
            server.createContext("/api/update", new UpdateHandler());
            server.createContext("/api/db", new DatabaseHandler());
            server.createContext("/", new StaticFileHandler());
            server.setExecutor(null);
            server.start();
            log("SERVER", "Background HTTP engine active at http://localhost:" + PORT);
        } catch (Exception e) {
            // Port may already be in use; purely optional for desktop app
            log("SERVER NOTICE", "Local server port notice: " + e.getMessage());
        }
    }

    // =========================================================================
    // SWING UI HELPERS & CUSTOM COMPONENTS
    // =========================================================================

    // Custom ModernButton that renders reliably on all Windows Look & Feels
    public static class ModernButton extends JButton {
        private final Color normalBg;
        private final Color hoverBg;
        private final Color textColor;
        private boolean isHovered = false;

        public ModernButton(String text, Color bg, Color fg) {
            super(text);
            this.normalBg = bg;
            this.hoverBg = bg.equals(Color.WHITE)
                    ? new Color(241, 245, 249)
                    : (bg.equals(COLOR_PRIMARY) ? new Color(30, 41, 59) : bg.brighter());
            this.textColor = fg;
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(fg);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 20, 10, 20));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(Math.max(d.width, 110), Math.max(d.height, 38));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Draw button background
            g2.setColor(isHovered ? hoverBg : normalBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            // Draw border for white / secondary button
            if (normalBg.equals(Color.WHITE)) {
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }

            // Draw button text
            g2.setColor(textColor);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            Rectangle r = fm.getStringBounds(getText(), g2).getBounds();
            int x = (getWidth() - r.width) / 2;
            int y = (getHeight() - r.height) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    public static JButton modernButton(String text, Color bg, Color fg) {
        return new ModernButton(text, bg, fg);
    }

    public static JButton primaryButton(String text) {
        return new ModernButton(text, COLOR_PRIMARY, Color.WHITE);
    }

    public static JButton secondaryButton(String text) {
        return new ModernButton(text, Color.WHITE, COLOR_PRIMARY);
    }

    public static JPanel cardPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(COLOR_CARD_BG);
        p.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));
        return p;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setGridColor(COLOR_BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(new Color(238, 242, 255));
        table.setSelectionForeground(COLOR_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(COLOR_PRIMARY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER));
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        // Center-align ID column
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        if (table.getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setCellRenderer(center);
        }

        // Custom Status Badge Renderer
        StatusBadgeRenderer badgeRenderer = new StatusBadgeRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            if ("status".equalsIgnoreCase(table.getColumnName(i))) {
                table.getColumnModel().getColumn(i).setCellRenderer(badgeRenderer);
            }
        }
    }

    static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));

            String status = value == null ? "" : value.toString();
            if ("Resolved".equalsIgnoreCase(status)) {
                setForeground(new Color(21, 128, 61));  // Green 700
                if (!isSelected) setBackground(new Color(240, 253, 244)); // Green 50
            } else if ("In Progress".equalsIgnoreCase(status)) {
                setForeground(new Color(29, 78, 216));  // Blue 700
                if (!isSelected) setBackground(new Color(239, 246, 255)); // Blue 50
            } else {
                setForeground(new Color(180, 83, 9));   // Amber 700
                if (!isSelected) setBackground(new Color(254, 252, 232)); // Amber 50
            }
            return c;
        }
    }

    static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();
        String[] colNames = new String[colCount];
        for (int i = 1; i <= colCount; i++) {
            colNames[i - 1] = md.getColumnLabel(i);
        }

        DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        while (rs.next()) {
            Object[] row = new Object[colCount];
            for (int i = 1; i <= colCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }
        return model;
    }

    // =========================================================================
    // 1. PURE JAVA LOGIN WINDOW (JFrame)
    // =========================================================================

    public static class LoginWindow extends JFrame {
        private final JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Student", "Administrator / Warden"});
        private final JTextField emailField = new JTextField("abhinav@example.com", 22);
        private final JPasswordField passwordField = new JPasswordField("student@123", 22);
        private final JLabel statusLabel = new JLabel(" ");

        public LoginWindow() {
            setTitle("SecureU — Pure Java Desktop Portal");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(540, 520);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(COLOR_BG);

            JPanel root = new JPanel(new BorderLayout(0, 16));
            root.setBackground(COLOR_BG);
            root.setBorder(new EmptyBorder(28, 36, 28, 36));

            // Header Banner
            JPanel header = new JPanel(new GridLayout(2, 1, 4, 4));
            header.setBackground(COLOR_BG);

            JLabel title = new JLabel("SecureU Portal");
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(COLOR_PRIMARY);

            JLabel subtitle = new JLabel("Pure Java Desktop Client (Swing + MySQL JDBC)");
            subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subtitle.setForeground(COLOR_TEXT_MUTED);

            header.add(title);
            header.add(subtitle);
            root.add(header, BorderLayout.NORTH);

            // Login Card Form
            JPanel card = cardPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(6, 6, 6, 6);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.weightx = 1.0;

            // Role
            g.gridx = 0; g.gridy = 0;
            JLabel lblRole = new JLabel("Login Role");
            lblRole.setFont(new Font("Segoe UI", Font.BOLD, 12));
            card.add(lblRole, g);

            g.gridy = 1;
            roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            card.add(roleCombo, g);

            // Email
            g.gridy = 2;
            JLabel lblEmail = new JLabel("Institutional Email");
            lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
            card.add(lblEmail, g);

            g.gridy = 3;
            emailField.setPreferredSize(new Dimension(100, 34));
            card.add(emailField, g);

            // Password
            g.gridy = 4;
            JLabel lblPass = new JLabel("Password");
            lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
            card.add(lblPass, g);

            g.gridy = 5;
            passwordField.setPreferredSize(new Dimension(100, 34));
            card.add(passwordField, g);

            // Submit Button
            g.gridy = 6;
            g.insets = new Insets(12, 6, 4, 6);
            JButton btnLogin = primaryButton("Sign In to Portal");
            card.add(btnLogin, g);

            // Status Notice
            g.gridy = 7;
            g.insets = new Insets(2, 6, 6, 6);
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusLabel.setHorizontalAlignment(JLabel.CENTER);
            card.add(statusLabel, g);

            root.add(card, BorderLayout.CENTER);

            // Quick-fill Demo Buttons at Bottom
            JPanel footer = new JPanel(new GridLayout(2, 1, 6, 6));
            footer.setBackground(COLOR_BG);

            JLabel quickLabel = new JLabel("Quick Fill for Teacher / Viva Evaluation:");
            quickLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            quickLabel.setForeground(COLOR_TEXT_MUTED);

            JPanel quickRow = new JPanel(new GridLayout(1, 2, 8, 0));
            quickRow.setBackground(COLOR_BG);

            JButton btnDemoStudent = secondaryButton("Student (Abhinav)");
            JButton btnDemoAdmin = secondaryButton("Admin (Dr. Raman)");

            btnDemoStudent.addActionListener(e -> {
                roleCombo.setSelectedIndex(0);
                emailField.setText("abhinav@example.com");
                passwordField.setText("student@123");
            });

            btnDemoAdmin.addActionListener(e -> {
                roleCombo.setSelectedIndex(1);
                emailField.setText("warden@example.com");
                passwordField.setText("admin@123");
            });

            quickRow.add(btnDemoStudent);
            quickRow.add(btnDemoAdmin);
            footer.add(quickLabel);
            footer.add(quickRow);

            root.add(footer, BorderLayout.SOUTH);
            add(root);

            getRootPane().setDefaultButton(btnLogin);
            btnLogin.addActionListener(e -> performLogin());

            roleCombo.addActionListener(e -> {
                if (roleCombo.getSelectedIndex() == 1) {
                    if ("abhinav@example.com".equals(emailField.getText())) {
                        emailField.setText("warden@example.com");
                        passwordField.setText("admin@123");
                    }
                } else {
                    if ("warden@example.com".equals(emailField.getText())) {
                        emailField.setText("abhinav@example.com");
                        passwordField.setText("student@123");
                    }
                }
            });
        }

        private void performLogin() {
            String email = emailField.getText().trim();
            String pass = new String(passwordField.getPassword());
            boolean isAdmin = roleCombo.getSelectedIndex() == 1;

            if (email.isEmpty() || pass.isEmpty()) {
                statusLabel.setText("Please enter both email and password.");
                statusLabel.setForeground(Color.RED);
                return;
            }

            statusLabel.setText("Authenticating with MySQL via JDBC...");
            statusLabel.setForeground(COLOR_PRIMARY);

            String sql = isAdmin
                    ? "SELECT admin_id, name, role FROM admin WHERE email=? AND password=?"
                    : "SELECT student_id, name, department FROM students WHERE email=? AND password=?";

            try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql)) {
                s.setString(1, email);
                s.setString(2, pass);
                try (ResultSet r = s.executeQuery()) {
                    if (r.next()) {
                        int id = r.getInt(1);
                        String name = r.getString(2);
                        String detail = r.getString(3);

                        log("JAVA SWING LOGIN", "SUCCESS: " + (isAdmin ? "Admin " : "Student ") + name + " logged into Pure Java GUI.");

                        if (isAdmin) {
                            new AdminDashboard(id, name, detail).setVisible(true);
                        } else {
                            new StudentDashboard(id, name, detail).setVisible(true);
                        }
                        dispose();
                    } else {
                        statusLabel.setText("Invalid credentials. Please verify email and password.");
                        statusLabel.setForeground(Color.RED);
                    }
                }
            } catch (SQLException ex) {
                statusLabel.setText("MySQL Database Error: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
                log("JDBC LOGIN ERROR", ex.getMessage());
            }
        }
    }

    // =========================================================================
    // 2. PURE JAVA STUDENT DASHBOARD (JFrame)
    // =========================================================================

    public static class StudentDashboard extends JFrame {
        private final int studentId;
        private final String studentName;
        private final String department;

        private final JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
                "Academic", "Hostel", "Food", "Physical Harassment", "Verbal Harassment", "Other"
        });
        private final JTextArea descArea = new JTextArea(5, 30);
        private final JTextField evidenceField = new JTextField(25);

        private final JTextField trackIdField = new JTextField(8);
        private final JLabel trackResultLabel = new JLabel("Enter a Complaint ID and click 'Search Grievance'.");

        private final JTable historyTable = new JTable();

        public StudentDashboard(int id, String name, String dept) {
            this.studentId = id;
            this.studentName = name;
            this.department = dept;

            setTitle("SecureU — Student Grievance Portal");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(980, 680);
            setLocationRelativeTo(null);
            getContentPane().setBackground(COLOR_BG);

            JPanel root = new JPanel(new BorderLayout(0, 16));
            root.setBackground(COLOR_BG);
            root.setBorder(new EmptyBorder(20, 24, 20, 24));

            // Top Header Bar
            JPanel topBar = new JPanel(new BorderLayout());
            topBar.setBackground(COLOR_BG);

            JPanel userDetails = new JPanel(new GridLayout(2, 1, 2, 2));
            userDetails.setBackground(COLOR_BG);

            JLabel welcome = new JLabel("Welcome, " + studentName);
            welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
            welcome.setForeground(COLOR_PRIMARY);

            JLabel deptLabel = new JLabel("Department: " + department + " | Student ID #" + studentId + " | Pure Java Swing GUI");
            deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            deptLabel.setForeground(COLOR_TEXT_MUTED);

            userDetails.add(welcome);
            userDetails.add(deptLabel);
            topBar.add(userDetails, BorderLayout.WEST);

            JButton btnLogout = secondaryButton("Logout");
            btnLogout.addActionListener(e -> {
                new LoginWindow().setVisible(true);
                dispose();
            });
            topBar.add(btnLogout, BorderLayout.EAST);
            root.add(topBar, BorderLayout.NORTH);

            // Tabbed Pane for Student Features
            JTabbedPane tabs = new JTabbedPane();
            tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

            tabs.addTab("  File New Grievance  ", createFileTab());
            tabs.addTab("  Track Status  ", createTrackTab());
            tabs.addTab("  My Grievance History  ", createHistoryTab());

            root.add(tabs, BorderLayout.CENTER);
            add(root);

            loadHistory();
        }

        private JPanel createFileTab() {
            JPanel card = cardPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(8, 8, 8, 8);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.weightx = 1.0;

            g.gridx = 0; g.gridy = 0;
            JLabel lblCat = new JLabel("Grievance Category");
            lblCat.setFont(new Font("Segoe UI", Font.BOLD, 13));
            card.add(lblCat, g);

            g.gridy = 1;
            categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            card.add(categoryCombo, g);

            g.gridy = 2;
            JLabel lblDesc = new JLabel("Detailed Description of the Issue");
            lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
            card.add(lblDesc, g);

            g.gridy = 3;
            descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            JScrollPane scrollDesc = new JScrollPane(descArea);
            scrollDesc.setPreferredSize(new Dimension(100, 110));
            card.add(scrollDesc, g);

            g.gridy = 4;
            JLabel lblEv = new JLabel("Evidence Filename or Reference (Optional)");
            lblEv.setFont(new Font("Segoe UI", Font.BOLD, 13));
            card.add(lblEv, g);

            g.gridy = 5;
            evidenceField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            evidenceField.setPreferredSize(new Dimension(100, 32));
            card.add(evidenceField, g);

            g.gridy = 6;
            g.insets = new Insets(16, 8, 8, 8);
            JButton btnSubmit = primaryButton("Submit Grievance to Administration");
            card.add(btnSubmit, g);

            btnSubmit.addActionListener(e -> submitComplaint());

            JPanel wrap = new JPanel(new BorderLayout());
            wrap.setBackground(COLOR_BG);
            wrap.setBorder(new EmptyBorder(12, 12, 12, 12));
            wrap.add(card, BorderLayout.NORTH);
            return wrap;
        }

        private JPanel createTrackTab() {
            JPanel card = cardPanel(new BorderLayout(12, 16));

            JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
            searchRow.setBackground(COLOR_CARD_BG);

            JLabel lblId = new JLabel("Enter Complaint ID:");
            lblId.setFont(new Font("Segoe UI", Font.BOLD, 13));
            searchRow.add(lblId);

            trackIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            trackIdField.setPreferredSize(new Dimension(120, 32));
            searchRow.add(trackIdField);

            JButton btnSearch = primaryButton("Search Grievance");
            searchRow.add(btnSearch);

            card.add(searchRow, BorderLayout.NORTH);

            JPanel resultCard = new JPanel(new BorderLayout());
            resultCard.setBackground(new Color(248, 250, 252));
            resultCard.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(20, 20, 20, 20)));

            trackResultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            trackResultLabel.setForeground(COLOR_PRIMARY);
            resultCard.add(trackResultLabel, BorderLayout.CENTER);

            card.add(resultCard, BorderLayout.CENTER);

            btnSearch.addActionListener(e -> trackComplaint());

            JPanel wrap = new JPanel(new BorderLayout());
            wrap.setBackground(COLOR_BG);
            wrap.setBorder(new EmptyBorder(12, 12, 12, 12));
            wrap.add(card, BorderLayout.NORTH);
            return wrap;
        }

        private JPanel createHistoryTab() {
            JPanel card = cardPanel(new BorderLayout(0, 12));

            JPanel top = new JPanel(new BorderLayout());
            top.setBackground(COLOR_CARD_BG);

            JLabel title = new JLabel("All Grievances Submitted By You");
            title.setFont(new Font("Segoe UI", Font.BOLD, 14));
            top.add(title, BorderLayout.WEST);

            JButton btnRefresh = secondaryButton("Refresh Table");
            btnRefresh.addActionListener(e -> loadHistory());
            top.add(btnRefresh, BorderLayout.EAST);

            card.add(top, BorderLayout.NORTH);

            styleTable(historyTable);
            card.add(new JScrollPane(historyTable), BorderLayout.CENTER);

            JPanel wrap = new JPanel(new BorderLayout());
            wrap.setBackground(COLOR_BG);
            wrap.setBorder(new EmptyBorder(12, 12, 12, 12));
            wrap.add(card, BorderLayout.CENTER);
            return wrap;
        }

        private void submitComplaint() {
            String desc = descArea.getText().trim();
            String cat = (String) categoryCombo.getSelectedItem();
            String ev = evidenceField.getText().trim();

            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please describe the grievance before submitting.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO complaints (student_id, category, description, evidence, status, date_filed) VALUES (?, ?, ?, ?, 'Pending', CURRENT_DATE())";
            try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                s.setInt(1, studentId);
                s.setString(2, cat);
                s.setString(3, desc);
                s.setString(4, ev.isEmpty() ? null : ev);
                s.executeUpdate();

                int newId = -1;
                try (ResultSet k = s.getGeneratedKeys()) {
                    if (k.next()) newId = k.getInt(1);
                }

                log("JAVA SWING INSERT", "Grievance #" + newId + " filed via Java Swing GUI for Student ID " + studentId);

                JOptionPane.showMessageDialog(this,
                        "Your grievance has been registered successfully in MySQL!\nAssigned Complaint ID: #" + newId,
                        "Submission Confirmed", JOptionPane.INFORMATION_MESSAGE);

                descArea.setText("");
                evidenceField.setText("");
                trackIdField.setText(String.valueOf(newId));
                loadHistory();

            } catch (SQLException ex) {
                log("JDBC ERROR", ex.getMessage());
                JOptionPane.showMessageDialog(this, "Database submission error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void trackComplaint() {
            String text = trackIdField.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a numeric Complaint ID.", "Search", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int cid = Integer.parseInt(text);
                String sql = "SELECT complaint_id, category, status, date_filed, remarks, description FROM complaints WHERE complaint_id=? AND student_id=?";
                try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql)) {
                    s.setInt(1, cid);
                    s.setInt(2, studentId);
                    try (ResultSet r = s.executeQuery()) {
                        if (r.next()) {
                            String remarks = r.getString("remarks");
                            if (remarks == null || remarks.isEmpty()) remarks = "Under review by assigned grievance committee.";

                            String html = String.format("<html><body style='width: 450px; font-family: Segoe UI;'>" +
                                            "<h3 style='margin: 0; color: #0f172a;'>Grievance #%d — %s</h3>" +
                                            "<p style='color: #64748b; margin: 4px 0 12px 0;'>Filed on %s | Status: <b>%s</b></p>" +
                                            "<p><b>Description:</b> %s</p>" +
                                            "<p style='background: #e2e8f0; padding: 10px; border-radius: 4px;'><b>Administrative Remarks:</b><br>%s</p>" +
                                            "</body></html>",
                                    r.getInt("complaint_id"),
                                    r.getString("category"),
                                    r.getDate("date_filed"),
                                    r.getString("status"),
                                    r.getString("description"),
                                    remarks);

                            trackResultLabel.setText(html);
                        } else {
                            trackResultLabel.setText("<html><span style='color: #dc2626;'>Complaint #" + cid + " not found under your student account.</span></html>");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Complaint ID must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database query error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void loadHistory() {
            String sql = "SELECT complaint_id AS ID, category AS Category, status AS Status, date_filed AS Date, description AS Description, remarks AS Remarks " +
                         "FROM complaints WHERE student_id=? ORDER BY complaint_id DESC";
            try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, studentId);
                try (ResultSet r = s.executeQuery()) {
                    historyTable.setModel(buildTableModel(r));
                    styleTable(historyTable);
                }
            } catch (SQLException ex) {
                log("JDBC LOAD ERROR", ex.getMessage());
            }
        }
    }

    // =========================================================================
    // 3. PURE JAVA ADMINISTRATOR DASHBOARD (JFrame)
    // =========================================================================

    public static class AdminDashboard extends JFrame {
        private final int adminId;
        private final String adminName;
        private final String adminRole;

        private final JTable complaintsTable = new JTable();
        private final JTextField filterField = new JTextField(14);
        private final JComboBox<String> statusFilterCombo = new JComboBox<>(new String[]{"All Statuses", "Pending", "In Progress", "Resolved"});

        // Bottom Update Controls
        private final JTextField selectedIdField = new JTextField(6);
        private final JComboBox<String> newStatusCombo = new JComboBox<>(new String[]{"Pending", "In Progress", "Resolved"});
        private final JTextField remarksField = new JTextField(28);

        // Stats Labels
        private final JLabel lblStatTotal = new JLabel("0");
        private final JLabel lblStatPending = new JLabel("0");
        private final JLabel lblStatProgress = new JLabel("0");
        private final JLabel lblStatResolved = new JLabel("0");

        public AdminDashboard(int id, String name, String role) {
            this.adminId = id;
            this.adminName = name;
            this.adminRole = role;

            setTitle("SecureU — Administrator Grievance Redressal Portal");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(1140, 720);
            setLocationRelativeTo(null);
            getContentPane().setBackground(COLOR_BG);

            JPanel root = new JPanel(new BorderLayout(0, 14));
            root.setBackground(COLOR_BG);
            root.setBorder(new EmptyBorder(18, 22, 18, 22));

            // Header Row
            JPanel topBar = new JPanel(new BorderLayout());
            topBar.setBackground(COLOR_BG);

            JPanel adminDetails = new JPanel(new GridLayout(2, 1, 2, 2));
            adminDetails.setBackground(COLOR_BG);

            JLabel welcome = new JLabel("SecureU Administration Portal");
            welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
            welcome.setForeground(COLOR_PRIMARY);

            JLabel adminSubtitle = new JLabel("Logged in as " + adminName + " (" + adminRole + ") | Pure Java Swing GUI");
            adminSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            adminSubtitle.setForeground(COLOR_TEXT_MUTED);

            adminDetails.add(welcome);
            adminDetails.add(adminSubtitle);
            topBar.add(adminDetails, BorderLayout.WEST);

            JButton btnLogout = secondaryButton("Logout");
            btnLogout.addActionListener(e -> {
                new LoginWindow().setVisible(true);
                dispose();
            });
            topBar.add(btnLogout, BorderLayout.EAST);
            root.add(topBar, BorderLayout.NORTH);

            // Center Panel: KPI Metrics + Table
            JPanel center = new JPanel(new BorderLayout(0, 12));
            center.setBackground(COLOR_BG);

            // 4 KPI Cards
            JPanel kpiRow = new JPanel(new GridLayout(1, 4, 12, 0));
            kpiRow.setBackground(COLOR_BG);
            kpiRow.add(createKpiCard("TOTAL GRIEVANCES", lblStatTotal, COLOR_PRIMARY));
            kpiRow.add(createKpiCard("PENDING REVIEW", lblStatPending, new Color(217, 119, 6)));
            kpiRow.add(createKpiCard("IN PROGRESS", lblStatProgress, new Color(37, 99, 235)));
            kpiRow.add(createKpiCard("RESOLVED CASES", lblStatResolved, new Color(22, 163, 74)));
            center.add(kpiRow, BorderLayout.NORTH);

            // Table Card
            JPanel tableCard = cardPanel(new BorderLayout(0, 10));

            // Filter Bar
            JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
            filterBar.setBackground(COLOR_CARD_BG);

            filterBar.add(new JLabel("Filter by Status:"));
            statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            filterBar.add(statusFilterCombo);

            filterBar.add(Box.createHorizontalStrut(10));
            filterBar.add(new JLabel("Search Keyword:"));
            filterField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            filterBar.add(filterField);

            JButton btnFilter = secondaryButton("Apply Filter");
            JButton btnReset = secondaryButton("Reset");
            filterBar.add(btnFilter);
            filterBar.add(btnReset);

            tableCard.add(filterBar, BorderLayout.NORTH);

            styleTable(complaintsTable);
            complaintsTable.setAutoCreateRowSorter(true);
            tableCard.add(new JScrollPane(complaintsTable), BorderLayout.CENTER);

            center.add(tableCard, BorderLayout.CENTER);
            root.add(center, BorderLayout.CENTER);

            // Bottom Update Form Card
            JPanel bottomCard = cardPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));

            JLabel lblUpdateTitle = new JLabel("Update Complaint Status:");
            lblUpdateTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
            bottomCard.add(lblUpdateTitle);

            bottomCard.add(new JLabel("ID:"));
            selectedIdField.setFont(new Font("Segoe UI", Font.BOLD, 13));
            selectedIdField.setEditable(false);
            bottomCard.add(selectedIdField);

            bottomCard.add(new JLabel("Status:"));
            newStatusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            bottomCard.add(newStatusCombo);

            bottomCard.add(new JLabel("Administrative Remarks:"));
            remarksField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            bottomCard.add(remarksField);

            JButton btnSave = primaryButton("Save Update");
            bottomCard.add(btnSave);

            root.add(bottomCard, BorderLayout.SOUTH);
            add(root);

            // Listeners
            btnFilter.addActionListener(e -> loadComplaints());
            btnReset.addActionListener(e -> {
                statusFilterCombo.setSelectedIndex(0);
                filterField.setText("");
                loadComplaints();
            });

            btnSave.addActionListener(e -> saveUpdate());

            // Click table row to populate update fields
            complaintsTable.getSelectionModel().addListSelectionListener(e -> {
                int selectedRow = complaintsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = complaintsTable.convertRowIndexToModel(selectedRow);
                    Object idVal = complaintsTable.getModel().getValueAt(modelRow, 0);
                    Object statusVal = complaintsTable.getModel().getValueAt(modelRow, 6);
                    Object remarksVal = complaintsTable.getModel().getValueAt(modelRow, 8);

                    if (idVal != null) selectedIdField.setText(idVal.toString());
                    if (statusVal != null) newStatusCombo.setSelectedItem(statusVal.toString());
                    if (remarksVal != null) remarksField.setText(remarksVal.toString());
                    else remarksField.setText("");
                }
            });

            loadComplaints();
        }

        private JPanel createKpiCard(String label, JLabel valueLabel, Color accent) {
            JPanel card = cardPanel(new GridLayout(2, 1, 2, 2));
            card.setBorder(new CompoundBorder(
                    new MatteBorder(0, 4, 0, 0, accent),
                    new CompoundBorder(new LineBorder(COLOR_BORDER, 1, true), new EmptyBorder(10, 14, 10, 14))
            ));

            JLabel title = new JLabel(label);
            title.setFont(new Font("Segoe UI", Font.BOLD, 10));
            title.setForeground(COLOR_TEXT_MUTED);

            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            valueLabel.setForeground(accent);

            card.add(title);
            card.add(valueLabel);
            return card;
        }

        private void loadComplaints() {
            String selectedStatus = (String) statusFilterCombo.getSelectedItem();
            String keyword = filterField.getText().trim();

            StringBuilder sql = new StringBuilder(
                    "SELECT c.complaint_id AS ID, c.student_id AS `Student ID`, COALESCE(s.name, 'Unknown') AS `Student Name`, " +
                    "COALESCE(s.department, 'N/A') AS Dept, c.category AS Category, c.description AS Description, " +
                    "c.status AS Status, c.date_filed AS Date, c.remarks AS Remarks " +
                    "FROM complaints c LEFT JOIN students s ON c.student_id=s.student_id WHERE 1=1 "
            );

            if (selectedStatus != null && !"All Statuses".equals(selectedStatus)) {
                sql.append("AND c.status = '").append(selectedStatus.replace("'", "''")).append("' ");
            }
            if (!keyword.isEmpty()) {
                String safe = keyword.replace("'", "''");
                sql.append("AND (c.description LIKE '%").append(safe).append("%' OR s.name LIKE '%").append(safe).append("%') ");
            }
            sql.append("ORDER BY c.complaint_id DESC");

            try (Connection c = connect(); Statement st = c.createStatement(); ResultSet r = st.executeQuery(sql.toString())) {
                complaintsTable.setModel(buildTableModel(r));
                styleTable(complaintsTable);
                refreshKpiCounts(c);
            } catch (SQLException ex) {
                log("ADMIN LOAD ERROR", ex.getMessage());
            }
        }

        private void refreshKpiCounts(Connection c) {
            try (Statement st = c.createStatement()) {
                try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM complaints")) {
                    if (r.next()) lblStatTotal.setText(String.valueOf(r.getInt(1)));
                }
                try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM complaints WHERE status='Pending'")) {
                    if (r.next()) lblStatPending.setText(String.valueOf(r.getInt(1)));
                }
                try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM complaints WHERE status='In Progress'")) {
                    if (r.next()) lblStatProgress.setText(String.valueOf(r.getInt(1)));
                }
                try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM complaints WHERE status='Resolved'")) {
                    if (r.next()) lblStatResolved.setText(String.valueOf(r.getInt(1)));
                }
            } catch (SQLException ignored) {}
        }

        private void saveUpdate() {
            String idStr = selectedIdField.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a complaint row from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int cid = Integer.parseInt(idStr);
                String status = (String) newStatusCombo.getSelectedItem();
                String remarks = remarksField.getText().trim();

                String sql = "UPDATE complaints SET status=?, remarks=? WHERE complaint_id=?";
                try (Connection c = connect(); PreparedStatement s = c.prepareStatement(sql)) {
                    s.setString(1, status);
                    s.setString(2, remarks);
                    s.setInt(3, cid);
                    int updated = s.executeUpdate();

                    if (updated > 0) {
                        log("JAVA SWING UPDATE", "Complaint #" + cid + " updated to '" + status + "' with remarks: '" + remarks + "'");
                        JOptionPane.showMessageDialog(this, "Complaint #" + cid + " status updated successfully in MySQL!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadComplaints();
                    } else {
                        JOptionPane.showMessageDialog(this, "Complaint #" + cid + " not found in database.", "Notice", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Complaint ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database update error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        Scanner scanner = new Scanner(System.in);
        while (true) {
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
                        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
                        System.out.println("[OK] Opened Pure Java Swing Desktop Window.");
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
        System.out.println("  [7] Open Pure Java Swing Desktop Window (JFrame)");
        System.out.println("  [8] Show This Menu Again (or type 'menu')");
        System.out.println("  [0] Exit Application (or type 'exit')");
        System.out.println("========================================================================================");
    }

    // =========================================================================
    // BACKGROUND REST API HANDLERS (for hybrid or web requests)
    // =========================================================================

    static class StatusHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            boolean dbOk = false;
            try (Connection c = connect()) { dbOk = true; } catch (Exception ignored) {}
            String json = "{\"status\":\"online\",\"engine\":\"Java " + System.getProperty("java.version") + " Pure Swing\",\"db_connected\":" + dbOk + "}";
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
                    log("JDBC SELECT", "Fetched " + rowCount + " complaints from MySQL.");
                    sendJsonResponse(ex, 200, sb.toString());
                } catch (SQLException e) {
                    sendJsonResponse(ex, 500, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
                }

            } else if ("POST".equalsIgnoreCase(method)) {
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
                    log("JDBC INSERT", "New complaint filed! ID #" + newId + " | Student ID: " + studentId);
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
                    log("JDBC UPDATE", "Complaint #" + cid + " status updated to '" + status + "'");
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
    // JSON & STRING HELPERS
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
}
