package com.smiles.calcinfo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CalcInfoRepo extends MongoRepository<CalcInfoEntity, String> {

    public CalcInfoEntity findByNalpha(long nalpha);

    public CalcInfoEntity findBySMILES(String SMILES);

//    public CalcInfoEntity findCalcInfoEntitiesBySMILES(String SMILES);

}
