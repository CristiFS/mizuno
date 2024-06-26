package com.example.mizuno.model;

import java.util.Objects;

public class PriceInputDto {
    private String instrumentName;
    private Long price;
    private String vendorName;

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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
        PriceInputDto that = (PriceInputDto) o;
        return Objects.equals(instrumentName, that.instrumentName) && Objects.equals(price, that.price) && Objects.equals(vendorName, that.vendorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentName, price, vendorName);
    }
}
