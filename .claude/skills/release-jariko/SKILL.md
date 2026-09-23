---
name: release-jariko
description: Cuts a new jariko release using git-flow, pinned to the latest smeup/reload release. Use when the user asks to "cut a release", "release jariko", "start a new jariko release", "make a new jariko version", "bump the jariko version", or references jariko's release checklist/process. Requires git-flow, an authenticated gh CLI, and a clean working tree on develop.
metadata:
  author: lanarimarco@gmail.com
  version: "1.1"
---

# Release jariko

Cuts a new jariko release: picks the next version (patch, minor or major), pins
`reloadVersion` to the latest released version of
[smeup/reload](https://github.com/smeup/reload), runs the build, finishes the
git-flow release, creates the GitHub release, and resets `develop` to a snapshot.

Rules that apply throughout:

- Follow the steps in order.
- **Never run `git push` yourself.** The two push steps are manual: print the
  commands, then wait for the user to confirm they ran them.
- Two values need explicit user confirmation via `AskUserQuestion` — the release
  version (Step 1) and the reload version (Step 2). Silence or earlier approval
  is never confirmation.

## Preconditions

Check all of these before starting; stop with a clear explanation if any fail:

| Check | Command |
|---|---|
| On `develop` | `git rev-parse --abbrev-ref HEAD` |
| Clean working tree | `git status --porcelain` (must be empty; if not, ask the user to commit or stash — never stash or discard on their behalf) |
| git-flow available | `git flow version` |
| gh authenticated | `gh auth status` |

## Step 1 — Choose the release version (suggest, then confirm)

### 1a. Gather context

```bash
grep '^jarikoVersion' gradle.properties
LAST_TAG=$(git describe --tags --abbrev=0 --match 'v*')
echo "$LAST_TAG"
git log "$LAST_TAG"..HEAD --no-merges --format='%h %s'
```

`LAST_TAG` is the reference version (`vX.Y.Z`). If there are no commits since it
(ignoring `bump ...` commits), tell the user there is nothing to release and stop.

### 1b. Propose a bump type

Classify the commits since `LAST_TAG` (they follow conventional-commit style,
possibly prefixed by an emoji) and suggest the **highest** applicable level:

| Level | When to suggest | Example (from `2.2.0`) |
|---|---|---|
| **major** | Any breaking change: `!` after the type/scope, a `BREAKING CHANGE` footer, or a removed/renamed public API or `SystemInterface`/`Program` contract change | `3.0.0` |
| **minor** | At least one `feat` (new RPG opcode, built-in function, feature flag, public API addition) and no breaking change | `2.3.0` |
| **patch** | Only `fix`, `perf`, `refactor`, `docs`, `test`, `chore`, `build`, `ci` | `2.2.1` |

If commit messages are ambiguous, inspect the diff of public API areas
(`interpreter/system_interface.kt`, `interpreter/program.kt`, `rpginterop/`,
`execution/Configuration`) before deciding. When unsure between two levels, suggest
the lower one and say why.

### 1c. Confirm with the user

Use `AskUserQuestion` with three options — patch, minor, major — each labelled with
the resulting version (e.g. `Minor → 2.3.0`). Put the suggested level first and mark
it "(Recommended)". In the question text, show the current version, `LAST_TAG`, and a
short summary of the commits that justify the suggestion (notably any `feat` or
breaking change). Allow "Other" for a custom `X.Y.Z`.

Use the confirmed version as `<X.Y.Z>` from here on (no `v` prefix — git-flow adds
it). Never pick it without the user's answer.

## Step 2 — Choose the reload version to pin (confirm)

```bash
gh api repos/smeup/reload/releases/latest --jq '.tag_name,.html_url'
```

Strip a leading `v` from the tag (`v2.0.0` → `2.0.0`). This is a real, non-SNAPSHOT
Maven version consumed by `rpgJavaInterpreter-core/build.gradle` (the
`io.github.smeup.reload:*` dependencies); reload is a remote artifact, not a
submodule.

**Mandatory confirmation gate.** Do not continue until the user confirms. Use
`AskUserQuestion` with the retrieved version as the first option ("Recommended"),
plus "Other" for a different version. State that the value was retrieved
automatically from the latest `smeup/reload` GitHub release and show the release URL
so the user can verify it. If the user supplies another version, use it verbatim
(minus any leading `v`).

## Step 3 — Prepare the release branch

```bash
git flow release start <X.Y.Z>
```

Edit `gradle.properties`:

- `jarikoVersion=v<X.Y.Z>` — **with** the `v` prefix (matches
  `gitflow.prefix.versiontag=v` and every past release commit).
- `reloadVersion=<reload version>` — **no** `v` prefix.

```bash
git add gradle.properties
```

## Step 4 — Build and verify

```bash
./gradlew ktlintCheck
./gradlew clean check
```

If either fails, stop and report the failure. Do not commit until both are clean.

## Step 5 — Commit and finish the release

```bash
git commit -m "bump v<X.Y.Z>"
git flow release finish -m "v<X.Y.Z>" <X.Y.Z>
```

The commit message matches every past release (`bump v2.0.0`, `bump v2.1.0`, ...).
`release finish` merges `release/<X.Y.Z>` into `master`, tags the merge commit
`v<X.Y.Z>`, merges back into `develop`, and leaves the tree on `develop`.

## Step 6 — STOP: manual push of master + tag

```bash
git checkout master
```

Print exactly these commands for the user to run, then wait for explicit
confirmation that both are done:

```bash
git push origin master
git push origin v<X.Y.Z>
```

Note for the user: pushing `master` triggers `.github/workflows/publish.yml` and
`publish-smeup.yml`, which publish the real release artifacts to Maven Central and
the internal Nexus using the pinned (non-SNAPSHOT) version.

## Step 7 — Create the GitHub release

Only after the user confirms the pushes from Step 6:

```bash
gh release create v<X.Y.Z> --title v<X.Y.Z> --generate-notes
```

## Step 8 — Reset develop to the next snapshot

```bash
git checkout develop
```

Edit `gradle.properties` back to `jarikoVersion=develop-SNAPSHOT` and
`reloadVersion=develop-SNAPSHOT`, then:

```bash
git add gradle.properties
git commit -m "bump develop-SNAPSHOT"
```

## Step 9 — STOP: manual push of develop

Print exactly this command for the user to run, and wait for confirmation:

```bash
git push origin develop
```

Note for the user: this triggers the same publish workflows, publishing the next
`develop-SNAPSHOT` build.

## Summary

Once both pushes are confirmed and the GitHub release exists, recap: the release
version (and bump type), the pinned reload version, the GitHub release URL (from the
`gh release create` output), and confirmation that `develop` is back on
`develop-SNAPSHOT`.
