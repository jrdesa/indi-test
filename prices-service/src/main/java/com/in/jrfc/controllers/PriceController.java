package com.in.jrfc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.in.jrfc.dtos.PriceRequestDto;
import com.in.jrfc.dtos.PriceResponseDto;
import com.in.jrfc.exceptions.PriceNotFoundException;
import com.in.jrfc.exceptions.PriceRunTimeException;
import com.in.jrfc.services.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class PriceController {

    @Autowired
    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @Operation(summary = "filterPrice", description = "Get a priceResponseDto by requestDate productId and brandId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PriceResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)})
    @RequestMapping(method = RequestMethod.GET, value = "/price", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceResponseDto> filterPrice(@RequestParam Map<String, String> filterParams ) throws PriceNotFoundException, PriceRunTimeException, InvalidFormatException {

        PriceRequestDto priceFilterParams;
        try {
            priceFilterParams = getPriceRequestDto(filterParams);
        } catch (Exception e) {
            throw new PriceRunTimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getCause().getLocalizedMessage());
        }
        PriceResponseDto priceResponseDto = priceService.getCurrentPriceByProductIdAndBrandId(priceFilterParams);
        if (priceResponseDto != null) {
            return new ResponseEntity<>(priceResponseDto, HttpStatus.OK);
        } else {
            throw new PriceNotFoundException();
        }
    }

    private PriceRequestDto getPriceRequestDto(Map<String, String> filterParams) throws PriceRunTimeException {
        final ObjectMapper mapper = new ObjectMapper();

        return mapper.convertValue(filterParams, PriceRequestDto.class);

    }
}



