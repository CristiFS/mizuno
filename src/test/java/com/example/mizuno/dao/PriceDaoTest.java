package com.example.mizuno.dao;

import com.example.mizuno.model.Instrument;
import com.example.mizuno.model.Price;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PriceDaoTest {

    public static final String INSTRUMENT_NAME = "petrolBid";
    PriceDao priceDao = new PriceDao();

    @Test
    void shouldDeletePriceByInstrument(){
        //given
        var instrumentId = UUID.randomUUID();
        var instrument = new Instrument();
        instrument.setInstrumentName(INSTRUMENT_NAME);
        instrument.setInstrumentId(instrumentId);
        var prices = new HashSet<Price>();
        var priceWithInstrument = new Price();
        priceWithInstrument.setInstrumentId(UUID.randomUUID());
        priceWithInstrument.setVendorId(UUID.randomUUID());
        priceWithInstrument.setValue(1L);
        prices.add(priceWithInstrument);
        var anotherPrice = new Price();
        anotherPrice.setInstrumentId(UUID.randomUUID());
        anotherPrice.setVendorId(UUID.randomUUID());
        priceWithInstrument.setValue(2L);
        prices.add(anotherPrice);
        priceDao.getPricesByInstrumentName().put(INSTRUMENT_NAME, prices);
        //when
        priceDao.deletePriceByInstrument(priceWithInstrument, instrument);
        //then
        assertEquals(1, priceDao.getPricesByInstrumentName().size());
        Set<Price> resultSet = priceDao.getPricesByInstrumentName().get(INSTRUMENT_NAME);
        assertEquals(anotherPrice, resultSet.iterator().next());
    }


   @Test
   void shouldDeletePriceByInstrumentAndInstrumentCategory(){
       //given
       var instrumentId = UUID.randomUUID();
       var instrument = new Instrument();
       instrument.setInstrumentName(INSTRUMENT_NAME);
       instrument.setInstrumentId(instrumentId);
       var prices = new HashSet<Price>();
       var price = new Price();
       price.setInstrumentId(UUID.randomUUID());
       price.setVendorId(UUID.randomUUID());
       price.setValue(1L);
       prices.add(price);
       priceDao.getPricesByInstrumentName().put(INSTRUMENT_NAME, prices);
       //when
       priceDao.deletePriceByInstrument(price, instrument);
       //then
       assertEquals(0, priceDao.getPricesByInstrumentName().size());

   }


}
