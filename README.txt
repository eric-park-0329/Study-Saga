# StudySaga (MVP)

This is a minimal Android app module (Kotlin + Jetpack Compose + Hilt + Room) for the "StudySaga" study habit RPG.
It supports:
- Add habits
- List habits on Home
- Complete a habit to earn XP and increment streaks
- Simple 25-minute timer screen (no persistence yet)

## How to run
Option A (recommended):
1) In Android Studio, create a new **Empty Activity (Compose)** project named `StudySaga` with package `com.studysaga.app` and min SDK 24+.
2) Close the project.
3) Replace the generated `app/` folder with the `app/` folder in this archive.
4) Open the project again and **Sync Gradle**. Run on an emulator or device.

Option B (standalone):
- Open the folder containing `settings.gradle.kts` in Android Studio and let it set up Gradle. (You may need to create gradle wrapper using your IDE).

## Next steps
- Add a dedicated Progress ViewModel and screen to display level, XP bar, and badges.
- Add reminders with WorkManager and a cadence/schedule for habits.
- Persist timer sessions and convert minutes to XP dynamically.
- Add badges and a cosmetic shop using coins.
