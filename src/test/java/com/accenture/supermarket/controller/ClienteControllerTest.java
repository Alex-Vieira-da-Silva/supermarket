package com.accenture.supermarket.controller;

import com.accenture.supermarket.SupermarketApplication;
import com.accenture.supermarket.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService service;

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
            SpringApplication.run(SupermarketApplication.class);
        });
    }

}
