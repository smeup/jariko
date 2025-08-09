# JaRIKo: Java RPG Interpreter written in Kotlin

JaRIKo is a cross-platform interpreter for the RPG programming language that runs on the JVM. It interprets RPG programs and provides features like doping (runtime program replacement), MUTE testing framework, and comprehensive logging.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Prerequisites
- **Java 17 or higher** is required. The project uses Java 17 as the target JVM version.
- **Linux/Unix environment** recommended (Windows users should use WSL or similar)

### Bootstrap and Build Commands
Execute these commands in order to set up the development environment:

1. **Generate grammar source** (ANTLR parser generation):
   ```bash
   ./gradlew generateGrammarSource
   ```
   - Takes approximately **1 minute**
   - **NEVER CANCEL** - let this complete fully

2. **Build the entire project**:
   ```bash
   ./gradlew build
   ```
   - Takes approximately **8-10 minutes** - **NEVER CANCEL**
   - Set timeout to **15+ minutes** when using this command
   - Compiles all modules: rpgJavaInterpreter-core, examples, kolasu, dspfparser
   - Runs all non-performance tests (2600+ tests)

3. **Run tests** (non-performance):
   ```bash
   ./gradlew check
   ```
   - Takes approximately **6-8 minutes** - **NEVER CANCEL**
   - Set timeout to **12+ minutes** when using this command
   - Includes Kotlin formatting checks and all unit tests

4. **Run performance tests**:
   ```bash
   ./gradlew testPerformance
   ```
   - Takes approximately **15-20 minutes** - **NEVER CANCEL**
   - Set timeout to **30+ minutes** when using this command
   - Many tests may show timeouts/failures - this is normal for performance tests

5. **Create executable JAR**:
   ```bash
   ./gradlew fatJar
   ```
   - Takes approximately **7-10 seconds**
   - Creates `rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar` (50MB)

6. **Create MUTE testing JAR**:
   ```bash
   ./gradlew fatMuteJar
   ```
   - Takes approximately **7-10 seconds**
   - Creates `rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-mute-all.jar`

### Code Quality Commands
Always run these before committing changes:

```bash
./gradlew ktlintCheck    # Check Kotlin code formatting (1 second)
./gradlew ktlintFormat   # Auto-format Kotlin code (1-2 seconds)
```

## Running RPG Programs

### Command Line Execution
```bash
# Run specific RPG program
java -jar rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar -psd <search-dirs> <program-name> [args]

# Example
java -jar rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar -psd examples/src/main/resources/rpg ECHO_PGM
```

### Interactive Shell Mode
```bash
# Start interactive shell (omit program name)
java -jar rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar -psd examples/src/main/resources/rpg
```
Commands in shell: `help`, `<program-name>`, `play`, `stop`, `exit`

### MUTE Testing
```bash
# Run MUTE tests
java -jar rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-mute-all.jar <rpgle-file> <program-search-dir>

# Example
java -jar rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-mute-all.jar examples/src/main/resources/rpg/MUTE10_75.rpgle examples/src/main/resources/rpg
```

## Validation and Testing

### Always validate changes by:
1. **Building the project**: `./gradlew build` (8-10 minutes)
2. **Running code checks**: `./gradlew check` (6-8 minutes)
3. **Formatting check**: `./gradlew ktlintCheck` (1 second)
4. **Testing RPG execution**: Run a simple RPG program: `java -jar rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar -psd examples/src/main/resources/rpg ECHO_PGM`
5. **Testing interactive shell**: Verify shell mode works with `help` command

### For CI/CD compatibility:
The GitHub Actions workflow runs:
1. `./gradlew ktlintCheck` - Kotlin formatting validation
2. `./gradlew check` - Build and test validation

Always run these locally before pushing changes.

## Repository Structure

### Key Modules
- **rpgJavaInterpreter-core** - Main interpreter implementation
- **examples** - Sample RPG programs and test cases
- **kolasu** - AST processing library (Kotlin-based)
- **dspfparser** - Display file format parser
- **docs/** - Comprehensive documentation
- **misc/** - Utilities and Docker support

### Important Files
- `build.gradle` - Main build configuration
- `gradle.properties` - Version and JVM configuration (Java 17)
- `settings.gradle` - Multi-module project setup
- `.github/workflows/unit-test.yml` - CI pipeline

### Development Files and Locations
- **RPG examples**: `examples/src/main/resources/rpg/`
- **Test files**: `examples/src/test/resources/rpg/`
- **MUTE tests**: Files starting with `MUTE*.rpgle`
- **Documentation**: `docs/` (development.md, logging.md, mute.md, etc.)

## Advanced Features

### Logging Configuration
Use `-lc` or `--log-configuration` with a config file to enable detailed logging:
```bash
java -jar rpgJavaInterpreter-core-all.jar -lc /path/to/logging.config -psd <dirs> <program>
```

### Performance Profiling
```bash
./gradlew profileRpgProgram -PrpgProgram=path_to_rpg_program
```

### Feature Flags
Control interpreter behavior with system properties:
- `jariko.features.UnlimitedStringTypeFlag`
- `jariko.features.ChainCacheFlag` 
- `jariko.features.ZAddLegacyFlag`

## Common Issues and Solutions

### Java Version Compatibility
- **Error**: "No matching variant" or Java compatibility issues
- **Solution**: Ensure Java 17+ is installed and `JAVA_HOME` is set correctly

### Build Failures
- **Error**: Grammar generation fails
- **Solution**: Run `./gradlew generateGrammarSource` first
- **Error**: Tests timeout
- **Solution**: Use longer timeouts (15+ minutes for build, 30+ minutes for performance tests)

### RPG Program Execution Issues
- **Error**: "Not found metadata for FileDefinition" or "missing property reloadConfig"
- **Cause**: Some RPG programs require database metadata configuration
- **Solution**: Simple programs like `ECHO_PGM` work without config. Complex programs need reload configuration files.
- **Workaround**: Use example programs in `examples/src/main/resources/rpg/` that are designed to work standalone

### Performance Test Failures
- Many performance tests showing timeouts is **normal behavior**
- Performance tests are designed to measure execution time against thresholds
- Focus on build success rather than individual performance test results

### Program Search Directory Issues
- **Error**: Program not found
- **Solution**: Ensure the `-psd` (program search directory) parameter points to the correct directory containing `.rpgle` files
- **Best Practice**: Use `examples/src/main/resources/rpg` as a known working directory

## Critical Reminders

- **NEVER CANCEL** long-running commands - builds take 8-10 minutes, performance tests take 15-20 minutes
- **ALWAYS** set appropriate timeouts: 15+ minutes for builds, 30+ minutes for performance tests
- **ALWAYS** run `./gradlew ktlintCheck` before committing
- **ALWAYS** test at least one RPG program execution after making changes
- The fat jar approach is the primary way to execute RPG programs
- Interactive shell mode provides a convenient development environment