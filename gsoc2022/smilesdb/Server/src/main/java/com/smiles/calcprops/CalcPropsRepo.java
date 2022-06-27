package com.smiles.calcprops;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CalcPropsRepo extends MongoRepository<CalcPropsEntity, String> {

//    public CalcPropsEntity findByPDB(String PDB);
    public CalcPropsEntity findBySMILES(String SMILES);

}
