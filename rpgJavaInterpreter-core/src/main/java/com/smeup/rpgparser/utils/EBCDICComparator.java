package com.smeup.rpgparser.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

public class EBCDICComparator implements Comparator<String> {

    private final static String EBCDIC_CODE = "CP037";
    private final static Charset STANDARD_CHARSET = StandardCharsets.ISO_8859_1;
    private static byte[] b1;
    private static byte[] b2;
    private int order = 1;

    public EBCDICComparator() { }

    public EBCDICComparator(boolean reverseOrder) {
        if(reverseOrder) order = -1;
    }

    @Override
    public int compare(String s1, String s2) {

        if (s1.equals(s2)) return 0;

        try {
            b1 = s1.getBytes(EBCDIC_CODE);
            b2 = s2.getBytes(EBCDIC_CODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return order * (new String(b1, STANDARD_CHARSET).compareTo(new String(b2, STANDARD_CHARSET)));
    }
}

