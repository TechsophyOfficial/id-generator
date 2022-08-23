package com.techsophy.idgenerator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@ExtendWith({MockitoExtension.class})
class IdGeneratorImplTest {
    @InjectMocks
    IdGeneratorImpl idGenerator;

    @Test
    void nextIdTest(){
        BigInteger prev = idGenerator.nextId();
        Assertions.assertNotEquals(prev,idGenerator.nextId());
    }

    @Test
    void parseTest(){
        Map<String,Long> map = new HashMap();
        map.put("timeStamp",1420070400000L);
        map.put("sequence", 57L);
        map.put("nodeId",3L);
        Assertions.assertEquals(map, idGenerator.parse(12345));
    }
    @Test
    void toStringTest(){
        IdGeneratorImpl id = new IdGeneratorImpl(123);
       Assertions.assertEquals("Snowflake Settings [EPOCH_BITS=41, NODE_ID_BITS=10, SEQUENCE_BITS=12, CUSTOM_EPOCH=1420070400000, NodeId=123]",id.toString());
    }
}
