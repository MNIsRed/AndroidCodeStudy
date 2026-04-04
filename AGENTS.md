# Repository Guidelines

## Project Structure & Module Organization
- Root Gradle build with Java 17/Kotlin 1.9. Modules:
  - `app/` — Android application (Kotlin + Java), resources in `app/src/main/res`.
  - `mole/base/` — reusable library module.
  - `ksp/` — KSP helper/library.
- Tests:
  - Unit tests: `app/src/test/kotlin/...` (JUnit4).
  - Instrumented tests: `app/src/androidTest/kotlin/...` (AndroidX Test/Espresso).

## Build, Test, and Development Commands
- Build debug APK: `./gradlew :app:assembleDebug` (Windows: `gradlew.bat ...`).
- Install & run on device: use Android Studio Run on `app` or `adb install` from `app/build/outputs/apk/...`.
- Unit tests: `./gradlew test` or `./gradlew :app:testDebugUnitTest`.
- Instrumented tests (device/emulator): `./gradlew :app:connectedDebugAndroidTest`.
- Lint (Android Lint): `./gradlew :app:lintDebug`.
- Clean: `./gradlew clean`.

## Coding Style & Naming Conventions
- Indentation: Kotlin/Java 4 spaces; XML 2 spaces. Max line length ~120.
- Kotlin-first (null-safety, data classes); prefer `val` over `var`.
- Naming: 
  - Packages: `lowercase.with.dots` (e.g., `com.mole.androidcodestudy`).
  - Classes/Objects: `PascalCase` (e.g., `MainActivity`).
  - Functions/vars: `camelCase`.
  - Resources: `lowercase_underscores` (e.g., `activity_main.xml`, `ic_back.xml`), IDs `lowercase_underscores`.
- Organize files under `feature/...` or `ui/`, `data/`, `di/` as appropriate; keep Hilt modules in `di/`.

## Testing Guidelines
- Frameworks: JUnit4 (unit), AndroidX Test + Espresso (instrumented).
- Place tests under matching package path; suffix with `Test` (e.g., `RxJavaFunctionTest`).
- Run locally with the commands above; ensure tests pass on CI (when applicable).
- Aim for meaningful coverage around business logic; write fast unit tests for Rx/Coroutines where possible.

## Commit & Pull Request Guidelines
- Commits: use concise prefixes (`feat:`, `fix:`, `docs:`, `test:`, `refactor:`, `chore:`); one change per commit. Short imperative subject; details in body if needed.
- PRs: clear description, scope of change, affected modules, test evidence (logs/screenshots for UI), and linked issues. Ensure `assembleDebug`, `test`, and `lint` succeed before requesting review.

## Security & Configuration Tips
- Do not commit secrets or local paths. Keep SDK paths in `local.properties`; shared settings in `gradle.properties`.
- Use Java 17 toolchain (configured); open and run via Android Studio Giraffe+.
