package com.example.mizuno.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Price {
    private Long value;
    private UUID instrumentId;
    private UUID vendorId;
    private LocalDateTime creationTime = LocalDateTime.now();

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public UUID getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(UUID instrumentId) {
        this.instrumentId = instrumentId;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(instrumentId, price1.instrumentId) && Objects.equals(vendorId, price1.vendorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentId, vendorId);
    }
}
