package com.jariko.samples.java;

import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;
import com.smeup.rpgparser.utils.RpgcompilerKt;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class JarikoCompilingSample {

    public static void main(String[] args) {
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
        javaSystemInterface.addJavaInteropPackage("com.smeup.jariko.samples");
        List<RpgProgramFinder> programFinders = Arrays.asList(new DirRpgProgramFinder(srcDir));
        // Start Jariko
        CommandLineProgram jariko = RunnerKt.getProgram("MUTE10_75L", javaSystemInterface, programFinders);
        System.out.println(jariko.singleCall(Arrays.asList(), configuration).getParmsList());
    }

}

