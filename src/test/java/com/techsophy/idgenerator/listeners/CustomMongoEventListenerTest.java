package com.techsophy.idgenerator.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.service.MongoService;
import com.techsophy.idgenerator.utils.SequenceIdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import static com.techsophy.idgenerator.constants.ApplicationConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith({MockitoExtension.class})
class CustomMongoEventListenerTest {

    @InjectMocks
    CustomMongoEventListener customMongoEventListener;

//    @Mock
//    BeforeConvertEvent<Object> beforeConvertEvent;

    @Mock
    MongoService mongoService;

    @Mock
    ObjectMapper objectMapper;

    @Test
    void onBeforeConvertTest() {

        Map<String, Object> map = new HashMap<>();
        map.put(OA_BRANCH, "oaBranch");

        Map<String, Object> formData = new HashMap<>();
        formData.put(SALES_ORDER, map);
        formData.put(SALES_QUOTE, map);

        Map<String, Object> source = new HashMap<>();
        source.put(FORM_ID, SALES_ORDER_FORM_ID);
        source.put(FORM_DATA,map);

        BeforeConvertEvent<Object> eventObject = new BeforeConvertEvent<>(source, "");

        Mockito.when(objectMapper.convertValue(any(),any(TypeReference.class))).thenReturn(formData);
        Mockito.when(mongoService.getNextSequence(any(), any())).thenReturn(_ID);

        customMongoEventListener.onBeforeConvert(eventObject);

        String uniqueId = new StringBuilder().append(map.get(OA_BRANCH))
                .append(String.format("%02d", LocalDate.now().getMonthValue()))
                .append(String.format("%02d", LocalDate.now().getYear() % 100))
                .append(SequenceIdUtils.addDigitToSequenceId(mongoService.getNextSequence(
                        map.get(OA_BRANCH).toString(), SALES_ORDER_SEQ).toString(), 6, "0"))
                .toString();

        Assertions.assertEquals(uniqueId ,formData.get(SALES_ORDER_ID));


    }

    @Test
    void onBeforeConvertTestSalesQouteFormID() {

        Map<String, Object> map = new HashMap<>();
        map.put(OA_BRANCH, "oaBranch");

        Map<String, Object> formData = new HashMap<>();
        formData.put(SALES_ORDER, map);
        formData.put(SALES_QUOTE, map);

        Map<String, Object> source = new HashMap<>();
        source.put(FORM_ID, SALES_QUOTE_FORM_ID);
        source.put(FORM_DATA,map);

        BeforeConvertEvent<Object> eventObject = new BeforeConvertEvent<>(source, "");

        Mockito.when(objectMapper.convertValue(any(),any(TypeReference.class))).thenReturn(formData);
        Mockito.when(mongoService.getNextSequence(any(), any())).thenReturn(_ID);

        customMongoEventListener.onBeforeConvert(eventObject);

        String uniqueId = new StringBuilder().append(map.get(OA_BRANCH))
                .append(String.format("%02d", LocalDate.now().getMonthValue()))
                .append(String.format("%02d", LocalDate.now().getYear() % 100))
                .append(SequenceIdUtils.addDigitToSequenceId(mongoService.getNextSequence(
                        map.get(OA_BRANCH).toString(), SALES_QUOTE_SEQ).toString(), 5, "0"))
                .toString();

        Assertions.assertEquals(uniqueId ,formData.get(SALES_QUOTE_ID));


    }
}