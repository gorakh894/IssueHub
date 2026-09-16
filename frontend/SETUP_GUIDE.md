# IssueHub Frontend - Quick Setup Guide

## 🚀 Get Started in 3 Minutes

### Step 1: Install Dependencies

```bash
cd frontend
npm install
```

### Step 2: Create Environment File

```bash
# Windows
copy .env.example .env

# Linux/Mac
cp .env.example .env
```

The `.env` file content:
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=IssueHub
VITE_APP_VERSION=1.0.0
```

### Step 3: Start Development Server

```bash
npm run dev
```

Application will run on: **http://localhost:5173**

---

## 🧪 Test the Application

### Make Sure Backend is Running

```bash
# In a separate terminal, navigate to backend
cd ..
mvn spring-boot:run
```

Backend should be running on: **http://localhost:8080**

### Login with Pre-seeded Accounts

The login page has **Quick Login** buttons for testing:

| Role | Email | Password |
|------|-------|----------|
| **Employee** | employee1@issuehub.com | emp123 |
| **Manager** | manager@issuehub.com | manager123 |
| **Technician** | tech1@issuehub.com | tech123 |
| **Admin** | admin@issuehub.com | admin123 |

---

## ✅ What's Working Now

### Core Features ✅
- [x] Login page with authentication
- [x] Register page
- [x] Role-based routing
- [x] Protected routes
- [x] JWT token management
- [x] Navbar with user menu
- [x] Sidebar navigation
- [x] Logout functionality
- [x] Profile page
- [x] Responsive layout

### Dashboard Placeholders ✅
- [x] Employee Dashboard
- [x] Manager Dashboard
- [x] Technician Dashboard
- [x] 404 Not Found page

### What You Can Test ✅
1. ✅ Login with different roles
2. ✅ See role-specific sidebar menus
3. ✅ Navigate between pages
4. ✅ View profile information
5. ✅ Logout and login again

---

## 🎯 Next Steps

### Priority 2 - Employee Features

To complete the employee functionality, you'll need to create:

1. **Services** (API calls):
   - `src/services/issueService.js`
   - `src/services/categoryService.js`
   - `src/services/fileService.js`

2. **Enhanced Employee Pages**:
   - Employee Dashboard with statistics
   - Create Issue form with file upload
   - My Issues list with filters
   - Issue Details with comments

3. **Utility Components**:
   - StatusBadge component
   - PriorityBadge component
   - LoadingSpinner component
   - IssueCard component

4. **Utils**:
   - Constants (statuses, priorities)
   - Helper functions

---

## 🐛 Troubleshooting

### Issue: npm install fails

**Solution:**
```bash
# Clear npm cache
npm cache clean --force

# Try again
npm install
```

### Issue: Port 5173 is already in use

**Solution:**
```bash
# Kill the process or change port in vite.config.js
# Change port to 3000:
server: {
  port: 3000,
}
```

### Issue: Cannot connect to backend

**Solution:**
1. Check backend is running on http://localhost:8080
2. Check `VITE_API_BASE_URL` in `.env`
3. Restart frontend: `npm run dev`

### Issue: CORS error

**Solution:**
Check backend `.env` file:
```env
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
```

### Issue: Login button doesn't work

**Solution:**
1. Open browser console (F12)
2. Check for error messages
3. Verify backend is running
4. Check network tab for API calls

---

## 📱 Responsive Design

The app is fully responsive:
- Mobile: 320px+
- Tablet: 768px+
- Desktop: 1024px+

Test on different screen sizes!

---

## 🎨 UI Components Available

### Buttons
```jsx
<button className="btn btn-primary">Primary</button>
<button className="btn btn-secondary">Secondary</button>
<button className="btn btn-danger">Danger</button>
```

### Badges
```jsx
<span className="badge badge-primary">Badge</span>
```

### Cards
```jsx
<div className="card">
  <h3>Card Title</h3>
  <p>Card content</p>
</div>
```

### Inputs
```jsx
<input type="text" className="input" placeholder="Text" />
```

---

## 🔄 Development Workflow

1. **Make changes** to any `.jsx` file
2. **Save** the file
3. **Hot reload** updates automatically
4. **Check browser** for updates

No need to restart the server for code changes!

---

## 🚢 Build for Production

When ready to deploy:

```bash
npm run build
```

Output directory: `dist/`

---

## 📚 Documentation

- **Frontend README**: `frontend/README.md`
- **Implementation Guide**: `FRONTEND_IMPLEMENTATION_GUIDE.md`
- **Backend API Docs**: `../API_TESTING.md`

---

## ✨ Features to Implement Next

Want to continue building? Here's the recommended order:

1. **Employee Create Issue** - Form with file upload
2. **Employee My Issues** - List with filters
3. **Manager Dashboard** - Charts and analytics
4. **Manager All Issues** - Table with assign functionality
5. **Technician Assigned Issues** - Update status
6. **Notifications** - Real-time notifications
7. **Categories Management** - CRUD operations

---

**🎉 Congratulations!** Your frontend is now running and connected to the backend!

Try logging in with different roles and exploring the navigation.

Need help? Check the console for errors or refer to the documentation.
