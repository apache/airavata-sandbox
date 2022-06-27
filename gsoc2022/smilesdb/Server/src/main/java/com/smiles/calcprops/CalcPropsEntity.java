package com.smiles.calcprops;

import org.springframework.data.annotation.Id;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CalcPropsEntity {

    @Id
    private String id;
    private String InChI;
    private String InChIKey;
    private String SMILES;
    private String CanonicalSMILES;
    private String PDB;
    private String SDF;
    private String ParsedBy;
    private String Formula;
    private long Charge;
    private long Multiplicity;
    private String Keywords;
    private String CalcType;
    private String Methods;
    private String Basis;
    private long NumBasis;
    private long NumFC;
    private long NumVirt;
    private String JobStatus;
    private String FinTime;
    private String InitGeom;
    private String FinalGeom;
    private String PG;
    private String ElecSym;
    private long NImag;
    private double Energy;
    private double EnergyKcal;
    private double ZPE;
    private double ZPEKcal;
    private double HF;
    private double HFKcal;
    private double Thermal;
    private double ThermalKcal;
    private double Enthalpy;
    private double EnthalpyKcal;
    private double Entropy;
    private double EntropyKcal;
    private double Gibbs;
    private double GibbsKcal;
    private String OrgSym;
    private double Dipole;
    private double Freq;
    private double AtomWeigh;
    private double S2;
    private String CodeVersion;
    private String CalcMachine;
    private String CalcBy;
    private String MemCost;
    private String TimeCost;
    private String CPUTime;
    private String Convergenece;
    private String Otherinfo;
    private String Comments;
    private long NAtom;
    private List<Double> Homos;
    private List<Double> ScfEnergies;
    private List<Double> MoEnergies;
    private List<Double> AtomCoords;
    private long Nmo;
    private long NBasis;


    public CalcPropsEntity(String inChI, String InChIKey, String SMILES, String canonicalSMILES, String PDB, String SDF, String parsedBy, String formula, long charge, long multiplicity, String keywords, String calcType, String methods, String basis, long numBasis, long numFC, long numVirt, String jobStatus, String finTime, String initGeom, String finalGeom, String PG, String elecSym, long NImag, double energy, double energyKcal, double ZPE, double ZPEKcal, double HF, double HFKcal, double thermal, double thermalKcal, double enthalpy, double enthalpyKcal, double entropy, double entropyKcal, double gibbs, double gibbsKcal, String orgSym, double dipole, double freq, double atomWeigh, double s2, String codeVersion, String calcMachine, String calcBy, String memCost, String timeCost, String CPUTime, String convergenece, String otherinfo, String comments, long NAtom, List<Double> homos, List<Double> scfEnergies, List<Double> moEnergies, List<Double> atomCoords, long nmo, long NBasis) {
        this.InChI = inChI;
        this.InChIKey = InChIKey;
        this.SMILES = SMILES;
        this.CanonicalSMILES = canonicalSMILES;
        this.PDB = PDB;
        this.SDF = SDF;
        this.ParsedBy = parsedBy;
        this.Formula = formula;
        this.Charge = charge;
        this.Multiplicity = multiplicity;
        this.Keywords = keywords;
        this.CalcType = calcType;
        this.Methods = methods;
        this.Basis = basis;
        this.NumBasis = numBasis;
        this.NumFC = numFC;
        this.NumVirt = numVirt;
        this.JobStatus = jobStatus;
        this.FinTime = finTime;
        this.InitGeom = initGeom;
        this.FinalGeom = finalGeom;
        this.PG = PG;
        this.ElecSym = elecSym;
        this.NImag = NImag;
        this.Energy = energy;
        this.EnergyKcal = energyKcal;
        this.ZPE = ZPE;
        this.ZPEKcal = ZPEKcal;
        this.HF = HF;
        this.HFKcal = HFKcal;
        this.Thermal = thermal;
        this.ThermalKcal = thermalKcal;
        this.Enthalpy = enthalpy;
        this.EnthalpyKcal = enthalpyKcal;
        this.Entropy = entropy;
        this.EntropyKcal = entropyKcal;
        this.Gibbs = gibbs;
        this.GibbsKcal = gibbsKcal;
        this.OrgSym = orgSym;
        this.Dipole = dipole;
        this.Freq = freq;
        this.AtomWeigh = atomWeigh;
        this.S2 = s2;
        this.CodeVersion = codeVersion;
        this.CalcMachine = calcMachine;
        this.CalcBy = calcBy;
        this.MemCost = memCost;
        this.TimeCost = timeCost;
        this.CPUTime = CPUTime;
        this.Convergenece = convergenece;
        this.Otherinfo = otherinfo;
        this.Comments = comments;
        this.NAtom = NAtom;
        this.Homos = homos;
        this.ScfEnergies = scfEnergies;
        this.MoEnergies = moEnergies;
        this.AtomCoords = atomCoords;
        this.Nmo = nmo;
        this.NBasis = NBasis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInChI() {
        return InChI;
    }

    public void setInChI(String inChI) {
        InChI = inChI;
    }

    public String getInChIKey() {
        return InChIKey;
    }

    public CalcPropsEntity setInChIKey(String inChIKey) {
        InChIKey = inChIKey;
        return this;
    }

    public String getSMILES() {
        return SMILES;
    }

    public void setSMILES(String SMILES) {
        this.SMILES = SMILES;
    }

    public String getCanonicalSMILES() {
        return CanonicalSMILES;
    }

    public void setCanonicalSMILES(String canonicalSMILES) {
        CanonicalSMILES = canonicalSMILES;
    }

    public String getPDB() {
        return PDB;
    }

    public void setPDB(String PDB) {
        this.PDB = PDB;
    }

    public String getSDF() {
        return SDF;
    }

    public void setSDF(String SDF) {
        this.SDF = SDF;
    }

    public String getParsedBy() {
        return ParsedBy;
    }

    public void setParsedBy(String parsedBy) {
        ParsedBy = parsedBy;
    }

    public String getFormula() {
        return Formula;
    }

    public void setFormula(String formula) {
        Formula = formula;
    }

    public long getCharge() {
        return Charge;
    }

    public void setCharge(long charge) {
        Charge = charge;
    }

    public long getMultiplicity() {
        return Multiplicity;
    }

    public void setMultiplicity(long multiplicity) {
        Multiplicity = multiplicity;
    }

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(String keywords) {
        Keywords = keywords;
    }

    public String getCalcType() {
        return CalcType;
    }

    public void setCalcType(String calcType) {
        CalcType = calcType;
    }

    public String getMethods() {
        return Methods;
    }

    public void setMethods(String methods) {
        Methods = methods;
    }

    public String getBasis() {
        return Basis;
    }

    public void setBasis(String basis) {
        Basis = basis;
    }

    public long getNumBasis() {
        return NumBasis;
    }

    public void setNumBasis(long numBasis) {
        NumBasis = numBasis;
    }

    public long getNumFC() {
        return NumFC;
    }

    public void setNumFC(long numFC) {
        NumFC = numFC;
    }

    public long getNumVirt() {
        return NumVirt;
    }

    public void setNumVirt(long numVirt) {
        NumVirt = numVirt;
    }

    public String getJobStatus() {
        return JobStatus;
    }

    public void setJobStatus(String jobStatus) {
        JobStatus = jobStatus;
    }

    public String getFinTime() {
        return FinTime;
    }

    public void setFinTime(String finTime) {
        FinTime = finTime;
    }

    public String getInitGeom() {
        return InitGeom;
    }

    public void setInitGeom(String initGeom) {
        InitGeom = initGeom;
    }

    public String getFinalGeom() {
        return FinalGeom;
    }

    public void setFinalGeom(String finalGeom) {
        FinalGeom = finalGeom;
    }

    public String getPG() {
        return PG;
    }

    public void setPG(String PG) {
        this.PG = PG;
    }

    public String getElecSym() {
        return ElecSym;
    }

    public void setElecSym(String elecSym) {
        ElecSym = elecSym;
    }

    public long getNImag() {
        return NImag;
    }

    public void setNImag(long NImag) {
        this.NImag = NImag;
    }

    public double getEnergy() {
        return Energy;
    }

    public void setEnergy(double energy) {
        Energy = energy;
    }

    public double getEnergyKcal() {
        return EnergyKcal;
    }

    public void setEnergyKcal(double energyKcal) {
        EnergyKcal = energyKcal;
    }

    public double getZPE() {
        return ZPE;
    }

    public void setZPE(double ZPE) {
        this.ZPE = ZPE;
    }

    public double getZPEKcal() {
        return ZPEKcal;
    }

    public void setZPEKcal(double ZPEKcal) {
        this.ZPEKcal = ZPEKcal;
    }

    public double getHF() {
        return HF;
    }

    public void setHF(double HF) {
        this.HF = HF;
    }

    public double getHFKcal() {
        return HFKcal;
    }

    public void setHFKcal(double HFKcal) {
        this.HFKcal = HFKcal;
    }

    public double getThermal() {
        return Thermal;
    }

    public void setThermal(double thermal) {
        Thermal = thermal;
    }

    public double getThermalKcal() {
        return ThermalKcal;
    }

    public void setThermalKcal(double thermalKcal) {
        ThermalKcal = thermalKcal;
    }

    public double getEnthalpy() {
        return Enthalpy;
    }

    public void setEnthalpy(double enthalpy) {
        Enthalpy = enthalpy;
    }

    public double getEnthalpyKcal() {
        return EnthalpyKcal;
    }

    public void setEnthalpyKcal(double enthalpyKcal) {
        EnthalpyKcal = enthalpyKcal;
    }

    public double getEntropy() {
        return Entropy;
    }

    public void setEntropy(double entropy) {
        Entropy = entropy;
    }

    public double getEntropyKcal() {
        return EntropyKcal;
    }

    public void setEntropyKcal(double entropyKcal) {
        EntropyKcal = entropyKcal;
    }

    public double getGibbs() {
        return Gibbs;
    }

    public void setGibbs(double gibbs) {
        Gibbs = gibbs;
    }

    public double getGibbsKcal() {
        return GibbsKcal;
    }

    public void setGibbsKcal(double gibbsKcal) {
        GibbsKcal = gibbsKcal;
    }

    public String getOrgSym() {
        return OrgSym;
    }

    public void setOrgSym(String orgSym) {
        OrgSym = orgSym;
    }

    public double getDipole() {
        return Dipole;
    }

    public void setDipole(double dipole) {
        Dipole = dipole;
    }

    public double getFreq() {
        return Freq;
    }

    public void setFreq(double freq) {
        Freq = freq;
    }

    public double getAtomWeigh() {
        return AtomWeigh;
    }

    public void setAtomWeigh(double atomWeigh) {
        AtomWeigh = atomWeigh;
    }

    public double getS2() {
        return S2;
    }

    public void setS2(double s2) {
        S2 = s2;
    }

    public String getCodeVersion() {
        return CodeVersion;
    }

    public void setCodeVersion(String codeVersion) {
        CodeVersion = codeVersion;
    }

    public String getCalcMachine() {
        return CalcMachine;
    }

    public void setCalcMachine(String calcMachine) {
        CalcMachine = calcMachine;
    }

    public String getCalcBy() {
        return CalcBy;
    }

    public void setCalcBy(String calcBy) {
        CalcBy = calcBy;
    }

    public String getMemCost() {
        return MemCost;
    }

    public void setMemCost(String memCost) {
        MemCost = memCost;
    }

    public String getTimeCost() {
        return TimeCost;
    }

    public void setTimeCost(String timeCost) {
        TimeCost = timeCost;
    }

    public String getCPUTime() {
        return CPUTime;
    }

    public void setCPUTime(String CPUTime) {
        this.CPUTime = CPUTime;
    }

    public String getConvergenece() {
        return Convergenece;
    }

    public void setConvergenece(String convergenece) {
        Convergenece = convergenece;
    }

    public String getOtherinfo() {
        return Otherinfo;
    }

    public void setOtherinfo(String otherinfo) {
        Otherinfo = otherinfo;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public long getNAtom() {
        return NAtom;
    }

    public void setNAtom(long NAtom) {
        this.NAtom = NAtom;
    }

    public List<Double> getHomos() {
        return Homos;
    }

    public void setHomos(List<Double> homos) {
        Homos = homos;
    }

    public List<Double> getScfEnergies() {
        return ScfEnergies;
    }

    public void setScfEnergies(List<Double> scfEnergies) {
        ScfEnergies = scfEnergies;
    }

    public List<Double> getMoEnergies() {
        return MoEnergies;
    }

    public void setMoEnergies(List<Double> moEnergies) {
        MoEnergies = moEnergies;
    }

    public List<Double> getAtomCoords() {
        return AtomCoords;
    }

    public void setAtomCoords(List<Double> atomCoords) {
        AtomCoords = atomCoords;
    }

    public long getNmo() {
        return Nmo;
    }

    public void setNmo(long nmo) {
        Nmo = nmo;
    }

    public long getNBasis() {
        return NBasis;
    }

    public void setNBasis(long NBasis) {
        this.NBasis = NBasis;
    }
}
