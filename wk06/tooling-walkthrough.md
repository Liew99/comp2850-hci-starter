# Development Environment Setup for COMP2850 HCI

This guide shows you how to build and run the Ktor + Pebble starter in **three environments**, listed in order of preference:

1. **Local machine** (IntelliJ IDEA or VSCode) - **Recommended**
2. **University lab** (RHEL9 with VSCode) - **Preferred for on-campus work**
3. **GitHub Codespaces** - **Fallback if local setup fails**

Choose the option that works best for you. Most students use local IntelliJ or university labs.

---

## Before You Start

**Requirements**:
- Repository cloned or forked from GitHub
- **Java JDK 17 minimum, 19+ preferred** (Temurin, OpenJDK, or Azul Zulu)
  - Check version: `java -version`
  - If <17, download from [Adoptium](https://adoptium.net/) or [Azul](https://www.azul.com/downloads/)
- Gradle wrapper (`./gradlew`) is included in repo - **do not install Gradle globally**

You'll run the Ktor app via `./gradlew run`. This compiles Kotlin code, starts the embedded server on port `8080`, and serves your Pebble templates.

---

## Option 1: Local Machine (IntelliJ IDEA) - Recommended

### 1.1 Clone and Open

1. Clone the repo locally:
   ```bash
   git clone git@github.com:<org>/comp2850-hci-starter.git
   cd comp2850-hci-starter
   ```
2. Launch **IntelliJ IDEA** (Community or Ultimate)
3. Choose **Open** → select the project directory
4. IntelliJ detects the Gradle project automatically - accept default settings

### 1.2 Configure JDK & Gradle

- **Check JDK**: `File → Project Structure → Project → SDK`
  - Should be **JDK 17 minimum, 19+ preferred**
  - If not available, click **Add SDK → Download JDK** → choose Temurin/Azul/OpenJDK 21 (latest LTS)
- **Gradle JVM**: In Gradle tool window (right sidebar), click wrench icon → **Gradle JVM** → select same JDK (17+)
- Wait for Gradle sync to complete (progress bar in bottom-right status bar)

### 1.3 Run the Application

1. Open `src/main/kotlin/Application.kt` (or `App.kt` depending on scaffold)
2. Click green play icon next to `fun main()` → **Run 'ApplicationKt'**
3. Run tool window shows server log - look for:
   ```
   Application started in X.XXX seconds.
   Listening on: http://0.0.0.0:8080
   ```
4. Open browser: `http://localhost:8080/tasks`

### 1.4 Develop and Refresh

- **Template changes** (Pebble `.peb` files): Save file → refresh browser (`Ctrl/Cmd+R`)
  - IntelliJ watches resource files automatically
- **Kotlin changes** (routes, controllers): Stop server (`Ctrl+F2`) → rerun (`Ctrl+F5`)
  - Or click green **Rerun** arrow in Run tool window

### 1.5 Disable JavaScript for No-JS Testing

**Chrome**:
- DevTools (F12) → Console → three-dot menu → Settings → Debugger → Check "Disable JavaScript"
- Reload page to test server-first path

**Firefox**:
- DevTools (F12) → Debugger → three-dot menu → Disable JavaScript

Re-enable when testing HTMX enhancements.

### 1.6 Run Tests (Optional)

- **Gradle tool window**: Double-click **Tasks → verification → test**
- **Or terminal**: `./gradlew test`

### 1.7 Commit and Push

- **IntelliJ UI**: `VCS → Commit` (`Ctrl+K`) → stage files → commit message → **Commit and Push** (`Ctrl+Shift+K`)
- **Or terminal**:
  ```bash
  git add .
  git commit -m "wk6: Implement task list view"
  git push origin main
  ```

---

## Option 2: Local Machine (VSCode) - Alternative

### 2.1 Prerequisites

- Install **Visual Studio Code**: [https://code.visualstudio.com/](https://code.visualstudio.com/)
- Install **Java Extension Pack** (Extension ID: `vscjava.vscode-java-pack`)
  - Includes Language Support for Java, Debugger, Maven/Gradle support
- Ensure **JDK 17+** (preferably 19+) installed and in PATH

### 2.2 Clone and Open

1. Clone repo:
   ```bash
   git clone git@github.com:<org>/comp2850-hci-starter.git
   cd comp2850-hci-starter
   ```
2. Open in VSCode: `code .` or **File → Open Folder**
3. VSCode detects Gradle project - accept Java extension prompts

### 2.3 Configure Java Version

- Open Command Palette (`Ctrl+Shift+P`)
- Search: **Java: Configure Java Runtime**
- Set **Java 17+** (preferably 19+) for project
- If JDK not listed, click **Configure** → point to JDK installation directory

### 2.4 Run the Application

**Terminal** (recommended):
```bash
./gradlew run
```
- Server starts on port `8080`
- Open browser: `http://localhost:8080/tasks`

**Or VSCode debugger**:
- Create `.vscode/launch.json`:
  ```json
  {
    "version": "0.2.0",
    "configurations": [
      {
        "type": "kotlin",
        "request": "launch",
        "name": "Run Ktor App",
        "mainClass": "ApplicationKt",
        "preLaunchTask": "gradle: build"
      }
    ]
  }
  ```
- Press F5 to run with debugger attached

### 2.5 Develop and Refresh

- **Template changes**: Save → refresh browser
- **Kotlin changes**: Stop terminal (`Ctrl+C`) → rerun `./gradlew run`

### 2.6 Disable JavaScript for Testing

Same as IntelliJ instructions (browser DevTools settings).

---

## Option 3: University Lab (RHEL9 with VSCode) - On-Campus Preferred

### 3.1 Environment Details

- **OS**: Red Hat Enterprise Linux 9 (RHEL9)
- **IDE**: Visual Studio Code (pre-installed)
- **Java**: Check version with `java -version` in terminal
  - If <17, contact IT support or use local machine setup

### 3.2 Setup Steps

1. **Log in** to university lab machine
2. **Open Terminal** (Applications → Terminal)
3. **Check Java version**:
   ```bash
   java -version
   # Should show 17 or higher
   ```
4. **Clone repo**:
   ```bash
   cd ~/Documents  # or preferred location
   git clone git@github.com:<org>/comp2850-hci-starter.git
   cd comp2850-hci-starter
   ```
5. **Open in VSCode**:
   ```bash
   code .
   ```

### 3.3 Run Application

```bash
./gradlew run
```
- Server starts on `localhost:8080`
- Open Firefox (pre-installed): `http://localhost:8080/tasks`

### 3.4 Development Workflow

Same as local VSCode setup (Section 2.5):
- Template changes: Save → refresh browser
- Kotlin changes: Stop terminal → rerun `./gradlew run`

### 3.5 Important Notes

- **File permissions**: Lab machines may have restricted home directories. Work in `~/Documents` or assigned project space.
- **Network access**: University network may block some external resources. If Gradle dependencies fail to download, try lab WiFi (not ethernet) or contact IT.
- **Session persistence**: Log out properly to avoid leaving ports open. Kill background processes: `pkill -f gradle` before logging out.

---

## Option 4: GitHub Codespaces - Fallback Only

**Use Codespaces only if**:
- Local machine setup fails (incompatible OS, no admin rights)
- University labs unavailable
- Temporary access needed (e.g., working from borrowed machine)

### 4.1 Launch Codespace

1. Open repository on GitHub
2. Click **Code → Codespaces → Create codespace on main**
3. Wait for container to build (~1-2 minutes first time)
4. VSCode in browser appears

### 4.2 Terminal, Build, Run

```bash
./gradlew run
```
- First run downloads dependencies (may take 2-3 minutes)
- Look for `Application started` with port `8080`

### 4.3 Preview the App

1. **Ports** tab (bottom panel) shows port `8080` when server starts
2. Click globe icon → opens forwarded port in new browser tab
3. Leave preview tab open while working

### 4.4 Develop and Refresh

- Edit templates under `src/main/resources/templates/` or Kotlin under `src/main/kotlin/`
- Save → refresh preview tab
- **Kotlin changes**: Stop server (`Ctrl+C`) → rerun `./gradlew run`

### 4.5 Disable JavaScript

- Preview tab → DevTools (F12) → Settings → Disable JavaScript
- Reload page → test server-first path → re-enable for HTMX checks

### 4.6 Git Workflow

- **VSCode UI**: Source Control (left sidebar) for staging/committing/pushing
- **Or terminal**: `git add .`, `git commit -m "..."`, `git push origin main`

### 4.7 Important Codespaces Limitations

- **Idle timeout**: Codespaces pause after 30 minutes inactivity. Reopen from GitHub → re-run server.
- **Storage limits**: Free tier has limited hours/month. Don't leave Codespaces running when not actively working.
- **Performance**: Slower than local machine (network latency for every request)
- **Cost**: Paid tiers required for heavy use. University may not reimburse.

**Recommendation**: Use Codespaces for emergency access only. Switch to local/lab setup ASAP.

---

## Quick Troubleshooting

| Symptom | Fix |
|---------|-----|
| `Address already in use: bind` | Port 8080 already taken. Stop previous server (`pkill -f gradle`) or change port in `src/main/resources/application.conf` (add `ktor.deployment.port = 8081`) |
| `Unsupported class file major version 61` | Wrong JDK. Code compiled with Java 17+ but running with older JDK. Check `java -version` and IDE/Gradle JVM settings. |
| `Unsupported major.minor version` | Same as above - upgrade to JDK 17 minimum, 19+ preferred. |
| Page blank / 500 errors | Check server log. Pebble reports template file + line number for syntax errors. |
| HTMX not firing | 1) Confirm `<script src="https://unpkg.com/htmx.org@1.9.10"></script>` in `_layout.peb`, 2) Check `hx-*` attributes on elements, 3) Browser Network tab: request should return HTML (not JSON/404) |
| Gradle build fails | Run `./gradlew clean build` to reset. If still fails, check `build.gradle.kts` for syntax errors. |
| Port forwarding not working (Codespaces) | Ports tab → right-click `8080` → **Port Visibility → Public**. Re-open preview. |
| Out of memory error | Increase Gradle heap: Add `org.gradle.jvmargs=-Xmx2048m` to `gradle.properties` |

---

## Verification Checklist

Before starting Week 6 labs, verify:

- [ ] **Java version**: `java -version` shows 17 or higher (19+ preferred)
- [ ] **Gradle runs**: `./gradlew --version` succeeds (uses wrapper, no global install needed)
- [ ] **Server starts**: `./gradlew run` completes without errors
- [ ] **Homepage loads**: `http://localhost:8080/tasks` shows task list (may be empty)
- [ ] **Hot reload works**: Edit `src/main/resources/templates/tasks/index.peb` → save → refresh browser → changes appear
- [ ] **No-JS fallback**: Disable JavaScript → reload → homepage still loads (server-rendered HTML)

If all checks pass, you're ready for Week 6 Lab 1.

---

## IDE Preferences Summary

**Recommended order**:
1. **Local IntelliJ IDEA** - Best Kotlin/Gradle support, powerful debugger, free Community edition
2. **Local VSCode** - Lightweight, good for template editing, decent Java/Kotlin support with extensions
3. **University lab VSCode** - RHEL9 machines, pre-configured, good for on-campus work
4. **Codespaces** - Cloud fallback, use only if local/lab options unavailable

**Which should I choose?**
- **Kotlin-heavy weeks** (routes, controllers): IntelliJ recommended (better autocomplete, refactoring)
- **Template-heavy weeks** (Pebble HTML): VSCode or IntelliJ both fine
- **Accessibility testing** (screen readers, keyboard): Local machine preferred (easier to run NVDA/VoiceOver)
- **Quick fixes on the go**: Codespaces (but switch to local for extended work)

**Can I switch environments mid-module?** Yes. Git keeps your code in sync. Clone repo in any environment and continue.

---

## Getting Help

**Setup issues**:
- **MS Teams**: COMP2850 forum → post error message + `java -version` + `./gradlew --version` output
- **Office hours**: Staff can troubleshoot JDK/Gradle issues (check Teams for schedule)
- **TAs in open labs**: Bragg 2.05 (check timetable for TA availability)

**Best practice**: Get setup working in **Week 6 Session 1** (40-minute exploration time). Don't wait until Week 7 - you'll fall behind.
