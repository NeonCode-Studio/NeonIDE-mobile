# NeonIDE Mobile

**A lightweight mobile coding environment by NeonCode Studio.**

NeonIDE Mobile is an experimental Android IDE project designed to bring basic coding tools to mobile devices.

The application combines a WebView-based interface with a native Android component to provide a simple environment for editing and experimenting with code.

## Features

- **Code editor** – edit code directly on your Android device.
- **C++ support** – compile C++ code online using the Wandbox API.
- **HTML support** – write HTML and preview your pages inside the app.
- **Syntax highlighting** – basic highlighting for supported languages.
- **File management** – work with multiple files and tabs.
- **Autosave** – editor data is stored locally.
- **Backup and restore** – export and import project data as JSON.
- **Customizable settings** – configure editor options and compiler selection.

## Current status

<em>Early development / Experimental</em>

NeonIDE Mobile is currently a work in progress.

The application can be built into an Android APK and launched on a device. However, some features are incomplete or may not work as expected.

The interface, application icon, editor functionality and input handling still need improvements.

This project is published as an early development build, not as a finished product.

## Technology

- Java
- Android SDK
- HTML
- CSS
- JavaScript
- Android WebView
- Wandbox API
- Gradle

## Building

The project uses Gradle and Android Gradle Plugin.

To build a debug APK locally:

```bash
gradle assembleDebug
```

The generated APK should be available at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

GitHub Actions is also configured to build the APK automatically.

## Project structure

```text
NeonIDE/
├── .github/
│   └── workflows/
│       └── build.yml
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── assets/
│       │   └── index.html
│       ├── java/
│       │   └── pl/codenote/
│       │       └── MainActivity.java
│       └── AndroidManifest.xml
├── build.gradle
├── gradle.properties
└── settings.gradle
```

## Developer

**NeonCode Studio**

GitHub: https://github.com/NeonCode-Studio

## License

No license has been specified yet.

---

*NeonIDE Mobile — Code beyond the desktop.*
