package com.example.mizuno.dao;

import com.example.mizuno.model.Instrument;
import com.example.mizuno.model.Price;
import com.example.mizuno.model.Vendor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class PriceDao {

    public static final int EXPIRATION_SECONDS = 70;
    private final Map<String, Set<Price>> pricesByVendorName = new HashMap<>();
    private final Map<String, Set<Price>> pricesByInstrumentName = new HashMap<>();
    private final Set<Price> allPrices = new HashSet<>();

    public void savePrice(Price price, Instrument instrument, Vendor vendor){
        //if price already present we just update the value
        if(allPrices.contains(price)){
            Price existingPrice = allPrices.stream().filter(element ->element.equals(price)).findFirst().get();
            existingPrice.setValue(price.getValue());
        }else{
            //otherwise we update the search indexes too
            allPrices.add(price);
            Set<Price> pricesByVendor = pricesByVendorName.computeIfAbsent(vendor.getVendorName(), k -> new HashSet<>());
            pricesByVendor.add(price);
            Set<Price> pricesByInstruments = pricesByInstrumentName.computeIfAbsent(instrument.getInstrumentName(), k -> new HashSet<>());
            pricesByInstruments.add(price);
        }
    }

    public Set<Price> findPricesByVendorName(String vendorName){
        return pricesByVendorName.get(vendorName);
    }

    public Set<Price> findPricesByInstrumentName(String instrumentName){
        return pricesByInstrumentName.get(instrumentName);
    }

    public List<Price> getInvalidPrices(){
        return allPrices.stream().filter(this::getInvalidPrice).toList();
    }

    private boolean getInvalidPrice(Price price){
        var priceCreationTime = price.getCreationTime();
        var duration = Duration.between(priceCreationTime, LocalDateTime.now());
        return duration.getSeconds()> EXPIRATION_SECONDS;
    }

    public void deletePrice(Price price){
        allPrices.remove(price);
    }

    public boolean deletePriceByInstrument(Price price, Instrument instrument){
        Set<Price> pricesByInstrument = pricesByInstrumentName.get(instrument.getInstrumentName());
        if(pricesByInstrument!=null && !pricesByInstrument.isEmpty()){
            pricesByInstrument.remove(price);
        }

        if(pricesByInstrument!= null && pricesByInstrument.isEmpty()){
            pricesByInstrumentName.remove(instrument.getInstrumentName());
            return true;
        }
        return false;
    }

    public boolean deletePriceByVendor(Price price, Vendor vendor){
        Set<Price> pricesByVendor = pricesByVendorName.get(vendor.getVendorName());
        if(pricesByVendor!=null && !pricesByVendor.isEmpty()){
            pricesByVendor.remove(price);
        }
        if(pricesByVendor!= null && pricesByVendor.isEmpty()){
            pricesByVendorName.remove(vendor.getVendorName());
            return true;
        }
        return false;
    }

    public Map<String, Set<Price>> getPricesByVendorName() {
        return pricesByVendorName;
    }

    public Map<String, Set<Price>> getPricesByInstrumentName() {
        return pricesByInstrumentName;
    }

    public Set<Price> getAllPrices() {
        return allPrices;
    }
}
