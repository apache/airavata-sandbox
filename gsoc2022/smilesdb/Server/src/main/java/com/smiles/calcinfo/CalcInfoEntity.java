package com.smiles.calcinfo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("calcInfo")
public class CalcInfoEntity {

    @Id
    private String id;
    private long nbasis;
    private long nmo;
    private long nalpha;
    private long nbeta;
    private long natom;
    private double energy;
    private String SMILES;

    public CalcInfoEntity(long nbasis, long nmo, long nalpha, long nbeta, long natom, double energy, String SMILES) {
        this.nbasis = nbasis;
        this.nmo = nmo;
        this.nalpha = nalpha;
        this.nbeta = nbeta;
        this.natom = natom;
        this.energy = energy;
        this.SMILES = SMILES;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNbasis() {
        return nbasis;
    }

    public void setNbasis(long nbasis) {
        this.nbasis = nbasis;
    }

    public long getNmo() {
        return nmo;
    }

    public void setNmo(long nmo) {
        this.nmo = nmo;
    }

    public long getNalpha() {
        return nalpha;
    }

    public void setNalpha(long nalpha) {
        this.nalpha = nalpha;
    }

    public long getNbeta() {
        return nbeta;
    }

    public void setNbeta(long nbeta) {
        this.nbeta = nbeta;
    }

    public long getNatom() {
        return natom;
    }

    public void setNatom(long natom) {
        this.natom = natom;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public String getSMILES() {
        return SMILES;
    }

    public void setSMILES(String SMILES) {
        this.SMILES = SMILES;
    }


}
