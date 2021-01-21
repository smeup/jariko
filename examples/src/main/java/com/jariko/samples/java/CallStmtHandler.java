package com.jariko.samples.java;

import com.smeup.rpgparser.execution.CallProgramHandler;
import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.SystemInterface;
import com.smeup.rpgparser.interpreter.Value;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        // alternatively my handler customize call behavior
        CallProgramHandler callProgramHandler = new CallProgramHandler(
                (String programName, SystemInterface si, LinkedHashMap<String, Value> paramsList) -> {
                    if (counter.incrementAndGet() % 2 == 0 ) {
                        Value value = new StringValue("CUSTOM_PGM", false);
                        return Arrays.asList(value);
                    }
                    else {
                        return null;
                    }
                }
        );

        try {
            configuration.getOptions().setCallProgramHandler(callProgramHandler);
            jariko.singleCall(Arrays.asList(""), configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}