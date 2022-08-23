package com.techsophy.idgenerator.utils;

import org.springframework.util.ObjectUtils;

public class SequenceIdUtils {
    private SequenceIdUtils() {}

    public static String addDigitToSequenceId(String id, int sequenceLength, String digit) {
        if (id != null && id.length() > 0 && !ObjectUtils.isEmpty(digit) && !digit.isBlank()) {
            return digit.repeat(sequenceLength - id.length()) + id;
        }
        return id;
    }
}
