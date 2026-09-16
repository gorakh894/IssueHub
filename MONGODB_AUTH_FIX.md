# MongoDB Authentication Error - How to Fix

## ❌ Current Error

```
Command failed with error 8000 (AtlasError): 'bad auth : authentication failed'
```

**Root Cause:** The password in your MongoDB connection string is incorrect.

## 🔧 How to Fix

### Option 1: Update Password in MongoDB Atlas (Recommended)

1. **Go to MongoDB Atlas**: https://cloud.mongodb.com
2. **Login** to your account
3. **Select your cluster** (cluster0)
4. **Click "Database Access"** (left sidebar)
5. **Find user "Gorakh"**
6. **Click "Edit"** (pencil icon)
7. **Click "Edit Password"**
8. **Set new password**: `Gorakh2307` (or your desired password)
9. **Click "Update User"**
10. **Wait 1-2 minutes** for changes to propagate

### Option 2: Get Correct Connection String

1. **Go to MongoDB Atlas**: https://cloud.mongodb.com
2. **Click "Connect"** on your cluster
3. **Choose "Connect your application"**
4. **Copy the connection string**
5. **Replace `<password>` with your actual password**
6. **Update `.env` file**

## 📝 Update .env File

Open `C:\Users\Gorakh Hembade\Downloads\IssueHub\.env` and update:

```env
# Replace with your correct MongoDB Atlas connection string
MONGODB_URI=mongodb+srv://Gorakh:<YOUR_ACTUAL_PASSWORD>@cluster0.fkhkg6j.mongodb.net/?appName=Cluster0
```

**Important:** Replace `<YOUR_ACTUAL_PASSWORD>` with your real MongoDB Atlas password!

## ✅ Quick Test

After updating the password:

1. **Restart the application** in IntelliJ
2. Look for this success message:
```
Monitor thread successfully connected to server
✓ IssueHub Application Started Successfully
```

## 🔐 Common Issues

### Issue 1: Special Characters in Password
If your password has special characters (`!@#$%^&*`), you need to URL-encode them:

| Character | Encoded |
|-----------|---------|
| `@` | `%40` |
| `#` | `%23` |
| `$` | `%24` |
| `%` | `%25` |
| `&` | `%26` |
| `+` | `%2B` |
| `/` | `%2F` |

**Example:**
- Password: `Pass@123#`
- Encoded: `Pass%40123%23`
- Connection: `mongodb+srv://Gorakh:Pass%40123%23@cluster0...`

### Issue 2: Wrong Username
Make sure the username is exactly as created in MongoDB Atlas (case-sensitive).

### Issue 3: IP Whitelist
Ensure your IP address is whitelisted in MongoDB Atlas:
1. Go to "Network Access" in MongoDB Atlas
2. Add your current IP or use `0.0.0.0/0` (allow from anywhere) for testing

## 🎯 Alternative: Create New Database User

If you can't remember the password:

1. **MongoDB Atlas** → **Database Access**
2. **Click "Add New Database User"**
3. **Username**: `issuehub_admin`
4. **Password**: `IssueHub@2026` (or your choice)
5. **Database User Privileges**: Atlas admin or Read/Write to any database
6. **Add User**
7. **Update `.env`**:
```env
MONGODB_URI=mongodb+srv://issuehub_admin:IssueHub@2026@cluster0.fkhkg6j.mongodb.net/?appName=Cluster0
```

## 📋 Verification Steps

After fixing the password:

1. ✅ MongoDB Atlas connection string updated in `.env`
2. ✅ Password is correct (or new user created)
3. ✅ IP address is whitelisted
4. ✅ Restart Spring Boot application
5. ✅ Check console for "Monitor thread successfully connected"

---

**Current Connection String:**
```
mongodb+srv://Gorakh:Gorakh2307@cluster0.fkhkg6j.mongodb.net/?appName=Cluster0
```

**Fix:** Verify `Gorakh2307` is the correct password in MongoDB Atlas!

---

**Status**: Waiting for correct MongoDB credentials
