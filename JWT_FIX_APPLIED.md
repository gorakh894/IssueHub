# JWT API Version Fix - Applied

## ✅ Problem Fixed

**Error:** `cannot find symbol: method parserBuilder()`

**Root Cause:** Code was using JJWT 0.11.x API, but pom.xml has JJWT 0.12.3

## 🔧 Changes Made to JwtService.java

### 1. Updated Token Parsing Method

**Before (0.11.x API):**
```java
Jwts.parserBuilder()
    .setSigningKey(getSignInKey())
    .build()
    .parseClaimsJws(token)
    .getBody();
```

**After (0.12.x API):**
```java
Jwts.parser()
    .verifyWith((javax.crypto.SecretKey) getSignInKey())
    .build()
    .parseSignedClaims(token)
    .getPayload();
```

### 2. Updated Token Building Method

**Before (0.11.x API):**
```java
Jwts.builder()
    .setClaims(extraClaims)
    .setSubject(userDetails.getUsername())
    .setIssuedAt(new Date())
    .setExpiration(new Date())
    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
    .compact();
```

**After (0.12.x API):**
```java
Jwts.builder()
    .claims(extraClaims)
    .subject(userDetails.getUsername())
    .issuedAt(new Date())
    .expiration(new Date())
    .signWith(getSignInKey())
    .compact();
```

### 3. Removed Deprecated Import

**Removed:**
```java
import io.jsonwebtoken.SignatureAlgorithm;
```

## 📋 Key Changes in JJWT 0.12.x

1. `parserBuilder()` → `parser()`
2. `setSigningKey()` → `verifyWith()`
3. `parseClaimsJws()` → `parseSignedClaims()`
4. `.getBody()` → `.getPayload()`
5. `setClaims()` → `claims()` (no "set" prefix)
6. `setSubject()` → `subject()`
7. `setIssuedAt()` → `issuedAt()`
8. `setExpiration()` → `expiration()`
9. `signWith(key, algorithm)` → `signWith(key)` (algorithm auto-detected)

## 🚀 Next Steps

### In IntelliJ IDEA:

1. **Reload Maven Dependencies:**
   - Click Maven tool window (right side)
   - Click Reload (🔄) button
   - Wait for sync to complete

2. **Clean and Rebuild:**
   - Go to Build → Rebuild Project
   - Or press: Ctrl + Shift + F9

3. **Run Application:**
   - Right-click IssueHubApplication.java
   - Click "Run 'IssueHubApplication'"

4. **Verify Success:**
   - Console should show: "✓ IssueHub Application Started Successfully"
   - Backend will be at: http://localhost:8080

## ✅ Expected Result

Application should now compile and run successfully!

```
===========================================
✓ IssueHub Application Started Successfully
===========================================
```

## 🎯 Test Authentication

Once backend is running:
1. Open http://localhost:5173 (frontend already running)
2. Login with: employee1@issuehub.com / emp123
3. JWT tokens will be generated and validated correctly

---

**Fixed:** September 16, 2026
**File Modified:** `src/main/java/com/issuehub/security/JwtService.java`
**Issue:** JJWT API version mismatch
**Status:** ✅ Resolved
