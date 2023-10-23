package com.in.jrfc;

import com.in.jrfc.dtos.PriceRequestDto;
import com.in.jrfc.dtos.PriceResponseDto;
import com.in.jrfc.entities.Price;
import com.in.jrfc.repositories.PriceRepository;
import com.in.jrfc.services.PriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ExpectedBehaviorServiceTest {
    private String requestUrl;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PriceService priceService;
    @MockBean
    private PriceRepository priceRepository;
    Price price;
    private final List<LocalDateTime> fromToMandatoryPriceDates = new ArrayList<>();
    private final List<Price> priceList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        Price price1 = Price.builder().brandId(1L)
                .startDate(Timestamp.valueOf("2020-06-14 00:00:00"))
                .endDate(Timestamp.valueOf("2020-12-31 23:59:59")).priceList(11)
                .productId(35455).priority(0).price(BigDecimal.valueOf(35.50))
                .curr("EUR").build();
        Price price2 = Price.builder().brandId(1L)
                .startDate(Timestamp.valueOf("2020-06-14 15:00:00"))
                .endDate(Timestamp.valueOf("2020-06-14 18:30:00")).priceList(12)
                .productId(35455).priority(1).price(BigDecimal.valueOf(25.45))
                .curr("EUR").build();
        Price price3 = Price.builder().brandId(1L)
                .startDate(Timestamp.valueOf("2020-06-15 00:00:00"))
                .endDate(Timestamp.valueOf("2020-12-15 11:00:00")).priceList(31)
                .productId(35455).priority(1).price(BigDecimal.valueOf(30.50))
                .curr("EUR").build();
        Price price4 = Price.builder().brandId(1L)
                .startDate(Timestamp.valueOf("2020-06-15 16:00:00"))
                .endDate(Timestamp.valueOf("2020-12-31 23:59:59")).priceList(14)
                .productId(35455).priority(1).price(BigDecimal.valueOf(38.95))
                .curr("EUR").build();
        priceList.add(price1);
        priceList.add(price2);
        priceList.add(price3);
        priceList.add(price4);
        price = price2;


//        Price price1 = Price.builder()
//                .startDate(Timestamp.valueOf("2020-06-14 00:00:00"))
//                .endDate(Timestamp.valueOf("2020-12-31 23:59:59")).priceList(1)
//
//                .productId(35455).priority(0).price(BigDecimal.valueOf(35.50))
//                .build();
        Price price12 = price1.builder()
                .startDate(Timestamp.valueOf("2020-06-14 15:00:00"))
                .endDate(Timestamp.valueOf("2020-06-14 18:30:00")).priceList(2)
                .priority(1).price(BigDecimal.valueOf(25.45))
                .build();
        Price price13 = price1.builder()
                .startDate(Timestamp.valueOf("2020-06-15 00:00:00"))
                .endDate(Timestamp.valueOf("2020-12-15 11:00:00")).priceList(3)
                .priority(1).price(BigDecimal.valueOf(30.50))
                .build();
        Price price14 = price1.builder()
                .startDate(Timestamp.valueOf("2020-06-15 16:00:00"))
                .endDate(Timestamp.valueOf("2020-12-31 23:59:59")).priceList(4)
                .priority(1).price(BigDecimal.valueOf(38.95))
                .build();
        priceList.add(price1);
        priceList.add(price2);
        priceList.add(price3);
        priceList.add(price4);
        price = price2;

//        PriceResponseDto priceResponseDto = PriceResponseDto.builder()
//                .productId(price3.getProductId())
//                .brandId(price3.getBrandId())
//                .priceList(price3.getPriceList())
//                .applicationDates(fromToMandatoryPriceDates)
//                .price(price3.getPrice()).build();

        PriceRequestDto priceRequestDto = PriceRequestDto.builder()
                .requestDate(Timestamp.valueOf("2020-06-14 11:55:00"))
                .brandId(1L)
                .productId(35455).build();
        this.requestUrl = "/api/price";
    }


    @ParameterizedTest
    @MethodSource("priceRequestDtoProviderFactory")
    Iterator<PriceRequestDto> testAllPrice(PriceRequestDto priceRequestDtoAll) throws RuntimeException {

        when(priceRepository.findByProductIdAndBrandId(priceRequestDtoAll.getProductId(), priceRequestDtoAll.getBrandId())).thenReturn(priceList);
        requestUrl = new StringBuilder(requestUrl)
                .append("?")
                .append("requestDate")
                .append("=")
                .append(priceRequestDtoAll.getRequestDate())
                .append("&")
                .append("productId")
                .append("=")
                .append(priceRequestDtoAll.getProductId())
                .append("&")
                .append("brandId")
                .append("=")
                .append(priceRequestDtoAll.getBrandId()).toString();
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(requestUrl
                            )
                            .contentType("application/json")
            ).andDo(print()).andExpect(status().isOk());
            PriceResponseDto responseDtoResult = priceService.getCurrentPriceByProductIdAndBrandId(priceRequestDtoAll);
            Assertions.assertAll(
                    () -> assertEquals(35455, responseDtoResult.getProductId()
                            , "productId " + responseDtoResult.getProductId()),
                    () -> assertEquals(1L, responseDtoResult.getBrandId()
                            , "brandID " + responseDtoResult.getBrandId()),
                    () -> assertTrue(responseDtoResult.getApplicationDates().size() > 0
                            , "mandatory aplication " + responseDtoResult.getApplicationDates().size()));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
  return priceRequestDtoIterator();  }

        static Iterator<PriceRequestDto> priceRequestDtoIterator() {
           List<PriceRequestDto> requestDtos = new ArrayList<>();
            //         Test 1: petición a las 10:00 del día 14 del producto 35455   para la brand 1 (ZARA)

            requestDtos.add(PriceRequestDto.builder()
                    .requestDate(Timestamp.valueOf("2020-06-14 10:00:00"))
                    .productId(35455)
                    .brandId(1L).build());

//            -          Test 2: petición a las 16:00 del día 14 del producto 35455   para la brand 1 (ZARA)
            requestDtos.add(PriceRequestDto.builder()
                    .requestDate(Timestamp.valueOf("2020-06-14 16:00:00"))
                    .productId(35455)
                    .brandId(1L).build());


//       Test 6: petición a las 10:00 del día 14 del producto 35455   para la brand 1 (ZARA)
            requestDtos.add(PriceRequestDto.builder()
                    .requestDate(Timestamp.valueOf("2020-06-14 16:00:00"))
                    .productId(35455)
                    .brandId(1L).build());
            //       Test 3: petición a las 21:00 del día 14 del producto 35455   para la brand 1 (ZARA)
            requestDtos.add(PriceRequestDto.builder()
                    .requestDate(Timestamp.valueOf("2020-06-14 21:00:00"))
                    .productId(35455)
                    .brandId(1L).build());
// @Test 4: petición a las 10:00 del día 15 del producto 35455   para la brand 1 (ZARA)
            requestDtos.add(PriceRequestDto.builder()
                    .requestDate(Timestamp.valueOf("2020-06-15 10:00:00"))
                    .productId(35455)
                    .brandId(1L).build());
//            Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1 (ZARA)
            requestDtos.add(PriceRequestDto.builder()
                    .requestDate(Timestamp.valueOf("2020-06-16 21:00:00"))
                    .productId(35455)
                    .brandId(1L).build());
            return requestDtos.stream().iterator();
        }

    }