package com.techsophy.idgenerator;

import com.techsophy.idgenerator.exception.InvalidTimeException;
import com.techsophy.idgenerator.exception.NodeLimitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;



@ExtendWith({MockitoExtension.class})
class IdGeneratorImplTest {
    @InjectMocks
    IdGeneratorImpl idGenerator;

    @Test
    void nextIdTest() {
        BigInteger prev = idGenerator.nextId();
        Assertions.assertNotEquals(prev, idGenerator.nextId());
    }

    @Test
    void parseTest() {
        Map<String, Long> map = new HashMap();
        map.put("timeStamp", 1420070400000L);
        map.put("sequence", 57L);
        map.put("nodeId", 3L);
        Assertions.assertEquals(map, idGenerator.parse(12345));
    }

    @Test
    void toStringTest() {
        IdGeneratorImpl id = new IdGeneratorImpl(123);
        Assertions.assertEquals("Snowflake Settings [EPOCH_BITS=41, NODE_ID_BITS=10, SEQUENCE_BITS=12, CUSTOM_EPOCH=1420070400000, NodeId=123]", id.toString());
    }

    @Test
    void nodeLimitExceptionTest(){
        Assertions.assertThrows(NodeLimitException.class, ()-> new IdGeneratorImpl(-1, 1420070400000L));
    }

    @Test
    void invalidTimeExceptionTest(){

        //Mockito.when(Instant.now().toEpochMilli()).thenReturn(-2L);
        IdGeneratorImpl id = new IdGeneratorImpl(123, 23614304021729L);
        Assertions.assertThrows(InvalidTimeException.class, ()-> id.nextId());
    }
}
