package com.example.mizuno.service;

import com.example.mizuno.dao.InstrumentDao;
import com.example.mizuno.dao.PriceDao;
import com.example.mizuno.dao.VendorDao;
import com.example.mizuno.model.Instrument;
import com.example.mizuno.model.Price;
import com.example.mizuno.model.Vendor;
import com.example.mizuno.model.PriceInputDto;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PriceServiceTest {

    public static final String INSTRUMENT_NAME = "petrolBid";
    public static final String VENDOR_NAME = "mizuno";
    VendorDao vendorDao = new VendorDao();

    InstrumentDao instrumentDao = new InstrumentDao();

    PriceDao priceDao = new PriceDao();

    PriceService priceService = new PriceService(vendorDao, instrumentDao, priceDao);

    @Test
    void shouldSaveNewVendor(){
        //given
        String vendorName = VENDOR_NAME;
        //when
        Vendor returnedVendor = priceService.saveVendor(vendorName);
        //then
        assertNotNull(returnedVendor);
        Vendor inMemoryByVendorName =  vendorDao.getVendorsByName().get(vendorName);
        assertEquals(returnedVendor, inMemoryByVendorName);
        Vendor inMemoryByVendorId =  vendorDao.getVendorsById().get(returnedVendor.getVendorId());
        assertEquals(returnedVendor, inMemoryByVendorId);
    }

    @Test
    void shouldNotUpdateExistingVendor(){
        //given
        Vendor vendor = new Vendor();
        String vendorName = VENDOR_NAME;
        vendor.setVendorName(vendorName);
        UUID vendorId = UUID.randomUUID();
        vendor.setVendorId(vendorId);
        vendorDao.getVendorsByName().put(vendorName, vendor);

        //when
        Vendor returnedVendor = priceService.saveVendor(vendorName);
        //then
        assertNotNull(returnedVendor);
        Vendor inMemoryVendor =  vendorDao.getVendorsByName().get(vendorName);
        assertEquals(inMemoryVendor.getVendorId(), vendorId);
    }

    @Test
    void shouldSaveInstrument(){
        //given
        String instrumentName = INSTRUMENT_NAME;
        //when
        Instrument returnedInstrument = priceService.saveInstrument(instrumentName);
        //then
        assertNotNull(returnedInstrument);
        Instrument savedByNameInstrument = instrumentDao.getInstrumentsByName().get(instrumentName);
        assertEquals(returnedInstrument, savedByNameInstrument);
        Instrument savedByIdInstrument = instrumentDao.getInstrumentsById().get(returnedInstrument.getInstrumentId());
        assertEquals(savedByIdInstrument, returnedInstrument);
    }

    @Test
    void shouldNotUpdateExistingInstrument(){
        //given
        String instrumentName = INSTRUMENT_NAME;
        UUID instrumentId = UUID.randomUUID();

        Instrument instrument = new Instrument();
        instrument.setInstrumentId(instrumentId);
        instrument.setInstrumentName(instrumentName);
        instrumentDao.getInstrumentsByName().put(instrumentName, instrument);
        //when
        Instrument returnedInstrument = priceService.saveInstrument(instrumentName);
        //then
        assertNotNull(returnedInstrument);
        Instrument inMemoryInstrument = instrumentDao.getInstrumentsByName().get(instrumentName);
        assertEquals(inMemoryInstrument.getInstrumentId(), instrumentId);

    }

    @Test
    void shouldSavePrice(){
        //given
        UUID instrumentId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();
        String instrumentName = INSTRUMENT_NAME;
        String vendorName = VENDOR_NAME;

        Instrument instrument = new Instrument();
        instrument.setInstrumentId(instrumentId);
        instrument.setInstrumentName(instrumentName);
        Vendor vendor = new Vendor();
        vendor.setVendorId(vendorId);
        vendor.setVendorName(vendorName);

        //when
        priceService.savePrice(instrument, vendor, 3L);
        //then
        Price price = priceDao.getAllPrices().iterator().next();
        assertEquals(price.getVendorId(), vendorId);
        assertEquals(price.getInstrumentId(), instrumentId);
        assertEquals(price.getValue(), 3L);

        assertTrue(priceDao.getPricesByInstrumentName().get(instrumentName).contains(price));
        assertTrue( priceDao.getPricesByVendorName().get(vendorName).contains(price));
    }


    @Test
    void shouldUpdatePrice(){
        //given
        UUID instrumentId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();
        String instrumentName = INSTRUMENT_NAME;
        String vendorName = VENDOR_NAME;

        Instrument instrument = new Instrument();
        instrument.setInstrumentId(instrumentId);
        instrument.setInstrumentName(instrumentName);
        Vendor vendor = new Vendor();
        vendor.setVendorId(vendorId);
        vendor.setVendorName(vendorName);

        Price existingPrice = new Price();
        existingPrice.setInstrumentId(instrumentId);
        existingPrice.setVendorId(vendorId);
        existingPrice.setValue(1L);
        priceDao.getAllPrices().add(existingPrice);

        //when
        priceService.savePrice(instrument, vendor, 3L);
        //then
        Price price = priceDao.getAllPrices().iterator().next();
        assertEquals(price.getVendorId(), vendorId);
        assertEquals(price.getInstrumentId(), instrumentId);
        assertEquals(price.getValue(), 3L);
    }

    @Test
    void shouldSaveInputDto(){
        //given
        String instrumentName = INSTRUMENT_NAME;
        String vendorName = VENDOR_NAME;

        PriceInputDto dto = new PriceInputDto();
        dto.setPrice(1L);
        dto.setVendorName(vendorName);
        dto.setInstrumentName(instrumentName);

        //when
        priceService.save(dto);
        //then
        Price price = priceDao.getAllPrices().iterator().next();
        assertTrue(priceDao.getPricesByInstrumentName().get(instrumentName).contains(price));
        assertTrue(priceDao.getPricesByVendorName().get(vendorName).contains(price));
        assertNotNull(instrumentDao.getInstrumentsByName().get(instrumentName));
        assertNotNull(vendorDao.getVendorsByName().get(vendorName));
    }

    @Test
    void shouldDeleteObsoletePrice(){
        //given
        var instrumentId = UUID.randomUUID();
        var vendorId = UUID.randomUUID();

        var price = new Price();
        price.setVendorId(vendorId);
        price.setInstrumentId(instrumentId);

        var instrument = new Instrument();
        instrument.setInstrumentId(instrumentId);
        instrument.setInstrumentName(INSTRUMENT_NAME);

        var vendor = new Vendor();
        vendor.setVendorId(vendorId);
        vendor.setVendorName(VENDOR_NAME);


        instrumentDao.getInstrumentsById().put(instrumentId, instrument);
        instrumentDao.getInstrumentsByName().put(INSTRUMENT_NAME, instrument);
        vendorDao.getVendorsById().put(vendorId, vendor);
        vendorDao.getVendorsByName().put(VENDOR_NAME, vendor);

        priceDao.getAllPrices().add(price);
        priceDao.getPricesByInstrumentName().put(INSTRUMENT_NAME, new HashSet<>(List.of(price)));
        priceDao.getPricesByVendorName().put(VENDOR_NAME, new HashSet<>(List.of(price)));

        //when
        priceService.deletePrice(price);
        //then
        assertNull(instrumentDao.getInstrumentsById().get(instrumentId));
        assertNull(instrumentDao.getInstrumentsByName().get(INSTRUMENT_NAME));
        assertNull(vendorDao.getVendorsById().get(vendorId));
        assertNull(vendorDao.getVendorsByName().get(VENDOR_NAME));
        assertEquals(0, priceDao.getAllPrices().size());
        assertNull(priceDao.getPricesByInstrumentName().get(INSTRUMENT_NAME));
        assertNull(priceDao.getPricesByVendorName().get(VENDOR_NAME));
    }

}
