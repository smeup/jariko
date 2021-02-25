package com.jariko.samples.java;

import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.parsing.parsetreetoast.Copy;
import com.smeup.rpgparser.interpreter.RpgProgram;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.parsing.ast.CopyId;
import com.smeup.rpgparser.parsing.ast.SourceProgram;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * In this example we are going to implement loading of rpg program by using the following lookup rules:
 *  - Looking for bin version
 *  - Looking for rpgle version if missing bin version
 * */
public class ProgramFinderSample {

    private static final File RPG_DIR = new File(ProgramFinderSample.class.getResource("/rpg").getPath());

    private static class BinProgramFinder implements RpgProgramFinder {

        @Nullable
        @Override
        public RpgProgram findRpgProgram(@NotNull String nameOrSource) {
            try {
                File bin = new File(RPG_DIR, nameOrSource + ".bin");
                if (bin.exists()) {
                    try (InputStream in = new FileInputStream(bin)) {
                        System.out.println("Loading bin version of: " + nameOrSource);
                        return RpgProgram.Companion.fromInputStream(in, nameOrSource, SourceProgram.BINARY);
                    }
                }
                else {
                    return null;
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Nullable
        @Override
        public Copy findCopy(@NotNull CopyId copyId) {
            return null;
        }
    }

    private static class SrcProgramFinder implements RpgProgramFinder {

        public RpgProgram findRpgProgram(@NotNull String nameOrSource) {
            try {
                File bin = new File(RPG_DIR, nameOrSource + ".rpgle");
                if (bin.exists()) {
                    try (InputStream in = new FileInputStream(bin)) {
                        System.out.println("Loading src version of: " + nameOrSource);
                        return RpgProgram.Companion.fromInputStream(in, nameOrSource, SourceProgram.RPGLE);
                    }
                }
                else {
                    return null;
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Nullable
        @Override
        public Copy findCopy(@NotNull CopyId copyId) {
            return null;
        }
    }

    public static void main(String[] args) {
        List<RpgProgramFinder> finders = Arrays.asList(new BinProgramFinder(), new SrcProgramFinder());

        // fibonacci.bin is present in resources/rpg
        CommandLineProgram fibonacci = RunnerKt.getProgram("fibonacci", new JavaSystemInterface(), finders);
        fibonacci.singleCall(Arrays.asList("10"));

        // SAMPLE01.bin is not present resources/rpg
        CommandLineProgram sample01 = RunnerKt.getProgram("SAMPLE01", new JavaSystemInterface(), finders);
        List<String> result = sample01.singleCall(Arrays.asList("A", "B", "")).getParmsList();
        System.out.println(result);
    }
}
