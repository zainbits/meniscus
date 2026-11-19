# Changelog

## Unreleased (feature/liquid-glass)

- `MeniscusNavBar(glass = MeniscusGlass(backdrop))`: the bar as liquid glass over the content behind it (blur, edge lens, lit rim; the selected pill tinted with the selected container colour), through the shared engine `Cohesion`. Without `glass` the bar is unchanged.
- Depends on `Cohesion` (built from `../cohesion` until it is published); Android Gradle Plugin 9.2.1.
- Showcase: Liquid glass switch and clear-to-tinted slider on the Tune screen (glass on by default on this branch).

## 0.1.0 (2026-11-15)

- First public release, extracted from ZnKeyboard and Krewd where the bar has shipped since October 2026.
- `MeniscusNavBar` with 1 to 6 tabs, drawable-resource or `ImageVector` icons, Material 3 `colors()` and label style.
- Pure-Kotlin spring and capillary-bridge engine with JVM tests; AGSL signed-distance renderer.
- Outer corners concentric with the screen's rounded corners, TalkBack tab semantics, RTL, system animation scale.
