package com.jariko.samples.java;

import com.smeup.rpgparser.execution.CommandLineParms;
import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.interpreter.DataStructValue;
import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.Value;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.logging.LoggingKt;
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder;
import com.smeup.rpgparser.rpginterop.RpgProgramFinder;
import kotlin.Unit;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CallJarikoWithParams {

    // Helper method to exec a PGM.
    // Return outputParams
    public static CommandLineParms execPgm(String name, CommandLineParms inputParams) {
        File srcDir = new File(CallJarikoWithParams.class.getResource("/rpg").getPath());
        List<RpgProgramFinder> programFinders = Arrays.asList(new DirRpgProgramFinder(srcDir));
        final JavaSystemInterface systemInterface = new JavaSystemInterface();
        systemInterface.setLoggingConfiguration(LoggingKt.consoleLoggingConfiguration(LoggingKt.PERFORMANCE_LOGGER));
        CommandLineProgram program = RunnerKt.getProgram(name, systemInterface, programFinders);
        final Configuration configuration = new  Configuration();
        configuration.getJarikoCallback().setLogInfo(message -> {
            System.out.println(message);
            return Unit.INSTANCE;
        });
        return program.singleCall(inputParams, configuration);
    }

    public static void execWithListOfString() {
        List<String> plist = Arrays.asList("V1", "V2", "");
        CommandLineParms commandLineParms = new CommandLineParms(plist);
        CommandLineParms out = execPgm("SAMPLE01", commandLineParms);
        System.out.println("execWithListOfStringParams: " + out);
        assert "V1V2V1        V2".equals(out.getParmsList().stream().map(s -> s.trim()).collect(Collectors.joining()));
    }

    public static void execWithNamedValues() {
        Map<String, Value> plist = new HashMap<>();
        plist.put("P1", new StringValue("V1", false));
        plist.put("P2", new StringValue("V2", false));
        plist.put("RESULT", new StringValue("", false));
        System.out.println(plist);
        CommandLineParms commandLineParms = new CommandLineParms(plist);
        CommandLineParms out = execPgm("SAMPLE01", commandLineParms);
        System.out.println("execWithNamedValues: " + out.getNamedParams());
        assert "V1V2V1        V2".equals(out.getParmsList().stream().map(s -> s.trim()).collect(Collectors.joining()));
    }

    public static void execWithDS() {
        String dsName = "MYDS";
        // In this case we use a callback command line params construct because
        // we need ast, but we don't have ast before the program executions, than we use
        // following approach
        CommandLineParms commandLineParms = new CommandLineParms(compilationUnit -> {
            Map<String, Value> dataStructFields = new HashMap<>();
            dataStructFields.put("P1", new StringValue("V1", false));
            dataStructFields.put("P2", new StringValue("V2", false));
            dataStructFields.put("RESULT", new StringValue("", false));
            Map<String, Value> plist = new HashMap<>();
            plist.put(dsName, DataStructValue.Companion.createInstance(compilationUnit, dsName, dataStructFields));
            return plist;
        });
        CommandLineParms out = execPgm("SAMPLE02", commandLineParms);
        System.out.println("execWithDS: " + out.getNamedParams());
        assert "V1V2V1        V2".equals(out.getParmsList().stream().map(s -> s.trim()).collect(Collectors.joining()));
    }

    public static void main(String[] args) {
        execWithListOfString();
        execWithNamedValues();
        execWithDS();
    }

}
