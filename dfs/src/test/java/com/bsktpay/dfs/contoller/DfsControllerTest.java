package com.bsktpay.dfs.contoller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bsktpay.dfs.controller.DfsController;
import com.bsktpay.dfs.dto.FileMessageRequest;
import com.bsktpay.dfs.serviceimp.Nodes;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(DfsController.class)
public class DfsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Nodes nodes;
    
    private static final String API_KEY_HEADER = "API_KEY";
    private static final String API_KEY_VALUE = "testing";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRetrieveFileContent() throws Exception {
        String fileName = "test.txt";
        String expectedContent = "Test content";
        when(nodes.getFileContent(fileName)).thenReturn(expectedContent);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dfs/retrieve-content/" + fileName).header(API_KEY_HEADER, API_KEY_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
               .andExpect(jsonPath("$.data").value(expectedContent));
    }

    @Order(1)
    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
            "file", 
            "filename.txt", 
            "text/plain", 
            "Test content".getBytes());


        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/dfs/upload")
                .file(mockFile).header(API_KEY_HEADER, API_KEY_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("File uploaded successfully: filename.txt"));
    }

    @Test
    public void testHandleFileMessage() throws Exception {
        FileMessageRequest request = new FileMessageRequest("test.txt", "Update message");
        when(nodes.updateFile(any(FileMessageRequest.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dfs/update-file")
        		.header(API_KEY_HEADER, API_KEY_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("successfully"));
    }

    // Add more tests for other scenarios and methods
}