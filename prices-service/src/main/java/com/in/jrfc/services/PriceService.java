package com.in.jrfc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.in.jrfc.dtos.PriceRequestDto;
import com.in.jrfc.dtos.PriceResponseDto;
import com.in.jrfc.entities.Price;
import com.in.jrfc.exceptions.PriceNotFoundException;
import com.in.jrfc.repositories.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PriceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PriceService.class);

    PriceRepository priceRepository;
    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

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

        Optional<Price> _price = Optional.empty();
        if (priceList.size() > 1) {
            _price = priceList.stream().max(Comparator.comparing(Price::getPriority));
        } else if (priceList.size() == 1) {
            _price = Optional.ofNullable(priceList.get(0));
        }
        Price price = _price.orElseThrow(() -> new PriceNotFoundException(HttpStatus.NOT_FOUND,
                "for productId :" + priceFilterParams.getProductId() + " and date "
                        + priceFilterParams.getRequestDate()));

        final ObjectMapper mapper = new ObjectMapper();
        List<LocalDateTime> localDateList = price.lookForAplicationDates(priceFilterParams.getRequestDate());

        final PriceResponseDto priceResponseDto = mapper.convertValue(price, PriceResponseDto.class);
        priceResponseDto.setAplicationDates(localDateList);

        return priceResponseDto;

    }


}
