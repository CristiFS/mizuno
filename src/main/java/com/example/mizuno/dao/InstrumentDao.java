package com.example.mizuno.dao;

import com.example.mizuno.model.Instrument;
import com.example.mizuno.model.Vendor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class InstrumentDao {

    private final Map<UUID, Instrument> instrumentsById = new HashMap<>();
    private final Map<String, Instrument> instrumentsByName = new HashMap<>();

    public Instrument findInstrumentByName(String instrumentName){
        return instrumentsByName.get(instrumentName);
    }

    public Instrument findInstrumentById(UUID instrumentId){
        return instrumentsById.get(instrumentId);
    }

    public void saveInstrument(Instrument instrument){
        instrumentsByName.put(instrument.getInstrumentName(), instrument);
        instrumentsById.put(instrument.getInstrumentId(), instrument);
    }

    public void removeInstrument(Instrument instrument){
        instrumentsById.remove(instrument.getInstrumentId());
        instrumentsByName.remove(instrument.getInstrumentName());
    }

    public Map<UUID, Instrument> getInstrumentsById() {
        return instrumentsById;
    }

    public Map<String, Instrument> getInstrumentsByName() {
        return instrumentsByName;
    }


}
