//package com.techsophy.idgenerator.service;
//
//import com.mongodb.BasicDBObject;
//import com.techsophy.idgenerator.constants.ApplicationConstants;
//import com.techsophy.idgenerator.exception.SequenceGenerationException;
//import com.techsophy.idgenerator.service.impl.MongoServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.FindAndModifyOptions;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//import static com.techsophy.idgenerator.constants.ApplicationConstants.*;
//
////@ActiveProfiles(TEST_ACTIVE_PROFILE)
////@SpringBootTest
////@ContextConfiguration
//@ExtendWith(MockitoExtension.class)
//class MongoServiceImplTest {
//    @Mock
//    MongoDatabaseFactory mongoDatabaseFactory;
//    @InjectMocks
//    MongoServiceImpl mongoServiceImpl;
//
//    @Test
//    void getNextSequenceTest() {
//        Query query = new Query();
//        Update update = new Update();
//        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory);
//
//        Mockito.when(mongoTemplate.exists(query, ApplicationConstants.SEQUENCE_COLLECTION_NAME)).thenReturn(true);
//        Mockito.when(mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
//                BasicDBObject.class, ApplicationConstants.SEQUENCE_COLLECTION_NAME)).thenReturn(null);
//
//        Assertions.assertThrows(SequenceGenerationException.class, () -> mongoServiceImpl.getNextSequence(ID, SALES_ORDER_SEQ));
//    }
//
//
//}