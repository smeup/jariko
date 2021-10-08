/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

