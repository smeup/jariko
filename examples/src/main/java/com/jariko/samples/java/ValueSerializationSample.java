package com.jariko.samples.java;

import com.smeup.rpgparser.interpreter.DecimalValue;
import com.smeup.rpgparser.interpreter.IntValue;
import com.smeup.rpgparser.interpreter.StringValue;
import com.smeup.rpgparser.interpreter.Value;
import com.smeup.rpgparser.interpreter.serialization.ValueSerializer;

import java.math.BigDecimal;

public class ValueSerializationSample {


    public static void main(String[] args) {
        Value intValue = new IntValue(1234);
        System.out.println(ValueSerializer.encode(intValue));
        System.out.println(ValueSerializer.decode(ValueSerializer.encode(intValue)));
        Value decimalValue = new DecimalValue(BigDecimal.valueOf(12.12));
        System.out.println(ValueSerializer.encode(decimalValue));
        System.out.println(ValueSerializer.decode(ValueSerializer.encode(decimalValue)));
        Value stringValue = new StringValue("Hello world!!!", true);
        System.out.println(ValueSerializer.encode(stringValue));
        System.out.println(ValueSerializer.decode(ValueSerializer.encode(stringValue)));
    }
}
