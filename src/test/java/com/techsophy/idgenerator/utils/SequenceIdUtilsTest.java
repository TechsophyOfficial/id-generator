package com.techsophy.idgenerator.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.techsophy.idgenerator.constants.ApplicationConstants.*;

@ExtendWith({MockitoExtension.class})
class SequenceIdUtilsTest {

    @Test
    void addDigitToSequenceIdTest() {

        Assertions.assertNull(SequenceIdUtils.addDigitToSequenceId(null, 5, "0"));
        Assertions.assertEquals("", SequenceIdUtils.addDigitToSequenceId("", 5, "0"));
        Assertions.assertEquals(_ID, SequenceIdUtils.addDigitToSequenceId(_ID, 5, ""));
        Assertions.assertEquals(_ID, SequenceIdUtils.addDigitToSequenceId(_ID, 5, "    "));
        Assertions.assertEquals("00" + _ID, SequenceIdUtils.addDigitToSequenceId(_ID, 5, "0"));
    }
}
