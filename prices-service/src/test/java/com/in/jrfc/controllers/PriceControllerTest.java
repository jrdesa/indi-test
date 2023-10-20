package com.in.jrfc.controllers;

import com.in.jrfc.dtos.PriceRequestDto;
import com.in.jrfc.dtos.PriceResponseDto;
import com.in.jrfc.entities.Price;
import com.in.jrfc.services.PriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@RunWith(SpringRunner.class)

@WebMvcTest(PriceController.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PricesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private Price price;
    private List<LocalDate> prices;
    private PriceRequestDto priceRequestDto;
    private PriceResponseDto priceResponseDto;

    @MockBean
    private PriceService priceService;

    @MockBean
    private PriceController priceController;


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        priceService = mock(PriceService.class);
        this.priceController = new PriceController(priceService);
        this.priceRequestDto = PriceRequestDto.builder().requestDate(Timestamp.valueOf("2020-06-14 00:00:00"))
                .productId(35455).brandId(1L).build();
        this.prices = new ArrayList<>();
        this.prices.add(LocalDate.ofInstant(this.priceRequestDto.getRequestDate().toInstant(), ZoneId.of("UTC")));


    }



    @Test
    @DisplayName("when_get_price_bad_request_then_return_bad_request_status")
    void getPriceBadRequest() throws Exception {
        //Given
        String requestDate = "2020-06_14 00:00:00";
        Integer pId = 35455;
        String bId = "1";
        when(priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto))
                .thenReturn(this.priceResponseDto);
        MvcResult priceResponseDto1 = mockMvc.perform(get("/prices/filter/{requestDate},{productId},{brandId}", requestDate, pId, bId)
                .contentType("application/json")
        ).andDo(print()).andReturn();

        PriceResponseDto responseDtoResult = priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto);
        Assertions.assertNull(responseDtoResult);
    }

    @Test
    @DisplayName("when_get_price_not_found_then_return_not_found_status")
    void getPriceNotFoundResponse() throws Exception {
        //Given
        String requestDate = "2021-06-14 00:00:00";
        Integer productId = 35455;
        String brandId = "1";
        when(priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto))
                .thenReturn(this.priceResponseDto);
        mockMvc.perform(get("/prices/filter/{requestDate},{productId},{brandId}", requestDate, productId, brandId)
                .contentType("application/json")
        ).andDo(print()).andExpect(status().isNotFound());
        PriceResponseDto responseDtoResult = priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto);
        Assertions.assertNull(responseDtoResult);
    }

}

