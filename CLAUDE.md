# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is

JaRIKo is a JVM-based interpreter for the RPG programming language (IBM AS/400 RPG), written in Kotlin. It is used as a library: RPG source code goes in, gets parsed and executed, results come out. It is part of Sme.UP's Open Architecture platform.

## Build commands

```bash
# Full build (compiles + runs all non-performance tests + MUTE tests)
./gradlew check

# Compile only
./gradlew build

# Regenerate ANTLR grammar parser (needed when .g4 files change)
./gradlew generateGrammarSource

# Auto-format Kotlin code
./gradlew ktlintFormat

# Check formatting
./gradlew ktlintCheck

# Run all tests including performance
./gradlew testAll

# Run performance tests only
./gradlew testPerformance

# Run MUTE annotation tests (default dir: mutes_for_ci/)
./gradlew runMutes

# Run MUTE tests from a custom directory
./gradlew runMutes -PmuteDir="/path/to/mutes"

# Build fat JAR (includes all dependencies)
./gradlew fatJar
# Output: rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar

# Install to local Maven repo (useful when depending on jariko from another project)
./gradlew publishToMavenLocal
```

### Running a single test

```bash
# Run a single test class
./gradlew :rpgJavaInterpreter-core:test --tests "com.smeup.rpgparser.evaluation.InterpreterTest"

# Run a single test method
./gradlew :rpgJavaInterpreter-core:test --tests "com.smeup.rpgparser.evaluation.InterpreterTest.someTestName"
```

## Architecture

### Submodules

- **`rpgJavaInterpreter-core`** — the main module; contains grammar, parser, AST, interpreter, and all tests
- **`kolasu`** — local copy of the Strumenta Kolasu AST framework (used for AST node modeling and traversal)
- **`dspfparser`** — parser for DSPF (display file) format
- **`examples`** — sample programs showing library usage

### Execution pipeline

```
RPG source (.rpgle)
  → ANTLR Lexer/Parser  (generated-src/antlr/main, grammars in src/main/antlr)
  → ParseTree
  → parsetreetoast/      (converts parse tree → AST nodes)
  → CompilationUnit      (root AST node, in parsing/ast/)
  → resolveAndValidate() (symbol resolution and semantic validation)
  → RpgProgram           (wraps CompilationUnit, entry point for execution)
  → InternalInterpreter  (walks the AST and executes statements)
```

### Key packages in `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/`

| Package | Purpose |
|---|---|
| `parsing/ast/` | AST node definitions (`CompilationUnit`, `Statement`, `Expression`, etc.) |
| `parsing/facade/` | `RpgParserFacade` — main entry to parse RPG source into a parse tree/AST |
| `parsing/parsetreetoast/` | Converts ANTLR parse tree to Kolasu AST nodes |
| `interpreter/` | `InternalInterpreter` — visits AST and executes; `SystemInterface` — I/O abstraction; type system (`typesystem.kt`, `values.kt`); `SymbolTable` |
| `execution/` | `MainExecutionContext` — thread-local context for a program run; `Configuration` — runtime options |
| `rpginterop/` | `RpgProgramFinder` / `DirRpgProgramFinder` — how programs are located at runtime; `RpgFacade` — Java-friendly wrapper |
| `jvminterop/` | `JavaSystemInterface` — concrete `SystemInterface` impl for JVM usage |
| `mute/` | MUTE test runner |
| `logging/` | Configurable log channels (`DATA`, `LOOP`, `STATEMENT`, `PERFORMANCE`, `ERROR`, etc.) |
| `serialization/` | AST and symbol table serialization |
| `experimental/` | `ExperimentalFeaturesFactory` — opt-in experimental features |

### Core interfaces / extension points

- **`SystemInterface`** (`interpreter/system_interface.kt`) — implement this to customize I/O, program lookup, copy file resolution, function resolution, and logging. All external interactions go through here.
- **`Program`** (`interpreter/program.kt`) — interface for anything callable by RPG's CALL opcode. `RpgProgram` is the RPG impl; JVM classes can implement it directly for "doping" (replacing RPG programs with JVM implementations at runtime).
- **`RpgProgramFinder`** (`rpginterop/rpg_system.kt`) — pluggable strategy for locating RPG source files by name.
- **`IFeaturesFactory`** (`interpreter/IFeaturesFactory.kt`) — factory for interpreter subsystems; enables feature flags and experimental features. Select via `-Djariko.featuresFactory=default|experimental|<classname>`.

### Test patterns

All tests extend **`AbstractTest`** which handles setup/teardown of `MainExecutionContext` and logging. The `AbstractTest.outputOf(programName)` method loads an `.rpgle` file from `src/test/resources/`, runs it, and returns the `DSPLY` output as a list of strings — this is the dominant test pattern.

Tests come in pairs: a base class (e.g. `InterpreterTest`) and a `*Compiled` subclass that overrides `useCompiledVersion() = true` to run the same tests against pre-compiled (serialized AST) programs.

RPG test programs live in `rpgJavaInterpreter-core/src/test/resources/`. `QILEGEN/` contains shared copy members (`/COPY` sources).

### MUTE annotations

RPG files can contain inline test assertions (lines starting with `MU*`). These are parsed separately and verified after execution. `mutes_for_ci/` contains MUTE files run on every `./gradlew check`. See `docs/mute.md` for syntax.

### Feature flags

Runtime feature flags are toggled via system properties or `Configuration.jarikoCallback.featureFlagIsOn`. Current flags: `jariko.features.UnlimitedStringTypeFlag`, `jariko.features.ChainCacheFlag`, `jariko.features.ZAddLegacyFlag`.
