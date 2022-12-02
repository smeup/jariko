/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jariko.samples.java;

import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.Options;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;
import com.smeup.rpgparser.utils.RpgcompilerKt;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JarikoCompilingSample {

    // Compilation from resources/rpg into /tmp/bin
    private static void massiveCompilationFromDirToDir() {
        // Set src dir
        File srcDir = new File(Objects.requireNonNull(JarikoCompilingSample.class.getResource("/rpg")).getPath());
        // Set dir where rpg sources will be compiled
        File compiledProgramsDir = new File(System.getProperty("java.io.tmpdir"), "bin");
        if (!compiledProgramsDir.exists()) {
            compiledProgramsDir.mkdirs();
        }
        Configuration configuration = new Configuration();
        // Set where Jariko should search for compiled programs
        Objects.requireNonNull(configuration.getOptions()).setCompiledProgramsDir(compiledProgramsDir);
        // With this setting, even in case of compiled files, we will be able
        // to find the errors easily because errors and source will be dumped together
        configuration.getOptions().setDebuggingInformation(true);
        // Compile all programs in srcDir
        System.out.println("Compiled programs: " + RpgcompilerKt.compile(srcDir, compiledProgramsDir, configuration));
        JavaSystemInterface javaSystemInterface = new JavaSystemInterface();
        javaSystemInterface.addJavaInteropPackage("com.jariko.samples.java");
        List<RpgProgramFinder> programFinders = Collections.singletonList(new DirRpgProgramFinder(srcDir));
        // Start Jariko
        CommandLineProgram jariko = RunnerKt.getProgram("MUTE10_75L", javaSystemInterface, programFinders);
        System.out.println(Objects.requireNonNull(jariko.singleCall(Collections.emptyList(), configuration)).getParmsList());

    }

    // compilation from resources/rpg/fibonacci.rpgle
    private static void singleCompilation() {
        File out = new File(System.getProperty("java.io.tmpdir"), "fibonacci.bin");
        try {
            File src = new File(Objects.requireNonNull(JarikoCompilingSample.class.getResource("/rpg/fibonacci.rpgle")).getPath());
            InputStream inputStream = new FileInputStream(src);
            // delete on exit just because we are writing a test
            out.deleteOnExit();
            Configuration configuration = new Configuration();
            configuration.setOptions(new Options());
            // Compile
            RpgcompilerKt.compile(inputStream, new FileOutputStream(out));
            // Execution
            List<RpgProgramFinder> programFinders = Collections.singletonList(new DirRpgProgramFinder(out.getParentFile()));
            CommandLineProgram jariko = RunnerKt.getProgram("fibonacci", new JavaSystemInterface(),
                    programFinders);
            jariko.singleCall(Collections.singletonList("12"), configuration);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            out.delete();
        }
    }

    public static void main(String[] args) {
        singleCompilation();
        massiveCompilationFromDirToDir();
    }

}

