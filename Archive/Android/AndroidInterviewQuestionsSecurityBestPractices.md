    Why should developers minimize permissions in Android apps?

Excessive permissions increase security risks by exposing user data to potential abuse. Apps should only request essential permissions to reduce attack vectors.

    How does ProGuard improve security in Android apps?

ProGuard obfuscates code, renaming methods, classes, and variables, making reverse engineering harder for attackers.

    Why is hardcoding sensitive data a security risk?

Decompilers can extract hardcoded API keys and passwords from the APK, compromising sensitive information. Encryption and Keystore should be used instead.

    What are best practices for encrypting sensitive data in Android apps?

Use EncryptedFile API for file encryption, SQLCipher for database encryption, and store cryptographic keys securely in Android Keystore.

    How does Android Keystore enhance security?

It protects cryptographic keys from unauthorized access by ensuring they never leave the hardware-backed security module.

    Why should developers enforce HTTPS over HTTP?

HTTPS encrypts transmitted data, preventing interception and manipulation by attackers.

    What is SSL certificate pinning? How does it help?

Certificate pinning restricts communication to trusted certificates, reducing the risk of Man-in-the-Middle (MITM) attacks.

    What are the advantages of using OAuth2 for authentication in Android apps?

OAuth2 eliminates the need to store passwords and ensures secure access delegation using tokens.

    Why should activities be marked as android:exported="false"?

It prevents other apps from accessing sensitive activities, reducing the risk of unauthorized interactions.

    How can developers securely handle Intent filters?

Validate incoming data from external apps before processing, ensuring no malicious payloads execute.

    What are the benefits of penetration testing in mobile security?

It simulates real-world attacks to uncover vulnerabilities before exploitation by malicious actors.

    How can Static Code Analysis tools like SonarQube improve security?

They automatically detect hardcoded credentials, weak encryption methods, and potential vulnerabilities during development.

    What are common security risks in API communication?

    Unauthorized access due to weak authentication.
    Sensitive data exposure if responses aren’t encrypted.
    Injection attacks from unsanitized user inputs.

    How can developers protect API keys in Android apps?

    Store API keys securely in Android Keystore instead of embedding them in the code.
    Use environment variables during runtime for sensitive configurations.
    Implement JWT tokens or OAuth2 authentication instead of API key-based access.

    Why is reverse engineering a threat to Android apps?

Attackers can decompile APKs, extract sensitive logic, modify behaviors, and inject malicious code.

    What are effective ways to prevent reverse engineering?

    Enable ProGuard or R8 to obfuscate the code.
    Use JNI (Java Native Interface) for critical logic, making reverse engineering harder.
    Apply runtime integrity checks using the Play Integrity API.

    Why is storing sensitive data in external storage unsafe?

External storage is accessible by other apps, making it vulnerable to unauthorized access or modification.

    How can developers safely store data in external storage?

    Encrypt files before saving them.
    Use Scoped Storage to limit access.
    Store sensitive data in Internal Storage or Encrypted Shared Preferences instead.

    How can third-party libraries introduce security risks?

    They may have vulnerabilities that attackers exploit.
    Unmaintained libraries may be outdated, increasing the risk.
    Some libraries unintentionally leak user data.

    What are best practices for using third-party libraries securely?

    Regularly update libraries to the latest stable versions.
    Prefer libraries with active security patches.
    Scan dependencies using OWASP Dependency-Check.

    What are security concerns with WebView?

    Vulnerable to JavaScript injection attacks.
    Can expose sensitive native methods to remote code.
    Malicious sites can phish user credentials.

    How to secure WebView in Android apps?

    Disable JavaScript unless absolutely needed.
    Restrict WebView to specific trusted domains using Content Security Policy.
    Ensure user input validation before loading external URLs.

    How can push notifications be a security risk?

    Unauthenticated notifications can mislead users.
    Attackers can send spoofed alerts if not properly secured.

    What are best practices for securing push notifications?

    Use Firebase Authentication for push services.
    Encrypt sensitive payloads to prevent interception.
    Prevent unauthorized notifications using JWT-based validation.

    Why should debug logs not contain sensitive information?

    Logs may expose passwords, API keys, or user data.
    Attackers can extract logs to gather system details.

    How to secure debug and logging practices?

    Avoid logging PII (Personally Identifiable Information).
    Use ProGuard rules to strip debug logs in release builds.
    Store critical logs in secured storage.