# Meniscus: guide for coding agents

Meniscus is a Jetpack Compose bottom navigation bar for Android 13+ whose selected tab pinches off like a liquid drop. Library module `meniscus/`, showcase app `sample/`, browser playground `playground/index.html`.

## Layout

- `meniscus/src/main/kotlin/io/github/zainbits/meniscus/`
  - `MeniscusNavBar.kt`: public API (`MeniscusNavBar`, `MeniscusNavItem`, `MeniscusNavBarColors`, `MeniscusNavBarDefaults`), Compose layout, and the AGSL renderer.
  - `MeniscusModel.kt`: pure-Kotlin engine (springs, neck/bridge physics). No Android imports.
  - `MeniscusMotion.kt`: motion constants. No Android imports.
- `meniscus/src/test/`: JVM checks for the engine (spring accuracy, tangency, 60/90/120 Hz independence, settling, interruption, snap).
- `sample/`: showcase app used for demos and screen recordings. All its content is made up.

## Rules

- Entry point: `./master.sh` (doctor, test, build, publish-local, sample, release).
- Explicit API mode is on: every public declaration needs a visibility modifier and KDoc. Keep the public surface small; anything not needed by users stays `internal` or `private`.
- Public API changes need a CHANGELOG entry and a minor version bump while below 2.0; breaking changes need a major bump after 1.0.
- `MeniscusModel` and `MeniscusMotion` must stay free of Android and Compose imports so the tests run on the bare JVM.
- `MeniscusMotion.MAX_SLOTS` is baked into the shader's uniform arrays; change both together.
- After changing the engine or motion constants run `./master.sh test`, and tune visually in `playground/index.html`, which mirrors the engine and shader. Keep the playground in sync.
- Hot paths (frame loop, `drawBehind`, shader uniforms) must not allocate.
- Never commit signing keys, Sonatype tokens or `local.properties`.
