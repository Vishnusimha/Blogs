---
title: "DAY 1 Android Manifest File Guide"
date: 2026-06-12
slug: "android-manifest-file-guide"
tags: [ "Notes", "Android", "AndroidManifest", "Permissions", "Intent Filters", "Deep Links", "Security" ]
summary: "Personal study notes explaining AndroidManifest.xml responsibilities, permissions, intent filters, deep links, exported components, manifest security, and key application-level flags."
categories: Notes
readTime: 10
---

# AndroidManifest.xml (Senior Android Interview Notes)

## Table of Contents

- [What is AndroidManifest.xml?](#what-is-androidmanifestxml)
- [Core Responsibilities of the Manifest](#core-responsibilities-of-the-manifest)
    - [App identity & entry points](#app-identity--entry-points)
    - [Declare components](#declare-components)
    - [Permissions](#permissions)
    - [Intent Filters & Deep links](#intent-filters--deep-links)
- [Manifest Structure (High Level)](#manifest-structure-high-level)
- [Permissions](#permissions-1)
    - [`uses-permission` (Requesting OS permissions)](#uses-permission-requesting-os-permissions)
    - [Permission Types (Interview must-know)](#permission-types-interview-must-know)
    - [Runtime Permission Flow (Senior expectation)](#runtime-permission-flow-senior-expectation)
    - [`uses-feature` vs `uses-permission` (Common confusion)](#uses-feature-vs-uses-permission-common-confusion)
- [Intent Filters (Deep links, routing, implicit intents)](#intent-filters-deep-links-routing-implicit-intents)
    - [What is an intent-filter?](#what-is-an-intent-filter)
    - [Intent-filter Matching Rules (Senior detail)](#intent-filter-matching-rules-senior-detail)
    - [Typical Launcher Activity Example](#typical-launcher-activity-example)
    - [Deep Links (App Links) — Senior must know](#deep-links-app-links--senior-must-know)
    - [Explicit vs Implicit intents (Manifest impact)](#explicit-vs-implicit-intents-manifest-impact)
- [`android:exported` + Security (Android 12+)](#androidexported--security-android-12)
    - [What is `android:exported`?](#what-is-androidexported)
    - [Why it became mandatory (Android 12+)](#why-it-became-mandatory-android-12)
    - [Exported Examples](#exported-examples)
    - [Manifest Security Best Practices (Senior)](#manifest-security-best-practices-senior)
- [Component Configuration (Senior topics)](#component-configuration-senior-topics)
    - [Task & launch behavior](#task--launch-behavior)
    - [Process management](#process-management)
    - [`configChanges` (Avoid activity recreation)](#configchanges-avoid-activity-recreation)
    - [Permissions per component](#permissions-per-component)
- [Application-level Manifest Flags Worth Knowing](#application-level-manifest-flags-worth-knowing)
    - [`android:allowBackup`](#androidallowbackup)
    - [Cleartext traffic](#cleartext-traffic)
    - [Network security config](#network-security-config)
- [Interview Rapid Fire (Quick Q&A)](#interview-rapid-fire-quick-qa)
    - [Can app run without manifest?](#can-app-run-without-manifest)
    - [Where does permission enforcement happen?](#where-does-permission-enforcement-happen)
    - [Why is exported risky?](#why-is-exported-risky)
    - [How do you protect exported components?](#how-do-you-protect-exported-components)
    - [What happens if exported missing on Android 12+?](#what-happens-if-exported-missing-on-android-12)
- [Best Final Senior Summary (15 seconds)](#best-final-senior-summary-15-seconds)

## What is AndroidManifest.xml?
`AndroidManifest.xml` is the **app’s contract with the Android OS**. It tells Android:

- what components the app has (**Activities / Services / Receivers / Providers**)
- what the app is allowed to do (**permissions / features**)
- how the OS can launch/route into the app (**intent-filters / deep links**)
- process info, metadata, SDK behavior, app configuration

✅ Senior one-liner:
> “Manifest is the declarative configuration for components, permissions, app identity, and OS integration.”

---

## Core Responsibilities of the Manifest

### App identity & entry points
- package name / namespace
- launcher activity
- app theme/icon/label
- application level config

### Declare components
- `<activity>`
- `<service>`
- `<receiver>`
- `<provider>`

### Permissions
- request system permissions
- define custom permissions
- restrict component access _(android:exported="false", other apps ❌ cannot launch this activity. ✅ Preventing other apps from launching/using your components unless you explicitly allow it.)_

### Intent Filters & Deep links
- URL handling
- app-to-app interaction
- broadcast matching

---

## Manifest Structure (High Level)

```xml
<manifest package="com.example.app">

    <uses-sdk ... />

    <uses-permission android:name="..." />
    <uses-feature android:name="..." />

    <application
        android:name="..."
        android:allowBackup="..."
        android:networkSecurityConfig="..."
        android:usesCleartextTraffic="...">

        <activity ...>
            <intent-filter>...</intent-filter>
        </activity>

        <service ... />
        <receiver ... />
        <provider ... />

    </application>
</manifest>
```

---

## Permissions

### `uses-permission` (Requesting OS permissions)
Declares permissions your app *wants to use*.

Example:
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

From Android 6+:
✅ **Manifest request is not enough** for dangerous permissions — you must also request them at runtime.

---

### Permission Types (Interview must-know)

#### Normal permissions
- granted automatically at install
- low risk  
  Example: `INTERNET`, `ACCESS_NETWORK_STATE`

#### Dangerous permissions
- need **runtime** user approval  
  Example: location, camera, mic, contacts, storage

#### Signature permissions
- granted only if apps share the same signing certificate
- used for enterprise/internal APIs

Example:
```xml
<permission
    android:name="com.example.permission.READ_DATA"
    android:protectionLevel="signature" />
```

---

### Runtime Permission Flow (Senior expectation)
For dangerous permissions:

1. declare in Manifest
2. check with `ContextCompat.checkSelfPermission()`
3. request with `ActivityCompat.requestPermissions()`
4. handle result callback

Senior notes:
- handle “Don’t ask again”
- redirect to settings if needed

---

### `uses-feature` vs `uses-permission` (Common confusion)

#### `uses-permission`
Asks for access.

#### `uses-feature`
Declares hardware/software dependency for Play Store filtering.

Example:
```xml
<uses-feature
    android:name="android.hardware.camera"
    android:required="false" />
```

✅ Senior point:
> “`uses-feature` impacts device compatibility in Play Store.”

---

## Intent Filters (Deep links, routing, implicit intents)

### What is an intent-filter?
`intent-filter` declares **which intents** a component can handle.

Used for:
- implicit intent resolution
- deep links (URL routes)
- broadcast matching
- launcher activities

---

### Intent-filter Matching Rules (Senior detail)
Intent resolution uses:
- `action`
- `category`
- `data` (scheme/host/path + mimeType)

#### Action
Must match **one** action in the filter.

#### Category
Intent must include all categories declared in filter.  
Launcher example requires:
- `android.intent.category.LAUNCHER`

#### Data
Matching is strict if specified:
- scheme (`https`)
- host (`example.com`)
- path / pathPrefix / pathPattern
- mimeType

---

### Typical Launcher Activity Example
```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

---

### Deep Links (App Links) — Senior must know

#### Basic deep link
```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />

    <data
        android:scheme="https"
        android:host="example.com"
        android:pathPrefix="/product" />
</intent-filter>
```

#### Android App Links (verified)
Add:
```xml
android:autoVerify="true"
```

Then host `assetlinks.json` on the domain.

✅ Senior point:
> “App Links avoid chooser dialog if domain is verified.”

---

### Explicit vs Implicit intents (Manifest impact)
- Explicit intent targets component directly → manifest filter not required
- Implicit intent requires `intent-filter` to resolve

---

## `android:exported` + Security (Android 12+)

### What is `android:exported`?
Defines whether a component can be launched by **other apps**.

- `true` → available to other apps
- `false` → only your app can launch

---

### Why it became mandatory (Android 12+)
From Android 12 (API 31), any component with an `<intent-filter>` must explicitly declare:

```xml
android:exported="true/false"
```

Reason: **security hardening**
- prevents accidental exposure of Activities/Services/Receivers

✅ Senior one-liner:
> “Android 12 forced explicit exported to prevent unintentional component exposure.”

---

### Exported Examples

#### Internal-only Activity (Recommended)
```xml
<activity
    android:name=".InternalActivity"
    android:exported="false" />
```

#### Deep link Activity (Must be exported)
```xml
<activity
    android:name=".DeepLinkActivity"
    android:exported="true">
    <intent-filter>...</intent-filter>
</activity>
```

#### Receiver example
```xml
<receiver
    android:name=".BootReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
```

---

### Manifest Security Best Practices (Senior)
✅ Default everything to **not exported**:
- activities: `exported=false`
- services: `exported=false`
- receivers: `exported=false`
- providers: `exported=false`

Only export components that **must be public**.

Add permission gate:
```xml
<service
    android:name=".MyService"
    android:exported="true"
    android:permission="com.example.permission.ACCESS_SERVICE" />
```

✅ Interview line:
> “Export only what is necessary, and protect exported components with permissions.”

---

## Component Configuration (Senior topics)

### Task & launch behavior
Launch modes:
```xml
android:launchMode="standard|singleTop|singleTask|singleInstance"
```

Task affinity:
```xml
android:taskAffinity="com.example.task"
```

Common interview trap:
> launchMode controls instance policy; taskAffinity controls task grouping.

---

### Process management
Run component in separate process:
```xml
android:process=":remote"
```

Use only if needed (adds memory + complexity).

---

### `configChanges` (Avoid activity recreation)
```xml
android:configChanges="orientation|screenSize"
```

✅ Senior stance:
- generally avoid unless strong reason
- handle state using ViewModel/Compose state

---

### Permissions per component
Restrict component access:
```xml
<activity
    android:name=".SecretActivity"
    android:exported="true"
    android:permission="com.example.permission.SECRET" />
```

---

## Application-level Manifest Flags Worth Knowing

### `android:allowBackup`
```xml
android:allowBackup="false"
```
Security: prevents adb/cloud backup extraction for sensitive apps.

### Cleartext traffic
```xml
android:usesCleartextTraffic="false"
```

### Network security config
```xml
android:networkSecurityConfig="@xml/network_security_config"
```

<div id="manifest-flags-top"></div>
<details>
<summary><b>🔴 <font color="red">APPLICATION-LEVEL MANIFEST FLAGS WORTH KNOWING (ELABORATED)</font> 🔴</b></summary>
<br>
<blockquote>

## Application-level Manifest Flags Worth Knowing (Elaborated)

### 1) `android:allowBackup`

```xml
<application
    android:allowBackup="false" />
```

#### What it controls

This flag decides whether Android is allowed to **backup and restore** your app’s data:

* Auto Backup (Google Drive backup)
* ADB backup (older devices / debug cases)
* restore happens during reinstall / device migration

#### Why it matters (Security)

If enabled (`true`), app data like:

* SharedPreferences
* internal files
* databases
  may get backed up and potentially restored/extracted (depending on OS/device/debug).

✅ For apps handling sensitive data (banking, payments, enterprise apps), you typically set:

> `android:allowBackup="false"`

#### Interview line

> “allowBackup false prevents untrusted backup/restore paths and reduces data leakage risk.”

✅ Best practice:

* **Finance / Health / Enterprise apps:** `false`
* normal apps: may keep true but control backup rules using `fullBackupContent`

---

### 2) `android:usesCleartextTraffic`

```xml
<application
    android:usesCleartextTraffic="false" />
```

#### What it controls

Whether the app can make **unencrypted HTTP traffic** (non-HTTPS).

* `true` → allows `http://`
* `false` → blocks cleartext HTTP traffic

#### Why it matters (Security)

Cleartext HTTP traffic:

* can be intercepted (MITM attack)
* credentials/tokens can leak
* violates modern security compliance

So in production apps, recommended:
✅ `android:usesCleartextTraffic="false"`

#### Common scenario

* your API must be HTTPS
* your app should reject insecure traffic

✅ Interview line

> “Disabling cleartext traffic enforces HTTPS-only network communication.”

---

### 3) `android:networkSecurityConfig`

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config" />
```

#### What it does

Allows you to define **advanced network security rules** in XML, such as:

* allow cleartext traffic only for specific domains
* custom CA certificates (enterprise proxy)
* certificate pinning support patterns (not true pinning alone, but assists control)
* trust anchors, debug overrides

#### Example config

`res/xml/network_security_config.xml`

```xml
<network-security-config>

    <!-- Default: block cleartext traffic -->
    <base-config cleartextTrafficPermitted="false" />

    <!-- Allow HTTP only for a specific domain (rare, usually for legacy) -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>

</network-security-config>
```

✅ Interview line

> “Network security config provides centralized control over domain-level HTTP/TLS rules and certificates.”

---

## Strong Senior Summary (10 sec)

> “allowBackup controls whether app data can be backed up/restored; for sensitive apps we disable it. usesCleartextTraffic blocks insecure HTTP and enforces HTTPS. networkSecurityConfig provides fine-grained domain and certificate policies for secure networking.”

If you want, I can also add the **best production setup**:
`allowBackup=false`, `usesCleartextTraffic=false`, plus a domain-config for dev/test only.

> [⬆️ Back to Top / Close](#manifest-flags-top)
</blockquote>
</details>

---

## Interview Rapid Fire (Quick Q&A)

### Can app run without manifest?
No. Manifest is mandatory.

### Where does permission enforcement happen?
At OS/framework level; also at component entry when exported.

### Why is exported risky?
Other apps can invoke your component with crafted intents.

### How do you protect exported components?
- set `exported=false` if not needed
- require permissions
- validate intent input (defensive coding)

### What happens if exported missing on Android 12+?
Build/installation fails due to manifest merger / install errors.

---

## Best Final Senior Summary (15 seconds)
> “AndroidManifest is the app’s OS contract defining components, permissions, and intent routing. Permissions control capabilities; intent-filters enable implicit intents and deep links. `android:exported` controls inter-app accessibility and became mandatory on Android 12+ for security. As a senior dev, I export only required components, guard them with permissions, validate intent input, and keep manifest minimal and secure.”
