#!/usr/bin/env bash
# Meniscus's one entry point. Run with no arguments for a menu, or name an action:
#   ./master.sh doctor | test | build | publish-local | sample | release | agent-prompt | help
set -uo pipefail
REPO="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$REPO"
B=$'\e[1m'; G=$'\e[32m'; Y=$'\e[33m'; R=$'\e[31m'; N=$'\e[0m'
[[ -t 1 ]] || { B=; G=; Y=; R=; N=; }
say()  { printf '%s\n' "$*"; }
ok()   { printf '  %sok%s    %s\n' "$G" "$N" "$1"; }
warn() { printf '  %swarn%s  %s\n' "$Y" "$N" "$1"; [[ -n "${2:-}" ]] && printf '        fix: %s\n' "$2"; }
bad()  { printf '  %sFAIL%s  %s\n' "$R" "$N" "$1"; [[ -n "${2:-}" ]] && printf '        fix: %s\n' "$2"; FAILS=$((FAILS + 1)); }
has()  { command -v "$1" >/dev/null 2>&1; }
version() { sed -n 's/^VERSION_NAME=//p' gradle.properties; }

doctor() {
  FAILS=0
  say "${B}Toolchain${N}"
  if [[ -n "${JAVA_HOME:-}" && -x "$JAVA_HOME/bin/java" ]] || has java; then ok "Java ($("${JAVA_HOME:+$JAVA_HOME/bin/}java" -version 2>&1 | head -1))"; else bad "no JDK" "install JDK 21 (Temurin) and set JAVA_HOME"; fi
  if [[ -n "${ANDROID_HOME:-}" && -d "$ANDROID_HOME" ]] || [[ -f local.properties ]]; then ok "Android SDK"; else bad "no Android SDK" "set ANDROID_HOME or write sdk.dir=... to local.properties"; fi
  has adb && ok "adb (for ./master.sh sample)" || warn "adb not on PATH" "only needed to install the showcase app"
  say "${B}Release${N} (only needed for Maven Central)"
  local props="$HOME/.gradle/gradle.properties"
  for k in mavenCentralUsername mavenCentralPassword signingInMemoryKey signingInMemoryKeyPassword; do
    if grep -q "^$k=" "$props" 2>/dev/null || [[ -n "$(printenv "ORG_GRADLE_PROJECT_$k")" ]]; then ok "$k is set"; else warn "$k is not set" "see RELEASING.md"; fi
  done
  say "Version: $(version)"
  [[ $FAILS -eq 0 ]]
}

test_() { ./gradlew :meniscus:testDebugUnitTest "$@"; }
build() { ./gradlew :meniscus:testDebugUnitTest :meniscus:assembleRelease :sample:assembleDebug "$@"; }
publish_local() { ./gradlew :meniscus:publishToMavenLocal "$@" && say "Published io.github.zainbits:meniscus:$(version) to ~/.m2"; }
sample() { ./gradlew :sample:installDebug && adb shell am start -n io.github.zainbits.meniscus.sample/.MainActivity; }
release() {
  doctor || { say "Fix the release settings first (RELEASING.md)."; return 1; }
  say "This uploads io.github.zainbits:meniscus:$(version) to the Maven Central portal (you still press Publish there)."
  read -r -p "Continue? [y/N] " r; [[ "$r" =~ ^[Yy] ]] || return 1
  ./gradlew :meniscus:testDebugUnitTest :meniscus:publishToMavenCentral --no-configuration-cache
}

agent_prompt() {
  local prompt="You are working on Meniscus, an Android Jetpack Compose library ($REPO). Read AGENTS.md first, then run ./master.sh doctor."
  if has pbcopy; then printf '%s' "$prompt" | pbcopy && say "Prompt copied to the clipboard.";
  elif has wl-copy; then printf '%s' "$prompt" | wl-copy && say "Prompt copied to the clipboard.";
  else say "$prompt"; fi
}

usage() {
  cat <<USAGE
${B}Meniscus${N} $(version)
  doctor         check JDK, Android SDK, adb and release settings
  test           run the engine unit tests
  build          tests + library AAR + showcase APK
  publish-local  publish to ~/.m2 to try it in another app
  sample         install and open the showcase app on a connected phone
  release        upload a signed release to Maven Central (see RELEASING.md)
  agent-prompt   copy a starter prompt for a coding agent
USAGE
}

menu() {
  usage; read -r -p "Action: " a; [[ -n "$a" ]] && dispatch "$a"
}

dispatch() {
  local action="${1:-}"; shift || true
  case "$action" in
    doctor) doctor ;;
    test) test_ "$@" ;;
    build) build "$@" ;;
    publish-local) publish_local "$@" ;;
    sample) sample ;;
    release) release ;;
    agent-prompt|agent) agent_prompt ;;
    help|-h|--help) usage ;;
    "") menu ;;
    *) say "Unknown action '$action'."; usage; return 1 ;;
  esac
}

dispatch "$@"
