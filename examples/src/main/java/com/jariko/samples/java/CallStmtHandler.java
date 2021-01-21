package com.jariko.samples.java;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.smeup.rpgparser.execution.CallProgramHandler;
import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.SystemInterface;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;

public class CallStmtHandler {

    public static void main(String[] args) {
        SystemInterface systemInterface = new JavaSystemInterface();
        File rpgDir = new File(CallStmtHandler.class.getResource("/rpg").getPath());
        if (!rpgDir.exists()){
            System.out.println(rpgDir.getPath() + " not exists");
            return;
        }
        List<RpgProgramFinder> programFinders = Arrays.asList(new DirRpgProgramFinder(rpgDir));
        Configuration configuration = new Configuration();
        CommandLineProgram jariko = RunnerKt.getProgram("CALL_STMT.rpgle", systemInterface, programFinders);

        AtomicInteger counter = new AtomicInteger(0);
        CallProgramHandler callProgramHandler = new CallProgramHandler(
                (p) -> {
                    counter.set(counter.get() + 1);
                    int c = counter.get();
                    return c % 2 == 0;
                },
                (p, s, l) -> {
                    StringValue stringValue = new StringValue("CUSTOM_PGM", false);
                    return Arrays.asList(stringValue);
                }
        );

        try {
            configuration.getOptions().setCallProgramHandler(callProgramHandler);
            jariko.singleCall(Arrays.asList(""), configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}