# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android application project designed for personal learning and testing of various Android APIs and components. The app is organized into three main categories:
- Widget: UI components and custom views
- System: System-level features like alarms, location, battery, etc.
- Library: Third-party library integrations

## Codebase Structure

```
AndroidCodeStudy/
├── app/                 # Main application module
├── mole/base/          # Shared library module with network and utility components
├── ksp/                # Kotlin Symbol Processing module for code generation
└── libs.versions.toml  # Centralized dependency version management
```

### Key Components

1. **Main Entry Point**: `MainActivity` with bottom navigation to three main fragments
2. **Dependency Injection**: Hilt for dependency injection
3. **Networking**: Retrofit with Gson for REST API calls (in base module)
4. **Architecture**: MVVM with ViewModels and LiveData
5. **Reactive Programming**: RxJava integration
6. **UI Frameworks**: Support for both traditional Views and Jetpack Compose
7. **Testing**: JUnit for unit tests, Espresso for instrumentation tests

## Common Development Tasks

### Building the Project
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug APK to connected device
./gradlew installDebug
```

### Running Tests
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run tests for specific module
./gradlew :app:testDebugUnitTest
```

### Code Generation
```bash
# Run KSP code generation
./gradlew kspDebugKotlin
```

### Linting
```bash
# Run lint checks
./gradlew lint
```

## Key Technologies and Libraries

- **Core**: Kotlin, AndroidX, Material Design
- **Networking**: Retrofit, Gson
- **DI**: Hilt (Dagger)
- **Reactive**: RxJava
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Component
- **Background Work**: WorkManager
- **Database**: Room (implied, not directly used)
- **Code Generation**: Kotlin Symbol Processing (KSP)
- **Testing**: JUnit, Espresso
- **Utilities**: Lombok, AutoValue

## Architecture Patterns

1. **MVVM**: ViewModels for UI-related data, LiveData for reactive updates
2. **Repository Pattern**: Network layer abstraction in base module
3. **Navigation**: Single Activity with multiple Fragments architecture
4. **Dependency Injection**: Hilt for managing dependencies across modules
5. **View Binding**: Type-safe view access in Activities and Fragments

## Module Dependencies

```
app --> mole:base
app --> ksp
```

The app module depends on the base module for shared utilities and networking, and on the ksp module for code generation capabilities.

## Development Environment

- **Minimum SDK**: 21 (Android 5.0)
- **Target SDK**: 33 (Android 13)
- **Compile SDK**: 34 (Android 14)
- **JVM Target**: Java 17
- **Kotlin**: 1.9.0
- **Build System**: Gradle with Kotlin DSL

## Important Notes

1. The project uses version catalogs (`libs.versions.toml`) for centralized dependency management
2. View Binding is enabled for type-safe view access
3. Jetpack Compose is enabled alongside traditional Views
4. The application includes extensive logging in base Activity/Fragment classes
5. Custom Application class initializes various tools including crash handling
6. Extensive permission declarations in AndroidManifest.xml for various system features