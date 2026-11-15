# Releasing Meniscus

Releases go to Maven Central as `io.github.zainbits:meniscus`. Everything here is free.

## One-time setup

1. Sign in to https://central.sonatype.com with the `zainbits` GitHub account. The `io.github.zainbits` namespace is verified automatically for GitHub sign-ins.
2. In the portal, generate a user token (Account > Generate User Token). It has a username and a password.
3. Create a GPG key for signing and publish its public half to a key server:
   ```sh
   gpg --quick-gen-key "Zain Shaikh <EMAIL>" ed25519 sign 3y
   gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>
   gpg --export-secret-keys --armor <KEY_ID>   # value for SIGNING_KEY
   ```
4. Add four repository secrets on GitHub (Settings > Secrets and variables > Actions):
   `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD` (the token), `SIGNING_KEY` (armored private key), `SIGNING_KEY_PASSWORD`.

## Each release

1. Set `VERSION_NAME` in `gradle.properties` and move the CHANGELOG's "unreleased" heading to the version.
2. Commit, then tag and push: `git tag v0.1.0 && git push origin v0.1.0`.
3. The Release workflow uploads a signed deployment. Open https://central.sonatype.com/publishing/deployments, check it validated, and press Publish. It appears on Maven Central within about 30 minutes.

To release from a machine instead of CI, put the same four values in `~/.gradle/gradle.properties` as `mavenCentralUsername`, `mavenCentralPassword`, `signingInMemoryKey`, `signingInMemoryKeyPassword` and run `./master.sh release`.
