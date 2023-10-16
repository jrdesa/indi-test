package com.in.jrfc.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class PriceRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date requestDate;
    private Integer productId;
    private Long brandId;

    @Override
    public String toString() {
        return String.format(
                "PriceRequestDto[requestDate = %s, productId = %s, brandId = %s]", getRequestDate(),
                getProductId(), getBrandId());
    }
}