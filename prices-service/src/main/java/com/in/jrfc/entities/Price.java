package com.in.jrfc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
public class Price {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long brandId;

    @JsonIgnore
    private Date startDate;

    @JsonIgnore
    private Date endDate;
    private Integer priceList;
    private Integer productId;
    @JsonIgnore
    private Integer priority;
    private BigDecimal price;
    @JsonIgnore
    private String curr;

    @JsonIgnore
    @Transient
    private List<LocalDateTime> priceMandatoryDays;

    public Price(Long brandId, Date startDate, Date endDate, Integer priceList, Integer productId, Integer priority, BigDecimal price, String curr) {
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
        this.curr = curr;
    }

    public boolean validPriceRange(Date applicationTime) {
        return aplicationDates(applicationTime);
    }

    private boolean aplicationDates(Date applicationTime) {
        return this.startDate.compareTo(applicationTime) <= 0 && this.endDate.compareTo(applicationTime) >= 0;
    }

    public List<LocalDateTime> lookForAplicationDates(Date filterDate) {

        this.priceMandatoryDays = Collections
                .unmodifiableList(listPriceMandatoryDays(filterDate));
        return this.priceMandatoryDays;
    }

    protected List<LocalDateTime> listPriceMandatoryDays(Date filterDate) {
        final List<LocalDateTime> localDateList = new ArrayList<>();
        if (this.endDate.after(filterDate)) {
            localDateList.add(LocalDateTime.ofInstant(filterDate.toInstant(), ZoneId.of("UTC")));
        }
        localDateList.add(LocalDateTime.ofInstant(this.endDate.toInstant(), ZoneId.of("UTC")));
        return localDateList;
    }


}
