# SecureU — Free 24/7 Online Hosting Guide (For Teacher & Project Submission)

This guide provides the fastest ways to host **SecureU** on the internet for **100% free** with a permanent public HTTPS link that you can share with your teacher.

---

## 🌟 Method 1: Netlify Drop (Easiest — 15 Seconds, Zero Setup)

This is the fastest method. You don't need any command line or GitHub account.

### Steps:
1. Open your browser and go to:
   👉 **[https://app.netlify.com/drop](https://app.netlify.com/drop)**
2. Open your File Explorer and navigate to your project directory:
   ```text
   C:\Users\Hariprasad\Videos\JAVA PROJECT
   ```
3. Locate the **`web`** folder.
4. **Drag and drop the `web` folder** directly into the upload box on the Netlify Drop page.
5. In 5 seconds, Netlify will build your site and give you a live URL, for example:
   ```text
   https://secure-u-demo.netlify.app
   ```
6. *(Optional)* Click **Site configuration** ➔ **Change site name** to rename it to something custom like `https://secureu-project.netlify.app`.
7. **Done!** Send this link to your teacher.

---

## ⚡ Method 2: Vercel (Most Popular & Professional)

Vercel provides blazing-fast 24/7 cloud hosting with automatic SSL certificates.

### Option A: Via GitHub (Recommended)
1. Push your project to a free repository on [GitHub](https://github.com).
2. Go to **[https://vercel.com](https://vercel.com)** and log in with GitHub.
3. Click **Add New Project** and select your repository.
4. In the Project Configuration:
   - **Root Directory**: Select or enter `web` (or leave default since `vercel.json` is already configured).
5. Click **Deploy**.
6. Within 15 seconds, you will receive your live link:
   ```text
   https://secure-u.vercel.app
   ```

### Option B: Via Vercel CLI (From PowerShell)
In your project folder, run:
```powershell
npx.cmd vercel
```
Follow the 3 prompts (Log in ➔ Confirm project name ➔ Deploy), and it will print your live URL right in the terminal!

---

## 🐙 Method 3: GitHub Pages (100% Free via GitHub)

1. Create a repository on GitHub named `SecureU`.
2. Push your code:
   ```powershell
   git add .
   git commit -m "SecureU Student Grievance Portal"
   git branch -M main
   git remote add origin https://github.com/<your-username>/SecureU.git
   git push -u origin main
   ```
3. On GitHub, go to **Settings** ➔ **Pages**.
4. Under **Branch**, select `main` and set the folder to `/web` (or root), then click **Save**.
5. Your live link will be:
   ```text
   https://<your-username>.github.io/SecureU/
   ```

---

## 📋 What to Tell Your Teacher

When sending the link to your teacher, you can provide this message:

> **Project Name:** SecureU — Student Safety & Grievance Redressal Portal  
> **Live Demo Link:** `https://your-app-name.netlify.app`  
> 
> **Key Features & Demo Accounts:**
> - **Student Portal**: Click *"Abhinav (Student)"* on the login page to file grievances with live image preview and track status in real-time.
> - **Warden Portal**: Click *"Priya (Warden)"* to review hostel complaints, update status, view evidence attachments, and add formal remarks.
> - **HOD Portal**: Click *"Suresh (HOD)"* to view departmental analytics charts and resolution metrics.
> - **Database Inspector**: Click the *"SQL Database"* button at the top to inspect the underlying MySQL relational schema (`students`, `admin`, `complaints`).
