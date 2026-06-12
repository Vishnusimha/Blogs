# Technical interview questions

#### DAY 7 Localization, Security & Release Readiness

1. How do you implement localization in Android?
2. What are some security best practices for Android app development?
3. Describe the steps involved in publishing an app on the Google Play Store.
4. How do you implement encryption in Android? 
5. How do you implement biometric authentication in an Android app?
6. What is the purpose of the `proguard-rules.pro` file in an Android project?

## Answers:

#### Day 7 Answers

### 1. How do you implement localization in Android?

Localization involves providing different resources for different locales:

- Create resource directories with locale-specific qualifiers, e.g., `res/values-fr/` for French.
- Use `strings.xml` files to define translations for strings.
- Use `Configuration` and `Locale` classes to dynamically change the app's locale.

### 2. What are some security best practices for Android app development?

Security best practices include:

- Using secure communication protocols (HTTPS).
- Encrypting sensitive data.
- Avoiding storing sensitive data on external storage.
- Using the Android Keystore system for managing cryptographic keys.
- Implementing proper permission management.
- Regularly updating libraries and dependencies to fix known vulnerabilities.

### 3. Describe the steps involved in publishing an app on the Google Play Store.

1. **Create a Developer Account**: Register and pay a one-time fee.
2. **Prepare the App**: Ensure your app meets all the guidelines.
3. **Create a Release**: Build and sign your APK or AAB.
4. **Set Up Google Play Console**: Create a new app in the Google Play Console.
5. **Upload the App**: Upload the APK or AAB file.
6. **Set Up Store Listing**: Provide necessary information like title, description, screenshots, etc.
7. **Configure Pricing and Distribution**: Set the price and target countries.
8. **Review and Rollout**: Submit for review and rollout once approved.

### 4. How do you implement encryption in Android?

Encryption can be implemented using the Android Keystore system and cryptographic libraries.

Example:

```kotlin
val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
keyGenerator.init(
    KeyGenParameterSpec.Builder(
        "MyKeyAlias",
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()
)

val secretKey = keyGenerator.generateKey()
val cipher =

    Cipher.getInstance("AES/GCM/NoPadding")
cipher.init(Cipher.ENCRYPT_MODE, secretKey)
val encryptedData = cipher.doFinal(plainText.toByteArray())
```

### 5. How do you implement biometric authentication in an Android app?

Use the `BiometricPrompt` API for biometric authentication.

Example:

```kotlin
val biometricPrompt =
    BiometricPrompt(this, Executors.newSingleThreadExecutor(), object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            // Handle successful authentication
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            // Handle authentication failure
        }
    })

val promptInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Biometric login")
    .setSubtitle("Log in using your biometric credential")
    .setNegativeButtonText("Use account password")
    .build()

biometricPrompt.authenticate(promptInfo)
```

### 6. What is the purpose of the `proguard-rules.pro` file in an Android project?

**Answer:**
The `proguard-rules.pro` file is used to configure ProGuard, a tool that shrinks, optimizes, and obfuscates your code.
It helps reduce the size of the APK and makes it harder to reverse-engineer. This file defines rules for how ProGuard
should treat your code, such as which classes and methods to keep, and which to optimize or obfuscate.

Example rules in `proguard-rules.pro`:

```proguard
# Keep all classes in the specified package
-keep class com.example.myapp.** { *; }

# Keep all annotated classes and methods
-keep @interface com.example.myapp.KeepMe
-keep class * {
    @com.example.myapp.KeepMe *;
}

# Keep the names of native methods
-keepclasseswithmembernames class * {
    native <methods>;
}
```
---
