package com.techsophy.idgenerator.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsophy.idgenerator.service.MongoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.AbstractMap;
import java.util.EventObject;
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

//    @Mock
//    MongoService mongoService;
//
    @Mock
    ObjectMapper objectMapper;

    @Test
    void onBeforeConvertTest() {

//        CustomMongoEventListener obj = new CustomMongoEventListener(mongoService, objectMapper);
//        Map<String, Object> source = Map.ofEntries(
//                new AbstractMap.SimpleEntry<String, Object>(FORM_ID, SALES_ORDER_FORM_ID),
//                new AbstractMap.SimpleEntry<String, Object>(FORM_DATA, SALES_ORDER_ID));
//        BeforeConvertEvent<Object> eventObject = new BeforeConvertEvent<>(source, "abc");
//
//        Map<String, Object> formData = Map.ofEntries(
//                new AbstractMap.SimpleEntry<String, Object>(SALES_ORDER_ID, SALES_ORDER_FORM_ID));
//                //new AbstractMap.SimpleEntry<String, Object>(SALES_QUOTE_ID, SALES_ORDER_FORM_ID));
//
//        Mockito.when(objectMapper.convertValue(any(), eq(new TypeReference<>() {
//        }))).thenReturn(formData);
//        //EventObject event = new EventObject(source);
//
//        //Assertions.assertNotNull(obj.onBeforeConvert(eventObject));
//        obj.onBeforeConvert(eventObject);

        Map<String, Object> formData = Map.ofEntries(
                new AbstractMap.SimpleEntry<String, Object>("abc", "abc"));

        Map<String, Object> source = Map.ofEntries(
                new AbstractMap.SimpleEntry<String, Object>(FORM_ID, SALES_ORDER_FORM_ID),
                new AbstractMap.SimpleEntry<String, Object>(FORM_DATA, formData));
        BeforeConvertEvent<Object> eventObject = new BeforeConvertEvent<>(source, "abc");


                //new AbstractMap.SimpleEntry<String, Object>(SALES_QUOTE_ID, SALES_ORDER_FORM_ID));

        Mockito.when(objectMapper.convertValue(any(), (Class<Object>) any())).thenReturn(formData);



        customMongoEventListener.onBeforeConvert(eventObject);




    }
}