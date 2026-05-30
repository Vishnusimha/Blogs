A solid foundation in Linux is crucial. Below is a comprehensive guide to mastering the Linux skills. We’ll cover
everything from basic navigation to advanced topics like scripting, system monitoring, and troubleshooting.

### 1. **Linux Basics: Navigating and Managing the System**

Before diving into advanced concepts, it’s essential to have a firm grasp of the basics.

#### 1.1. **Understanding the Linux Filesystem Structure**

- The Linux filesystem is hierarchical, with everything organized under the root (`/`).
- Key directories include:
    - `/home`: User home directories.
    - `/etc`: Configuration files for the system.
    - `/var`: Variable files like logs and caches.
    - `/bin`, `/sbin`, `/usr/bin`: Essential system binaries.
    - `/tmp`: Temporary files.

#### 1.2. **Basic Linux Commands**

- **Navigation:**
    - `ls`: List files and directories. Example: `ls -l /var/log`
    - `cd`: Change directories. Example: `cd /etc`
    - `pwd`: Print current working directory. Example: `pwd`
- **File Management:**
    - `cp`, `mv`, `rm`: Copy, move, and remove files. Example: `cp /etc/hosts /tmp/`
    - `touch`, `mkdir`, `rmdir`: Create files and directories. Example: `mkdir /var/myapp`
    - `find`, `grep`: Search files and directories. Example: `find /var/log -name "*.log"`
- **Permissions and Ownership:**
    - `chmod`, `chown`, `chgrp`: Modify file permissions and ownership. Example: `chmod 755 /var/myapp`
    - File permission notation (e.g., `rwxr-xr--`) and how to modify them.

#### 1.3. **Editing Files in Linux**

- Familiarity with text editors like `vim` or `nano`.
    - Example: Editing the `hosts` file: `vim /etc/hosts`
- Key `vim` commands:
    - `i` (insert mode), `:wq` (save and exit), `:q!` (quit without saving).

### 2. **Process Management**

Understanding and managing processes is crucial in production environments.

#### 2.1. **Viewing and Controlling Processes**

- **Monitoring Processes:**
    - `ps`, `top`, `htop`: View running processes. Example: `ps aux | grep apache`
- **Managing Processes:**
    - `kill`, `killall`: Terminate processes by PID or name. Example: `kill 1234`
    - `nice`, `renice`: Adjust process priorities.
    - `systemctl`: Manage services. Example: `systemctl status nginx`

### 3. **Linux Networking Fundamentals**

In a cloud and SRE role, you must understand how networking works in Linux.

#### 3.1. **Basic Networking Commands**

- **Viewing Network Configuration:**
    - `ip a`, `ifconfig`: Display network interfaces and IP addresses.
- **Testing Connectivity:**
    - `ping`: Check network reachability. Example: `ping google.com`
    - `traceroute`: Track the route packets take. Example: `traceroute ibm.com`
- **Working with DNS:**
    - `nslookup`, `dig`: Query DNS information. Example: `dig ibm.com`
- **Port Scanning and Listening Services:**
    - `netstat`, `ss`: View listening services and open ports. Example: `ss -tuln`

### 4. **System Monitoring and Log Management**

Monitoring is key to ensuring uptime and diagnosing issues.

#### 4.1. **Monitoring System Health**

- **System Resource Usage:**
    - `df`: Check disk space usage. Example: `df -h`
    - `du`: Measure directory space usage. Example: `du -sh /var/log`
    - `free`, `vmstat`: Monitor memory and swap usage.
- **Viewing Running Services:**
    - `systemctl`, `service`: Check service status. Example: `systemctl status sshd`

#### 4.2. **Managing Logs**

- **Understanding Log Files:**
    - Logs are usually stored in `/var/log/`. Key logs include:
        - `/var/log/syslog` or `/var/log/messages`: General system logs.
        - `/var/log/auth.log`: Authentication-related logs.
- **Analyzing Logs:**
    - Use `grep`, `tail`, and `less` to examine logs. Example: `tail -f /var/log/syslog`
    - Example: Troubleshoot SSH issues using: `grep "sshd" /var/log/auth.log`

### 5. **Package Management and Software Installation**

You will often need to install or update software on Linux systems.

#### 5.1. **Package Managers**

- **Debian/Ubuntu-based Systems:**
    - `apt`, `dpkg`: Install and manage software packages. Example: `sudo apt update && sudo apt upgrade`
- **Red Hat/CentOS-based Systems:**
    - `yum`, `dnf`, `rpm`: Manage software packages. Example: `sudo yum install nginx`

### 6. **System Automation and Scripting**

Automation is central to the SRE role. You must be proficient in scripting, especially in **Bash** and **Python**.

#### 6.1. **Bash Scripting**

- **Creating and Running Scripts:**
    - Example: A simple script to monitor disk space:
      ```bash
      #!/bin/bash
      df -h | grep "/dev/sda1" | awk '{print $5}'
      ```
- **Key Bash Concepts:**
    - Variables, conditionals (`if` statements), loops (`for`, `while`), and functions.
    - Example: A loop to restart a service if it fails:
      ```bash
      while true; do
          if ! systemctl is-active --quiet nginx; then
              systemctl restart nginx
          fi
          sleep 60
      done
      ```
- **Scheduling Jobs:**
    - Use `cron` for automation. Example: A daily backup at midnight:
      ```bash
      0 0 * * * /usr/bin/backup.sh
      ```

#### 6.2. **Advanced Scripting with Python**

- **File Operations:**
    - Example: Reading and writing files in Python:
      ```python
        with open('/var/log/syslog', 'r') as log_file:
        for line in log_file:
        if "ERROR" in line:
        print(line)
      ```

- **Interacting with System Commands:**
    - Example: Using `subprocess` to run Linux commands:
      ```python
      import subprocess
      result = subprocess.run(['df', '-h'], capture_output=True, text=True)
      print(result.stdout)
      ```

### 7. **Security and Access Control**

Security is vital in production systems.

#### 7.1. **User and Group Management**

- **Creating and Managing Users:**
    - `useradd`, `usermod`, `passwd`: Manage user accounts.
- **Managing Permissions:**
    - Understand and configure user, group, and others’ permissions using `chmod`, `chown`, and `chgrp`.

#### 7.2. **Securing SSH Access**

- **Best Practices:**
    - Disable root login (`PermitRootLogin no`), use key-based authentication, and enforce strong passwords.
    - Configure `sshd_config` securely. Example: Allow only specific users:
      ```bash
      AllowUsers admin1 admin2
      ```

### 8. **System Troubleshooting and Problem Analysis**

When production issues arise, you need to quickly identify and resolve the root cause.

#### 8.1. **Diagnostic Commands**

- **Checking System Load and Performance:**
    - `uptime`, `top`, `htop`: Monitor CPU, memory, and load averages.
- **Debugging Network Issues:**
    - Use `ping`, `traceroute`, `netstat`, and `ss` to diagnose connectivity problems.
- **Analyzing Log Files:**
    - `grep`, `awk`, and `sed` are powerful tools for filtering log files.

### 9. **Configuration Management and Automation**

Managing and scaling infrastructure requires automated configuration.

#### 9.1. **Automation Tools**

- **Ansible:** Automate repetitive tasks, manage configurations, and deploy applications.
    - Example: Restarting a service on multiple servers using Ansible:
      ```yaml
      - hosts: webservers
        tasks:
          - name: Restart NGINX
            service:
              name: nginx
              state: restarted
      ```
- **Using Git for Version Control:**
    - Keep track of configuration changes and collaborate with teams using Git.

---

### Conclusion and Learning Path

1. **Start with Basics:** Master Linux navigation, file management, and system administration.
2. **Build Scripting Skills:** Learn and practice writing automation scripts in Bash and Python.
3. **Dive into Networking and Monitoring:** Understand how to monitor and diagnose issues across networks and services.
4. **Automate Everything:** Use tools like Ansible to automate configurations and deployments.
5. **Security and Troubleshooting:** Develop expertise in securing systems and responding quickly to incidents.

