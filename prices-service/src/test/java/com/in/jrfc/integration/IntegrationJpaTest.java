package com.in.jrfc.integration;

import com.in.jrfc.entities.Price;
import com.in.jrfc.repositories.PriceRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class IntegrationJpaTest {
    private final Integer PRODUCT_ID = 99999;
    private Price price;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PriceRepository priceRepository;

//    public IntegrationJpaTest(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    @BeforeEach
    void setUp() {
        this.price = Price.builder().id(5L).brandId(1L)
                .startDate(Timestamp.valueOf("2021-06-14 00:00:00"))
                .endDate(Timestamp.valueOf("2021-12-31 23:59:59")).priceList(5)
                .productId(PRODUCT_ID).priority(1).price(BigDecimal.valueOf(160.60))
                .curr("EUR").build();
        new Price();
//when save a price
        priceRepository.save(this.price);
    }
    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(priceRepository).isNotNull();
    }

    @Test
    @DisplayName("can_get_a_price_by_productId_and_brandId")
    void findByProductIdAndBrandIdTest() {
        //given  prize
        //them save a price
        //   priceRepository.save(this.price);
        //then find a price by product id and brand id
        //and check if is not null

        assertThat(priceRepository.findByProductIdAndBrandId(99999, 1L)).isNotNull();
//and check if the price is equal to the price saved
        assertThat(priceRepository.findByProductIdAndBrandId(99999, 1L).size()).isEqualTo(1);


    }

    @Test

    @DisplayName("givendb_contains_prices_when_find_prices_by_product_id")
    public void findPricesByProductIdTest() {
        //Given a db with prices
      /*  Price price = Price.builder().id(5L).brandId(1L)
                .startDate(Timestamp.valueOf("2021-06-14 00:00:00"))
                .endDate(Timestamp.valueOf("2021-12-31 23:59:59")).priceList(5)
                .productId(PRODUCT_ID).priority(1).price(BigDecimal.valueOf(160.60))
                .curr("EUR").build();
//when save a price*/
//        priceRepository.save(this.price);
        //then find prices by product id
        List<Price> prices = priceRepository.findAllByProductId(PRODUCT_ID);
        Assertions.assertEquals(1, prices.size());
    }

    @BeforeEach
    void tearDown() {
        priceRepository.deleteAll();
    }
}

