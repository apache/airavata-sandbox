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
        MoleculeEntity moleculeEntity = new MoleculeEntity(molecule.getCasNr(),
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
        repo.save(moleculeEntity);
        responseObserver.onNext(molecule);
        responseObserver.onCompleted();


    }

    @Override
    public void updateMolecule(MoleculeRequest request, StreamObserver<Molecule> responseObserver) {
//        super.updateMolecule(request, responseObserver);
        Molecule molecule = request.getMolecule();
        System.out.println(request.getAllFields());
        MoleculeEntity moleculeEntity = (MoleculeEntity) repo.findBySmiles(molecule.getSmiles());
        System.out.println(moleculeEntity.getId());
        moleculeEntity.setCas_nr(molecule.getCasNr());
        moleculeEntity.setSmiles(molecule.getSmiles());
        moleculeEntity.setSmiles_stereo(molecule.getSmilesStereo());
        moleculeEntity.setInchi(molecule.getInchi());
        moleculeEntity.setMolfile_blob_source(molecule.getMolfileBlobSource());
        moleculeEntity.setEmp_formula(molecule.getEmpFormula());
        moleculeEntity.setEmp_formula_sort(molecule.getEmpFormulaSort());
        moleculeEntity.setEmp_formula_source(molecule.getEmpFormulaSource());
        moleculeEntity.setMw(molecule.getMw());
        moleculeEntity.setMw_monoiso(molecule.getMwMonoiso());
        moleculeEntity.setRdb(molecule.getRdb());
        moleculeEntity.setMw_source(molecule.getMwSource());
        moleculeEntity.setValidated_by(molecule.getValidatedBy());
        moleculeEntity.setJournal(molecule.getJournal());
        moleculeEntity.setAuth_of_intr(molecule.getAuthOfIntr());
        moleculeEntity.setJour_cit(molecule.getJourCit());
        moleculeEntity.setYear_publ(molecule.getYearPubl());
        moleculeEntity.setDoi_link(molecule.getDoiLink());
        moleculeEntity.setComp_class(molecule.getCompClass());
        moleculeEntity.setCuniq(molecule.getCuniq());
        moleculeEntity.setCalc_perf(molecule.getCalcPerf());
        moleculeEntity.setOrg_met(molecule.getOrgMet());
        moleculeEntity.setMol_chrg(molecule.getMolChrg());
        moleculeEntity.setState_ofmat(molecule.getStateOfmat());
        moleculeEntity.setColor_white(molecule.getColorWhite());
        moleculeEntity.setColor_uv(molecule.getColorUv());
        moleculeEntity.setAbsorb_max(molecule.getAbsorbMax());
        moleculeEntity.setSolvent_ae(molecule.getSolventAe());
        moleculeEntity.setAbsorb(molecule.getAbsorb());
        moleculeEntity.setConc(molecule.getConc());
        moleculeEntity.setExtinc(molecule.getExtinc());
        moleculeEntity.setEmis_max(molecule.getEmisMax());
        moleculeEntity.setTemp_abs(molecule.getTempAbs());
        moleculeEntity.setEmis_qy(molecule.getEmisQy());
        moleculeEntity.setTemp_ems(molecule.getTempEms());
        moleculeEntity.setLifetime(molecule.getLifetime());
        moleculeEntity.setTemp_cv(molecule.getTempCv());
        moleculeEntity.setReduc_pot(molecule.getReducPot());
        moleculeEntity.setHw_or_pk_rp(molecule.getHwOrPkRp());
        moleculeEntity.setOxid_pot(molecule.getOxidPot());
        moleculeEntity.setHw_or_pk_op(molecule.getHwOrPkOp());
        moleculeEntity.setSolvent_cv(molecule.getSolventCv());
        moleculeEntity.setElectrolyte(molecule.getElectrolyte());
        moleculeEntity.setRef_electrd(molecule.getRefElectrd());
        moleculeEntity.setInter_thngs(molecule.getInterThngs());
        moleculeEntity.setDensity_20(molecule.getDensity20());
        moleculeEntity.setDensity_20_source(molecule.getDensity20Source());
        moleculeEntity.setDefault_warn_level(molecule.getDefaultWarnLevel());
        moleculeEntity.setN_20(molecule.getN20());
        moleculeEntity.setN_20_source(molecule.getN20Source());
        moleculeEntity.setMp_low(molecule.getMpLow());
        moleculeEntity.setMp_high(molecule.getMpHigh());
        moleculeEntity.setMp_source(molecule.getMpSource());
        moleculeEntity.setBp_low(molecule.getBpLow());
        moleculeEntity.setBp_high(molecule.getBpHigh());
        moleculeEntity.setBp_press(molecule.getBpPress());
        moleculeEntity.setPress_unit(molecule.getPressUnit());
        moleculeEntity.setBp_source(molecule.getBpSource());
        moleculeEntity.setSafety_r(molecule.getSafetyR());
        moleculeEntity.setSafety_h(molecule.getSafetyH());
        moleculeEntity.setSafety_s(molecule.getSafetyS());
        moleculeEntity.setSafety_p(molecule.getSafetyP());
        moleculeEntity.setSafety_text(molecule.getSafetyText());
        moleculeEntity.setSafety_sym(molecule.getSafetySym());
        moleculeEntity.setSafety_sym_ghs(molecule.getSafetySymGhs());
        moleculeEntity.setComment_mol(molecule.getCommentMol());
        repo.save(moleculeEntity);
        responseObserver.onNext(molecule);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMolecule(MoleculeRequest request, StreamObserver<Molecule> responseObserver) {
//        super.deleteMolecule(request, responseObserver);
        Molecule molecule = request.getMolecule();
        MoleculeEntity moleculeEntity = (MoleculeEntity) repo.findBySmiles(molecule.getSmiles());
        repo.delete(moleculeEntity);
        responseObserver.onNext(molecule);
        responseObserver.onCompleted();
    }
}
