package com.jariko.samples.java;

import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;
import com.smeup.rpgparser.utils.RpgcompilerKt;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class JarikoCompilingSample {

    // Compilation from resources/rpg into /tmp/bin
    private static void massiveCompilationFromDirToDir() {
        // Set src dir
        File srcDir = new File(JarikoCompilingSample.class.getResource("/rpg").getPath());
        // Set dir where rpg sources will be compiled
        File compiledProgramsDir = new File(System.getProperty("java.io.tmpdir"), "bin");
        if (!compiledProgramsDir.exists()) {
            compiledProgramsDir.mkdirs();
        }
        // Compile all programms in srcDir
        System.out.println("Compiled programs: " + RpgcompilerKt.compile(srcDir, compiledProgramsDir));
        Configuration configuration = new Configuration();
        // Set where Jariko should search for compiled programs
        configuration.getOptions().setCompiledProgramsDir(compiledProgramsDir);
        JavaSystemInterface javaSystemInterface = new JavaSystemInterface();
        javaSystemInterface.addJavaInteropPackage("com.jariko.samples.java");
        List<RpgProgramFinder> programFinders = Arrays.asList(new DirRpgProgramFinder(srcDir));
        // Start Jariko
        CommandLineProgram jariko = RunnerKt.getProgram("MUTE10_75L", javaSystemInterface, programFinders);
        System.out.println(jariko.singleCall(Arrays.asList(), configuration).getParmsList());

    }

    // compilation from resources/rpg/fibonacci.rpgle
    private static void singleCompilation() {
        File out = new File(System.getProperty("java.io.tmpdir"), "fibonacci.bin");
        try {
            File src = new File(JarikoCompilingSample.class.getResource("/rpg/fibonacci.rpgle").getPath());
            InputStream inputStream = new FileInputStream(src);
            // delete on exit just because we are writing a test
            out.deleteOnExit();
            // Here compile
            RpgcompilerKt.compile(inputStream, new FileOutputStream(out));

            // trying execution
            List<RpgProgramFinder> programFinders = Arrays.asList(new DirRpgProgramFinder(out.getParentFile()));
            CommandLineProgram jariko = RunnerKt.getProgram("fibonacci", new JavaSystemInterface(), programFinders);
            jariko.singleCall(Arrays.asList("12"));
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

