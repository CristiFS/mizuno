package com.example.mizuno.service;

import com.example.mizuno.dao.InstrumentDao;
import com.example.mizuno.dao.PriceDao;
import com.example.mizuno.dao.VendorDao;
import com.example.mizuno.model.Instrument;
import com.example.mizuno.model.Price;
import com.example.mizuno.model.Vendor;
import com.example.mizuno.model.PriceInputDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PriceServiceUnitTest {
    @Mock
    private VendorDao vendorDao;
    @Mock
    private InstrumentDao instrumentDao;
    @Mock
    private PriceDao priceDao;

    @InjectMocks
    PriceService priceService;

   @Test
   void shouldGetPricesByVendor(){
       //given
       var instrumentId1 = UUID.randomUUID();
       var instrumentId2 = UUID.randomUUID();

       var firstInstrument = new Instrument();
       firstInstrument.setInstrumentName("firstInstrument");

       var secondInstrument = new Instrument();
       secondInstrument.setInstrumentName("secondInstrument");

       when(priceDao.findPricesByVendorName("mizuno")).thenReturn(populatePricesWithInstruments(instrumentId1, instrumentId2));
       when(instrumentDao.findInstrumentById(instrumentId1)).thenReturn(firstInstrument);
       when(instrumentDao.findInstrumentById(instrumentId2)).thenReturn(secondInstrument);
       //when
       List<PriceInputDto> dtos =  priceService.getPricesByVendor("mizuno");
       //then
        assertEquals(2, dtos.size());
        for(PriceInputDto dto: dtos){
            if(dto.getPrice() == 1L){
                assertEquals("firstInstrument", dto.getInstrumentName());
                assertEquals("mizuno", dto.getVendorName());
            }else{
                assertEquals("secondInstrument", dto.getInstrumentName());
                assertEquals("mizuno", dto.getVendorName());
            }
        }
        verify(priceDao).findPricesByVendorName("mizuno");
        verify(instrumentDao).findInstrumentById(instrumentId1);
        verify(instrumentDao).findInstrumentById(instrumentId2);
   }

   private Set<Price> populatePricesWithInstruments(UUID instrumentId1, UUID instrumentId2){
       var prices = new HashSet<Price>();
       var firstPricePerVendor = new Price();
       firstPricePerVendor.setInstrumentId(instrumentId1);
       firstPricePerVendor.setValue(1L);
       prices.add(firstPricePerVendor);

       var secondPricePerVendor = new Price();
       secondPricePerVendor.setInstrumentId(instrumentId2);
       secondPricePerVendor.setValue(2L);
       prices.add(secondPricePerVendor);

       return prices;
   }

   private Set<Price> populatePricesWithVendors(UUID vendorId1, UUID vendorId2){
       var prices = new HashSet<Price>();
       var firstPricePerVendor = new Price();
       firstPricePerVendor.setVendorId(vendorId1);
       firstPricePerVendor.setValue(1L);
       prices.add(firstPricePerVendor);

       var secondPricePerVendor = new Price();
       secondPricePerVendor.setVendorId(vendorId2);
       secondPricePerVendor.setValue(2L);
       prices.add(secondPricePerVendor);

       return prices;
   }

    @Test
   void shouldGetPricesByInstrument(){
        //given
        var vendorId1 = UUID.randomUUID();
        var vendorId2 = UUID.randomUUID();

        var firstVendor = new Vendor();
        firstVendor.setVendorName("mizuno");

        var secondVendor = new Vendor();
        secondVendor.setVendorName("hirachi");

        when(priceDao.findPricesByInstrumentName("petrolBid")).thenReturn(populatePricesWithVendors(vendorId1, vendorId2));
        when(vendorDao.findVendorById(vendorId1)).thenReturn(firstVendor);
        when(vendorDao.findVendorById(vendorId2)).thenReturn(secondVendor);

        //when
        List<PriceInputDto> dtos =  priceService.getPricesByInstrument("petrolBid");
        //then
        assertEquals(2, dtos.size());
        for(PriceInputDto dto:dtos){
            if(dto.getPrice() == 1L){
                assertEquals("petrolBid", dto.getInstrumentName());
                assertEquals("mizuno", dto.getVendorName());
            }else{
                assertEquals("petrolBid", dto.getInstrumentName());
                assertEquals("hirachi", dto.getVendorName());
            }
        }

        verify(priceDao).findPricesByInstrumentName("petrolBid");
        verify(vendorDao).findVendorById(vendorId1);
        verify(vendorDao).findVendorById(vendorId2);
   }


}
