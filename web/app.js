/**
 * CollegeComplaint — In-Browser Client Engine
 * Fully connected to Java Backend (com.sun.net.httpserver) & MySQL JDBC API
 */

function daysAgo(n) {
  const d = new Date();
  d.setDate(d.getDate() - n);
  return d.toISOString().split("T")[0];
}

// Initial Fallback Seed Data
const INITIAL_DATA = {
  students: [
    { student_id: 1, name: "Abhinav U", department: "CSE", email: "abhinav@example.com", password: "student@123", phone: "9876543210" },
    { student_id: 2, name: "Sneha S", department: "ECE", email: "sneha@example.com", password: "student@123", phone: "8765432109" },
    { student_id: 3, name: "Rahul V", department: "MECH", email: "rahul@example.com", password: "student@123", phone: "9123456780" },
    { student_id: 4, name: "Priya S", department: "ECE", email: "priya.s@example.com", password: "student@123", phone: "9988776655" },
    { student_id: 5, name: "Vignesh R", department: "CIVIL", email: "vignesh@example.com", password: "student@123", phone: "9090909090" },
    { student_id: 6, name: "Anjali K", department: "CSE", email: "anjali@example.com", password: "student@123", phone: "9811122233" },
    { student_id: 7, name: "Mohammed F", department: "EEE", email: "farhan@example.com", password: "student@123", phone: "9822233344" },
    { student_id: 8, name: "Divya M", department: "IT", email: "divya@example.com", password: "student@123", phone: "9833344455" },
    { student_id: 9, name: "Karthik N", department: "MECH", email: "karthik@example.com", password: "student@123", phone: "9844455566" },
    { student_id: 10, name: "Fathima Z", department: "CSE", email: "fathima@example.com", password: "student@123", phone: "9855566677" },
    { student_id: 11, name: "Arjun P", department: "EEE", email: "arjun@example.com", password: "student@123", phone: "9866677788" },
    { student_id: 12, name: "Meera J", department: "IT", email: "meera@example.com", password: "student@123", phone: "9877788899" }
  ],
  admin: [
    { admin_id: 1, name: "Priya Nair", role: "Warden", email: "warden@example.com", password: "admin@123" },
    { admin_id: 2, name: "Suresh Babu", role: "HOD", email: "hod@example.com", password: "admin@123" }
  ],
  complaints: [
    { complaint_id: 101, student_id: 1, student_name: "Abhinav U", department: "CSE", category: "Hostel", description: "Hot water geyser on 2nd floor of Block B not working for 3 days.", evidence: "geyser_photo.jpg", status: "In Progress", date_filed: daysAgo(4), remarks: "Electrician assigned. Heating coil being replaced." },
    { complaint_id: 102, student_id: 2, student_name: "Sneha S", department: "ECE", category: "Academic", description: "DSP lab workstation #14 crashes repeatedly during practicals.", evidence: "lab_bsod.png", status: "Pending", date_filed: daysAgo(2), remarks: "" },
    { complaint_id: 103, student_id: 3, student_name: "Rahul V", department: "MECH", category: "Academic", description: "Internal assessment marks for Thermodynamics not updated on portal.", evidence: null, status: "Pending", date_filed: daysAgo(1), remarks: "" },
    { complaint_id: 104, student_id: 4, student_name: "Priya S", department: "ECE", category: "Food", description: "Mess dinner served stale rice on Wednesday night.", evidence: "mess_rice.jpg", status: "Resolved", date_filed: daysAgo(9), remarks: "Food committee inspected. Vendor warned and menu revised." },
    { complaint_id: 105, student_id: 5, student_name: "Vignesh R", department: "CIVIL", category: "Other", description: "Classroom 204 air conditioner not working.", evidence: "ac_room204.jpg", status: "Resolved", date_filed: daysAgo(12), remarks: "AC compressor repaired by maintenance." },
    { complaint_id: 106, student_id: 6, student_name: "Anjali K", department: "CSE", category: "Verbal Harassment", description: "Senior students using abusive language in hostel corridor.", evidence: null, status: "In Progress", date_filed: daysAgo(6), remarks: "Anti-ragging committee investigating." },
    { complaint_id: 107, student_id: 7, student_name: "Mohammed F", department: "EEE", category: "Academic", description: "Revaluation request for Circuits paper not processed.", evidence: "reval_receipt.pdf", status: "Pending", date_filed: daysAgo(3), remarks: "" },
    { complaint_id: 108, student_id: 8, student_name: "Divya M", department: "IT", category: "Hostel", description: "Wi-Fi in Girls Hostel Block C down for a week.", evidence: null, status: "Resolved", date_filed: daysAgo(15), remarks: "Router replaced; bandwidth upgraded." },
    { complaint_id: 109, student_id: 9, student_name: "Karthik N", department: "MECH", category: "Food", description: "Drinking water dispenser in canteen is unhygienic.", evidence: "dispenser.jpg", status: "Resolved", date_filed: daysAgo(20), remarks: "Filter replaced and weekly cleaning scheduled." },
    { complaint_id: 110, student_id: 10, student_name: "Fathima Z", department: "CSE", category: "Physical Harassment", description: "Ragging incident reported near hostel mess.", evidence: null, status: "In Progress", date_filed: daysAgo(5), remarks: "Statements recorded. Disciplinary committee meeting scheduled." },
    { complaint_id: 111, student_id: 11, student_name: "Arjun P", department: "EEE", category: "Academic", description: "Library not open on weekends during exam season.", evidence: null, status: "Resolved", date_filed: daysAgo(25), remarks: "Weekend library hours extended till 8 PM." },
    { complaint_id: 112, student_id: 12, student_name: "Meera J", department: "IT", category: "Other", description: "Street lights near parking area not working.", evidence: "parking_dark.jpg", status: "Pending", date_filed: daysAgo(7), remarks: "" },
    { complaint_id: 113, student_id: 1, student_name: "Abhinav U", department: "CSE", category: "Food", description: "Insects found in breakfast sambar.", evidence: "sambar.jpg", status: "Resolved", date_filed: daysAgo(34), remarks: "Kitchen deep cleaned; pest control done." },
    { complaint_id: 114, student_id: 2, student_name: "Sneha S", department: "ECE", category: "Hostel", description: "Bathroom taps leaking in Block A, room 112.", evidence: null, status: "Resolved", date_filed: daysAgo(38), remarks: "Plumber fixed leakage." },
    { complaint_id: 115, student_id: 3, student_name: "Rahul V", department: "MECH", category: "Academic", description: "Faculty frequently absent for Machine Design lectures.", evidence: null, status: "Resolved", date_filed: daysAgo(41), remarks: "HOD discussed with faculty; extra classes arranged." },
    { complaint_id: 116, student_id: 4, student_name: "Priya S", department: "ECE", category: "Verbal Harassment", description: "Lab assistant shouts at students during practicals.", evidence: null, status: "Resolved", date_filed: daysAgo(45), remarks: "Counselling given to staff member." },
    { complaint_id: 117, student_id: 5, student_name: "Vignesh R", department: "CIVIL", category: "Hostel", description: "Room fan making loud noise and slow.", evidence: null, status: "Resolved", date_filed: daysAgo(48), remarks: "Fan replaced." },
    { complaint_id: 118, student_id: 6, student_name: "Anjali K", department: "CSE", category: "Academic", description: "Projector in Seminar Hall not working.", evidence: "projector.jpg", status: "In Progress", date_filed: daysAgo(50), remarks: "New projector purchase approved." },
    { complaint_id: 119, student_id: 7, student_name: "Mohammed F", department: "EEE", category: "Other", description: "Bus route 7 consistently late by 30 minutes.", evidence: null, status: "Pending", date_filed: daysAgo(52), remarks: "" },
    { complaint_id: 120, student_id: 8, student_name: "Divya M", department: "IT", category: "Food", description: "Canteen overcharging for snacks.", evidence: "bill.jpg", status: "Resolved", date_filed: daysAgo(55), remarks: "Price list displayed; vendor warned." },
    { complaint_id: 121, student_id: 9, student_name: "Karthik N", department: "MECH", category: "Hostel", description: "Mosquito problem in hostel rooms.", evidence: null, status: "Resolved", date_filed: daysAgo(63), remarks: "Fogging done and window nets installed." },
    { complaint_id: 122, student_id: 10, student_name: "Fathima Z", department: "CSE", category: "Academic", description: "Exam timetable clash between two electives.", evidence: null, status: "Resolved", date_filed: daysAgo(66), remarks: "Exam rescheduled by controller." },
    { complaint_id: 123, student_id: 11, student_name: "Arjun P", department: "EEE", category: "Physical Harassment", description: "Student pushed during inter-department match.", evidence: null, status: "Resolved", date_filed: daysAgo(70), remarks: "Sports committee issued warning." },
    { complaint_id: 124, student_id: 12, student_name: "Meera J", department: "IT", category: "Food", description: "No vegetarian option on Sunday dinner.", evidence: null, status: "Resolved", date_filed: daysAgo(72), remarks: "Menu updated." },
    { complaint_id: 125, student_id: 1, student_name: "Abhinav U", department: "CSE", category: "Academic", description: "Lab manual not provided for Compiler Design lab.", evidence: null, status: "Resolved", date_filed: daysAgo(76), remarks: "Manuals distributed." },
    { complaint_id: 126, student_id: 2, student_name: "Sneha S", department: "ECE", category: "Hostel", description: "Hostel gate closes before library closing time.", evidence: null, status: "In Progress", date_filed: daysAgo(80), remarks: "Proposal sent to Dean." },
    { complaint_id: 127, student_id: 3, student_name: "Rahul V", department: "MECH", category: "Other", description: "Drinking water cooler in Mech block not cooling.", evidence: null, status: "Resolved", date_filed: daysAgo(84), remarks: "Cooler serviced." },
    { complaint_id: 128, student_id: 4, student_name: "Priya S", department: "ECE", category: "Academic", description: "Attendance wrongly marked absent for 3 days.", evidence: "attendance.png", status: "Resolved", date_filed: daysAgo(88), remarks: "Attendance corrected." },
    { complaint_id: 129, student_id: 5, student_name: "Vignesh R", department: "CIVIL", category: "Food", description: "Mess timings too short for lab students.", evidence: null, status: "Pending", date_filed: daysAgo(92), remarks: "" },
    { complaint_id: 130, student_id: 6, student_name: "Anjali K", department: "CSE", category: "Hostel", description: "Laundry machine broken in Girls Hostel.", evidence: null, status: "Resolved", date_filed: daysAgo(96), remarks: "Machine repaired." },
    { complaint_id: 131, student_id: 7, student_name: "Mohammed F", department: "EEE", category: "Verbal Harassment", description: "Derogatory comments on department WhatsApp group.", evidence: "screenshot.png", status: "Resolved", date_filed: daysAgo(101), remarks: "Students counselled; group moderated." },
    { complaint_id: 132, student_id: 8, student_name: "Divya M", department: "IT", category: "Academic", description: "Internship NOC delayed by department office.", evidence: null, status: "Resolved", date_filed: daysAgo(105), remarks: "NOC issued." },
    { complaint_id: 133, student_id: 9, student_name: "Karthik N", department: "MECH", category: "Other", description: "Gym equipment damaged.", evidence: null, status: "In Progress", date_filed: daysAgo(110), remarks: "Quotation requested for replacement." },
    { complaint_id: 134, student_id: 10, student_name: "Fathima Z", department: "CSE", category: "Hostel", description: "Power cuts every night in Block D.", evidence: null, status: "Resolved", date_filed: daysAgo(115), remarks: "Wiring fixed; inverter installed." },
    { complaint_id: 135, student_id: 11, student_name: "Arjun P", department: "EEE", category: "Academic", description: "Question paper had out-of-syllabus questions.", evidence: null, status: "Resolved", date_filed: daysAgo(120), remarks: "Grace marks awarded." },
    { complaint_id: 136, student_id: 12, student_name: "Meera J", department: "IT", category: "Food", description: "Juice stall hygiene issues.", evidence: null, status: "Resolved", date_filed: daysAgo(126), remarks: "Stall licence reviewed." },
    { complaint_id: 137, student_id: 1, student_name: "Abhinav U", department: "CSE", category: "Hostel", description: "Water tank not cleaned for months.", evidence: null, status: "Resolved", date_filed: daysAgo(132), remarks: "Tank cleaned." },
    { complaint_id: 138, student_id: 2, student_name: "Sneha S", department: "ECE", category: "Academic", description: "Delay in publishing semester results.", evidence: null, status: "Resolved", date_filed: daysAgo(138), remarks: "Results published." },
    { complaint_id: 139, student_id: 3, student_name: "Rahul V", department: "MECH", category: "Other", description: "Parking space insufficient for two-wheelers.", evidence: null, status: "Pending", date_filed: daysAgo(142), remarks: "" },
    { complaint_id: 140, student_id: 4, student_name: "Priya S", department: "ECE", category: "Physical Harassment", description: "Fight between students outside canteen.", evidence: null, status: "Resolved", date_filed: daysAgo(148), remarks: "Both parties suspended for 3 days." },
    { complaint_id: 141, student_id: 5, student_name: "Vignesh R", department: "CIVIL", category: "Food", description: "Mess food too spicy every day.", evidence: null, status: "Resolved", date_filed: daysAgo(152), remarks: "Spice levels reduced." },
    { complaint_id: 142, student_id: 6, student_name: "Anjali K", department: "CSE", category: "Academic", description: "Lab computers lack required software (MATLAB).", evidence: null, status: "Resolved", date_filed: daysAgo(158), remarks: "Licences installed." },
    { complaint_id: 143, student_id: 7, student_name: "Mohammed F", department: "EEE", category: "Hostel", description: "Broken window in room 305.", evidence: null, status: "Resolved", date_filed: daysAgo(164), remarks: "Glass replaced." },
    { complaint_id: 144, student_id: 8, student_name: "Divya M", department: "IT", category: "Other", description: "Medical room closed during evening hours.", evidence: null, status: "In Progress", date_filed: daysAgo(170), remarks: "Nurse duty extension under review." },
    { complaint_id: 145, student_id: 9, student_name: "Karthik N", department: "MECH", category: "Academic", description: "Assignment deadlines clashing across subjects.", evidence: null, status: "Resolved", date_filed: daysAgo(175), remarks: "Faculty coordinated new schedule." }
  ]
};

let currentUser = null;
let allComplaints = [];
let isJavaBackend = true;

document.addEventListener("DOMContentLoaded", () => {
  checkBackendStatus();
  initLogin();
  initStudentDashboard();
  initAdminDashboard();
  initDbInspector();
  restoreSession();
});

// Check if Java HTTP Server is running
async function checkBackendStatus() {
  try {
    const res = await fetch("/api/status");
    if (res.ok) {
      const data = await res.json();
      console.log("Connected to Java Backend Server:", data);
      isJavaBackend = true;
    }
  } catch (e) {
    console.warn("Java backend not responding, running in local fallback mode.", e);
    isJavaBackend = false;
  }
}

// Toast Notifications
function showToast(message, type = "info") {
  const container = document.getElementById("toast-container");
  if (!container) return;

  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;
  let iconSvg = type === "success"
    ? `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#10b981" stroke-width="2.5"><polyline points="20 6 9 17 4 12"></polyline></svg>`
    : (type === "error"
      ? `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#ef4444" stroke-width="2.5"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>`
      : `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#6366f1" stroke-width="2.5"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg>`);

  toast.innerHTML = `${iconSvg}<span>${escapeHtml(message)}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = "0";
    toast.style.transform = "translateX(20px)";
    toast.style.transition = "all 0.3s ease";
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

function escapeHtml(str) {
  if (!str) return "";
  return String(str).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}

function saveSession() {
  if (currentUser) sessionStorage.setItem("JAVA_COMPLAINT_USER", JSON.stringify(currentUser));
  else sessionStorage.removeItem("JAVA_COMPLAINT_USER");
}

function restoreSession() {
  try {
    const saved = sessionStorage.getItem("JAVA_COMPLAINT_USER");
    if (saved) {
      currentUser = JSON.parse(saved);
      applyLoginState();
      return;
    }
  } catch (e) {}
  switchView("login-view");
}

function applyLoginState() {
  const userBadge = document.getElementById("nav-user-badge");
  const navLogoutBtn = document.getElementById("nav-logout-btn");

  if (!currentUser) {
    userBadge.style.display = "none";
    navLogoutBtn.style.display = "none";
    switchView("login-view");
    return;
  }

  userBadge.style.display = "flex";
  navLogoutBtn.style.display = "inline-flex";

  document.getElementById("nav-avatar").textContent = currentUser.data.name.charAt(0).toUpperCase();
  document.getElementById("nav-user-name").textContent = currentUser.data.name;

  if (currentUser.role === "student") {
    document.getElementById("nav-user-role").textContent = `Student (${currentUser.data.department})`;
    renderStudentDashboard();
    switchView("student-view");
  } else {
    document.getElementById("nav-user-role").textContent = `Administrator (${currentUser.data.role})`;
    renderAdminDashboard();
    switchView("admin-view");
  }
}

function switchView(viewId) {
  document.querySelectorAll(".view").forEach(v => v.classList.remove("active"));
  const target = document.getElementById(viewId);
  if (target) target.classList.add("active");
}

// ==========================================================================
// Authentication Logic (Pure Java Backend API)
// ==========================================================================
function initLogin() {
  const loginForm = document.getElementById("login-form");
  const roleStudentBtn = document.getElementById("role-student-btn");
  const roleAdminBtn = document.getElementById("role-admin-btn");
  const emailInput = document.getElementById("login-email");
  const passwordInput = document.getElementById("login-password");
  let activeRole = "student";

  roleStudentBtn.addEventListener("click", () => {
    activeRole = "student";
    roleStudentBtn.classList.add("active");
    roleAdminBtn.classList.remove("active");
  });

  roleAdminBtn.addEventListener("click", () => {
    activeRole = "admin";
    roleAdminBtn.classList.add("active");
    roleStudentBtn.classList.remove("active");
  });

  // 1-Click Demo Buttons
  document.querySelectorAll(".demo-pill-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const email = btn.getAttribute("data-email");
      const pass = btn.getAttribute("data-password");
      const role = btn.getAttribute("data-role");
      emailInput.value = email;
      passwordInput.value = pass;
      if (role === "student") roleStudentBtn.click();
      else roleAdminBtn.click();
      login(email, pass, role);
    });
  });

  loginForm.addEventListener("submit", (e) => {
    e.preventDefault();
    login(emailInput.value.trim(), passwordInput.value, activeRole);
  });

  document.getElementById("nav-logout-btn").addEventListener("click", () => {
    currentUser = null;
    saveSession();
    applyLoginState();
    showToast("Signed out successfully.", "info");
  });

  // Wire up registration & directory modal trigger buttons on Login screen
  const btnOpenReg = document.getElementById("btn-open-student-reg");
  if (btnOpenReg) {
    btnOpenReg.addEventListener("click", () => {
      document.getElementById("student-registration-form")?.reset();
      const errBox = document.getElementById("reg-error-msg");
      if (errBox) { errBox.style.display = "none"; errBox.textContent = ""; }
      openModal("student-registration-modal");
    });
  }

  const btnOpenDir = document.getElementById("btn-open-users-directory");
  if (btnOpenDir) {
    btnOpenDir.addEventListener("click", () => {
      openUsersDirectory();
    });
  }

  document.getElementById("dir-search-input")?.addEventListener("input", () => {
    renderDirectoryTable();
  });

  // Registration Form Submission
  const regForm = document.getElementById("student-registration-form");
  if (regForm) {
    regForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const errBox = document.getElementById("reg-error-msg");
      if (errBox) errBox.style.display = "none";

      const name = document.getElementById("reg-student-name").value.trim();
      const department = document.getElementById("reg-student-dept").value;
      const phone = document.getElementById("reg-student-phone").value.trim();
      const email = document.getElementById("reg-student-email").value.trim();
      const password = document.getElementById("reg-student-password").value;
      const confirmPass = document.getElementById("reg-student-confirm").value;

      if (!name || !email || !password) {
        if (errBox) { errBox.textContent = "Please fill in all required fields."; errBox.style.display = "block"; }
        return;
      }

      if (password !== confirmPass) {
        if (errBox) { errBox.textContent = "Passwords do not match. Please re-enter."; errBox.style.display = "block"; }
        return;
      }

      const submitBtn = document.getElementById("btn-submit-registration");
      if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerText = "Registering...";
      }

      try {
        const res = await fetch("/api/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name, department, phone, email, password })
        });

        const data = await res.json();
        if (data.success) {
          closeModal("student-registration-modal");
          showToast(`Student ${name} registered successfully! Auto-filled credentials.`, "success");
          autoFillUserLogin("student", email, password);
          return;
        } else {
          if (errBox) { errBox.textContent = data.message || "Registration failed."; errBox.style.display = "block"; }
        }
      } catch (err) {
        console.warn("Backend register API offline, saving locally:", err);
        const existing = INITIAL_DATA.students.find(s => s.email.toLowerCase() === email.toLowerCase());
        if (existing) {
          if (errBox) { errBox.textContent = "A student with this email address already exists."; errBox.style.display = "block"; }
        } else {
          const newId = INITIAL_DATA.students.length ? Math.max(...INITIAL_DATA.students.map(s => s.student_id)) + 1 : 1;
          const newStudent = { student_id: newId, name, department, email, password, phone };
          INITIAL_DATA.students.push(newStudent);
          closeModal("student-registration-modal");
          showToast(`Student ${name} registered locally! Auto-filled credentials.`, "success");
          autoFillUserLogin("student", email, password);
        }
      } finally {
        if (submitBtn) {
          submitBtn.disabled = false;
          submitBtn.innerHTML = `<span>Register &amp; Proceed to Login</span><svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="9 18 15 12 9 6"></polyline></svg>`;
        }
      }
    });
  }
}

// ==========================================================================
// Users Directory Modal Logic
// ==========================================================================
let currentDirectoryTab = "students";
let directoryDataCache = null;

async function openUsersDirectory() {
  openModal("users-directory-modal");
  await fetchDirectoryUsers();
  renderDirectoryTable();
}

async function fetchDirectoryUsers() {
  try {
    const res = await fetch("/api/users");
    if (res.ok) {
      const data = await res.json();
      if (data.students && data.admin) {
        directoryDataCache = data;
        return;
      }
    }
  } catch (err) {
    console.warn("Failed to load /api/users, using local seed data", err);
  }
  directoryDataCache = {
    students: INITIAL_DATA.students,
    admin: INITIAL_DATA.admin
  };
}

window.switchDirectoryTab = function(tab) {
  currentDirectoryTab = tab;
  const sBtn = document.getElementById("dir-tab-students-btn");
  const aBtn = document.getElementById("dir-tab-admins-btn");
  if (tab === "students") {
    sBtn?.classList.remove("btn-outline");
    sBtn?.classList.add("btn-primary");
    aBtn?.classList.remove("btn-primary");
    aBtn?.classList.add("btn-outline");
  } else {
    aBtn?.classList.remove("btn-outline");
    aBtn?.classList.add("btn-primary");
    sBtn?.classList.remove("btn-primary");
    sBtn?.classList.add("btn-outline");
  }
  renderDirectoryTable();
};

function renderDirectoryTable() {
  if (!directoryDataCache) return;
  const studentsBadge = document.getElementById("dir-students-badge");
  const adminsBadge = document.getElementById("dir-admins-badge");
  if (studentsBadge) studentsBadge.textContent = directoryDataCache.students.length;
  if (adminsBadge) adminsBadge.textContent = directoryDataCache.admin.length;

  const searchInput = document.getElementById("dir-search-input");
  const term = searchInput ? searchInput.value.toLowerCase().trim() : "";
  const tbody = document.getElementById("dir-table-tbody");
  if (!tbody) return;

  if (currentDirectoryTab === "students") {
    const filtered = directoryDataCache.students.filter(s =>
      !term ||
      (s.name && s.name.toLowerCase().includes(term)) ||
      (s.email && s.email.toLowerCase().includes(term)) ||
      (s.department && s.department.toLowerCase().includes(term))
    );

    if (filtered.length === 0) {
      tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--text-muted); padding: 2rem;">No registered students match your search.</td></tr>`;
      return;
    }

    tbody.innerHTML = filtered.map(s => `
      <tr>
        <td><strong>#${s.student_id}</strong></td>
        <td>
          <div style="font-weight: 600; color: var(--text-primary);">${escapeHtml(s.name)}</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);">${escapeHtml(s.phone || "No phone")}</div>
        </td>
        <td><span class="category-tag">${escapeHtml(s.department || "General")}</span></td>
        <td><code style="color: #818cf8;">${escapeHtml(s.email)}</code></td>
        <td><code style="background: rgba(255,255,255,0.06); padding: 2px 6px; border-radius: 4px;">${escapeHtml(s.password)}</code></td>
        <td style="text-align: center;">
          <button type="button" class="btn btn-outline btn-sm" style="padding: 0.3rem 0.6rem; font-size: 0.75rem; white-space: nowrap;"
            onclick="autoFillUserLogin('student', '${escapeHtml(s.email)}', '${escapeHtml(s.password)}')">
            ⚡ Auto-Fill
          </button>
        </td>
      </tr>
    `).join("");
  } else {
    const filtered = directoryDataCache.admin.filter(a =>
      !term ||
      (a.name && a.name.toLowerCase().includes(term)) ||
      (a.email && a.email.toLowerCase().includes(term)) ||
      (a.role && a.role.toLowerCase().includes(term))
    );

    if (filtered.length === 0) {
      tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--text-muted); padding: 2rem;">No administrators match your search.</td></tr>`;
      return;
    }

    tbody.innerHTML = filtered.map(a => `
      <tr>
        <td><strong>#${a.admin_id}</strong></td>
        <td>
          <div style="font-weight: 600; color: var(--text-primary);">${escapeHtml(a.name)}</div>
          <div style="font-size: 0.72rem; color: #10b981;">Staff / Official</div>
        </td>
        <td><span class="badge ${a.role === 'Warden' ? 'badge-progress' : 'badge-resolved'}">${escapeHtml(a.role)}</span></td>
        <td><code style="color: #34d399;">${escapeHtml(a.email)}</code></td>
        <td><code style="background: rgba(255,255,255,0.06); padding: 2px 6px; border-radius: 4px;">${escapeHtml(a.password)}</code></td>
        <td style="text-align: center;">
          <button type="button" class="btn btn-outline btn-sm" style="padding: 0.3rem 0.6rem; font-size: 0.75rem; white-space: nowrap;"
            onclick="autoFillUserLogin('admin', '${escapeHtml(a.email)}', '${escapeHtml(a.password)}')">
            ⚡ Auto-Fill
          </button>
        </td>
      </tr>
    `).join("");
  }
}

window.autoFillUserLogin = function(role, email, password) {
  closeModal("users-directory-modal");
  const roleStudentBtn = document.getElementById("role-student-btn");
  const roleAdminBtn = document.getElementById("role-admin-btn");
  const emailInput = document.getElementById("login-email");
  const passwordInput = document.getElementById("login-password");

  if (role === "student") {
    roleStudentBtn?.click();
  } else {
    roleAdminBtn?.click();
  }

  if (emailInput) emailInput.value = email;
  if (passwordInput) {
    passwordInput.value = password;
    passwordInput.focus();
  }

  showToast(`Auto-filled credentials for ${email} (${role.toUpperCase()})`, "info");
};

async function login(email, password, role) {
  try {
    // 1. Try Java Backend REST API (Runs SELECT ... via Java JDBC)
    const res = await fetch("/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password, role })
    });

    if (res.ok) {
      const result = await res.json();
      if (result.success) {
        currentUser = result.user;
        saveSession();
        applyLoginState();
        showToast(`Welcome back, ${currentUser.data.name}! (Authenticated via Java JDBC)`, "success");
        return;
      }
    }
  } catch (e) {
    console.warn("Backend API login failed, checking fallback data...", e);
  }

  // Fallback to local data
  if (role === "student") {
    const student = INITIAL_DATA.students.find(s => s.email.toLowerCase() === email.toLowerCase() && s.password === password);
    if (!student) { showToast("Invalid student credentials.", "error"); return; }
    currentUser = { role: "student", data: student };
  } else {
    const admin = INITIAL_DATA.admin.find(a => a.email.toLowerCase() === email.toLowerCase() && a.password === password);
    if (!admin) { showToast("Invalid admin credentials.", "error"); return; }
    currentUser = { role: "admin", data: admin };
  }
  saveSession();
  applyLoginState();
  showToast(`Welcome, ${currentUser.data.name}!`, "success");
}

// ==========================================================================
// Complaints Loading (from Java JDBC API)
// ==========================================================================
async function fetchComplaints() {
  try {
    const res = await fetch("/api/complaints");
    if (res.ok) {
      allComplaints = await res.json();
      return allComplaints;
    }
  } catch (e) {
    console.warn("Could not fetch from Java API, using memory list", e);
  }
  if (allComplaints.length === 0) allComplaints = [...INITIAL_DATA.complaints];
  return allComplaints;
}

// ==========================================================================
// Attachment Management & Live Previews
// ==========================================================================
const ATTACHMENT_STORE = (() => {
  try {
    return JSON.parse(localStorage.getItem("COLLEGE_COMPLAINT_ATTACHMENTS") || "{}");
  } catch (e) {
    return {};
  }
})();

function saveAttachment(name, dataUrl) {
  try {
    ATTACHMENT_STORE[name] = dataUrl;
    localStorage.setItem("COLLEGE_COMPLAINT_ATTACHMENTS", JSON.stringify(ATTACHMENT_STORE));
  } catch (e) {
    console.warn("Storage quota exceeded or unavailable", e);
  }
}

function getFileType(filename) {
  if (!filename) return "other";
  const ext = filename.split(".").pop().toLowerCase();
  if (["png", "jpg", "jpeg", "webp", "gif", "svg"].includes(ext)) return "image";
  if (["pdf"].includes(ext)) return "pdf";
  if (["doc", "docx", "txt", "rtf"].includes(ext)) return "doc";
  return "other";
}

// Generate realistic SVG image if no uploaded dataUrl exists (for sample data)
function generateSampleAttachmentGraphic(filename, complaintId, category = "Campus Grievance") {
  const ext = (filename || "file.jpg").split(".").pop().toUpperCase();
  const safeName = (filename || `evidence_${complaintId}.jpg`).replace(/[<>&"]/g, "");
  const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 520" width="800" height="520">
    <defs>
      <linearGradient id="bg" x1="0" y1="0" x2="1" y2="1">
        <stop offset="0%" stop-color="#18181b"/>
        <stop offset="100%" stop-color="#09090b"/>
      </linearGradient>
      <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
        <path d="M 40 0 L 0 0 0 40" fill="none" stroke="#27272a" stroke-width="0.8"/>
      </pattern>
    </defs>
    <rect width="800" height="520" fill="url(#bg)"/>
    <rect width="800" height="520" fill="url(#grid)"/>
    
    <!-- Camera viewfinder overlay -->
    <path d="M 40 70 L 40 40 L 70 40" fill="none" stroke="#ffffff" stroke-width="2.5" opacity="0.6"/>
    <path d="M 760 70 L 760 40 L 730 40" fill="none" stroke="#ffffff" stroke-width="2.5" opacity="0.6"/>
    <path d="M 40 450 L 40 480 L 70 480" fill="none" stroke="#ffffff" stroke-width="2.5" opacity="0.6"/>
    <path d="M 760 450 L 760 480 L 730 480" fill="none" stroke="#ffffff" stroke-width="2.5" opacity="0.6"/>
    
    <!-- Center crosshair -->
    <circle cx="400" cy="260" r="48" fill="none" stroke="#ffffff" stroke-width="1.5" stroke-dasharray="6,4" opacity="0.3"/>
    <line x1="400" y1="230" x2="400" y2="290" stroke="#ffffff" stroke-width="1.5" opacity="0.4"/>
    <line x1="370" y1="260" x2="430" y2="260" stroke="#ffffff" stroke-width="1.5" opacity="0.4"/>
    
    <!-- Red recording/verified dot -->
    <circle cx="60" cy="70" r="6" fill="#ef4444"/>
    <text x="75" y="74" fill="#ffffff" font-family="'Plus Jakarta Sans',sans-serif" font-size="12" font-weight="700" letter-spacing="1.5">OFFICIAL EVIDENCE ARCHIVE</text>
    
    <!-- File Details -->
    <rect x="200" y="150" width="400" height="220" rx="14" fill="#000000" fill-opacity="0.75" stroke="#3f3f46" stroke-width="1"/>
    <circle cx="400" cy="210" r="26" fill="#27272a"/>
    <path d="M 392 210 L 408 210 M 400 202 L 400 218" stroke="#ffffff" stroke-width="2.5" stroke-linecap="round"/>
    <text x="400" y="265" text-anchor="middle" fill="#ffffff" font-family="'Plus Jakarta Sans',sans-serif" font-size="16" font-weight="800">${safeName}</text>
    <text x="400" y="292" text-anchor="middle" fill="#a1a1aa" font-family="'Plus Jakarta Sans',sans-serif" font-size="12">Grievance #${complaintId || "NEW"} • Category: ${category}</text>
    <text x="400" y="325" text-anchor="middle" fill="#10b981" font-family="'Plus Jakarta Sans',sans-serif" font-size="11" font-weight="700">✓ VERIFIED TIMESTAMPED ATTACHMENT</text>
    
    <!-- Timestamp bar -->
    <text x="60" y="470" fill="#71717a" font-family="'JetBrains Mono',monospace" font-size="12">IMG_${complaintId || "0"}_CAPTURE • 1920x1080 • FORMAT: ${ext}</text>
    <text x="740" y="470" text-anchor="end" fill="#71717a" font-family="'JetBrains Mono',monospace" font-size="12">SECURE U • STUDENT SAFETY REDRESSAL</text>
  </svg>`;
  return "data:image/svg+xml;utf8," + encodeURIComponent(svg);
}

function showSubmissionPreview(filename, dataUrl, size = 0) {
  const box = document.getElementById("submission-preview-box");
  if (!box) return;
  const cleanFileName = filename.replace(/^.*[\\\/]/, '').trim();
  const type = getFileType(cleanFileName);
  const sizeText = size > 0 ? `${(size / 1024).toFixed(1)} KB` : "Attached file";
  const effectiveSrc = dataUrl || ATTACHMENT_STORE[cleanFileName] || ("/" + encodeURIComponent(cleanFileName));
  const fallbackSvg = generateSampleAttachmentGraphic(cleanFileName, 0, "Submission");
  
  let thumbHtml = "";
  if (type === "image") {
    thumbHtml = `<img src="${effectiveSrc}" class="submission-thumb-img" alt="preview" onerror="if(!this.dataset.fallbackTried){ this.dataset.fallbackTried='1'; this.src='${fallbackSvg}'; }" onclick="viewComplaintAttachment('${escapeHtml(cleanFileName)}', 0)">`;
  } else if (type === "pdf") {
    thumbHtml = `<div class="submission-file-icon" onclick="viewComplaintAttachment('${escapeHtml(cleanFileName)}', 0)"><span>PDF</span></div>`;
  } else {
    thumbHtml = `<div class="submission-file-icon" onclick="viewComplaintAttachment('${escapeHtml(cleanFileName)}', 0)"><span>DOC</span></div>`;
  }

  box.innerHTML = `
    <div class="submission-preview-card">
      <div class="submission-preview-left">
        ${thumbHtml}
        <div class="submission-meta">
          <div class="submission-meta-name" title="${escapeHtml(cleanFileName)}">${escapeHtml(cleanFileName)}</div>
          <div class="submission-meta-sub">
            <span style="color: #10b981;">● File ready for grievance filing</span>
            <span>• ${sizeText}</span>
          </div>
        </div>
      </div>
      <div style="display: flex; gap: 0.5rem; align-items: center;">
        <button type="button" class="btn btn-outline btn-sm" style="padding: 0.35rem 0.75rem; font-size: 0.76rem;" onclick="viewComplaintAttachment('${escapeHtml(cleanFileName)}', 0)">
          👁️ Preview
        </button>
        <button type="button" class="submission-btn-remove" onclick="removeSubmissionAttachment()">
          ✕ Remove
        </button>
      </div>
    </div>
  `;
  box.style.display = "block";
}

window.removeSubmissionAttachment = function() {
  const fileInput = document.getElementById("complaint-evidence-file");
  const textInput = document.getElementById("complaint-evidence-text");
  const fileNameDisplay = document.getElementById("file-upload-name");
  const box = document.getElementById("submission-preview-box");
  if (fileInput) fileInput.value = "";
  if (textInput) textInput.value = "";
  if (fileNameDisplay) fileNameDisplay.textContent = "No file chosen";
  if (box) { box.innerHTML = ""; box.style.display = "none"; }
};

window.viewComplaintAttachment = function(filename, complaintId = 0) {
  if (!filename || filename === "null" || filename === "None") {
    showToast("No attachment file is associated with this complaint.", "info");
    return;
  }

  const cleanFileName = filename.replace(/^.*[\\\/]/, '').trim();
  const c = allComplaints.find(item => item.complaint_id === complaintId);
  const category = c ? c.category : "Campus Grievance";
  const type = getFileType(cleanFileName);
  let dataUrl = ATTACHMENT_STORE[cleanFileName] || ATTACHMENT_STORE[filename] || null;

  document.getElementById("attachment-modal-title").textContent = cleanFileName;
  document.getElementById("attachment-modal-subtitle").textContent = complaintId
    ? `Supporting Evidence for Grievance #${complaintId} (${category})`
    : `Submission Attachment Preview (${category})`;

  const downloadBtn = document.getElementById("attachment-download-btn");
  const metaInfo = document.getElementById("attachment-meta-info");
  const body = document.getElementById("attachment-modal-body");

  const serverUrl = "/" + encodeURIComponent(cleanFileName);
  const fallbackSvg = generateSampleAttachmentGraphic(cleanFileName, complaintId, category);

  if (type === "image") {
    const primarySrc = dataUrl || serverUrl;
    body.innerHTML = `
      <div style="display: flex; justify-content: center; align-items: center; width: 100%; min-height: 280px; padding: 0.5rem;">
        <img src="${primarySrc}" class="attachment-img-preview" alt="${escapeHtml(cleanFileName)}" onerror="if(!this.dataset.fallbackTried){ this.dataset.fallbackTried='1'; this.src='${fallbackSvg}'; }">
      </div>
    `;
    downloadBtn.href = primarySrc;
    downloadBtn.download = cleanFileName;
    metaInfo.textContent = `Format: Image (${cleanFileName.split(".").pop().toUpperCase()}) • Verified Evidence`;
  } else if (type === "pdf") {
    const pdfSrc = dataUrl || serverUrl;
    if (dataUrl && dataUrl.startsWith("data:application/pdf")) {
      body.innerHTML = `<iframe src="${dataUrl}" style="width: 100%; height: 500px; border: none; border-radius: 8px;"></iframe>`;
      downloadBtn.href = dataUrl;
    } else {
      // High-resolution authentic college grievance document sheet
      body.innerHTML = `
        <div class="attachment-pdf-sheet">
          <div class="attachment-pdf-header">
            <div>
              <div style="font-size: 0.72rem; text-transform: uppercase; letter-spacing: 0.1em; color: #737373; font-weight: 800;">Official College Grievance Record</div>
              <h4 style="font-size: 1.15rem; font-weight: 800; color: #0a0a0a; margin: 0.2rem 0;">Grievance Redressal Cell Document</h4>
              <span style="font-size: 0.75rem; color: #525252;">Reference ID: <strong>#${complaintId || "PENDING"}</strong> • File: <code>${escapeHtml(cleanFileName)}</code></span>
            </div>
            <div style="width: 44px; height: 44px; border-radius: 50%; border: 2px solid #0a0a0a; display: flex; align-items: center; justify-content: center; font-weight: 800; font-size: 0.75rem;">
              SEAL
            </div>
          </div>
          <div class="attachment-pdf-body">
            <p style="margin-bottom: 0.85rem;"><strong>Incident Category:</strong> ${escapeHtml(category)}</p>
            <p style="margin-bottom: 0.85rem;"><strong>Filed By Student:</strong> ${c ? escapeHtml(c.student_name || "Student #" + c.student_id) : "Authenticated Student"}</p>
            <p style="margin-bottom: 1rem;"><strong>Incident Description:</strong><br><span style="color: #525252; font-style: italic;">"${c ? escapeHtml(c.description) : "Supporting documentary evidence provided for this submission."}"</span></p>
            <div style="padding: 0.85rem; background: #f5f5f5; border-left: 3px solid #0a0a0a; font-size: 0.8rem; margin-top: 1.25rem;">
              <strong>Digital Verification:</strong> This PDF document represents official supporting documentation registered into the SecureU MySQL repository.
            </div>
          </div>
        </div>
      `;
      downloadBtn.href = serverUrl;
    }
    downloadBtn.download = cleanFileName;
    metaInfo.textContent = "Format: Portable Document Format (PDF) • Verified Document";
  } else {
    const docSrc = dataUrl || serverUrl;
    body.innerHTML = `
      <div style="padding: 2.5rem; text-align: center;">
        <div style="font-size: 3rem; margin-bottom: 1rem;">📁</div>
        <h4 style="font-size: 1.1rem; font-weight: 800; color: #0a0a0a; margin-bottom: 0.4rem;">${escapeHtml(cleanFileName)}</h4>
        <p style="font-size: 0.85rem; color: #525252; max-width: 400px; margin: 0 auto 1.5rem;">
          Supporting file for Complaint #${complaintId || "NEW"}. Click download to open with your system viewer.
        </p>
      </div>
    `;
    downloadBtn.href = docSrc;
    downloadBtn.download = cleanFileName;
    metaInfo.textContent = `Format: ${cleanFileName.split(".").pop().toUpperCase()} Document`;
  }

  openModal("attachment-preview-modal");
};

// ==========================================================================
// Student Dashboard
// ==========================================================================
let studentSearchQuery = "";

function initStudentDashboard() {
  // Submit Complaint Form
  const form = document.getElementById("student-complaint-form");
  const fileInput = document.getElementById("complaint-evidence-file");
  const textInput = document.getElementById("complaint-evidence-text");
  const fileNameDisplay = document.getElementById("file-upload-name");

  if (fileInput) {
    fileInput.addEventListener("change", () => {
      if (fileInput.files.length > 0) {
        const file = fileInput.files[0];
        fileNameDisplay.textContent = file.name;
        if (textInput) textInput.value = file.name;
        const reader = new FileReader();
        reader.onload = (e) => {
          const dataUrl = e.target.result;
          saveAttachment(file.name, dataUrl);
          showSubmissionPreview(file.name, dataUrl, file.size);
        };
        reader.readAsDataURL(file);
      } else {
        fileNameDisplay.textContent = "No file chosen";
        removeSubmissionAttachment();
      }
    });
  }

  if (textInput) {
    textInput.addEventListener("input", (e) => {
      const val = e.target.value.trim();
      if (val && (!fileInput || !fileInput.files.length)) {
        showSubmissionPreview(val, ATTACHMENT_STORE[val] || null, 0);
      } else if (!val && (!fileInput || !fileInput.files.length)) {
        removeSubmissionAttachment();
      }
    });
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const category = document.getElementById("complaint-category").value;
    const description = document.getElementById("complaint-desc").value.trim();
    let evidence = (fileInput && fileInput.files.length > 0)
      ? fileInput.files[0].name
      : (textInput ? textInput.value.trim() : "");

    if (!description) {
      showToast("Please enter a description.", "error");
      return;
    }

    try {
      // Send to Java REST API (Runs INSERT INTO complaints via Java JDBC)
      const res = await fetch("/api/complaints", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          student_id: currentUser.data.student_id,
          category: category,
          description: description,
          evidence: evidence || null
        })
      });

      if (res.ok) {
        const result = await res.json();
        const newId = result.complaint_id || 104;
        form.reset();
        removeSubmissionAttachment();
        closeModal("student-file-modal");

        showToast(`Grievance submitted via Java JDBC! Tracking ID: #${newId}`, "success");
        await renderStudentDashboard();
        document.getElementById("track-complaint-id").value = newId;
        trackComplaint(newId);
        openModal("student-track-modal");
        return;
      }
    } catch (err) {
      console.warn("Java API insert failed, inserting in-memory", err);
    }

    // Fallback in-memory
    const newId = (allComplaints.length > 0 ? Math.max(...allComplaints.map(c => c.complaint_id)) : 100) + 1;
    allComplaints.unshift({
      complaint_id: newId,
      student_id: currentUser.data.student_id,
      student_name: currentUser.data.name,
      department: currentUser.data.department,
      category: category,
      description: description,
      evidence: evidence || null,
      status: "Pending",
      date_filed: new Date().toISOString().split("T")[0],
      remarks: ""
    });

    form.reset();
    removeSubmissionAttachment();
    closeModal("student-file-modal");
    showToast(`Grievance submitted! Tracking ID: #${newId}`, "success");
    await renderStudentDashboard();
    document.getElementById("track-complaint-id").value = newId;
    trackComplaint(newId);
    openModal("student-track-modal");
  });

  // Tracking Action
  document.getElementById("track-submit-btn").addEventListener("click", () => {
    const id = parseInt(document.getElementById("track-complaint-id").value.trim(), 10);
    if (isNaN(id)) { showToast("Enter a numeric complaint ID.", "error"); return; }
    trackComplaint(id);
  });

  // Search Input Filter
  const searchInput = document.getElementById("student-search-input");
  if (searchInput) {
    searchInput.addEventListener("input", (e) => {
      studentSearchQuery = e.target.value.toLowerCase().trim();
      renderStudentDashboard(false);
    });
  }

  // Refresh
  document.getElementById("student-refresh-btn").addEventListener("click", async () => {
    await renderStudentDashboard(true);
    showToast("Complaints refreshed from Java MySQL backend.", "info");
  });
}

async function renderStudentDashboard(refetch = true) {
  if (!currentUser || currentUser.role !== "student") return;

  document.getElementById("student-welcome-title").textContent = `Student Portal — ${currentUser.data.name}`;
  document.getElementById("student-dept-tag").textContent = currentUser.data.department;

  if (refetch || allComplaints.length === 0) await fetchComplaints();
  let list = allComplaints.filter(c => c.student_id === currentUser.data.student_id);

  // Stats
  document.getElementById("student-stat-total").textContent = list.length;
  document.getElementById("student-stat-pending").textContent = list.filter(c => c.status === "Pending").length;
  document.getElementById("student-stat-progress").textContent = list.filter(c => c.status === "In Progress").length;
  document.getElementById("student-stat-resolved").textContent = list.filter(c => c.status === "Resolved").length;
  const tabBadge = document.getElementById("student-tab-badge");
  if (tabBadge) tabBadge.textContent = list.length;

  if (studentSearchQuery) {
    list = list.filter(c =>
      String(c.complaint_id).includes(studentSearchQuery) ||
      c.category.toLowerCase().includes(studentSearchQuery) ||
      c.description.toLowerCase().includes(studentSearchQuery)
    );
  }

  const tbody = document.getElementById("student-complaints-tbody");
  tbody.innerHTML = "";

  if (list.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--text-muted); padding: 2rem;">No complaints registered yet.</td></tr>`;
    return;
  }

  list.forEach(c => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td><strong>#${c.complaint_id}</strong></td>
      <td><span class="category-tag">${escapeHtml(c.category)}</span></td>
      <td style="max-width: 280px;">
        <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${escapeHtml(c.description)}">${escapeHtml(c.description)}</div>
        ${c.evidence ? `<button type="button" class="table-evidence-btn" onclick="viewComplaintAttachment('${escapeHtml(c.evidence)}', ${c.complaint_id})">📎 View ${escapeHtml(c.evidence)}</button>` : ""}
      </td>
      <td>${getStatusBadge(c.status)}</td>
      <td style="color: var(--text-muted);">${c.date_filed}</td>
      <td>
        <button class="btn btn-outline btn-sm" onclick="trackFromTable(${c.complaint_id})">
          Track Status
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

window.trackFromTable = function(id) {
  document.getElementById("track-complaint-id").value = id;
  trackComplaint(id);
  openModal("student-track-modal");
};

function trackComplaint(id) {
  const complaint = allComplaints.find(c => c.complaint_id === id && c.student_id === currentUser.data.student_id);
  const resultCard = document.getElementById("tracker-result-card");
  const emptyPrompt = document.getElementById("tracker-empty-prompt");

  if (!complaint) {
    resultCard.style.display = "none";
    emptyPrompt.style.display = "block";
    emptyPrompt.innerHTML = `<span style="color: var(--danger)">Complaint #${id} not found under your account.</span>`;
    return;
  }

  emptyPrompt.style.display = "none";
  resultCard.style.display = "block";

  document.getElementById("track-disp-id").textContent = `#${complaint.complaint_id}`;
  document.getElementById("track-disp-category").textContent = complaint.category;
  document.getElementById("track-disp-date").textContent = complaint.date_filed;
  document.getElementById("track-disp-desc").textContent = complaint.description;

  const evBox = document.getElementById("track-disp-evidence-wrap");
  if (evBox) {
    if (complaint.evidence && complaint.evidence.trim() && complaint.evidence !== "null") {
      evBox.innerHTML = `
        Evidence: <strong style="color: var(--text-secondary); max-width: 200px; overflow: hidden; text-overflow: ellipsis; display: inline-block; vertical-align: middle;">${escapeHtml(complaint.evidence)}</strong>
        <button type="button" class="btn-view-attachment" style="padding: 0.22rem 0.65rem !important; font-size: 0.74rem !important;" onclick="viewComplaintAttachment('${escapeHtml(complaint.evidence)}', ${complaint.complaint_id})">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
          View Attachment
        </button>
      `;
    } else {
      evBox.innerHTML = `Evidence: <strong style="color: var(--text-secondary);">None</strong>`;
    }
  } else {
    document.getElementById("track-disp-evidence").textContent = complaint.evidence ? complaint.evidence : "No attachment";
  }

  const remarksBox = document.getElementById("track-disp-remarks-box");
  const remarksText = document.getElementById("track-disp-remarks");
  if (complaint.remarks && complaint.remarks.trim()) {
    remarksBox.style.display = "block";
    remarksText.textContent = complaint.remarks;
  } else {
    remarksBox.style.display = "none";
  }

  // Stepper
  const stepSubmit = document.getElementById("step-submitted");
  const stepReview = document.getElementById("step-review");
  const stepResolved = document.getElementById("step-resolved");

  stepSubmit.className = "timeline-step completed";
  stepReview.className = "timeline-step";
  stepResolved.className = "timeline-step";

  if (complaint.status === "Pending") {
    stepReview.className = "timeline-step active";
    document.getElementById("review-step-text").textContent = "Under Review (Awaiting Warden/HOD Assignment)";
  } else if (complaint.status === "In Progress") {
    stepReview.className = "timeline-step completed";
    stepResolved.className = "timeline-step active";
    document.getElementById("review-step-text").textContent = "Action In Progress by Administration";
  } else if (complaint.status === "Resolved") {
    stepReview.className = "timeline-step completed";
    stepResolved.className = "timeline-step completed";
    document.getElementById("review-step-text").textContent = "Resolution Completed & Documented";
  }
}

// ==========================================================================
// Admin Dashboard
// ==========================================================================
let adminFilterCategory = "ALL";
let adminFilterStatus = "ALL";
let adminFilterDept = "ALL";
let adminSearchQuery = "";
let adminScope = "mine";        // "mine" = role jurisdiction, "all" = every complaint
let adminTrendMonths = 6;
let adminCategoryMetric = "all";
let adminPage = 1;
const ADMIN_PAGE_SIZE = 8;

// Which categories each administrator role is responsible for
const ROLE_JURISDICTION = {
  Warden: ["Hostel", "Food", "Physical Harassment", "Verbal Harassment", "Other"],
  HOD: ["Academic", "Physical Harassment", "Verbal Harassment", "Other"]
};

function initAdminDashboard() {
  document.querySelectorAll("#admin-scope-toggle .scope-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      document.querySelectorAll("#admin-scope-toggle .scope-btn").forEach(b => b.classList.remove("active"));
      btn.classList.add("active");
      adminScope = btn.getAttribute("data-scope");
      adminPage = 1;
      renderAdminDashboard(false);
    });
  });
  document.getElementById("trend-range-select").addEventListener("change", (e) => {
    adminTrendMonths = parseInt(e.target.value, 10); renderAdminDashboard(false);
  });
  document.getElementById("category-metric-select").addEventListener("change", (e) => {
    adminCategoryMetric = e.target.value; renderAdminDashboard(false);
  });
  document.getElementById("admin-pagination").addEventListener("click", (e) => {
    const b = e.target.closest("[data-page]");
    if (!b || b.disabled) return;
    adminPage = parseInt(b.getAttribute("data-page"), 10);
    renderAdminDashboard(false);
  });
  document.getElementById("admin-filter-category").addEventListener("change", (e) => {
    adminFilterCategory = e.target.value; adminPage = 1; renderAdminDashboard(false);
  });
  document.getElementById("admin-filter-status").addEventListener("change", (e) => {
    adminFilterStatus = e.target.value; adminPage = 1; renderAdminDashboard(false);
  });
  document.getElementById("admin-filter-dept").addEventListener("change", (e) => {
    adminFilterDept = e.target.value; adminPage = 1; renderAdminDashboard(false);
  });
  document.getElementById("admin-search-input").addEventListener("input", (e) => {
    adminSearchQuery = e.target.value.trim().toLowerCase(); adminPage = 1; renderAdminDashboard(false);
  });
  document.getElementById("admin-refresh-btn").addEventListener("click", async () => {
    await renderAdminDashboard();
    showToast("Grievances refreshed from Java backend.", "info");
  });

  // Save Update Modal Form (Calls POST /api/update on Java Server)
  document.getElementById("admin-update-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const cid = parseInt(document.getElementById("modal-update-id").value, 10);
    const newStatus = document.getElementById("modal-update-status").value;
    const newRemarks = document.getElementById("modal-update-remarks").value.trim();

    try {
      const res = await fetch("/api/update", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ complaint_id: cid, status: newStatus, remarks: newRemarks })
      });

      if (res.ok) {
        closeModal("admin-update-modal");
        await renderAdminDashboard();
        showToast(`Complaint #${cid} updated via Java JDBC to '${newStatus}'!`, "success");
        return;
      }
    } catch (err) {
      console.warn("Java update API failed, updating in-memory", err);
    }

    const item = allComplaints.find(c => c.complaint_id === cid);
    if (item) { item.status = newStatus; item.remarks = newRemarks; }
    closeModal("admin-update-modal");
    renderAdminDashboard();
    showToast(`Complaint #${cid} updated to '${newStatus}'!`, "success");
  });
}

async function renderAdminDashboard(refetch = true) {
  if (!currentUser || currentUser.role !== "admin") return;

  const role = currentUser.data.role;
  const hour = new Date().getHours();
  const greet = hour < 12 ? "Good morning" : (hour < 17 ? "Good afternoon" : "Good evening");
  document.getElementById("admin-welcome-title").textContent = `${greet}, ${currentUser.data.name}! 👋`;
  document.getElementById("admin-role-tag").textContent = role === "HOD" ? "Head of Department" : "Hostel Warden";
  const scopeCats = ROLE_JURISDICTION[role] || null;
  document.getElementById("admin-subtitle").textContent = adminScope === "mine" && scopeCats
    ? `Overview of grievances under your jurisdiction (${scopeCats.join(", ")}).`
    : "Here's an overview of all student grievances across the college.";

  if (refetch || allComplaints.length === 0) await fetchComplaints();
  document.getElementById("admin-last-updated").textContent =
    "Last updated: " + new Date().toLocaleString("en-IN", { day: "2-digit", month: "short", year: "numeric", hour: "2-digit", minute: "2-digit" });

  // Scope dataset by role jurisdiction
  const scoped = (adminScope === "mine" && scopeCats)
    ? allComplaints.filter(c => scopeCats.includes(c.category))
    : [...allComplaints];

  const total = scoped.length;
  const pending = scoped.filter(c => c.status === "Pending").length;
  const inProgress = scoped.filter(c => c.status === "In Progress").length;
  const resolved = scoped.filter(c => c.status === "Resolved").length;
  const rate = total > 0 ? Math.round((resolved / total) * 100) : 0;

  animateCount("admin-stat-total", total);
  animateCount("admin-stat-pending", pending);
  animateCount("admin-stat-progress", inProgress);
  animateCount("admin-stat-resolved", resolved);
  animateCount("admin-stat-rate", rate, "%");

  renderAdminAnalytics(scoped);

  let list = [...scoped];
  if (adminFilterCategory !== "ALL") list = list.filter(c => c.category === adminFilterCategory);
  if (adminFilterStatus !== "ALL") list = list.filter(c => c.status === adminFilterStatus);
  if (adminFilterDept !== "ALL") list = list.filter(c => c.department === adminFilterDept);
  if (adminSearchQuery) {
    list = list.filter(c =>
      String(c.complaint_id).includes(adminSearchQuery) ||
      (c.student_name && c.student_name.toLowerCase().includes(adminSearchQuery)) ||
      c.category.toLowerCase().includes(adminSearchQuery) ||
      c.description.toLowerCase().includes(adminSearchQuery)
    );
  }

  const tbody = document.getElementById("admin-complaints-tbody");
  const pager = document.getElementById("admin-pagination");
  tbody.innerHTML = "";

  if (list.length === 0) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; padding: 2rem; color: var(--text-muted);">No records found.</td></tr>`;
    pager.innerHTML = "";
    return;
  }

  const pages = Math.max(1, Math.ceil(list.length / ADMIN_PAGE_SIZE));
  if (adminPage > pages) adminPage = pages;
  const start = (adminPage - 1) * ADMIN_PAGE_SIZE;
  const pageItems = list.slice(start, start + ADMIN_PAGE_SIZE);

  const clip = `<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l9.19-9.19a4 4 0 0 1 5.66 5.66l-9.2 9.19a2 2 0 0 1-2.83-2.83l8.49-8.48"/></svg>`;

  pageItems.forEach(c => {
    const name = c.student_name || "Student #" + c.student_id;
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td><strong>#${c.complaint_id}</strong></td>
      <td>
        <div class="person-cell">
          <div class="person-avatar">${escapeHtml(name.charAt(0).toUpperCase())}</div>
          <div>
            <div style="font-weight: 700;">${escapeHtml(name)}</div>
            <div style="font-size: 0.75rem; color: var(--text-muted);">${escapeHtml(c.department || "N/A")}</div>
          </div>
        </div>
      </td>
      <td><span class="category-tag">${escapeHtml(c.category)}</span></td>
      <td style="max-width: 300px;">
        <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${escapeHtml(c.description)}">${escapeHtml(c.description)}</div>
        ${c.evidence ? `<button type="button" class="table-evidence-btn" onclick="viewComplaintAttachment('${escapeHtml(c.evidence)}', ${c.complaint_id})">${clip} View ${escapeHtml(c.evidence)}</button>` : ""}
      </td>
      <td>${getStatusBadge(c.status)}</td>
      <td style="font-size: 0.8rem; color: var(--text-muted); white-space: nowrap;">${c.date_filed}</td>
      <td>
        <button class="btn ${c.status === "Resolved" ? "btn-outline" : "btn-primary"} btn-sm" style="min-width: 78px;" onclick="openAdminUpdateModal(${c.complaint_id})">
          ${c.status === "Resolved" ? "View" : "Update"}
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });

  let btns = `<button class="page-btn" data-page="${adminPage - 1}" ${adminPage === 1 ? "disabled" : ""}>‹</button>`;
  for (let p = 1; p <= pages; p++) {
    btns += `<button class="page-btn ${p === adminPage ? "active" : ""}" data-page="${p}">${p}</button>`;
  }
  btns += `<button class="page-btn" data-page="${adminPage + 1}" ${adminPage === pages ? "disabled" : ""}>›</button>`;
  pager.innerHTML = `
    <span>Showing ${start + 1}–${Math.min(start + ADMIN_PAGE_SIZE, list.length)} of ${list.length} complaints</span>
    <div class="page-btns">${btns}</div>`;
}

// ==========================================================================
// Admin Analytics & Monochrome SVG Charts (no external libraries)
// ==========================================================================
function parseDate(s) {
  const [y, m, d] = String(s).split("-").map(Number);
  return new Date(y, (m || 1) - 1, d || 1);
}

function animateCount(id, target, suffix = "") {
  const el = document.getElementById(id);
  if (!el) return;
  const from = parseInt(el.textContent, 10) || 0;
  const t0 = performance.now();
  const dur = 600;
  function step(t) {
    const k = Math.min(1, (t - t0) / dur);
    const eased = 1 - Math.pow(1 - k, 3);
    el.textContent = Math.round(from + (target - from) * eased) + suffix;
    if (k < 1) requestAnimationFrame(step);
  }
  requestAnimationFrame(step);
}

function monthlyBuckets(list, months) {
  const now = new Date();
  const buckets = [];
  for (let i = months - 1; i >= 0; i--) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    buckets.push({ y: d.getFullYear(), m: d.getMonth(), label: d.toLocaleString("en", { month: "short" }), total: 0, resolved: 0, pending: 0, progress: 0, cumTotal: 0, cumResolved: 0 });
  }
  list.forEach(c => {
    const d = parseDate(c.date_filed);
    const b = buckets.find(x => x.y === d.getFullYear() && x.m === d.getMonth());
    if (b) {
      b.total++;
      if (c.status === "Resolved") b.resolved++;
      else if (c.status === "Pending") b.pending++;
      else if (c.status === "In Progress") b.progress++;
    }
    buckets.forEach(x => {
      const end = new Date(x.y, x.m + 1, 1);
      if (d < end) { x.cumTotal++; if (c.status === "Resolved") x.cumResolved++; }
    });
  });
  buckets.forEach(x => x.rate = x.cumTotal ? Math.round((x.cumResolved / x.cumTotal) * 100) : 0);
  return buckets;
}

function renderAdminAnalytics(list) {
  // KPI deltas + sparklines (last 6 months)
  const b6 = monthlyBuckets(list, 6);
  const cur = b6[b6.length - 1], prev = b6[b6.length - 2];
  const setDelta = (id, a, b, isRate) => {
    const el = document.getElementById(id);
    let diff, txt;
    if (isRate) { diff = a - b; txt = `${Math.abs(diff)} pts`; }
    else if (b === 0) { diff = a; txt = a === 0 ? "0%" : "new"; }
    else { diff = Math.round(((a - b) / b) * 100); txt = `${Math.abs(diff)}%`; }
    const arrow = diff > 0 ? "↑" : (diff < 0 ? "↓" : "→");
    el.innerHTML = `<strong>${arrow} ${txt}</strong><span>vs last month</span>`;
  };
  setDelta("admin-delta-total", cur.total, prev.total);
  setDelta("admin-delta-pending", cur.pending, prev.pending);
  setDelta("admin-delta-progress", cur.progress, prev.progress);
  setDelta("admin-delta-resolved", cur.resolved, prev.resolved);
  setDelta("admin-delta-rate", cur.rate, prev.rate, true);

  drawSpark("admin-spark-total", b6.map(x => x.total));
  drawSpark("admin-spark-pending", b6.map(x => x.pending));
  drawSpark("admin-spark-progress", b6.map(x => x.progress));
  drawSpark("admin-spark-resolved", b6.map(x => x.resolved));
  drawSpark("admin-spark-rate", b6.map(x => x.rate));

  drawTrendChart(monthlyBuckets(list, adminTrendMonths));
  drawDonutChart(list);
  drawCategoryBars(list);
}

function drawSpark(id, values) {
  const svg = document.getElementById(id);
  if (!svg) return;
  const max = Math.max(1, ...values), min = Math.min(0, ...values);
  const pts = values.map((v, i) => [i * (100 / (values.length - 1)), 28 - ((v - min) / (max - min || 1)) * 24]);
  const line = pts.map((p, i) => (i ? "L" : "M") + p[0].toFixed(1) + "," + p[1].toFixed(1)).join(" ");
  const gid = id + "-g";
  svg.innerHTML = `
    <defs><linearGradient id="${gid}" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stop-color="#0a0a0a" stop-opacity="0.18"/><stop offset="100%" stop-color="#0a0a0a" stop-opacity="0"/></linearGradient></defs>
    <path d="${line} L100,32 L0,32 Z" fill="url(#${gid})"/>
    <path d="${line}" fill="none" stroke="#0a0a0a" stroke-width="1.8" vector-effect="non-scaling-stroke" stroke-linejoin="round" stroke-linecap="round"/>`;
}

function drawTrendChart(buckets) {
  const el = document.getElementById("chart-trend");
  const W = 520, H = 230, L = 34, R = 12, T = 12, B = 28;
  const iw = W - L - R, ih = H - T - B;
  const maxV = Math.max(4, ...buckets.map(b => b.total));
  const step = Math.ceil(maxV / 4);
  const top = step * 4;
  const x = i => L + (buckets.length === 1 ? iw / 2 : (i * iw) / (buckets.length - 1));
  const y = v => T + ih - (v / top) * ih;

  let grid = "";
  for (let g = 0; g <= 4; g++) {
    const gv = g * step;
    grid += `<line x1="${L}" x2="${W - R}" y1="${y(gv)}" y2="${y(gv)}" stroke="#eeeeee"/>
             <text x="${L - 8}" y="${y(gv) + 4}" text-anchor="end" class="axis-text">${gv}</text>`;
  }
  const labels = buckets.map((b, i) => `<text x="${x(i)}" y="${H - 8}" text-anchor="middle" class="axis-text">${b.label}</text>`).join("");

  const path = key => buckets.map((b, i) => (i ? "L" : "M") + x(i).toFixed(1) + "," + y(b[key]).toFixed(1)).join(" ");
  const dots = (key, fill, stroke) => buckets.map((b, i) =>
    `<circle cx="${x(i)}" cy="${y(b[key])}" r="4" fill="${fill}" stroke="${stroke}" stroke-width="2" class="chart-dot"><title>${b.label}: ${b[key]} ${key}</title></circle>`).join("");

  el.innerHTML = `
    <svg viewBox="0 0 ${W} ${H}" class="chart-svg">
      <defs><linearGradient id="trendFill" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stop-color="#0a0a0a" stop-opacity="0.14"/><stop offset="100%" stop-color="#0a0a0a" stop-opacity="0"/></linearGradient></defs>
      ${grid}${labels}
      <path d="${path("total")} L${x(buckets.length - 1)},${y(0)} L${x(0)},${y(0)} Z" fill="url(#trendFill)"/>
      <path d="${path("total")}" fill="none" stroke="#0a0a0a" stroke-width="2.5" class="chart-line"/>
      <path d="${path("resolved")}" fill="none" stroke="#9ca3af" stroke-width="2.5" class="chart-line"/>
      <path d="${path("pending")}" fill="none" stroke="#525252" stroke-width="2" stroke-dasharray="5 4"/>
      ${dots("total", "#0a0a0a", "#ffffff")}
      ${dots("resolved", "#ffffff", "#9ca3af")}
      ${dots("pending", "#ffffff", "#525252")}
    </svg>`;
}

function drawDonutChart(list) {
  const el = document.getElementById("chart-donut");
  const legend = document.getElementById("chart-donut-legend");
  const data = [
    { label: "Resolved", value: list.filter(c => c.status === "Resolved").length, color: "#0a0a0a" },
    { label: "In Progress", value: list.filter(c => c.status === "In Progress").length, color: "#737373" },
    { label: "Pending", value: list.filter(c => c.status === "Pending").length, color: "#d4d4d4" }
  ];
  const total = data.reduce((s, d) => s + d.value, 0);
  const r = 70, cx = 90, cy = 90, sw = 26, C = 2 * Math.PI * r;
  let offset = 0;
  const segs = data.map(d => {
    const len = total ? (d.value / total) * C : 0;
    const s = `<circle cx="${cx}" cy="${cy}" r="${r}" fill="none" stroke="${d.color}" stroke-width="${sw}"
        stroke-dasharray="${len} ${C - len}" stroke-dashoffset="${-offset}" transform="rotate(-90 ${cx} ${cy})" class="donut-seg">
        <title>${d.label}: ${d.value}</title></circle>`;
    offset += len;
    return s;
  }).join("");
  el.innerHTML = `
    <svg viewBox="0 0 180 180" class="donut-svg">
      <circle cx="${cx}" cy="${cy}" r="${r}" fill="none" stroke="#f5f5f5" stroke-width="${sw}"/>
      ${segs}
      <text x="${cx}" y="${cy + 2}" text-anchor="middle" class="donut-total">${total}</text>
      <text x="${cx}" y="${cy + 22}" text-anchor="middle" class="donut-sub">Total</text>
    </svg>`;
  legend.innerHTML = data.map(d => `
    <div class="donut-legend-row">
      <span class="lg-dot" style="background:${d.color}"></span>
      <span class="lg-label">${d.label}</span>
      <span class="lg-val">${d.value} <em>(${total ? Math.round((d.value / total) * 100) : 0}%)</em></span>
    </div>`).join("");
}

function drawCategoryBars(list) {
  const el = document.getElementById("chart-category");
  let src = list;
  if (adminCategoryMetric === "open") src = list.filter(c => c.status !== "Resolved");
  else if (adminCategoryMetric === "Resolved") src = list.filter(c => c.status === "Resolved");
  const counts = {};
  src.forEach(c => { counts[c.category] = (counts[c.category] || 0) + 1; });
  const rows = Object.entries(counts).sort((a, b) => b[1] - a[1]);
  if (rows.length === 0) { el.innerHTML = `<div style="color: var(--text-muted); font-size: 0.85rem; padding: 2rem 0; text-align:center;">No data</div>`; return; }
  const max = rows[0][1];
  const shades = ["#0a0a0a", "#262626", "#404040", "#525252", "#737373", "#a3a3a3"];
  el.innerHTML = rows.map(([cat, n], i) => `
    <div class="cat-row" title="${escapeHtml(cat)}: ${n}">
      <span class="cat-name">${escapeHtml(cat)}</span>
      <div class="cat-track"><div class="cat-fill" style="width:0%; background:${shades[Math.min(i, shades.length - 1)]}" data-w="${(n / max) * 100}"></div></div>
      <span class="cat-count">${n}</span>
    </div>`).join("");
  requestAnimationFrame(() => el.querySelectorAll(".cat-fill").forEach(f => { f.style.width = f.getAttribute("data-w") + "%"; }));
}

window.openAdminUpdateModal = function(id) {
  const c = allComplaints.find(item => item.complaint_id === id);
  if (!c) return;

  document.getElementById("modal-update-id").value = c.complaint_id;
  document.getElementById("modal-disp-id").textContent = `#${c.complaint_id}`;
  document.getElementById("modal-disp-student").textContent = `${c.student_name || "Student #" + c.student_id} (${c.department || "Dept"})`;
  document.getElementById("modal-disp-category").textContent = c.category;
  document.getElementById("modal-disp-desc").textContent = c.description;

  const evBox = document.getElementById("modal-disp-evidence");
  if (c.evidence && c.evidence.trim() && c.evidence !== "null") {
    evBox.innerHTML = `
      <div class="evidence-pill-box">
        <div class="evidence-pill-left">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l9.19-9.19a4 4 0 0 1 5.66 5.66l-9.2 9.19a2 2 0 0 1-2.83-2.83l8.49-8.48"/></svg>
          <span class="evidence-filename" title="${escapeHtml(c.evidence)}">${escapeHtml(c.evidence)}</span>
        </div>
        <button type="button" class="btn-view-attachment" onclick="viewComplaintAttachment('${escapeHtml(c.evidence)}', ${c.complaint_id})">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
          View Attachment
        </button>
      </div>
    `;
  } else {
    evBox.innerHTML = `<span style="font-size: 0.82rem; color: var(--text-muted); margin-top: 0.3rem;">📎 No attachment provided</span>`;
  }

  document.getElementById("modal-update-status").value = c.status;
  document.getElementById("modal-update-remarks").value = c.remarks || "";

  openModal("admin-update-modal");
};

// ==========================================================================
// Database Inspector Modal (Reads real MySQL tables from Java API)
// ==========================================================================
function initDbInspector() {
  document.getElementById("nav-db-btn").addEventListener("click", async () => {
    await renderDbInspector();
    openModal("db-inspector-modal");
  });

  document.getElementById("db-reset-btn").addEventListener("click", () => {
    showToast("To reset MySQL tables, re-import sql/CollegeComplaint.sql in MySQL.", "info");
  });

  document.getElementById("db-export-json-btn").addEventListener("click", () => {
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(allComplaints, null, 2));
    const downloadAnchor = document.createElement("a");
    downloadAnchor.setAttribute("href", dataStr);
    downloadAnchor.setAttribute("download", `college_complaints_export.json`);
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();
    showToast("Complaints JSON exported.", "info");
  });
}

async function renderDbInspector() {
  let dbData = null;
  try {
    const res = await fetch("/api/db");
    if (res.ok) dbData = await res.json();
  } catch (e) {}

  if (!dbData) {
    dbData = {
      students: INITIAL_DATA.students,
      admin: INITIAL_DATA.admin,
      complaints: allComplaints.length > 0 ? allComplaints : INITIAL_DATA.complaints
    };
  }

  document.getElementById("db-students-tbody").innerHTML = dbData.students.map(s => `
    <tr>
      <td>${s.student_id}</td>
      <td>${escapeHtml(s.name)}</td>
      <td>${escapeHtml(s.department)}</td>
      <td>${escapeHtml(s.email)}</td>
      <td><code>${escapeHtml(s.password)}</code></td>
      <td>${escapeHtml(s.phone)}</td>
    </tr>
  `).join("");

  document.getElementById("db-admin-tbody").innerHTML = dbData.admin.map(a => `
    <tr>
      <td>${a.admin_id}</td>
      <td>${escapeHtml(a.name)}</td>
      <td><span class="category-tag">${escapeHtml(a.role)}</span></td>
      <td>${escapeHtml(a.email)}</td>
      <td><code>${escapeHtml(a.password)}</code></td>
    </tr>
  `).join("");

  document.getElementById("db-complaints-tbody").innerHTML = dbData.complaints.map(c => `
    <tr>
      <td><strong>${c.complaint_id}</strong></td>
      <td>${c.student_id}</td>
      <td><span class="category-tag">${escapeHtml(c.category)}</span></td>
      <td style="max-width: 220px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">${escapeHtml(c.description)}</td>
      <td>${escapeHtml(c.evidence || "NULL")}</td>
      <td>${getStatusBadge(c.status)}</td>
      <td>${c.date_filed}</td>
      <td style="max-width: 150px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">${escapeHtml(c.remarks || "NULL")}</td>
    </tr>
  `).join("");
}

function getStatusBadge(status) {
  if (status === "Pending") return `<span class="badge badge-pending">● Pending</span>`;
  if (status === "In Progress") return `<span class="badge badge-progress">● In Progress</span>`;
  if (status === "Resolved") return `<span class="badge badge-resolved">✔ Resolved</span>`;
  return `<span class="badge">${escapeHtml(status)}</span>`;
}

window.openModal = function(id) {
  const m = document.getElementById(id);
  if (m) m.classList.add("active");
};

window.closeModal = function(id) {
  const m = document.getElementById(id);
  if (m) m.classList.remove("active");
};
