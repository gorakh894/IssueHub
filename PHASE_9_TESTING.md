# Phase 9 Testing Guide - File Upload with Cloudinary

Complete testing guide for file upload features using Cloudinary.

## 📋 Prerequisites

### 1. Create Cloudinary Account

1. Go to https://cloudinary.com/
2. Sign up for a free account
3. After login, go to **Dashboard**
4. Note down:
   - **Cloud Name**
   - **API Key**
   - **API Secret**

### 2. Configure Environment Variables

Update your `.env` file:

```env
CLOUDINARY_CLOUD_NAME=your_cloud_name_here
CLOUDINARY_API_KEY=your_api_key_here
CLOUDINARY_API_SECRET=your_api_secret_here
```

### 3. Restart Application

```bash
mvn spring-boot:run
```

---

## 1. File Upload APIs

### 1.1 Upload Single File (General)

**Endpoint:** `POST /api/uploads/single`

**Form Data:**
- `file` (required) - The file to upload
- `folder` (optional, default: "general") - Cloudinary folder name

**Example using cURL (Windows):**
```bash
curl -X POST "http://localhost:8080/api/uploads/single?folder=test" ^
  -H "Authorization: Bearer YOUR_TOKEN" ^
  -F "file=@C:\path\to\your\image.jpg"
```

**Example using cURL (Linux/Mac):**
```bash
curl -X POST "http://localhost:8080/api/uploads/single?folder=test" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@/path/to/your/image.jpg"
```

**Response:**
```json
{
  "success": true,
  "message": "File uploaded successfully",
  "data": {
    "url": "https://res.cloudinary.com/your-cloud/image/upload/v123456/issuehub/test/filename.jpg",
    "publicId": "issuehub/test/filename",
    "format": "jpg",
    "size": 245678,
    "originalFilename": "image.jpg",
    "uploadedAt": "2026-09-16T10:30:00.000+00:00"
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

### 1.2 Upload Multiple Files

**Endpoint:** `POST /api/uploads/multiple`

**Form Data:**
- `files` (required) - Multiple files (max 5)
- `folder` (optional, default: "general")

**Example:**
```bash
curl -X POST "http://localhost:8080/api/uploads/multiple?folder=issues" ^
  -H "Authorization: Bearer YOUR_TOKEN" ^
  -F "files=@C:\images\image1.jpg" ^
  -F "files=@C:\images\image2.jpg" ^
  -F "files=@C:\images\image3.jpg"
```

**Response:**
```json
{
  "success": true,
  "message": "3 file(s) uploaded successfully",
  "data": [
    {
      "url": "https://res.cloudinary.com/.../image1.jpg",
      "publicId": "issuehub/issues/image1",
      "format": "jpg",
      "size": 245678,
      "originalFilename": "image1.jpg",
      "uploadedAt": "2026-09-16T10:30:00.000+00:00"
    },
    {
      "url": "https://res.cloudinary.com/.../image2.jpg",
      "publicId": "issuehub/issues/image2",
      "format": "jpg",
      "size": 189234,
      "originalFilename": "image2.jpg",
      "uploadedAt": "2026-09-16T10:30:01.000+00:00"
    },
    {
      "url": "https://res.cloudinary.com/.../image3.jpg",
      "publicId": "issuehub/issues/image3",
      "format": "jpg",
      "size": 312456,
      "originalFilename": "image3.jpg",
      "uploadedAt": "2026-09-16T10:30:02.000+00:00"
    }
  ]
}
```

### 1.3 Upload Issue Attachment

**Endpoint:** `POST /api/uploads/issue-attachment`

**Form Data:**
- `file` (required)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/uploads/issue-attachment" ^
  -H "Authorization: Bearer YOUR_TOKEN" ^
  -F "file=@C:\images\broken_ac.jpg"
```

**Files are automatically stored in `issuehub/issues/` folder**

### 1.4 Upload Profile Image

**Endpoint:** `POST /api/uploads/profile-image`

**Form Data:**
- `file` (required)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/uploads/profile-image" ^
  -H "Authorization: Bearer YOUR_TOKEN" ^
  -F "file=@C:\images\profile.jpg"
```

**Files are automatically stored in `issuehub/profiles/` folder**

### 1.5 Delete File

**Endpoint:** `DELETE /api/uploads`

**Query Parameters:**
- `publicId` (required) - Cloudinary public ID

**Example:**
```bash
curl -X DELETE "http://localhost:8080/api/uploads?publicId=issuehub/issues/image1" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 2. User Profile Image APIs

### 2.1 Update Profile Image

**Endpoint:** `POST /api/users/profile-image`

**Form Data:**
- `file` (required)

**Example:**
```bash
curl -X POST "http://localhost:8080/api/users/profile-image" ^
  -H "Authorization: Bearer YOUR_TOKEN" ^
  -F "file=@C:\images\my_photo.jpg"
```

**Response:**
```json
{
  "success": true,
  "message": "Profile image updated successfully",
  "data": {
    "id": "user123",
    "name": "John Doe",
    "email": "john@example.com",
    "profileImage": "https://res.cloudinary.com/.../profile.jpg",
    ...
  }
}
```

**Note:** This endpoint automatically:
1. Deletes the old profile image from Cloudinary (if exists)
2. Uploads the new image
3. Updates the user's profile

### 2.2 Delete Profile Image

**Endpoint:** `DELETE /api/users/profile-image`

**Example:**
```bash
curl -X DELETE "http://localhost:8080/api/users/profile-image" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 3. User Management APIs (New)

### 3.1 Get All Users

**Endpoint:** `GET /api/users`

**Roles:** MANAGER, ADMIN

**Example:**
```bash
curl -X GET "http://localhost:8080/api/users" ^
  -H "Authorization: Bearer MANAGER_TOKEN"
```

### 3.2 Get Users by Role

**Endpoint:** `GET /api/users/role/{role}`

**Roles:** MANAGER, ADMIN

**Example:**
```bash
curl -X GET "http://localhost:8080/api/users/role/TECHNICIAN" ^
  -H "Authorization: Bearer MANAGER_TOKEN"
```

### 3.3 Get All Technicians

**Endpoint:** `GET /api/users/technicians`

**Query Parameters:**
- `activeOnly` (optional, default: false)

**Roles:** MANAGER, ADMIN

**Example:**
```bash
# Get all technicians
curl -X GET "http://localhost:8080/api/users/technicians" ^
  -H "Authorization: Bearer MANAGER_TOKEN"

# Get only active technicians
curl -X GET "http://localhost:8080/api/users/technicians?activeOnly=true" ^
  -H "Authorization: Bearer MANAGER_TOKEN"
```

### 3.4 Toggle User Status

**Endpoint:** `PATCH /api/users/{id}/toggle-status`

**Roles:** ADMIN only

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/users/USER_ID/toggle-status" ^
  -H "Authorization: Bearer ADMIN_TOKEN"
```

---

## 4. Complete End-to-End Testing Workflow

### Step 1: Login as Employee

```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"employee1@issuehub.com\",\"password\":\"emp123\"}"
```

Save the token as `EMPLOYEE_TOKEN`

### Step 2: Update Profile Image

```bash
curl -X POST "http://localhost:8080/api/users/profile-image" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -F "file=@C:\images\profile.jpg"
```

Save the `profileImage` URL from response.

### Step 3: Upload Issue Attachments

```bash
# Upload multiple images for an issue
curl -X POST "http://localhost:8080/api/uploads/multiple?folder=issues" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -F "files=@C:\images\ac_broken1.jpg" ^
  -F "files=@C:\images\ac_broken2.jpg"
```

Save the URLs from the response.

### Step 4: Create Issue with Attachments

```bash
curl -X POST http://localhost:8080/api/issues ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"AC not working\",\"description\":\"Air conditioner stopped working\",\"categoryId\":\"CATEGORY_ID\",\"location\":{\"building\":\"Main\",\"floor\":\"2\",\"room\":\"301\"},\"priority\":\"HIGH\",\"attachments\":[\"https://res.cloudinary.com/.../ac_broken1.jpg\",\"https://res.cloudinary.com/.../ac_broken2.jpg\"]}"
```

### Step 5: Verify Issue with Attachments

```bash
curl -X GET "http://localhost:8080/api/issues/ISSUE_ID" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"
```

Check that `attachments` array contains your uploaded image URLs.

### Step 6: View Updated Profile

```bash
curl -X GET "http://localhost:8080/api/auth/me" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"
```

Check that `profileImage` contains your uploaded image URL.

---

## 5. File Upload Constraints

### Supported File Types:
- JPEG/JPG
- PNG
- WEBP
- GIF

### File Size Limit:
- **Maximum:** 10 MB per file

### Multiple Upload Limit:
- **Maximum:** 5 files at once

### Validation Errors:

**Empty File:**
```json
{
  "success": false,
  "message": "File is empty"
}
```

**File Too Large:**
```json
{
  "success": false,
  "message": "File size exceeds maximum limit of 10 MB"
}
```

**Invalid File Type:**
```json
{
  "success": false,
  "message": "Invalid file type. Allowed types: JPEG, JPG, PNG, WEBP, GIF"
}
```

**Too Many Files:**
```json
{
  "success": false,
  "message": "Maximum 5 files can be uploaded at once"
}
```

---

## 6. Cloudinary Folder Structure

Files are organized in Cloudinary as follows:

```
issuehub/
├── issues/          # Issue attachments
├── profiles/        # User profile images
├── general/         # General uploads
└── test/           # Test uploads
```

You can view all uploaded files in your Cloudinary Dashboard:
- Go to https://cloudinary.com/console/media_library
- Navigate to `issuehub` folder

---

## 7. Testing with Postman

### Setup:

1. Create a new Collection "IssueHub File Upload"
2. Add `Authorization` header with `Bearer {{token}}`
3. Set `token` variable after login

### Upload Single File:

1. **Method:** POST
2. **URL:** `http://localhost:8080/api/uploads/single?folder=test`
3. **Headers:** `Authorization: Bearer {{token}}`
4. **Body:** 
   - Select `form-data`
   - Key: `file`, Type: `File`
   - Select a file from your computer

### Upload Multiple Files:

1. **Method:** POST
2. **URL:** `http://localhost:8080/api/uploads/multiple?folder=issues`
3. **Headers:** `Authorization: Bearer {{token}}`
4. **Body:**
   - Select `form-data`
   - Key: `files`, Type: `File` (add multiple entries)
   - Select files for each entry

---

## 8. Common Issues & Solutions

### Issue 1: "Cloudinary credentials not configured"

**Solution:** Check your `.env` file:
```env
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

Restart the application after updating.

### Issue 2: "Failed to upload file: 401 Unauthorized"

**Solution:** Your Cloudinary API credentials are incorrect. Double-check:
- Cloud Name
- API Key
- API Secret

### Issue 3: "File too large"

**Solution:** Reduce file size to under 10MB or compress the image.

### Issue 4: "Invalid file type"

**Solution:** Only JPEG, JPG, PNG, WEBP, and GIF files are supported.

---

## 9. Verification Checklist

- [ ] Cloudinary account created
- [ ] Environment variables configured
- [ ] Application restarted
- [ ] Single file upload works
- [ ] Multiple file upload works
- [ ] Issue attachment upload works
- [ ] Profile image upload works
- [ ] Profile image update works
- [ ] Profile image delete works
- [ ] File delete works
- [ ] Files visible in Cloudinary dashboard
- [ ] Issue created with attachments
- [ ] Profile displays uploaded image

---

## 10. Security Features

✅ **File Type Validation** - Only allowed image types
✅ **File Size Validation** - Maximum 10MB
✅ **Authentication Required** - JWT token required
✅ **Secure Upload** - HTTPS enforced
✅ **Auto Image Optimization** - Cloudinary auto-optimizes images
✅ **CDN Delivery** - Fast delivery via Cloudinary CDN

---

Happy Testing! 📸🚀
