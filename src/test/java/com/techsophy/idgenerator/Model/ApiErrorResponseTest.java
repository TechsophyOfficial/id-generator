package com.techsophy.idgenerator.Model;


import com.techsophy.idgenerator.model.ApiErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@ExtendWith({MockitoExtension.class})
class ApiErrorResponseTest {

    Instant instant = Instant.now();
    ApiErrorResponse apiErrorResponse = new ApiErrorResponse(instant,"Test msg","Test Error", HttpStatus.ACCEPTED,"Test Path");

    @Test
    void ApiErrorResponse(){
        ApiErrorResponse apiErrorResponse1 = new ApiErrorResponse(instant,"Test msg","Test Error", HttpStatus.ACCEPTED,"Test Path");
        Assertions.assertEquals("Test Error", apiErrorResponse.getError());
        Assertions.assertEquals("Test msg", apiErrorResponse.getMessage());
        Assertions.assertEquals(apiErrorResponse.getTimestamp(),instant);
        Assertions.assertEquals("Test Path", apiErrorResponse.getPath());
        Assertions.assertEquals(HttpStatus.ACCEPTED, apiErrorResponse.getStatus());
        Assertions.assertNotNull(apiErrorResponse.toString());
        Assertions.assertEquals(apiErrorResponse.hashCode(),apiErrorResponse1.hashCode());
        Assertions.assertEquals(apiErrorResponse, apiErrorResponse1);
    }
}
