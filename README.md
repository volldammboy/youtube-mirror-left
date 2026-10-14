# YouTube Mirror Left

Android app that horizontally mirrors YouTube videos so right-handed crochet tutorials can be followed by left-handed crocheters. It also provides playback speed presets from `0.1x` to `2.0x`.

[Leer en español](README.es.md)

## Features

- In-app YouTube browser using `WebView`, with login persistence for free and Premium content.
- Horizontal mirror toggle that flips the video with `scaleX(-1)` while keeping the player controls usable.
- Playback speed cycle: `0.1x, 0.2x, 0.3x, 0.4x, 0.5x, 0.75x, 1.0x, 1.25x, 1.5x, 2.0x`.
- Material 3 UI.
- Basic fullscreen, back navigation, and reload support.

## Requirements

- Android SDK 36
- JDK 17 or newer
- Gradle Wrapper included in the project

## Build

Debug build:

```sh
./gradlew assembleDebug
```

Release build requires a local `keystore.properties` file. Create it in the project root, but do not commit it:

```properties
storeFile=C:/path/to/your-release.jks
storePassword=...
keyAlias=release
keyPassword=...
```

Then:

```sh
./gradlew assembleRelease bundleRelease
```

Debug APK output (local build only, not published in releases):

```text
app/build/outputs/apk/debug/app-debug.apk
```

Signed release outputs:

```text
app/build/outputs/apk/release/app-release.apk
app/build/outputs/bundle/release/app-release.aab
```

## Install

Sideload the APK on Android. You may need to allow installation from your file manager (for example, Mixplorer) in Android settings.

## Notes

- Login happens inside the app's `WebView`, so account access and Premium content depend on YouTube's web session.
- Mirroring inverts text, patterns, and other on-screen elements inside the video. It is useful for hand movement, but not for text-heavy patterns or designs with directional lateral detail.
- Orientation matters: right-handed crochet is typically shown right-to-left, while left-handed work goes left-to-right.

## Release signing

Generate a release keystore and keep it and its password outside the repository. `keystore.properties` is already gitignored.

Example:

```sh
keytool -genkeypair -v \
  -keystore my-release.jks \
  -alias release \
  -keyalg RSA -keysize 2048 -validity 10000
```

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/M4A0285O9U)
