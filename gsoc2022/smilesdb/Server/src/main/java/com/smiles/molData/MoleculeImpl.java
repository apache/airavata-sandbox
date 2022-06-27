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
        super.updateMolecule(request, responseObserver);
    }

    @Override
    public void deleteMolecule(MoleculeRequest request, StreamObserver<Molecule> responseObserver) {
        super.deleteMolecule(request, responseObserver);
    }
}
