package com.smiles.molData;

import com.smiles.Molecule;
import com.smiles.MoleculeRequest;
import com.smiles.MoleculeServiceGrpc;
import com.smiles.SpringContext;
import io.grpc.stub.StreamObserver;

public class MoleculeImpl extends MoleculeServiceGrpc.MoleculeServiceImplBase {
    MoleculeRepo repo = SpringContext.getBean(MoleculeRepo.class);

    @Override
    public void getMolecule(MoleculeRequest request, StreamObserver<Molecule> responseObserver) {
//        super.getMolecule(request, responseObserver);
        MoleculeEntity molecule = repo.findBySmiles(request.getMoleculeQuery());
        System.out.println(molecule.toString());
        Molecule reply = Molecule.newBuilder().setCasNr(molecule.getCas_nr())
                .setSmiles(molecule.getSmiles()).setSmilesStereo(molecule.getSmiles_stereo())
                .setInchi(molecule.getInchi()).setMolfileBlobSource(molecule.getMolfile_blob_source())
                .setEmpFormula(molecule.getEmp_formula()).setEmpFormulaSort(molecule.getEmp_formula_sort())
                .setMw(molecule.getMw()).setMwMonoiso(molecule.getMw_monoiso())
                .setRdb(molecule.getRdb()).setMwSource(molecule.getMw_source())
                .setValidatedBy(molecule.getValidated_by()).setJournal(molecule.getJournal())
                .setAuthOfIntr(molecule.getAuth_of_intr()).setJourCit(molecule.getJour_cit())
                .setYearPubl(molecule.getYear_publ()).setDoiLink(molecule.getDoi_link())
                .setCompClass(molecule.getComp_class()).setCuniq(molecule.getCuniq())
                .setCalcPerf(molecule.getCalc_perf()).setOrgMet(molecule.getOrg_met())
                .setMolChrg(molecule.getMol_chrg()).setStateOfmat(molecule.getState_ofmat())
                .setColorWhite(molecule.getColor_white()).setColorUv(molecule.getColor_uv())
                .setAbsorbMax(molecule.getAbsorb_max()).setSolventAe(molecule.getSolvent_ae())
                .setAbsorb(molecule.getAbsorb()).setConc(molecule.getConc()).setExtinc(molecule.getExtinc())
                .setEmisMax(molecule.getEmis_max()).setTempAbs(molecule.getTemp_abs())
                .setEmisQy(molecule.getEmis_qy()).setLifetime(molecule.getLifetime())
                .setTempCv(molecule.getTemp_cv()).setReducPot(molecule.getReduc_pot())
                .setHwOrPkRp(molecule.getHw_or_pk_rp()).setSolventCv(molecule.getSolvent_cv())
                .setElectrolyte(molecule.getElectrolyte()).setRefElectrd(molecule.getRef_electrd())
                .setInterThngs(molecule.getInter_thngs()).setDensity20(molecule.getDensity_20())
                .setDensity20Source(molecule.getDensity_20_source()).setDefaultWarnLevel(molecule.getDefault_warn_level())
                .setN20(molecule.getN_20()).setN20Source(molecule.getN_20_source())
                .setMpLow(molecule.getMp_low()).setMpHigh(molecule.getMp_high())
                .setMpSource(molecule.getMp_source()).setBpLow(molecule.getBp_low())
                .setBpHigh(molecule.getBp_high()).setBpPress(molecule.getBp_press())
                .setPressUnit(molecule.getPress_unit()).setBpSource(molecule.getBp_source())
                .setSafetyR(molecule.getSafety_r()).setSafetyH(molecule.getSafety_h())
                .setSafetyS(molecule.getSafety_s()).setSafetyP(molecule.getSafety_p())
                .setSafetyText(molecule.getSafety_text()).setSafetySym(molecule.getSafety_sym())
                .setSafetySymGhs(molecule.getSafety_sym_ghs()).setSafetySource(molecule.getSafety_source())
                .setCommentMol(molecule.getComment_mol()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void createMolecule(MoleculeRequest request, StreamObserver<Molecule> responseObserver) {
//        super.createMolecule(request, responseObserver);
        Molecule molecule = request.getMolecule();
        System.out.println(request.getAllFields());
        MoleculeEntity c4001 = new MoleculeEntity(molecule.getCasNr(),
                molecule.getSmiles(), molecule.getSmilesStereo(),
                molecule.getInchi(), molecule.getMolfileBlobSource(),
                molecule.getEmpFormula(), molecule.getEmpFormulaSort(),
                molecule.getEmpFormulaSource(), molecule.getMw(), molecule.getMwMonoiso(),
                molecule.getRdb(), molecule.getMwSource(), molecule.getValidatedBy(),
                molecule.getJournal(), molecule.getAuthOfIntr(), molecule.getJourCit(),
                molecule.getYearPubl(), molecule.getDoiLink(), molecule.getCompClass(),
                molecule.getCuniq(), molecule.getCalcPerf(), molecule.getOrgMet(),
                molecule.getMolChrg(), molecule.getStateOfmat(), molecule.getColorWhite(),
                molecule.getColorUv(), molecule.getAbsorbMax(), molecule.getSolventAe(),
                molecule.getAbsorb(), molecule.getConc(), molecule.getExtinc(), molecule.getEmisMax(),
                molecule.getTempAbs(), molecule.getEmisQy(), molecule.getTempEms(), molecule.getLifetime(),
                molecule.getTempCv(), molecule.getReducPot(), molecule.getHwOrPkRp(),
                molecule.getOxidPot(), molecule.getHwOrPkOp(), molecule.getSolventCv(),
                molecule.getElectrolyte(), molecule.getRefElectrd(), molecule.getInterThngs(),
                molecule.getDensity20(), molecule.getDensity20Source(), molecule.getDefaultWarnLevel(),
                molecule.getN20(), molecule.getN20Source(), molecule.getMpLow(),
                molecule.getMpHigh(), molecule.getMpSource(), molecule.getBpLow(),
                molecule.getBpHigh(), molecule.getBpPress(), molecule.getPressUnit(),
                molecule.getBpSource(), molecule.getSafetyR(), molecule.getSafetyH(), molecule.getSafetyS(),
                molecule.getSafetyP(), molecule.getSafetyText(), molecule.getSafetySym(),
                molecule.getSafetySymGhs(), molecule.getSafetySource(), molecule.getCommentMol());
        repo.save(c4001);
        responseObserver.onNext(molecule);
        responseObserver.onCompleted();


    }

    @Override
    public void updateMolecule(MoleculeRequest request, StreamObserver<Molecule> responseObserver) {
//        super.updateMolecule(request, responseObserver);
        Molecule molecule = request.getMolecule();
        System.out.println(request.getAllFields());
        MoleculeEntity c4001 = (MoleculeEntity) repo.findBySmiles(molecule.getSmiles());
        System.out.println(c4001.getId());
        c4001.setCas_nr(molecule.getCasNr());
        c4001.setSmiles(molecule.getSmiles());
        c4001.setSmiles_stereo(molecule.getSmilesStereo());
        c4001.setInchi(molecule.getInchi());
        c4001.setMolfile_blob_source(molecule.getMolfileBlobSource());
        c4001.setEmp_formula(molecule.getEmpFormula());
        c4001.setEmp_formula_sort(molecule.getEmpFormulaSort());
        c4001.setEmp_formula_source(molecule.getEmpFormulaSource());
        c4001.setMw(molecule.getMw());
        c4001.setMw_monoiso(molecule.getMwMonoiso());
        c4001.setRdb(molecule.getRdb());
        c4001.setMw_source(molecule.getMwSource());
        c4001.setValidated_by(molecule.getValidatedBy());
        c4001.setJournal(molecule.getJournal());
        c4001.setAuth_of_intr(molecule.getAuthOfIntr());
        c4001.setJour_cit(molecule.getJourCit());
        c4001.setYear_publ(molecule.getYearPubl());
        c4001.setDoi_link(molecule.getDoiLink());
        c4001.setComp_class(molecule.getCompClass());
        c4001.setCuniq(molecule.getCuniq());
        c4001.setCalc_perf(molecule.getCalcPerf());
        c4001.setOrg_met(molecule.getOrgMet());
        c4001.setMol_chrg(molecule.getMolChrg());
        c4001.setState_ofmat(molecule.getStateOfmat());
        c4001.setColor_white(molecule.getColorWhite());
        c4001.setColor_uv(molecule.getColorUv());
        c4001.setAbsorb_max(molecule.getAbsorbMax());
        c4001.setSolvent_ae(molecule.getSolventAe());
        c4001.setAbsorb(molecule.getAbsorb());
        c4001.setConc(molecule.getConc());
        c4001.setExtinc(molecule.getExtinc());
        c4001.setEmis_max(molecule.getEmisMax());
        c4001.setTemp_abs(molecule.getTempAbs());
        c4001.setEmis_qy(molecule.getEmisQy());
        c4001.setTemp_ems(molecule.getTempEms());
        c4001.setLifetime(molecule.getLifetime());
        c4001.setTemp_cv(molecule.getTempCv());
        c4001.setReduc_pot(molecule.getReducPot());
        c4001.setHw_or_pk_rp(molecule.getHwOrPkRp());
        c4001.setOxid_pot(molecule.getOxidPot());
        c4001.setHw_or_pk_op(molecule.getHwOrPkOp());
        c4001.setSolvent_cv(molecule.getSolventCv());
        c4001.setElectrolyte(molecule.getElectrolyte());
        c4001.setRef_electrd(molecule.getRefElectrd());
        c4001.setInter_thngs(molecule.getInterThngs());
        c4001.setDensity_20(molecule.getDensity20());
        c4001.setDensity_20_source(molecule.getDensity20Source());
        c4001.setDefault_warn_level(molecule.getDefaultWarnLevel());
        c4001.setN_20(molecule.getN20());
        c4001.setN_20_source(molecule.getN20Source());
        c4001.setMp_low(molecule.getMpLow());
        c4001.setMp_high(molecule.getMpHigh());
        c4001.setMp_source(molecule.getMpSource());
        c4001.setBp_low(molecule.getBpLow());
        c4001.setBp_high(molecule.getBpHigh());
        c4001.setBp_press(molecule.getBpPress());
        c4001.setPress_unit(molecule.getPressUnit());
        c4001.setBp_source(molecule.getBpSource());
        c4001.setSafety_r(molecule.getSafetyR());
        c4001.setSafety_h(molecule.getSafetyH());
        c4001.setSafety_s(molecule.getSafetyS());
        c4001.setSafety_p(molecule.getSafetyP());
        c4001.setSafety_text(molecule.getSafetyText());
        c4001.setSafety_sym(molecule.getSafetySym());
        c4001.setSafety_sym_ghs(molecule.getSafetySymGhs());
        c4001.setComment_mol(molecule.getCommentMol());
        repo.save(c4001);
        responseObserver.onNext(molecule);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMolecule(MoleculeRequest request, StreamObserver<Molecule> responseObserver) {
//        super.deleteMolecule(request, responseObserver);
        Molecule molecule = request.getMolecule();
        MoleculeEntity c4001 = (MoleculeEntity) repo.findBySmiles(molecule.getSmiles());
        repo.delete(c4001);
        responseObserver.onNext(molecule);
        responseObserver.onCompleted();
    }
}
