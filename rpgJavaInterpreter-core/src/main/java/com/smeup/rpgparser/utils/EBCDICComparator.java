package com.smeup.rpgparser.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

public class EBCDICComparator implements Comparator<String> {

    private final String EBCDIC_CODE = "CP037";
    private final Charset STANDARD_CHARSET = StandardCharsets.ISO_8859_1;
    private int order = 1;

    public EBCDICComparator() {
    }

    public EBCDICComparator(boolean descend) {
        if (descend)
            order = -1;
    }

    @Override
    public int compare(String s1, String s2) {

        if (s1.equals(s2))
            return 0;

        byte[] b1;
        byte[] b2;
        try {
            b1 = s1.getBytes(EBCDIC_CODE);
            b2 = s2.getBytes(EBCDIC_CODE);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return order * new String(b1, STANDARD_CHARSET).compareTo(new String(b2, STANDARD_CHARSET));
    }
}

