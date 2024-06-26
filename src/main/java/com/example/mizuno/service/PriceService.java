package com.example.mizuno.service;

import com.example.mizuno.dao.InstrumentDao;
import com.example.mizuno.dao.PriceDao;
import com.example.mizuno.dao.VendorDao;
import com.example.mizuno.model.Instrument;
import com.example.mizuno.model.Price;
import com.example.mizuno.model.Vendor;
import com.example.mizuno.model.PriceInputDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PriceService {

    private final VendorDao vendorDao;

    private final InstrumentDao instrumentDao;

    private final PriceDao priceDao;

    public PriceService(VendorDao vendorDao, InstrumentDao instrumentDao, PriceDao priceDao) {
        this.vendorDao = vendorDao;
        this.instrumentDao = instrumentDao;
        this.priceDao = priceDao;
    }

    public void save(PriceInputDto priceDto) {
        Instrument instrument = saveInstrument(priceDto.getInstrumentName());

        Vendor vendor = saveVendor(priceDto.getVendorName());
        savePrice(instrument, vendor, priceDto.getPrice());
    }

    void savePrice(Instrument instrument, Vendor vendor, Long priceValue){
        Price price = new Price();
        price.setInstrumentId(instrument.getInstrumentId());
        price.setVendorId(vendor.getVendorId());
        price.setValue(priceValue);
        priceDao.savePrice(price, instrument, vendor);
    }

    Vendor saveVendor(String vendorName){
        Vendor vendor = null;

        Vendor existingVendor = vendorDao.findVendorByName(vendorName);
        if(existingVendor==null){
            vendor = new Vendor();
            vendor.setVendorName(vendorName);
            vendor.setVendorId(UUID.randomUUID());
            vendorDao.saveVendor(vendor);
        }else{
            vendor = existingVendor;
        }
        return vendor;
    }

    Instrument saveInstrument(String instrumentName) {
        Instrument instrument = null;

        Instrument existingInstrument = instrumentDao.findInstrumentByName(instrumentName);
        if(existingInstrument==null){
            instrument = new Instrument();
            instrument.setInstrumentId(UUID.randomUUID());
            instrument.setInstrumentName(instrumentName);
            instrumentDao.saveInstrument(instrument);
        }else{
            instrument = existingInstrument;

        }
        return instrument;
    }

    public List<PriceInputDto> getPricesByVendor(String vendorName) {
        List<PriceInputDto> pricesWithInstrumentsPerVendor = new ArrayList<>();

        Set<Price> prices = priceDao.findPricesByVendorName(vendorName);
        if(prices != null){
            for (Price price : prices) {
                PriceInputDto dto = new PriceInputDto();
                dto.setPrice(price.getValue());
                dto.setVendorName(vendorName);

                Instrument instrument = instrumentDao.findInstrumentById(price.getInstrumentId());
                dto.setInstrumentName(instrument.getInstrumentName());
                pricesWithInstrumentsPerVendor.add(dto);
            }
        }

        return pricesWithInstrumentsPerVendor;
    }

    public List<PriceInputDto> getPricesByInstrument(String instrumentName) {
        List<PriceInputDto> pricesWithVendorPerInstrument = new ArrayList<>();

        Set<Price> prices = priceDao.findPricesByInstrumentName(instrumentName);
        if (prices != null) {
            for (Price price : prices) {
                PriceInputDto dto = new PriceInputDto();
                dto.setPrice(price.getValue());
                dto.setInstrumentName(instrumentName);
                Vendor vendor = vendorDao.findVendorById(price.getVendorId());
                dto.setVendorName(vendor.getVendorName());
                pricesWithVendorPerInstrument.add(dto);
            }
        }
        return pricesWithVendorPerInstrument;
    }

    @Scheduled(cron = "0 * * * * *")
    public void deleteObsoletePrices(){

        List<Price> toBeDeletedPrices = priceDao.getInvalidPrices();
        toBeDeletedPrices.forEach(this::deletePrice);

    }

    void deletePrice(Price price){
        priceDao.deletePrice(price);
        Instrument instrument = instrumentDao.findInstrumentById(price.getInstrumentId());
        Vendor vendor = vendorDao.findVendorById(price.getVendorId());

        boolean deleteVendor = priceDao.deletePriceByVendor(price, vendor);
        if(deleteVendor){
            vendorDao.removeVendor(vendor);
        }

        boolean deleteInstrument = priceDao.deletePriceByInstrument(price, instrument);
        if(deleteInstrument){
            instrumentDao.removeInstrument(instrument);
        }
    }

}
