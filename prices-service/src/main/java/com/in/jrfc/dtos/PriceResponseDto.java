package com.in.jrfc.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class PriceResponseDto {
    private Integer productId;
    private Long brandId;
    private Integer priceList;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private List<LocalDateTime> applicationDates;
    private BigDecimal price;

    @Override
    public String toString() {
        return String.format(
                "PriceResponseDto[brandId =%s, priceList =%s, productId =%s, price =%s]",
                getProductId(), getBrandId(), getPriceList(), getPrice());
    }
}