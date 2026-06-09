---
title: "Setting Up Android CI with GitHub Actions: A Practical Step-by-Step Guide"
date: 2026-06-01
slug: "android-ci-github-actions-step-by-step-guide"
tags: [ "Android", "GitHub Actions", "CI/CD", "Gradle", "Branch Protection" ]
summary: "Step-by-step guide to setting up Android CI with GitHub Actions, including unit tests, lint checks, APK/AAB builds, artifact uploads, and branch protection rules."
categories: Android
readTime: 12
---

# Setting Up Android CI with GitHub Actions: A Practical Step-by-Step Guide

When I started tightening my Android development workflow, one thing became very clear: relying only on local testing is not enough.

As an Android developer, I wanted a simple but professional setup where every pull request is automatically checked before it reaches important branches like `develop`, `main`, or `release/*`.

In this guide, I will walk through the exact process I follow to set up **Continuous Integration (CI)** for an Android project using **GitHub Actions**.

The goal is simple:

> Before any code is merged, GitHub should automatically build the Android app, run unit tests, run lint checks, and confirm that the project is still healthy.

---

## What We Are Setting Up

By the end of this setup, the Android project will have:

- A GitHub Actions workflow for Android CI
- Automated checks on pull requests
- Unit test execution
- Android lint execution
- Debug APK build verification
- Release AAB build verification
- APK artifact upload
- Release AAB artifact upload
- Branch protection rules for `develop`, `main`, and `release/*`
- A disciplined PR-based Git workflow

The final development flow will look like this:

```text
feature/*  → PR → develop  → CI pass → merge
bugfix/*   → PR → develop  → CI pass → merge
develop    → PR → main     → CI pass → merge
release/*  → PR flow       → CI pass → merge
```

---

## Why CI Matters for Android Projects

In Android development, even small changes can break things unexpectedly.

A UI change can break tests.  
A Gradle change can break the build.  
A dependency update can introduce lint issues.  
A quick fix can accidentally impact another feature.

CI gives a safety layer.

Instead of manually checking everything every time, GitHub Actions does the routine validation automatically.

For a production-minded Android project, this is the minimum quality gate I want before merging code:

```text
Build should pass
Unit tests should pass
Lint should pass
Debug APK should be generated successfully
Release AAB should be generated successfully
```

---

## Recommended Branch Strategy

Before adding CI, I prefer having a clear branch structure.

For my Android projects, I follow this:

```text
main        → stable branch / release-ready code
develop     → active integration branch
feature/*   → new features
bugfix/*    → bug fixes
release/*   → release preparation branches
```

Example branch names:

```text
feature/add-login-screen
feature/add-android-ci
bugfix/fix-profile-crash
release/1.0.0
```

The important rule is:

> Do not directly push feature or bugfix work into `develop` or `main`.

Instead, use pull requests.

---

## Step 1: Create a `develop` Branch

If the repository only has `main`, create a `develop` branch first.

```bash
git checkout main
git pull origin main
git checkout -b develop
git push -u origin develop
```

Now the repository has:

```text
main
develop
```

---

## Step 2: Create a Feature Branch for CI Setup

Even CI setup should go through a feature branch.

```bash
git checkout develop
git pull origin develop
git checkout -b feature/add-android-ci
```

This keeps the workflow clean and traceable.

---

## Step 3: Create the GitHub Actions Workflow File

GitHub Actions workflows live inside this folder:

```text
.github/workflows/
```

Create the folder and workflow file:

```bash
mkdir -p .github/workflows
touch .github/workflows/android-ci.yml
```

The file path should be:

```text
.github/workflows/android-ci.yml
```

---

## Step 4: Add the Android CI Workflow

Open:

```text
.github/workflows/android-ci.yml
```

Add this workflow:

```yaml
name: Android CI

on:
  pull_request:
    branches:
      - develop
      - main
      - 'release/**'
    types:
      - opened
      - synchronize
      - reopened

  push:
    branches:
      - develop
      - main
      - 'release/**'

jobs:
  android-ci:
    name: Build, Test and Lint Android App
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up Java 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17

      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v4

      - name: Make Gradle executable
        run: chmod +x ./gradlew

      - name: Run unit tests
        run: ./gradlew testDebugUnitTest

      - name: Run Android lint
        run: ./gradlew lintDebug

      - name: Build debug APK
        run: ./gradlew assembleDebug

      - name: Build release AAB
        run: ./gradlew :app:bundleRelease

      - name: Upload debug APK
        uses: actions/upload-artifact@v4
        with:
          name: debug-apk
          path: app/build/outputs/apk/debug/*.apk

      - name: Upload release AAB
        uses: actions/upload-artifact@v4
        with:
          name: release-aab
          path: app/build/outputs/bundle/release/*.aab
```

---

## What This Workflow Does

This workflow runs when:

```text
A pull request targets develop
A pull request targets main
A pull request targets release/*
A push happens on develop
A push happens on main
A push happens on release/*
```

The job performs these steps:

| Step | Purpose |
|---|---|
| Checkout repository | Downloads the repository code into the runner |
| Set up Java 17 | Installs the required JDK |
| Set up Gradle | Configures Gradle and improves build performance |
| Make Gradle executable | Ensures `gradlew` can run on Linux |
| Run unit tests | Runs Android unit tests |
| Run Android lint | Runs lint checks |
| Build debug APK | Confirms the debug variant can build |
| Build release AAB | Confirms the release Android App Bundle can build |
| Upload debug APK | Stores the generated debug APK as a workflow artifact |
| Upload release AAB | Stores the generated release AAB as a workflow artifact |

---

## Why I Also Add a Release AAB Build Check

A debug build check is useful, but it does not always prove that the release variant is healthy.

In Android projects, release builds can fail for reasons that may not appear during a debug build, such as:

```text
R8 or ProGuard issues
release-only Gradle configuration problems
resource shrinking issues
missing release variant dependencies
build variant differences
```

That is why I prefer adding this command to CI:

```bash
./gradlew :app:bundleRelease
```

This does not mean the app is being uploaded to the Play Store. It only confirms that the project can successfully generate a release Android App Bundle.

The generated `.aab` is also uploaded as a GitHub Actions artifact, so it can be downloaded from the workflow run when needed.

---

## Step 5: Check the Commands Locally First

Before pushing the workflow, I prefer checking the same Gradle commands locally.

```bash
chmod +x ./gradlew
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
./gradlew :app:bundleRelease
```

If these commands fail locally, they will most likely fail in GitHub Actions too.

Fix local issues first, then push.

---

## Step 6: Commit and Push the Workflow

```bash
git status
git add .github/workflows/android-ci.yml
git commit -m "Add Android CI workflow"
git push -u origin feature/add-android-ci
```

---

## Step 7: Create a Pull Request into `develop`

Go to GitHub and create a pull request:

```text
base: develop
compare: feature/add-android-ci
```

Use a clear PR title:

```text
Add Android CI workflow
```

Once the PR is created, GitHub Actions should start running automatically.

On the PR page, check for a section like:

```text
Android CI / Build, Test and Lint Android App
```

Wait until all checks pass.

---

## Step 8: Merge the CI Pull Request

Once the check is green and GitHub shows that the branch is ready to merge, merge the PR.

For a clean history, I usually prefer:

```text
Squash and merge
```

Then delete the temporary branch:

```text
feature/add-android-ci
```

After merging, update local `develop`:

```bash
git checkout develop
git pull origin develop
```

---

## Step 9: Confirm the Workflow Runs After Merge

After merging into `develop`, go to:

```text
GitHub repository → Actions tab
```

You should now see the `Android CI` workflow.

A new run should appear because the workflow also runs on push to `develop`.

This confirms that CI is active.

---

## Step 10: Protect the `develop` Branch

Once CI has run successfully at least once, protect the `develop` branch.

Go to:

```text
Settings → Rules → Rulesets → New branch ruleset
```

Create a ruleset:

```text
Ruleset name: Protect develop
Enforcement status: Active
Target branch: develop
```

Enable these rules:

```text
Restrict deletions
Require a pull request before merging
Require status checks to pass
Require branches to be up to date before merging
Block force pushes
```

Under required status checks, select:

```text
Build, Test and Lint Android App
```

This ensures code cannot be merged into `develop` unless Android CI passes.

---

## Step 11: Protect the `main` Branch

Create another ruleset:

```text
Ruleset name: Protect main
Enforcement status: Active
Target branch: main
```

Enable the same protections:

```text
Restrict deletions
Require a pull request before merging
Require status checks to pass
Require branches to be up to date before merging
Block force pushes
```

Required check:

```text
Build, Test and Lint Android App
```

Now `main` is protected too.

The expected flow becomes:

```text
develop → PR → main
```

---

## Step 12: Protect Release Branches

For release branches, create one more ruleset:

```text
Ruleset name: Protect Release
Enforcement status: Active
Target branch: release/*
```

The target should be:

```text
release/*
```

not just:

```text
release
```

This is because release branches are usually named like:

```text
release/1.0.0
release/1.0.1
release/1.1.0
```

Enable these rules:

```text
Restrict deletions
Require a pull request before merging
Require status checks to pass
Require branches to be up to date before merging
Do not require status checks on creation
Block force pushes
```

Required check:

```text
Build, Test and Lint Android App
```

The important release-specific setting is:

```text
Do not require status checks on creation
```

This allows a new branch like `release/1.0.0` to be created without GitHub blocking it before CI has had a chance to run.

---

## Final Branch Protection Summary

After setup, the rules should look like this.

### `develop`

```text
Target: develop

Enabled:
- Restrict deletions
- Require pull request before merging
- Require status checks to pass
- Require branch to be up to date before merging
- Required check: Build, Test and Lint Android App
- Block force pushes
```

### `main`

```text
Target: main

Enabled:
- Restrict deletions
- Require pull request before merging
- Require status checks to pass
- Require branch to be up to date before merging
- Required check: Build, Test and Lint Android App
- Block force pushes
```

### `release/*`

```text
Target: release/*

Enabled:
- Restrict deletions
- Require pull request before merging
- Require status checks to pass
- Require branch to be up to date before merging
- Do not require status checks on creation
- Required check: Build, Test and Lint Android App
- Block force pushes
```

---

## Day-to-Day Development Flow

### For a New Feature

```bash
git checkout develop
git pull origin develop
git checkout -b feature/my-new-feature
```

After changes:

```bash
git add .
git commit -m "Add my new feature"
git push -u origin feature/my-new-feature
```

Create PR:

```text
base: develop
compare: feature/my-new-feature
```

Wait for CI to pass, then merge.

---

### For a Bug Fix

```bash
git checkout develop
git pull origin develop
git checkout -b bugfix/fix-login-crash
```

After changes:

```bash
git add .
git commit -m "Fix login crash"
git push -u origin bugfix/fix-login-crash
```

Create PR:

```text
base: develop
compare: bugfix/fix-login-crash
```

Wait for CI to pass, then merge.

---

### For Merging `develop` into `main`

When `develop` is stable:

```text
Create Pull Request
base: main
compare: develop
```

Wait for CI to pass, then merge.

---

### For Creating a Release Branch

When preparing a release:

```bash
git checkout develop
git pull origin develop
git checkout -b release/1.0.0
git push -u origin release/1.0.0
```

Then use pull requests depending on the release process.

A common flow is:

```text
develop → release/1.0.0
release/1.0.0 → main
```

---

## Common Issues and Fixes

### 1. `chmod: testDebugUnitTest: No such file or directory`

This usually happens when two commands are accidentally combined:

```bash
chmod +x ./gradlew testDebugUnitTest
```

That is wrong.

Use separate commands:

```bash
chmod +x ./gradlew
./gradlew testDebugUnitTest
```

---

### 2. Required Check Is Not Showing in Rulesets

If `Build, Test and Lint Android App` does not appear when adding required checks, it usually means the workflow has not run yet.

Fix:

```text
1. Push the workflow file
2. Open a PR
3. Let GitHub Actions run once
4. Come back to Rulesets
5. Add the required check
```

---

### 3. Workflow Does Not Appear in the Actions Tab

Check whether the file exists in the correct path:

```text
.github/workflows/android-ci.yml
```

Also confirm that it has been pushed to GitHub and merged into the correct branch.

---

### 4. APK Upload Fails

Check the module name.

The workflow assumes the Android app module is named:

```text
app
```

If the module is named `mobile`, update the artifact path:

```yaml
path: mobile/build/outputs/apk/debug/*.apk
```

You may also need to use module-specific Gradle commands:

```bash
./gradlew :mobile:testDebugUnitTest
./gradlew :mobile:lintDebug
./gradlew :mobile:assembleDebug
```

For the release AAB path, update this too:

```yaml
path: mobile/build/outputs/bundle/release/*.aab
```

And use:

```bash
./gradlew :mobile:bundleRelease
```

---

### 5. Java Version Issues

If the project uses Java 21 instead of Java 17, update:

```yaml
java-version: 17
```

to:

```yaml
java-version: 21
```

---

## What This Setup Gives You

After this setup, the Android project has a proper CI foundation.

The main benefits are:

```text
Every PR is automatically checked
Broken code is blocked before merging
Debug APK generation is verified
Release AAB generation is verified
Lint issues are caught early
Branch discipline is enforced
Release branches are safer
Main branch stays stable
```

This is not full release automation yet, but it is a strong CI setup.

---

## What Can Be Added Later

Once the CI foundation is stable, the next upgrades can be:

```text
Build signed release AAB using GitHub Secrets
Create GitHub Releases
Upload builds to Firebase App Distribution
Upload release builds to Google Play Console
Run connected Android UI tests
Generate test coverage reports
Add Detekt or Ktlint
Add dependency vulnerability checks
```

For me, the right order is:

```text
First: CI quality gate
Later: release automation
```

Trying to automate everything from day one can make the setup confusing. A clean CI pipeline is the right first milestone.

---

## Final Thought

A good Android CI setup is not about adding one YAML file and forgetting it.

It is about creating a development discipline where every change goes through the same quality gate before entering important branches.

For Android projects, a practical first CI goal is:

```text
PR opened → tests run → lint runs → debug APK builds → release AAB builds → merge only if everything passes
```

Once that is working, the project already has a much stronger engineering foundation.
