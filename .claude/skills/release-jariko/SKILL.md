---
name: release-jariko
description: Cuts a new jariko release using git-flow, pinned to the latest smeup/reload release. Use when the user asks to "cut a release", "release jariko", "start a new jariko release", "make a new jariko version", "bump the jariko version", or references jariko's release checklist/process. Requires git-flow, an authenticated gh CLI, and a clean working tree on develop.
metadata:
  author: lanarimarco@gmail.com
  version: "1.0"
---

# Release jariko

Cuts a new jariko release: bumps `jarikoVersion` and pins `reloadVersion` to the
latest released version of [smeup/reload](https://github.com/smeup/reload), runs
the build, finishes the git-flow release, creates the GitHub release, and resets
`develop` back to a snapshot. Follow the steps in order. **Never run `git push`
yourself** — the two push steps are explicitly manual; stop and wait for the user
to confirm they ran the command.

## Preconditions

Check all of these before starting, and stop with a clear explanation if any fail:

- Current branch is `develop`: `git rev-parse --abbrev-ref HEAD`.
- Working tree is clean: `git status --porcelain` must be empty. If not, stop and
  ask the user to commit or stash first — do not stash or discard on their behalf.
- `git flow version` succeeds (git-flow is installed/initialized in this repo).
- `gh auth status` succeeds (gh CLI is authenticated).

## Step 1 — Determine the release version

Show the user the current `jarikoVersion` from `gradle.properties` and the latest
local `v*` tag (`git tag -l "v*" --sort=-v:refname | head -1`) as reference, then
ask which version to release (`X.Y.Z`, no `v` prefix — git-flow adds it). Do not
guess or auto-pick it.

## Step 2 — Determine the reload version to pin

Fetch reload's latest release tag:

```bash
gh api repos/smeup/reload/releases/latest --jq '.tag_name'
```

Strip a leading `v` (e.g. `v2.0.0` → `2.0.0`). This is a real, non-SNAPSHOT Maven
coordinate version consumed by `rpgJavaInterpreter-core/build.gradle` (the `io.github.smeup.reload:*`
dependencies) — reload is not a submodule of this repo, just a remote artifact.
Show the resolved version to the user and ask them to confirm it or provide a
different one before continuing.

## Step 3 — Start the release branch

```bash
git flow release start <X.Y.Z>
```

## Step 4 — Bump `gradle.properties`

Edit `gradle.properties`:

- `jarikoVersion=v<X.Y.Z>` (**with** the `v` prefix — matches the `gitflow.prefix.versiontag=v`
  configured for this repo and every past release commit).
- `reloadVersion=<reload version from Step 2>` (**no** `v` prefix).

```bash
git add gradle.properties
```

## Step 5 — Build and verify

```bash
./gradlew ktlintCheck
./gradlew clean check
```

If either fails, stop and report the failure — do not continue to the commit step
until both are clean.

## Step 6 — Commit the bump

```bash
git commit -m "bump v<X.Y.Z>"
```

This matches the historical commit message convention used for every past release
(`bump v2.0.0`, `bump v2.1.0`, ...).

## Step 7 — Finish the release

```bash
git flow release finish -m "v<X.Y.Z>" <X.Y.Z>
```

This merges `release/<X.Y.Z>` into `master`, tags the merge commit `v<X.Y.Z>`,
merges the release back into `develop`, and leaves the working tree on `develop`.

## Step 8 — STOP: manual push of master + tag

```bash
git checkout master
```

Print exactly these two commands and ask the user to run them himself. **Do not
run `git push` yourself.** Wait for the user's explicit confirmation that both
pushes are done before moving to Step 9.

```bash
git push origin master
git push origin v<X.Y.Z>
```

Note for the user: pushing `master` triggers `.github/workflows/publish.yml` and
`publish-smeup.yml`, which publish the real release artifacts to Maven Central and
the internal Nexus using the pinned (non-SNAPSHOT) version.

## Step 9 — Create the GitHub release

Only after the user confirms both pushes from Step 8 are done:

```bash
gh release create v<X.Y.Z> --title v<X.Y.Z> --generate-notes
```

This replaces the manual "New release" GitHub web page flow (tag, generated
release notes, publish) with the equivalent `gh` command against the tag just
pushed.

## Step 10 — Reset develop to the next snapshot

```bash
git checkout develop
```

Edit `gradle.properties` back to:

- `jarikoVersion=develop-SNAPSHOT`
- `reloadVersion=develop-SNAPSHOT`

```bash
git add gradle.properties
git commit -m "bump develop-SNAPSHOT"
```

## Step 11 — STOP: manual push of develop

Print exactly this command and ask the user to run it himself. **Do not run
`git push` yourself.**

```bash
git push origin develop
```

Note for the user: this triggers the same publish workflows again, publishing the
next `develop-SNAPSHOT` build.

## Summary

Once both pushes are confirmed and the GitHub release is created, recap for the
user: the release version, the pinned reload version, the GitHub release URL
(from the `gh release create` output), and confirmation that `develop` is back on
`develop-SNAPSHOT`.
