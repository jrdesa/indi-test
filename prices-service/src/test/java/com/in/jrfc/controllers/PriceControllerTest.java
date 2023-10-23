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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
private static final String REQUEST_URL = "/api/price?requestDate=2020-06-14 16:00:00&productId=35455&brandId=1";
private static final String REQUEST_URL_NOT_FOUND = "/apisss/price?requestDate=2020-06-14 16:00:00&productId=35455&brandId=1";
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
    private Map<String, String> requestParams;

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
    @DisplayName("when_get_price_then_return_ok_status")
    void getPriceTest() throws Exception {
        this.price = Price.builder().brandId(1L)
                .startDate(Timestamp.valueOf("2020-06-14 00:00:00"))
                .endDate(Timestamp.valueOf("2020-12-31 23:59:59")).priceList(1)
                .productId(35455).priority(0).price(BigDecimal.valueOf(35.50))
                .curr("EUR").build();
        this.priceResponseDto = PriceResponseDto.builder()
                .productId(price.getProductId())
                .brandId(price.getBrandId())
                .priceList(price.getPriceList())
                .applicationDates(price.lookForAplicationDates(this.priceRequestDto.getRequestDate()))
                .price(price.getPrice()).build();
        final String requestDate = "2020-06-14 00:00:00";
        final Integer productId = 35455;
        final String brandId = "1";
        when(this.priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto))
                .thenReturn(this.priceResponseDto);

      mockMvc.perform(get(REQUEST_URL)
                .contentType("application/json")
        ).andDo(print()).andExpect(status().isOk());

        PriceResponseDto responseDtoResult = priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto);

        Assertions.assertEquals(priceResponseDto.getPrice(), BigDecimal.valueOf(35.50));
        Assertions.assertEquals(priceResponseDto.getProductId(), 35455);
        Assertions.assertEquals(priceResponseDto.getBrandId(), 1L);
        Assertions.assertEquals(
                priceResponseDto.getApplicationDates().size(), 2);


    }


    @Test
    @DisplayName("when_get_price_not_found_then_return_not_found_status")
    void getPriceNotFoundResponseTest() throws Exception {
        when(priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto))
                .thenReturn(this.priceResponseDto);
        mockMvc.perform(get(REQUEST_URL_NOT_FOUND)
                .contentType("application/json")).andDo(print()).andExpect(status().isNotFound());
        PriceResponseDto responseDtoResult = priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto);
        Assertions.assertNull(responseDtoResult);
    }

    @Test
    void getPriceRequestDtoTest() {
        this.requestParams = Map.of("requestDate", "2020-06-14 00:00:00", "productId", "35455", "brandId", "1");

        when(priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDto))
                .thenReturn(this.priceResponseDto);
        Assertions.assertNotNull(priceController.getPriceRequestDto(requestParams));

    }
}

