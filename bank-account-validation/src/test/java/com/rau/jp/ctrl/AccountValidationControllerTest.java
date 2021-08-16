package com.rau.jp.ctrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rau.jp.dto.RequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountValidationControllerTest {

    @Autowired
    MockMvc mockMvc;

    MockHttpServletRequestBuilder mockRequest;

    @BeforeEach
    void setUp(){
        mockRequest = MockMvcRequestBuilders.get("/account_validation")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testAllValidAccount() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setAccountNumber("12345678");
        requestDto.setProviders(new String[]{"provider1","provider2"});

        mockRequest.content(new ObjectMapper().writeValueAsString(requestDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[0].provider").value("provider1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[0].valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[1].provider").value("provider2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[1].valid").value(true));
    }

    @Test
    public void testOneValidAndOneInvalidAccount() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setAccountNumber("10000001");
        requestDto.setProviders(new String[]{"provider1","provider2"});

        mockRequest.content(new ObjectMapper().writeValueAsString(requestDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[0].provider").value("provider1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[0].valid").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[1].provider").value("provider2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[1].valid").value(false));
    }

    @Test
    public void testTimeOut() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setAccountNumber("12345678");
        requestDto.setProviders(new String[]{"provider1","provider3"});

        mockRequest.content(new ObjectMapper().writeValueAsString(requestDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().is(HttpStatus.REQUEST_TIMEOUT.value()));
    }

    @Test
    public void testWrongProvider() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setAccountNumber("12345678");
        requestDto.setProviders(new String[]{"provider5"});

        mockRequest.content(new ObjectMapper().writeValueAsString(requestDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

}
