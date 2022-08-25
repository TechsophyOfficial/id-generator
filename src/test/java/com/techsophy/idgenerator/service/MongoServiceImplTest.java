package com.techsophy.idgenerator.service.impl;

import com.mongodb.BasicDBObject;
import com.techsophy.idgenerator.constants.ApplicationConstants;
import com.techsophy.idgenerator.exception.SequenceGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;

import static com.techsophy.idgenerator.constants.ApplicationConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

//@ActiveProfiles(TEST_ACTIVE_PROFILE)
//@SpringBootTest
//@ContextConfiguration
@ExtendWith(MockitoExtension.class)
class MongoServiceImplTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @InjectMocks
    MongoServiceImpl mongoServiceImpl;


    //@Test
    void getNextSequenceTest() {
        Query query = new Query();
        Update update = new Update();

        Mockito.when(mongoTemplate.exists(any(), (Class<?>) any())).thenReturn(true);
        Mockito.when(mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
                BasicDBObject.class, ApplicationConstants.SEQUENCE_COLLECTION_NAME)).thenReturn(null);

        Assertions.assertThrows(SequenceGenerationException.class, () -> mongoServiceImpl.getNextSequence(ID, SALES_ORDER_SEQ));
    }


}