---
title: "Firebase Cloud Messaging (FCM) for Android Developers"
date: 2026-06-23
slug: "android-fcm-complete-guide"
tags: [ "Notes", "Android", "Firebase", "FCM", "Push Notifications", "Architecture" ]
summary: "A deep dive into Firebase Cloud Messaging, explaining the difference between notification and data messages, token management, and production-ready implementation strategies."
categories: Notes
readTime: 12
---

From a hiring and architecture standpoint, a **mid-level Android developer is not expected to just
“send notifications”** — they are expected to understand the full FCM lifecycle, delivery behavior,
and production constraints.

Think of it like this:

> Junior = can integrate FCM
> Mid-level = can design reliable notification systems

Below is the **minimum practical FCM knowledge baseline** companies expect from a mid-level Android
developer.

---

# 🎯 1. Core FCM Fundamentals (Non-Negotiable)

You must clearly understand:

### ✔️ What FCM actually is

* Google’s push messaging infrastructure
* Backend → FCM → Device pipeline
* Works even when app is killed

### ✔️ FCM Architecture Flow

```
Backend / Firebase Console
        ↓
    FCM Server
        ↓
 Device (Token based)
        ↓
 FirebaseMessagingService
```

If you can’t explain this flow in interviews → red flag at mid-level.

---

# 📩 2. Types of FCM Messages (VERY IMPORTANT)

This is one of the most asked interview topics.

## A. Notification Message (Console friendly)

* Contains: title + body
* Auto-handled by system (background)
* Limited customization

Use case:

* Marketing pushes
* Announcements

---

## B. Data Message (Production preferred)

* Custom key-value payload
* Always delivered to `onMessageReceived()`
* Full control over UI & logic

Example payload:

```json
{
  "data": {
    "type": "chat",
    "userId": "123"
  }
}
```

Real apps (WhatsApp, Uber) mostly use **data messages**, not pure notification messages.

---

# 🧠 3. Token Management (Critical Mid-Level Skill)

You must know:

### ✔️ What is FCM Token

* Unique per app install + device
* Used for 1-to-1 targeting

### ✔️ Token lifecycle

* Can refresh anytime
* Must handle in:

```kotlin
override fun onNewToken(token: String)
```

### ✔️ Production best practice

Send token to backend:

```kotlin
onNewToken(token) → API → Save in server DB
```

---

# 🔔 4. Foreground vs Background vs Killed Behavior

Mid-level dev MUST know this table:

| App State  | Notification Payload      | Data Payload                    |
|------------|---------------------------|---------------------------------|
| Foreground | onMessageReceived()       | onMessageReceived()             |
| Background | System shows notification | onMessageReceived()             |
| Killed     | System shows notification | Delivered (depends on priority) |

This is a **frequent interview trap**.

---

# ⚙️ 5. FirebaseMessagingService Implementation (Mandatory)

You should confidently implement:

```kotlin
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // Send to backend
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle notification + data payload
    }
}
```

And properly register in manifest (with correct package path).

---

# 📦 6. Notification Channel (Android 8+ Requirement)

Mid-level dev must know:

* Notifications won’t show without channel (API 26+)
* Channel importance affects visibility

Example:

```kotlin
NotificationChannel(
    "fcm_channel",
    "FCM Notifications",
    NotificationManager.IMPORTANCE_HIGH
)
```

If you skip this → notifications silently fail on modern Android.

---

# 🎯 7. Targeting Strategies (Real Product Knowledge)

You should know when to use:

| Target Type  | Use Case                     |
|--------------|------------------------------|
| Token        | Personal alerts (OTP, chat)  |
| Topic        | Group users (sports, offers) |
| Condition    | Advanced segmentation        |
| User Segment | Marketing campaigns          |

Example:

```kotlin
FirebaseMessaging.getInstance().subscribeToTopic("premium_users")
```

---

# 🔒 8. Android 13+ Notification Permission (NEW REQUIREMENT)

Mid-level dev must handle:

```xml
android.permission.POST_NOTIFICATIONS
```

and runtime request:

```kotlin
requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
```

Many apps fail in production because of this.

---

# 🧪 9. Debugging & Troubleshooting (Highly Expected Skill)

You should be able to debug:

* Token not generated
* Notification not received
* Topic not working
* Background delivery issues

Key Logcat tags to monitor:

```
FCM
FirebaseMessaging
onMessageReceived
```

---

# 🌐 10. Backend Integration Awareness (Even if not backend dev)

You don’t need to build backend, but must understand:

* FCM HTTP v1 API
* Server sends push using token/topic
* JSON payload structure

Basic request concept:

```
POST https://fcm.googleapis.com/v1/projects/{project-id}/messages:send
```

---

# 🔥 11. Real-World Production Best Practices (Mid-Level Expectation)

You should know:

* Avoid spam notifications (rate control)
* Handle deep links from notification
* Support image notifications (BigPictureStyle)
* Use data payload for navigation logic
* Handle notification click actions

---

# 📊 12. Security Awareness (Often Ignored but Important)

Mid-level dev should understand:

* Never expose server key in app
* Token is sensitive (user targeting)
* Use backend to send pushes (not client)

---

# 🎓 Interview Questions You MUST Be Able to Answer

If you can answer these, you are mid-level ready:

* Difference between notification vs data payload?
* Why `onMessageReceived()` not called sometimes?
* How does FCM work when app is killed?
* What happens when token refreshes?
* Topic vs token messaging?
* Why notification not showing on Android 13?

---

# 🧩 BONUS (What Makes You STRONG Mid-Level)

Not mandatory but highly valued:

* Data-only push handling
* Deep link navigation from notification
* WorkManager + FCM sync
* Analytics tracking (open rate)
* Silent background updates

---

# 🚀 Your Current Level (Based on This Chat)

You already know:

* Token generation ✔
* Topic subscription ✔
* MessagingService ✔
* Console campaigns ✔
* Notification channel ✔
* Image notifications ✔

This is already **above junior level** in FCM.

---

# Final Strategic Question (to tailor your checklist)

Are you preparing FCM mainly for:

* 📱 Android interviews
* 🏢 Production app development
* 💬 Chat/real-time app features

(I’ll give a focused “must-know only” list based on your goal.)

---
Short answer:
❌ No — your current code does **NOT automatically send the new token back to your server**.
It only logs it locally.

And this is a very important mid-level FCM concept.

---

# 🧠 How FCM Token Flow Actually Works (Real Architecture)

Many developers misunderstand this part.

### Correct lifecycle:

```
App installed →
FCM generates token →
onNewToken() triggered →
YOU must send token to your backend →
Backend stores token →
Backend uses token to send push later
```

FCM will **never send the token to your server automatically**.
That responsibility is 100% on your app code.

---

# 🔍 Review of Your Current Code (Professional Audit)

Your implementation:

```kotlin
override fun onNewToken(token: String) {
    super.onNewToken(token)
    Log.d(TAG, "New FCM Token: $token")
    // TODO: Send token to your app server for registration
}
```

### What it currently does:

* ✔ Receives refreshed token
* ✔ Logs token
* ✔ Subscribes to topic
* ❌ DOES NOT send token to backend
* ❌ DOES NOT persist token locally
* ❌ No retry strategy

So in production, your backend would **never know the new token**.

---

# 🚨 Critical Production Risk (If You Don’t Send Token)

If token refresh happens and you don’t update backend:

* Server keeps old token
* Push notifications start failing silently
* Users stop receiving notifications
* Very common real-world bug

This happens frequently when:

* App reinstalled
* User clears app data
* Token rotated by FCM
* Device restored from backup

---

# 🎯 When Does `onNewToken()` Get Triggered?

Mid-level dev must know this:

| Scenario                            | onNewToken Called? |
|-------------------------------------|--------------------|
| First app install                   | ✅ YES              |
| App data cleared                    | ✅ YES              |
| Token security rotation (by Google) | ✅ YES              |
| App reinstalled                     | ✅ YES              |
| Every app launch                    | ❌ NO               |

It is **NOT called regularly**. Only when token changes.

---

# 🏗️ What Your Code Is Missing (Minimum Production Fix)

You must send token to server inside `onNewToken()`.

Example (real-world pattern):

```kotlin
override fun onNewToken(token: String) {
    super.onNewToken(token)

    Log.d(TAG, "New FCM Token: $token")

    // 1. Save locally (optional but recommended)
    saveTokenLocally(token)

    // 2. Send to backend API (CRITICAL)
    sendTokenToServer(token)

    // 3. Re-subscribe to topics (good practice)
    FirebaseMessaging.getInstance().subscribeToTopic("test")
}
```

---

# 📡 Example: Proper Server Sync Method (Clean Architecture Style)

```kotlin
private fun sendTokenToServer(token: String) {
    // Example: Retrofit API call
    Log.d(TAG, "Sending token to backend: $token")

    // In real app:
    // api.registerFcmToken(token)
}
```

In production backend (Spring Boot / Node):

```
POST /api/device/register-token
{
   "userId": "123",
   "fcmToken": "abcxyz..."
}
```

---

# 🧩 Another Subtle Gap in Your Implementation (Important)

You only handle token inside `onNewToken()`.

But what if:

* App launches
* Token already exists
* `onNewToken()` NOT called

Then backend still won’t have the token.

---

# 🔥 Mid-Level Best Practice (Industry Standard)

Fetch token ALSO on app launch (you already do this in MainActivity — good 👍)

But you should:

* Compare with stored token
* Send to backend if changed

Example:

```kotlin
FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
    val savedToken = getSavedToken()

    if (savedToken != token) {
        sendTokenToServer(token)
        saveTokenLocally(token)
    }
}
```

This prevents token desync issues.

---

# 📊 Topic Subscription Placement (Your Code Review)

You placed:

```kotlin
FirebaseMessaging.getInstance().subscribeToTopic("test")
```

inside `onNewToken()`.

### Is this correct?

✅ Yes — this is actually GOOD practice.

Because:

* When token refreshes
* Old topic subscription may be lost
* Re-subscribing ensures consistency

Senior Android devs do exactly this.

---

# 🛡️ Enterprise-Level Improvements (What Strong Mid Devs Add)

Your current level: Functional
Production-ready level requires:

1. Token caching (SharedPreferences/DataStore)
2. Backend sync
3. Retry if API fails
4. Topic re-subscription strategy
5. Analytics logging (optional)

---

# 🧪 Final Verdict on Your Code (Professional Rating)

| Area                  | Status               |
|-----------------------|----------------------|
| onNewToken override   | ✅ Correct            |
| Topic resubscription  | ✅ Good               |
| Notification handling | ✅ Solid              |
| Backend token sync    | ❌ Missing (CRITICAL) |
| Token persistence     | ❌ Missing            |
| Retry mechanism       | ❌ Missing            |

Overall:

> 7/10 — Good for learning, not production-safe yet.

---

# 🎯 One Advanced Interview Question (Based on Your Code)

If interviewer asks:

> “What happens if FCM token refreshes and you don’t update backend?”

Correct mid-level answer:

> The server will continue using the stale token, causing push delivery failures until the app sends
> the updated token via onNewToken() or app launch token fetch.

---

# Final Strategic Question (to tailor next upgrade)

Are you planning to use FCM with:

* A custom backend (Spring Boot / Node)?
* Or only Firebase Console campaigns?

---

Excellent question — and this is exactly the point where many Android developers get confused about
FCM architecture.

Let’s clarify this in a clean, production-grade way.

---

# 🧠 Key Concept: Firebase Console ≠ Your Backend

Firebase Console is **not your app backend**.
It is only a **manual campaign tool** for testing and marketing pushes.

It does NOT:

* Store your users’ tokens for app logic
* Automatically manage per-user targeting
* Act like your business backend
* Sync user-token mapping for your app

So you do **NOT “send token to Firebase Console.”**

That is not a real production flow.

---

# 🎯 If You DON’T Have a Backend (Your Current Situation)

Then your current setup is actually **completely fine**.

Because you are using:

* Firebase Console
* Topic messaging (`test`)
* Manual campaigns

In this model:

```
App → Subscribes to topic
Firebase Console → Sends to topic
Device receives notification
```

No backend required.

And importantly:

> You do NOT need to send token anywhere right now.

---

# 📦 When Do You NEED a Backend for FCM?

You only need a backend when your app requires:

* Personalized notifications (per user)
* Real-time events (orders, chat, etc.)
* Secure logic (server-triggered pushes)
* Automation (not manual campaigns)

---

# 🏗️ Real-World Production Architecture (Industry Standard)

Example: Food Delivery App (Swiggy/Zomato style)

```
User places order →
Spring Boot Backend →
Backend calls FCM API with user’s token →
FCM delivers notification to device
```

Here backend is mandatory.

---

# 🔥 Two Valid FCM Models (You Must Understand as Mid-Level Dev)

## 🟢 Model 1 — No Backend (Console + Topics) ✔ (Your Current Model)

You:

* Subscribe users to topics
* Send campaigns via Firebase Console
* No server needed

Good for:

* Learning
* Marketing notifications
* Broadcast alerts
* Small apps
* MVP apps

Your current flow:

```kotlin
subscribeToTopic("test")
```

Console → Topic → All devices receive.

This is perfectly correct.

---

## 🔴 Model 2 — With Backend (Production Apps)

You build:

* Spring Boot / Node.js / Firebase Functions backend

Then flow becomes:

```
App generates token →
Send token to YOUR backend →
Backend stores token in DB →
Backend sends push via FCM API
```

This enables:

* 1-to-1 notifications
* Order updates
* Chat messages
* OTP alerts

---

# ❗ Direct Answer to Your Question

> “Which backend should I send token to? Firebase console or my own backend?”

Correct answer:

> You should send token ONLY to your own backend (if you have one).
> NOT to Firebase Console.

Firebase Console does NOT accept token registration APIs from apps.

---

# 🧪 In Your Current Learning Project (Best Practice)

Since you:

* Are testing FCM
* Using topics
* Sending campaigns manually

You can safely:

```kotlin
// Just log token (enough for now)
override fun onNewToken(token: String) {
    Log.d(TAG, "Token: $token")
}
```

No backend needed at all.

---

# 💼 What Companies Expect (Real Job Scenario)

If you join a company, usually they already have:

* Backend team
* Notification service
* Token registration API

You will integrate like this:

```kotlin
override fun onNewToken(token: String) {
    api.registerFcmToken(token) // Company backend endpoint
}
```

Example backend stack:

* Spring Boot (very common)
* Node.js (very common)
* Firebase Cloud Functions
* Django / .NET (enterprise)

---

# 🧩 When Firebase Itself Acts Like Backend (Special Case)

If you use:

* Firebase Cloud Functions
* Firebase Firestore
* Firebase Auth

Then you can avoid building a separate Spring Boot backend.

This is called:

> Serverless FCM Architecture

Used in startups & MVP apps.

---

# 📊 Recommendation Based on YOUR Profile (Android Dev)

Given you already know:

* Spring Boot ✔
* Full-stack apps ✔
* Firebase ✔

Best learning upgrade:

> Use Spring Boot + FCM HTTP v1 API
> This is considered **mid-to-senior level architecture**.

---

# 🎯 Simple Decision Guide

| App Type                   | Need Backend? |
|----------------------------|---------------|
| Learning / Demo app        | ❌ No          |
| Marketing campaigns        | ❌ No          |
| Chat app                   | ✅ Yes         |
| E-commerce app             | ✅ Yes         |
| OTP / Alerts               | ✅ Yes         |
| Personalized notifications | ✅ Yes         |

---

# 🚀 Final Insight (Very Important for Interviews)

If interviewer asks:

> “Do we always need a backend for FCM?”

Strong mid-level answer:

> No. For broadcast or topic-based notifications, Firebase Console is sufficient. But for
> personalized, event-driven, and automated notifications, a backend is required to manage tokens and
> trigger FCM API calls securely.

---

# Next Power Upgrade Question (This will level you up fast)

Do you want me to show:
1️⃣ How to send FCM using Postman (without backend)
2️⃣ Or how to integrate FCM with Spring Boot backend (production architecture)
