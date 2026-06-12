# App Bundles & APK Structure (Senior Android Interview Guide)

> This document explains **APK internals, build pipeline, signing, splits, Play delivery, AAB vs APK trade-offs**, and common interview traps.  
> Goal: help you answer like a **senior Android developer**.

---

## Table of Contents
- [1) What is an APK?](#1-what-is-an-apk)
- [2) APK Internal Structure (A-to-Z)](#2-apk-internal-structure-a-to-z)
- [3) Build Pipeline: Kotlin/Java → APK/AAB](#3-build-pipeline-kotlinjava--apkaab)
- [4) What is DEX? (classes.dex)](#4-what-is-dex-classesdex)
- [5) What is resources.arsc and R class?](#5-what-is-resourcesarsc-and-r-class)
- [6) res vs assets (runtime behavior)](#6-res-vs-assets-runtime-behavior)
- [7) Native libraries (lib/ and ABI splits)](#7-native-libraries-lib-and-abi-splits)
- [8) APK Signing (v1/v2/v3/v4) + why it matters](#8-apk-signing-v1v2v3v4--why-it-matters)
- [9) What is an AAB (Android App Bundle)?](#9-what-is-an-aab-android-app-bundle)
- [10) AAB Delivery: Split APKs & Dynamic Delivery](#10-aab-delivery-split-apks--dynamic-delivery)
- [11) AAB vs APK (Senior comparison)](#11-aab-vs-apk-senior-comparison)
- [12) Splits: density/ABI/language + versionCode strategy](#12-splits-densityabilanguage--versioncode-strategy)
- [13) MultiDex (limit, modern reality)](#13-multidex-limit-modern-reality)
- [14) R8/ProGuard, shrinking & obfuscation](#14-r8proguard-shrinking--obfuscation)
- [15) Play App Signing & upload key model](#15-play-app-signing--upload-key-model)
- [16) Common senior interview traps](#16-common-senior-interview-traps)
- [17) Quick 30-second interview answer](#17-quick-30-second-interview-answer)

---

## 1) What is an APK?
An **APK (Android Package Kit)** is the **installable package** format for Android apps.  
It contains everything needed to run an app on a device: compiled code, resources, manifest, assets, and native libs.

✅ Senior one-liner:
> “APK is the final install artifact: a zip-like container holding app code (DEX), resources, manifest and signatures.”

---

## 2) APK Internal Structure (A-to-Z)
An APK is essentially a ZIP file with a known structure.

### Key files/folders inside an APK
- `AndroidManifest.xml`
  - component declarations, permissions, intent-filters (**binary XML format** inside APK)
- `classes.dex` (and possibly `classes2.dex`, `classes3.dex`…)
  - DEX bytecode executed by ART
- `resources.arsc`
  - compiled resource table: maps resource IDs → actual resource entries
- `res/`
  - compiled resources (layouts, drawables, values)
- `assets/`
  - raw files shipped as-is
- `lib/`
  - native shared libraries `.so` (ABI-specific)
  - example: `lib/arm64-v8a/*.so`
- `META-INF/`
  - signing metadata (especially for v1 signatures)
- `kotlin/` *(optional)*
  - Kotlin module metadata (depending on build)
- `classesN.dex`
  - additional dex files if MultiDex

✅ Quick mental model:
> Manifest defines components + permissions; DEX is code; resources.arsc + res is UI/resources; assets is raw; lib is native; META-INF is signing.

---

## 3) Build Pipeline: Kotlin/Java → APK/AAB
Typical pipeline:

1. **Compile source**
   - `kotlinc` / `javac` → `.class`
2. **Bytecode transformation**
   - D8 converts `.class` → `.dex`
   - R8 optionally shrinks/obfuscates/optimizes before dexing
3. **Resource processing**
   - AAPT2 compiles resources ((Android Asset Packaging Tool 2)
   - generates `R` class and `resources.arsc`
4. **Packaging**
   - creates APK or AAB
5. **Signing**
   - debug/release signing
6. **Alignment**
   - zipalign optimizes ZIP structure

✅ Senior point:
> “AAPT2 handles resources; D8/R8 handles code; packaging + signing produce final artifacts.”

---

## 4) What is DEX? (classes.dex)
DEX = **Dalvik Executable** bytecode format.  
ART runs DEX (not `.class`).

### Why DEX exists
- optimized for Android runtime (memory + quick class lookup)
- constant pool optimization
- designed for constrained devices

### Modern reality
- ART uses JIT + profile-guided compilation + AOT optimizations.
- DEX remains the distribution format.

---

## 5) What is resources.arsc and R class?
### resources.arsc
Compiled resource table:
- maps `R.string.app_name` (int ID) → actual string in correct configuration (locale/density/night)

### R class generation
Generated during build by AAPT2:
- `R.layout.*`
- `R.id.*`
- `R.drawable.*`
- `R.string.*`

✅ Senior note:
> “R IDs are stable integers; actual values live in resources.arsc and res folders.”

---

## 6) res vs assets (runtime behavior)

### `res/`
- processed/compiled by AAPT2
- referenced via `R.*`
- supports configuration qualifiers:
  - `layout-sw600dp`
  - `drawable-night`
  - `values-en`
- loaded by `Resources` API

### `assets/`
- raw files, unchanged
- no IDs / no resource qualifiers
- accessed via `AssetManager`

Example:
```kotlin
assets.open("config.json").use { stream ->
    // read raw bytes
}
```

✅ Senior one-liner:
> “res is compiled and type-safe with qualifiers; assets are raw packaged files accessed manually.”

---

## 7) Native libraries (lib/ and ABI splits)
Native libs `.so` are stored in:
- `lib/armeabi-v7a/`
- `lib/arm64-v8a/`
- `lib/x86/`
- `lib/x86_64/`

These may come from:
- NDK
- third-party SDKs

### Why ABI matters
If you ship all ABIs in one APK:
- APK becomes large

Play (with AAB) delivers only the needed ABI per device.

---

## 8) APK Signing (v1/v2/v3/v4) + why it matters
Signing ensures:
- integrity: APK not tampered
- identity: publisher verification
- update compatibility: only same key can update app

### Signature schemes
- **v1 (JAR signing)**
  - signatures in `META-INF/`
  - older scheme
- **v2 (APK Signature Scheme v2)**
  - signs entire APK
  - faster verification, more secure
- **v3**
  - key rotation support
- **v4**
  - incremental installs (ADB / modern install flows)

✅ Senior one-liner:
> “v2+ signs whole APK, prevents modification; v3 enables key rotation; Play relies on modern schemes.”

---

## 9) What is an AAB (Android App Bundle)?
An **AAB** is a **publishing format** for Play Store.  
It is **not installed directly** on devices.

Play Store uses AAB to generate device-optimized APKs.

✅ Senior one-liner:
> “AAB is a modular publishing container; Play generates split APKs tailored per device.”

---

## 10) AAB Delivery: Split APKs & Dynamic Delivery

### Split APKs
Play generates multiple APKs:
- **base APK** (required)
- **config APKs**
  - ABI split
  - density split
  - language split

Device installs only required splits → smaller download/install.

### Dynamic Feature Modules
AAB supports optional modules:
- installed on demand
- installed conditionally (country/device)

✅ Senior examples:
- AR module only on supported devices
- language pack module installed per language

---

## 11) AAB vs APK (Senior comparison)

| Topic | APK | AAB |
|------|-----|-----|
| What is it | Install artifact | Play publishing artifact |
| Installable directly | ✅ yes | ❌ no |
| Delivery optimization | ❌ none | ✅ split APK delivery |
| Size | bigger | smaller on device |
| Dynamic features | limited | ✅ supported |
| Side loading | easy | harder (need bundletool) |
| Typical usage | enterprise / direct distribution | Play Store releases |

✅ Interview line:
> “AAB reduces size via split delivery and enables dynamic features; APK is the final standalone installable artifact.”

---

## 12) Splits: density/ABI/language + versionCode strategy
### Splits
- ABI: `arm64-v8a` vs `armeabi-v7a`
- density: `xxhdpi` vs `xhdpi`
- language: `en`, `fr`, `hi`

### versionCode strategy
When building multiple APK variants (legacy split APKs):
- each split gets unique versionCode (Play requirement)

With AAB:
- Play handles split versioning automatically.

---

## 13) MultiDex (limit, modern reality)
Classic limit:
- **65,536 method references** per DEX.

### When MultiDex happens
- large apps / many dependencies

Modern reality:
- `minSdk >= 21` → native multidex supported by platform
- `minSdk < 21` → need multidex support library

✅ Senior one-liner:
> “Method count limit is DEX reference limit; multidex splits code into multiple dex files.”

---

## 14) R8/ProGuard, shrinking & obfuscation
### What R8 does
- removes unused classes/methods/resources (shrinking)
- renames symbols (obfuscation)
- optimizes bytecode

### Why important
- reduces size
- improves runtime performance
- security through obscurity (not true security)

Senior pitfall:
- need `-keep` rules for reflection (Gson/Moshi/DI)

---

## 15) Play App Signing & upload key model
With Play App Signing:
- you upload with **upload key**
- Play stores **app signing key**
- Play signs the final delivered APKs

Benefits:
- safer key management
- supports key rotation

✅ Senior line:
> “Play App Signing decouples upload key from app signing key; Play signs device APKs.”

---

## 16) Common senior interview traps

### Trap 1: “AAB is installed on device”
❌ Wrong. AAB is not installed.
✅ Play generates split APKs for installation.

### Trap 2: “assets behave like res/”
❌ assets aren’t compiled, no qualifiers.
✅ assets are raw packaged files.

### Trap 3: “R.java contains actual resource values”
❌ It contains integer IDs.
✅ actual values stored in `resources.arsc`.

### Trap 4: “v1 signing is enough”
❌ not for modern Android expectations.
✅ v2+ required for robust security.

### Trap 5: MultiDex confusion
- limit is DEX method references, not Kotlin functions count

---

## 17) Quick 30-second interview answer
> “APK is the install artifact containing Manifest, DEX bytecode, compiled resources (resources.arsc + res), assets, native libs and signatures. D8/R8 build DEX, AAPT2 builds resources. AAB is a Play publishing format; Play generates split APKs per ABI/density/language, reducing size and enabling dynamic feature delivery. Signing ensures integrity and update trust; Play App Signing manages keys and signs final delivered APKs.”

---
