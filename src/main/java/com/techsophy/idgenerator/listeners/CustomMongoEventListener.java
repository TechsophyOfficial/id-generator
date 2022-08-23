package com.techsophy.idgenerator.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.constants.ApplicationConstants;
import com.techsophy.idgenerator.service.MongoService;
import com.techsophy.idgenerator.utils.SequenceIdUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

import static com.techsophy.idgenerator.constants.ApplicationConstants.*;

@Component
@AllArgsConstructor
@Slf4j
public class CustomMongoEventListener extends AbstractMongoEventListener<Object>
{
    private final MongoService mongoService;
    private final ObjectMapper objectMapper;

    // TODO: Generify the validations & id-generator

    private void formatUniqueId(Map<String, Object> formData, String formId, BeforeConvertEvent<Object> event){

        String oaBranch;
        Object sequenceId;
        String key = SALES_QUOTE_ID;
        if(SALES_QUOTE_FORM_ID.equals(formId))
        {
            log.info("FormId matches SalesQuoteFormId");
            oaBranch = ((Map<?, ?>)formData.get(SALES_QUOTE)).get(OA_BRANCH).toString();
            sequenceId = SequenceIdUtils.addDigitToSequenceId(mongoService.getNextSequence(oaBranch, SALES_QUOTE_SEQ).toString(), 5, "0");
        }
        else
        {
            log.info("FormId matches SalesOrderFormId");
            oaBranch = ((Map<?, ?>)formData.get(SALES_ORDER)).get(OA_BRANCH).toString();
            key = SALES_ORDER_ID;
            sequenceId = SequenceIdUtils.addDigitToSequenceId(mongoService.getNextSequence(oaBranch, SALES_ORDER_SEQ).toString(), 6, "0");
        }

        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        if (month <= 3)
        {
            --year;
        }
        String uniqueId = new StringBuilder().append(oaBranch)
                .append(String.format("%02d", month))
                .append(String.format("%02d", year%100))
                .append(sequenceId).toString();
        log.info(key+" : "+uniqueId);
        formData.put(key, uniqueId);
        try {
            log.info(objectMapper.writeValueAsString(formData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ((Map<String, Object>) event.getSource()).put(FORM_DATA,formData);
        try {
            log.info(objectMapper.writeValueAsString(event.getSource()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event)
    {
        log.info("Mongo Event Listener Called");
        //if(event.getSource() instanceof Map && ((Map<?, ?>) event.getSource()).containsKey(FORM_ID))
        if(((Map<?, ?>) event.getSource()).containsKey(FORM_ID))
        {
            Map<String, Object> source = (Map) event.getSource();
            String formId = source.get(ApplicationConstants.FORM_ID).toString();
            log.info("FormId : "+formId);
            if (SALES_QUOTE_FORM_ID.equals(formId) || SALES_ORDER_FORM_ID.equals(formId))
            {
                log.info("FormID matches Sales Quote or SalesOrder formId");
                if (source.get(FORM_DATA) instanceof Map)
                {
                    Map<String, Object> formData = objectMapper.convertValue(source.get(FORM_DATA), new TypeReference<>() {});
                    if (!(formData.containsKey(SALES_QUOTE_ID) || formData.containsKey(SALES_ORDER_ID)))
                    {
                        log.info("FormData does not contain SalesQuoteId & SalesOrderId");
//                        String oaBranch;
//                        Object sequenceId;
//                        String key = SALES_QUOTE_ID;
//                        if(SALES_QUOTE_FORM_ID.equals(formId))
//                        {
//                            log.info("FormId matches SalesQuoteFormId");
//                            oaBranch = ((Map<?, ?>)formData.get(SALES_QUOTE)).get(OA_BRANCH).toString();
//                            sequenceId = SequenceIdUtils.addDigitToSequenceId(mongoService.getNextSequence(oaBranch, SALES_QUOTE_SEQ).toString(), 5, "0");
//                        }
//                        else
//                        {
//                            log.info("FormId matches SalesOrderFormId");
//                            oaBranch = ((Map<?, ?>)formData.get(SALES_ORDER)).get(OA_BRANCH).toString();
//                            key = SALES_ORDER_ID;
//                            sequenceId = SequenceIdUtils.addDigitToSequenceId(mongoService.getNextSequence(oaBranch, SALES_ORDER_SEQ).toString(), 6, "0");
//                        }
//                        int month = LocalDate.now().getMonthValue();
//                        int year = LocalDate.now().getYear();
//                        if (month <= 3)
//                        {
//                            --year;
//                        }
//                        String uniqueId = new StringBuilder().append(oaBranch)
//                                .append(String.format("%02d", month))
//                                .append(String.format("%02d", year%100))
//                                .append(sequenceId).toString();
//                        log.info(key+" : "+uniqueId);
//                        formData.put(key, uniqueId);
//                        try {
//                            log.info(objectMapper.writeValueAsString(formData));
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
//                        ((Map<String, Object>) event.getSource()).put(FORM_DATA,formData);
//                        try {
//                            log.info(objectMapper.writeValueAsString(event.getSource()));
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
                        formatUniqueId(formData, formId, event);
                    }
                }
            }
        }
    }
}
