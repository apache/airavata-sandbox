package com.smiles.calctopology;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CalcTopologyRepo extends MongoRepository<CalcTopologyEntity, String> {

    public CalcTopologyEntity findBySymbols(List<String> symbols);
}
