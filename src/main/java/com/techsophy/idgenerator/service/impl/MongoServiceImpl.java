package com.techsophy.idgenerator.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.techsophy.idgenerator.constants.ApplicationConstants;
import com.techsophy.idgenerator.exception.SequenceGenerationException;
import com.techsophy.idgenerator.service.MongoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.techsophy.idgenerator.constants.ExceptionMessages.SEQUENCE_GENERATION_EXCEPTION;


@Service
@Slf4j
@AllArgsConstructor
public class MongoServiceImpl implements MongoService
{
    private final MongoTemplate mongoTemplate;

    @Override
    public Object getNextSequence(String id, String column)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(ApplicationConstants.ID).is(id));
        if (!mongoTemplate.exists(query,ApplicationConstants.SEQUENCE_COLLECTION_NAME))
        {
            log.info("id does not exist. Inserting new pcil sequence id");
            Map<String,Object> sequenceMap = new HashMap<>();
            sequenceMap.put(ApplicationConstants.ID,id);
            sequenceMap.put(ApplicationConstants.SALES_ORDER_SEQ,0);
            sequenceMap.put(ApplicationConstants.SALES_QUOTE_SEQ,0);
            mongoTemplate.save(sequenceMap,ApplicationConstants.SEQUENCE_COLLECTION_NAME);
        }
        Update update = new Update();
        update.inc(column, 1);
        DBObject obj =  mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), BasicDBObject.class, ApplicationConstants.SEQUENCE_COLLECTION_NAME);
        if(obj == null)
        {
            throw new SequenceGenerationException(SEQUENCE_GENERATION_EXCEPTION);
        }
        return obj.get(column);
    }
}
