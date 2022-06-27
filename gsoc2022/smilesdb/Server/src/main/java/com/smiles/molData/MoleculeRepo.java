package com.smiles.molData;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MoleculeRepo extends MongoRepository<MoleculeEntity, String> {

    MoleculeEntity findBySmiles(String smiles);
}
