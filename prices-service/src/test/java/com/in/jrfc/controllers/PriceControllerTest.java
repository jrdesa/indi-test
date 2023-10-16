package com.in.jrfc.controllers;
import static java.sql.Timestamp.valueOf;
import static org.mockito.Mockito.when;

import com.in.jrfc.dtos.PriceRequestDto;
import com.in.jrfc.dtos.PriceResponseDto;
import com.in.jrfc.entities.Price;
import com.in.jrfc.services.PriceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.sql.Timestamp;

@WebMvcTest(PriceController.class)
class PriceControllerTest {
    private static final String DATE="2021-06-14T00:00:00Z";

    private static final Integer PRODUCT_ID = 55555;
    private static final Long BRAND_ID = 1L;
    @MockBean
    private PriceService priceService;
    @Autowired
    private MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        PriceController priceController= new PriceController(priceService);
    }
    @Test
    void filterPrice() throws Exception {
        PriceResponseDto priceResponseDto=PriceResponseDto.builder().build();

        PriceRequestDto priceRequestDto= PriceRequestDto.builder().requestDate(Date.from(Instant.parse(DATE)))
        .brandId(BRAND_ID).productId(PRODUCT_ID).build();
        when(priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto)).thenReturn(priceResponseDto);

        Assertions.assertNull(priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto));
    }


    @AfterEach
    void tearDown() {
    }


}