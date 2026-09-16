# Employee Module - Testing Guide

## 🚀 Quick Start

### 1. Start the Application

**Terminal 1 - Backend:**
```bash
# From IssueHub root directory
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm install  # First time only
npm run dev
```

**Access:** http://localhost:5173

---

## 👤 Test User Credentials

| Role | Email | Password |
|------|-------|----------|
| Employee | employee1@issuehub.com | emp123 |
| Manager | manager@issuehub.com | manager123 |
| Technician | tech1@issuehub.com | tech123 |

---

## ✅ Test Scenarios

### Scenario 1: Create New Issue ✅

**Steps:**
1. Login as Employee (`employee1@issuehub.com` / `emp123`)
2. Click "Create Issue" button (top right or quick action)
3. Fill in the form:
   - **Title**: "Air conditioner not working in Lab 3"
   - **Description**: "The AC unit stopped working. Room temperature is rising."
   - **Category**: Select "Infrastructure"
   - **Priority**: Select "High"
   - **Building**: "Main Building"
   - **Floor**: "2"
   - **Room**: "Lab 3"
4. Click "Click to upload images" and select 1-3 test images
5. Verify file previews appear below
6. Click "Create Issue"

**Expected Results:**
- ✅ Loading spinner appears during upload
- ✅ Success toast: "Issue created successfully!"
- ✅ Redirects to Issue Details page
- ✅ All information displays correctly
- ✅ Attachments visible in gallery

**Edge Cases to Test:**
- ❌ Submit empty form → Should show validation errors
- ❌ Upload 6 files → Should reject with error toast
- ❌ Upload .pdf file → Should reject (images only)
- ❌ Upload 15MB file → Should reject (max 10MB)

---

### Scenario 2: View My Issues ✅

**Steps:**
1. Navigate to "My Issues" from sidebar
2. Verify all your created issues appear
3. Test search: Type "air conditioner" in search box
4. Test filter: Select "High" priority
5. Test filter: Select "Reported" status
6. Click "Clear Filters"
7. Change "Items per page" to 5
8. Click on any issue card

**Expected Results:**
- ✅ Issues display in cards with all details
- ✅ Search filters results (debounced, 500ms)
- ✅ Status filter works
- ✅ Priority filter works
- ✅ Pagination appears if > 1 page
- ✅ Page numbers work correctly
- ✅ Clicking issue opens details page

**Test Cases:**
- Search for non-existent term → Shows "No issues found"
- Filter with no matches → Shows adjust filters message
- Create 15 issues → Test pagination (page 1, 2, etc.)

---

### Scenario 3: Dashboard View ✅

**Steps:**
1. Navigate to "Dashboard" from sidebar
2. Verify statistics cards show correct numbers
3. Scroll to "Issues by Status" chart
4. Scroll to "Issues by Priority" chart
5. Check "Recent Issues" section
6. Click any quick action card

**Expected Results:**
- ✅ Statistics cards display:
  - Total Issues
  - In Progress
  - Resolved
  - SLA Breached
- ✅ Status chart shows progress bars
- ✅ Priority chart shows progress bars
- ✅ Recent 5 issues listed
- ✅ Quick actions clickable
- ✅ Loading spinner while fetching

**Test Cases:**
- New account with 0 issues → Shows empty state
- Account with issues → Shows statistics
- Click "Create Issue" quick action → Opens form

---

### Scenario 4: Issue Details & Comments ✅

**Steps:**
1. Open any issue from My Issues
2. Verify all information displays
3. Click "Comments" tab
4. Type a comment: "I've checked the AC unit. Needs technician."
5. Click "Post Comment"
6. Verify comment appears
7. Click "History" tab
8. Review timeline events
9. Click "Details" tab
10. Click any attachment image

**Expected Results:**
- ✅ Issue details displayed:
  - Issue ID, status, priority
  - Title, description
  - Reporter, category, location
  - Assignee (if assigned)
  - Created/updated dates
  - SLA status
- ✅ Comments tab:
  - Can add new comment
  - All comments listed
  - User avatars shown
  - Relative timestamps
- ✅ History tab:
  - Timeline view
  - All actions listed
  - Before/after values shown
- ✅ Details tab:
  - Full description
  - Attachments gallery
  - Click image → Modal opens

**Test Cases:**
- Empty comment → Shows error
- Very long comment (500 chars) → Displays correctly
- Issue with no comments → Shows "Be the first to comment"
- Issue with no history → Shows "No history available"
- Click image → Modal opens with full-size image
- Press ESC key → Modal closes

---

### Scenario 5: Search & Filter Combinations ✅

**Steps:**
1. Go to "My Issues"
2. Test combinations:
   - Search "AC" + Filter Priority "High"
   - Search "Lab" + Filter Status "Reported"
   - Filter Status "In Progress" + Filter Priority "Critical"
3. Click "Clear Filters" after each test

**Expected Results:**
- ✅ Multiple filters work together (AND logic)
- ✅ Results update immediately
- ✅ Issue count updates
- ✅ Clear filters resets everything

---

### Scenario 6: Pagination & Page Size ✅

**Prerequisites:** Create 15+ issues for testing

**Steps:**
1. Go to "My Issues"
2. Set "Items per page" to 5
3. Verify only 5 issues show
4. Verify pagination controls appear
5. Click "Next" button
6. Click page number "3"
7. Click "Previous" button
8. Change "Items per page" to 20

**Expected Results:**
- ✅ Page size changes immediately
- ✅ Pagination recalculates
- ✅ Page numbers update
- ✅ Current page highlighted
- ✅ Previous/Next buttons work
- ✅ First/Last page buttons disabled appropriately

---

## 🐛 Common Issues & Fixes

### Issue: "Failed to load categories"
**Fix:** Ensure backend is running and database has seeded categories

### Issue: "Failed to upload files"
**Fix:** Check Cloudinary configuration in backend `.env` file

### Issue: 401 Unauthorized errors
**Fix:** Token expired. Logout and login again

### Issue: Images not displaying
**Fix:** Check browser console for CORS errors. Verify Cloudinary URLs

### Issue: Pagination not appearing
**Fix:** Need more than 10 issues (default page size) to see pagination

---

## 📊 Performance Checks

### Page Load Times:
- ✅ Dashboard: < 2 seconds
- ✅ My Issues: < 1 second
- ✅ Issue Details: < 1 second
- ✅ Create Issue: < 500ms

### Search Response:
- ✅ Search results: < 500ms (debounced)
- ✅ Filter change: Immediate

### File Upload:
- ✅ 1 image (2MB): ~2-3 seconds
- ✅ 5 images (10MB): ~5-8 seconds

---

## ✅ Feature Checklist

### CreateIssue Page:
- [x] Form validation
- [x] Category dropdown
- [x] Priority selection
- [x] Location inputs
- [x] File upload (1-5 files)
- [x] File preview
- [x] Size/type validation
- [x] Submit button loading state
- [x] Success redirect
- [x] Error handling

### MyIssues Page:
- [x] List all issues
- [x] Search by keyword
- [x] Filter by status
- [x] Filter by priority
- [x] Page size selector
- [x] Pagination
- [x] Issue count
- [x] Empty state
- [x] Create button
- [x] Issue cards clickable

### Dashboard Page:
- [x] 4 statistics cards
- [x] Issues by status chart
- [x] Issues by priority chart
- [x] Recent issues list
- [x] Quick actions
- [x] Loading states
- [x] Empty states

### IssueDetails Page:
- [x] Full issue display
- [x] Status/priority badges
- [x] SLA status
- [x] 3 tabs (Details/Comments/History)
- [x] Add comment
- [x] View comments
- [x] View history timeline
- [x] Attachments gallery
- [x] Image preview modal
- [x] Breadcrumb navigation

---

## 🎯 Success Criteria

**All tests passing = Employee Module 100% Complete**

✅ User can create issues with attachments
✅ User can view all their issues
✅ User can search and filter issues
✅ User can view dashboard statistics
✅ User can view issue details
✅ User can add comments
✅ User can view history
✅ User can preview attachments
✅ All loading states work
✅ All error states handled
✅ Responsive design works

---

## 📱 Browser Testing

Test in:
- ✅ Chrome (latest)
- ✅ Firefox (latest)
- ✅ Edge (latest)
- ✅ Safari (if on Mac)

Test responsive:
- ✅ Desktop (1920x1080)
- ✅ Tablet (768x1024)
- ✅ Mobile (375x667)

---

**Happy Testing! 🎉**

If you find any bugs, check the browser console for errors and verify backend logs.
