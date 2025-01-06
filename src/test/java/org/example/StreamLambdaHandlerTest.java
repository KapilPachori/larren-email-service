package org.example;


import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;

import jakarta.mail.MessagingException;
import org.example.controller.SendEmailController;
import org.example.service.SendEmailService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class StreamLambdaHandlerTest {

    private static StreamLambdaHandler handler;
    private static Context lambdaContext;

    //private SendEmailController sendEmailController;
    //private SendEmailService sendEmailService;

    @BeforeAll
    public static void setUp() {
        handler = new StreamLambdaHandler();
        lambdaContext = new MockLambdaContext();
    }

    /*@BeforeEach
    public void setUpTest() throws MessagingException {
        // Mock the service and pass it to the controller
        sendEmailService = Mockito.mock(SendEmailService.class);
        sendEmailController = new SendEmailController(sendEmailService);

        // Optionally configure behavior for the mocked service
        Mockito.doNothing().when(sendEmailService).sendEmailWithAttachment(Mockito.anyString(), Mockito.any(MultipartFile.class));
    }

    @Test
    public void testSendEmailWithAttachment() throws Exception {

        String request = "{\"jobTitle\":\"Back Support\",\"name\":\"Kapil Pachori\",\"email\":\"pachori.kapil00@gmail.com\",\"phone\":\"9582439470\",\"address\":\"Balaji Puram\",\"linkedin\":\"NA\",\"experience\":\"5 Years of experience\"}";
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "text/plain",
                "Sample file content".getBytes()
        );

        ResponseEntity<String> response = sendEmailController.sendMailWithAttachment(request,mockFile);

        // Assert the response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mail Sent Successfully", response.getBody());

    }*/

    @Test
    public void ping_streamRequest_respondsWithHello() {
        InputStream requestStream = new AwsProxyRequestBuilder("/ping", HttpMethod.GET)
                                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                            .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertFalse(response.isBase64Encoded());

        assertTrue(response.getBody().contains("pong"));
        assertTrue(response.getBody().contains("Hello, World!"));

        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void invalidResource_streamRequest_responds404() {
        InputStream requestStream = new AwsProxyRequestBuilder("/pong", HttpMethod.GET)
                                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                            .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
    }

    private void handle(InputStream is, ByteArrayOutputStream os) {
        try {
            handler.handleRequest(is, os, lambdaContext);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private AwsProxyResponse readResponse(ByteArrayOutputStream responseStream) {
        try {
            return LambdaContainerHandler.getObjectMapper().readValue(responseStream.toByteArray(), AwsProxyResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error while parsing response: " + e.getMessage());
        }
        return null;
    }
}
