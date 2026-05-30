Here’s a structured guide to **GitHub Actions**, covering everything you need to learn as a software developer—from **basics to advanced**:

---

## 🔰 **Basics of GitHub Actions**

### 1. **What is GitHub Actions?**

* GitHub Actions is a **CI/CD platform** that lets you automate tasks within your software development lifecycle.
* Use it to run tests, build projects, deploy code, lint files, etc., whenever events like `push`, `pull_request`, or `release` happen.

---

### 2. **Key Concepts**

| Term         | Description                                                                          |
| ------------ | ------------------------------------------------------------------------------------ |
| **Workflow** | Automation process triggered by events. Defined in `.github/workflows/*.yml`.        |
| **Job**      | A set of steps executed on the same runner.                                          |
| **Step**     | An individual task (running commands, calling actions).                              |
| **Action**   | A reusable piece of code to perform a task.                                          |
| **Runner**   | A server that runs your jobs (hosted by GitHub or self-hosted).                      |
| **Event**    | Triggers the workflow, like `push`, `pull_request`, `schedule`, `workflow_dispatch`. |

---

### 3. **Creating Your First Workflow**

Example: `.github/workflows/hello.yml`

```yaml
name: Hello World

on: [push]

jobs:
  say-hello:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3
      
      - name: Say Hello
        run: echo "Hello, GitHub Actions!"
```

---

## 🧰 **Intermediate Usage**

### 4. **Common Built-in Actions**

* `actions/checkout`: Checks out your repo.
* `actions/setup-node`: Sets up a Node.js environment.
* `actions/setup-java`: Sets up Java.
* `actions/upload-artifact`: Uploads build/test artifacts.
* `actions/cache`: Caches dependencies to speed up builds.

---

### 5. **Environment Variables**

```yaml
env:
  NODE_ENV: production
  APP_NAME: myapp

steps:
  - run: echo "Running in $NODE_ENV for $APP_NAME"
```

---

### 6. **Matrix Builds**

Run tests across multiple environments:

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest]
    node: [14, 16]
jobs:
  test:
    runs-on: ${{ matrix.os }}
    steps:
      - uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node }}
      - run: npm install && npm test
```

---

### 7. **Secrets and Security**

Store sensitive info like API keys in **Repository Settings → Secrets**.

```yaml
env:
  API_KEY: ${{ secrets.MY_API_KEY }}
```

---

### 8. **Workflow Triggers**

```yaml
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
  schedule:
    - cron: '0 12 * * MON'  # Every Monday at noon
  workflow_dispatch:  # Manual trigger
```

---

## 🚀 **Advanced Topics**

### 9. **Reusable Workflows**

Split logic into reusable parts:

```yaml
# .github/workflows/main.yml
jobs:
  call-reusable:
    uses: ./.github/workflows/build.yml
    with:
      build_type: "release"
```

```yaml
# .github/workflows/build.yml
on:
  workflow_call:
    inputs:
      build_type:
        required: true
        type: string
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - run: echo "Building type ${{ inputs.build_type }}"
```

---

### 10. **Creating Your Own Actions**

#### Composite Action

```yaml
# action.yml
name: Greet
runs:
  using: "composite"
  steps:
    - run: echo "Hello from custom action!"
```

#### Docker Action

Create an action that runs in a Docker container.

```dockerfile
FROM ubuntu
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
```

---

### 11. **Advanced Caching**

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.npm
    key: ${{ runner.os }}-node-${{ hashFiles('**/package-lock.json') }}
    restore-keys: |
      ${{ runner.os }}-node-
```

---

### 12. **Artifacts and Reports**

Upload build or test reports:

```yaml
- name: Upload build
  uses: actions/upload-artifact@v3
  with:
    name: build-files
    path: build/
```

---

### 13. **Monorepo and Conditional Jobs**

```yaml
if: github.event.pull_request.changed_files > 0
```

Or with path filters:

```yaml
on:
  push:
    paths:
      - 'backend/**'
```

---

### 14. **Self-Hosted Runners**

Use your own hardware to run workflows.

* Install runner software
* Register with your repo
* Use with `runs-on: self-hosted`

---

### 15. **Monitoring & Debugging**

* Use `ACTIONS_RUNNER_DEBUG=true` as a secret for debug logs.
* Use `continue-on-error: true` to avoid workflow failures.

---

## 📚 Recommended Learning Resources

* 📘 [GitHub Actions Docs](https://docs.github.com/en/actions)
* 🧪 [GitHub Actions Examples](https://github.com/actions)
* 🎥 YouTube Channels: GitHub, Fireship, The Net Ninja
* 📓 Projects to practice:

    * CI/CD for a Node.js or Spring Boot app
    * Linting & testing automation
    * Deploying to Firebase/AWS/DockerHub

---

Would you like a **PDF cheat sheet** or a **GitHub Actions project template** next?
