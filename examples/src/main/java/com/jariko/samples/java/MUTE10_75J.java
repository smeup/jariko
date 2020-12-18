package com.jariko.samples.java;

import com.smeup.rpgparser.interpreter.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class MUTE10_75J implements Program {

    private final static String RESPGM = "$RESPGM";
    private final static List<ProgramParam> PARAMS = Arrays.asList(new ProgramParam(RESPGM, new StringType(50, false)));

    public List<Value> execute(SystemInterface systemInterface, LinkedHashMap<String, Value> params) {
        return Arrays.asList(new StringValue("Hello World!!!" + params.get(RESPGM).asString().getValue(), false));
    }

    public List<ProgramParam> params() {
        return PARAMS;
    }
}
