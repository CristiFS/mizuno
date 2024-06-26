package com.example.mizuno.dao;

import com.example.mizuno.model.Vendor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class VendorDao {

    private final Map<UUID, Vendor> vendorsById = new HashMap<>();
    private final Map<String, Vendor> vendorsByName = new HashMap<>();

    public Vendor findVendorByName(String vendorName){
        return vendorsByName.get(vendorName);
    }

    public Vendor findVendorById(UUID vendorId){
        return vendorsById.get(vendorId);
    }

    public void saveVendor(Vendor vendor){
        vendorsByName.put(vendor.getVendorName(), vendor);
        vendorsById.put(vendor.getVendorId(), vendor);
    }

    public void removeVendor(Vendor vendor){
        vendorsById.remove(vendor.getVendorId());
        vendorsByName.remove(vendor.getVendorName());
    }

    public Map<UUID, Vendor> getVendorsById() {
        return vendorsById;
    }

    public Map<String, Vendor> getVendorsByName() {
        return vendorsByName;
    }


}
