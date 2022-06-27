package com.smiles.calcprops;

import com.smiles.CalcProps;
import com.smiles.CalcPropsRequest;
import com.smiles.CalcPropsServiceGrpc;
import com.smiles.SpringContext;
import com.smiles.calcinfo.CalcInfoEntity;
import io.grpc.stub.StreamObserver;

public class CalcPropsImpl extends CalcPropsServiceGrpc.CalcPropsServiceImplBase {

    CalcPropsRepo repo1 = SpringContext.getBean(CalcPropsRepo.class);

    @Override
    public void getCalcProps(CalcPropsRequest request, StreamObserver<CalcProps> responseObserver) {
//        super.getCalcInfo(request, responseObserver);

        CalcPropsEntity calcProps = repo1.findBySMILES(request.getCalcPropsQuery());
        System.out.println(calcProps.toString());

        CalcProps reply = CalcProps.newBuilder()
                .setInChI(calcProps.getInChI())
                .setInChIKey(calcProps.getInChIKey())
                .setSMILES(calcProps.getSMILES())
                .setCanonicalSMILES(calcProps.getCanonicalSMILES())
                .setPDB(calcProps.getPDB())
                .setSDF(calcProps.getSDF())
                .setParsedBy(calcProps.getParsedBy())
                .setFormula(calcProps.getFormula())
                .setCharge(calcProps.getCharge())
                .setMultiplicity(calcProps.getMultiplicity())
                .setKeywords(calcProps.getKeywords())
                .setCalcType(calcProps.getCalcType())
                .setMethods(calcProps.getMethods())
                .setBasis(calcProps.getBasis())
                .setNumBasis(calcProps.getNumBasis())
                .setNumFC(calcProps.getNumFC())
                .setNumVirt(calcProps.getNumVirt())
                .setJobStatus(calcProps.getJobStatus())
                .setFinTime(calcProps.getFinTime())
                .setInitGeom(calcProps.getInitGeom())
                .setFinalGeom(calcProps.getFinalGeom())
                .setPG(calcProps.getPG()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void createCalcProps(CalcPropsRequest request, StreamObserver<CalcProps> responseObserver) {
//        super.createCalcProps(request, responseObserver);
        CalcProps calcProps = request.getCalcProp();
        System.out.println(request.getAllFields());
        CalcPropsEntity c2001 = new CalcPropsEntity(calcProps.getInChI(),
                calcProps.getInChIKey(), calcProps.getSMILES(), calcProps.getCanonicalSMILES(),
                calcProps.getPDB(), calcProps.getSDF(), calcProps.getParsedBy(),
                calcProps.getFormula(), calcProps.getCharge(), calcProps.getMultiplicity(),
                calcProps.getKeywords(), calcProps.getCalcType(), calcProps.getMethods(),
                calcProps.getBasis(), calcProps.getNumBasis(), calcProps.getNumFC(),
                calcProps.getNumVirt(), calcProps.getJobStatus(), calcProps.getFinTime(),
                calcProps.getInitGeom(), calcProps.getFinalGeom(), calcProps.getPG(),
                calcProps.getElecSym(), calcProps.getNImag(), calcProps.getEnergy(),
                calcProps.getEnergyKcal(), calcProps.getZPE(), calcProps.getZPEKcal(),
                calcProps.getHF(), calcProps.getHFKcal(), calcProps.getThermal(), calcProps.getThermalKcal(),
                calcProps.getEnthalpy(), calcProps.getEnthalpyKcal(), calcProps.getEntropy(),
                calcProps.getEntropyKcal(), calcProps.getGibbs(), calcProps.getGibbsKcal(),
                calcProps.getOrbSym(), calcProps.getDipole(), calcProps.getFreq(), calcProps.getAtomWeigh(),
                calcProps.getS2(), calcProps.getCodeVersion(), calcProps.getCalcMachine(), calcProps.getCalcBy(),
                calcProps.getMemCost(), calcProps.getTimeCost(), calcProps.getCPUTime(), calcProps.getConvergenece(),
                calcProps.getOtherinfo(), calcProps.getComments(), calcProps.getNAtom(),
                calcProps.getHomosList(), calcProps.getScfEnergiesList(), calcProps.getMoEnergiesList(),
                calcProps.getAtomCoordsList(), calcProps.getNmo(), calcProps.getNBasis());
        repo1.save(c2001);
        responseObserver.onNext(calcProps);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCalcProps(CalcPropsRequest request, StreamObserver<CalcProps> responseObserver) {
//        super.updateCalcProps(request, responseObserver);
        CalcProps calcProps = request.getCalcProp();
        System.out.println(request.getAllFields());
        CalcPropsEntity c2001 = (CalcPropsEntity) repo1.findBySMILES(calcProps.getSMILES());
        System.out.println(c2001.getId());
        c2001.setInChI(calcProps.getInChI());
        c2001.setInChIKey(calcProps.getInChIKey());
        c2001.setSMILES(calcProps.getSMILES());
        c2001.setCanonicalSMILES(calcProps.getCanonicalSMILES());
        c2001.setPDB(calcProps.getPDB());
        c2001.setSDF(calcProps.getSDF());
        c2001.setParsedBy(calcProps.getParsedBy());
        c2001.setFormula(calcProps.getFormula());
        c2001.setCharge(calcProps.getCharge());
        c2001.setMultiplicity(calcProps.getMultiplicity());
        c2001.setKeywords(calcProps.getKeywords());
        c2001.setCalcType(calcProps.getCalcType());
        c2001.setMethods(calcProps.getMethods());
        c2001.setBasis(calcProps.getBasis());
        c2001.setNBasis(calcProps.getNumBasis());
        c2001.setNumFC(calcProps.getNumFC());
        c2001.setNumVirt(calcProps.getNumVirt());
        c2001.setJobStatus(calcProps.getJobStatus());
        c2001.setFinTime(calcProps.getFinTime());
        c2001.setInitGeom(calcProps.getInitGeom());
        c2001.setFinalGeom(calcProps.getFinalGeom());
        c2001.setPG(calcProps.getPG());
        c2001.setElecSym(calcProps.getElecSym());
        c2001.setNImag(calcProps.getNImag());
        c2001.setEnergy(calcProps.getEnergy());
        c2001.setEnergyKcal(calcProps.getEnergyKcal());
        c2001.setZPE(calcProps.getZPE());
        c2001.setZPEKcal(calcProps.getZPEKcal());
        c2001.setHF(calcProps.getHF());
        c2001.setHFKcal(calcProps.getHFKcal());
        c2001.setThermal(calcProps.getThermal());
        c2001.setThermalKcal(calcProps.getThermalKcal());
        c2001.setEnthalpy(calcProps.getEnthalpy());
        c2001.setEnthalpyKcal(calcProps.getEnthalpyKcal());
        c2001.setEntropy(calcProps.getEntropy());
        c2001.setEntropyKcal(calcProps.getEntropyKcal());
        c2001.setGibbs(calcProps.getGibbs());
        c2001.setGibbsKcal(calcProps.getGibbsKcal());
        c2001.setOrgSym(calcProps.getOrbSym());
        c2001.setDipole(calcProps.getDipole());
        c2001.setFreq(calcProps.getFreq());
        c2001.setAtomWeigh(calcProps.getAtomWeigh());
        c2001.setS2(calcProps.getS2());
        c2001.setCodeVersion(calcProps.getCodeVersion());
        c2001.setCalcMachine(calcProps.getCalcMachine());
        c2001.setCalcBy(calcProps.getCalcBy());
        c2001.setMemCost(calcProps.getMemCost());
        c2001.setTimeCost(calcProps.getTimeCost());
        c2001.setCPUTime(calcProps.getCPUTime());
        c2001.setConvergenece(calcProps.getConvergenece());
        c2001.setOtherinfo(calcProps.getOtherinfo());
        c2001.setComments(calcProps.getComments());
        c2001.setNAtom(calcProps.getNAtom());
        c2001.setNmo(calcProps.getNmo());
        c2001.setNBasis(calcProps.getNBasis());
        repo1.save(c2001);
        responseObserver.onNext(calcProps);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCalcProps(CalcPropsRequest request, StreamObserver<CalcProps> responseObserver) {
//        super.deleteCalcProps(request, responseObserver);
    CalcProps calcProps = request.getCalcProp();
    CalcPropsEntity c2001 = (CalcPropsEntity) repo1.findBySMILES(calcProps.getSMILES());
    repo1.delete(c2001);
    responseObserver.onNext(calcProps);
    responseObserver.onCompleted();
    }

}
