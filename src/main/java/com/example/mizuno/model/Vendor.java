package com.example.mizuno.model;

import java.util.Objects;
import java.util.UUID;

public class Vendor {
    private UUID vendorId;
    private String vendorName;

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendor vendor = (Vendor) o;
        return Objects.equals(vendorId, vendor.vendorId) && Objects.equals(vendorName, vendor.vendorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorId, vendorName);
    }
}
