package com.example.mizuno.controller;

import com.example.mizuno.model.PriceInputDto;
import com.example.mizuno.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prices")
public class PriceController {
    Logger logger = LoggerFactory.getLogger(PriceController.class);

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }


    @PostMapping("/save")
    public ResponseEntity<PriceInputDto> createPriceData(@RequestBody PriceInputDto priceDto){
        priceService.save(priceDto);
        return ResponseEntity.ok(priceDto);
    }

    @GetMapping("/getPricesByVendor/{vendorName}")
    public ResponseEntity<List<PriceInputDto>> getPricesByVendor(@PathVariable String vendorName){
        List<PriceInputDto> prices = priceService.getPricesByVendor(vendorName);
        if(prices == null || prices.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/getPricesByInstrument/{instrumentName}")
    public ResponseEntity<List<PriceInputDto>> getPricesByInstrument(@PathVariable String instrumentName){
        List<PriceInputDto> prices = priceService.getPricesByInstrument(instrumentName);
        if(prices == null || prices.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prices);
    }

}
