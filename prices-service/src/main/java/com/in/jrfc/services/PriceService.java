package com.in.jrfc.services;

import com.in.jrfc.dtos.PriceRequestDto;
import com.in.jrfc.dtos.PriceResponseDto;
import com.in.jrfc.entities.Price;
import com.in.jrfc.exceptions.PriceNotFoundException;
import com.in.jrfc.exceptions.PriceRunTimeException;
import com.in.jrfc.repositories.PriceRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class PriceService {
    @Autowired
    private PriceRepository priceRepository;
    @Transactional(readOnly = true)
    public PriceResponseDto getCurrentPriceByProductIdAndBrandId(PriceRequestDto priceFilterParams) throws PriceNotFoundException {

        return entityToDto(
                priceRepository
                        .findByProductIdAndBrandId(priceFilterParams.getProductId(), priceFilterParams.getBrandId())
                        .stream()
                        .filter(price -> price.validPriceRange(priceFilterParams.getRequestDate()))
                        .collect(Collectors.toList())
                , priceFilterParams);
    }

    protected PriceResponseDto entityToDto(List<Price> priceList, PriceRequestDto priceFilterParams) throws PriceNotFoundException {

        Optional<Price> oprice = Optional.empty();
        if (priceList.size() > 1) {
            oprice = priceList.stream().max(Comparator.comparing(Price::getPriority));
        } else if (priceList.size() == 1) {
            oprice = Optional.ofNullable(priceList.get(0));
        }
        Price price = oprice.orElseThrow(() -> new PriceNotFoundException(HttpStatus.NOT_FOUND,
                "for productId :" + priceFilterParams.getProductId() + " and date "
                        + priceFilterParams.getRequestDate()));


        return entityToDto(price, priceFilterParams.getRequestDate());

    }

    private PriceResponseDto entityToDto(Price price, Date filterDate) throws PriceRunTimeException {
        return PriceResponseDto.builder().productId(price.getProductId())
                .brandId(price.getBrandId())
                .priceList(price.getPriceList())
                .applicationDates(price.lookForAplicationDates(filterDate))
                .price(price.getPrice())
                .build();

    }
}
