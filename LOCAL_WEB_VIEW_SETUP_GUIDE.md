# SecureU — Local Web View Setup & Execution Guide

This guide explains how to run the **SecureU Web Portal locally** in your browser (`http://localhost:3000`).

---

## ⚡ Method 1: 1-Click Web Launcher (Recommended)

In your project folder (`c:\Users\Hariprasad\Videos\JAVA PROJECT`):

### 👉 Double-click **`start-browser-app.bat`**

**What happens automatically:**
1. Checks and frees port `3000` if previously occupied.
2. Starts the lightweight local server (`local-server.js`).
3. Automatically opens your default web browser to:
   ```text
   http://localhost:3000
   ```

---

## ☕ Method 2: Via the Java Server (Pure Java + JDBC)

When you run the Java application, Java's built-in HTTP server (`com.sun.net.httpserver.HttpServer`) starts on port `3000` simultaneously and connects to MySQL:

1. Launch Java via **`Launch-CollegeComplaint.bat`** or **`Launch-Terminal-Console.bat`** (or `.\run.ps1`).
2. Open any web browser (Chrome, Edge, Brave, Firefox).
3. Navigate to:
   ```text
   http://localhost:3000
   ```
4. In this mode, every action in the web browser (logging in, submitting complaints, updating status) connects directly to MySQL and **logs live into your Java terminal window**!

---

## 💻 Method 3: From PowerShell or Command Prompt

If you prefer running from the command line:

```powershell
cd "c:\Users\Hariprasad\Videos\JAVA PROJECT"
node local-server.js
```
Then open `http://localhost:3000` in your browser.

*(If you have Python installed, you can also run: `python -m http.server 3000 --directory web`)*

---

## 📱 How to Test Mobile & Tablet View on Local

1. Open `http://localhost:3000` in Google Chrome or Microsoft Edge.
2. Press **`F12`** (or right-click anywhere and select **Inspect**).
3. Click the **Device Toggle Icon** in the top-left of DevTools (or press **`Ctrl + Shift + M`**).
4. In the top dropdown, select:
   * **iPhone 14 Pro / 12 Pro** (390 × 844) &rarr; notice the single-column vertical login, compact navbar, and bottom-sheet modal.
   * **iPad Air / Mini** (820 × 1180) &rarr; notice the 2-column KPI cards and charts.
   * **Responsive** &rarr; drag the screen slider to any width!

---

## 🔑 Login Credentials for Local Web View

| Role | Email | Password | Demo Pill in UI |
| :--- | :--- | :--- | :--- |
| **Student** | `abhinav@example.com` | `student@123` | Click **"Abhinav U (Student)"** |
| **Admin** | `warden@example.com` | `admin@123` | Click **"Dr. K. Raman (Admin)"** |
