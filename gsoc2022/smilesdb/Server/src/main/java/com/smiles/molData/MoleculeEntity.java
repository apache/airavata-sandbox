package com.smiles.molData;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("molecule")
public class MoleculeEntity {

    @Id
    private String id;
    private String cas_nr;
    private String smiles;
    private String smiles_stereo;
    private String inchi;
    private String molfile_blob_source;
    private String emp_formula;
    private String emp_formula_sort;
    private String emp_formula_source;
    private Double mw;
    private Double mw_monoiso;
    private Double rdb;
    private String mw_source;
    private String validated_by;
    private String journal;
    private String auth_of_intr;

    private String jour_cit;
    private String year_publ;
    private String doi_link;
    private String comp_class;
    private String cuniq;
    private String calc_perf;
    private String org_met;
    private long mol_chrg;
    private String state_ofmat;
    private String color_white;
    private String color_uv;
    private Double absorb_max;
    private String solvent_ae;
    private Double absorb;
    private Double conc;
    private Double extinc;
    private Double emis_max;
    private Double temp_abs;
    private Double emis_qy;
    private Double temp_ems;
    private Double lifetime;
    private Double temp_cv;
    private Double reduc_pot;
    private String hw_or_pk_rp;
    private Double oxid_pot;
    private String hw_or_pk_op;
    private String solvent_cv;
    private String electrolyte;
    private String ref_electrd;
    private String inter_thngs;
    private Double density_20;
    private String density_20_source;
    private Double default_warn_level;
    private Double n_20;
    private String n_20_source;
    private Double mp_low;
    private Double mp_high;
    private String mp_source;
    private Double bp_low;
    private Double bp_high;
    private Double bp_press;
    private String press_unit;
    private String bp_source;
    private String safety_r;
    private String safety_h;
    private String safety_s;
    private String safety_p;
    private String safety_text;
    private String safety_sym;
    private String safety_sym_ghs;
    private String safety_source;
    private String comment_mol;

    public MoleculeEntity(String cas_nr, String smiles, String smiles_stereo, String inchi, String molfile_blob_source, String emp_formula, String emp_formula_sort, String emp_formula_source, Double mw, Double mw_monoiso, Double rdb, String mw_source, String validated_by, String journal, String auth_of_intr, String jour_cit, String year_publ, String doi_link, String comp_class, String cuniq, String calc_perf, String org_met, long mol_chrg, String state_ofmat, String color_white, String color_uv, Double absorb_max, String solvent_ae, Double absorb, Double conc, Double extinc, Double emis_max, Double temp_abs, Double emis_qy, Double temp_ems, Double lifetime, Double temp_cv, Double reduc_pot, String hw_or_pk_rp, Double oxid_pot, String hw_or_pk_op, String solvent_cv, String electrolyte, String ref_electrd, String inter_thngs, Double density_20, String density_20_source, Double default_warn_level, Double n_20, String n_20_source, Double mp_low, Double mp_high, String mp_source, Double bp_low, Double bp_high, Double bp_press, String press_unit, String bp_source, String safety_r, String safety_h, String safety_s, String safety_p, String safety_text, String safety_sym, String safety_sym_ghs, String safety_source, String comment_mol) {
        this.cas_nr = cas_nr;
        this.smiles = smiles;
        this.smiles_stereo = smiles_stereo;
        this.inchi = inchi;
        this.molfile_blob_source = molfile_blob_source;
        this.emp_formula = emp_formula;
        this.emp_formula_sort = emp_formula_sort;
        this.emp_formula_source = emp_formula_source;
        this.mw = mw;
        this.mw_monoiso = mw_monoiso;
        this.rdb = rdb;
        this.mw_source = mw_source;
        this.validated_by = validated_by;
        this.journal = journal;
        this.auth_of_intr = auth_of_intr;
        this.jour_cit = jour_cit;
        this.year_publ = year_publ;
        this.doi_link = doi_link;
        this.comp_class = comp_class;
        this.cuniq = cuniq;
        this.calc_perf = calc_perf;
        this.org_met = org_met;
        this.mol_chrg = mol_chrg;
        this.state_ofmat = state_ofmat;
        this.color_white = color_white;
        this.color_uv = color_uv;
        this.absorb_max = absorb_max;
        this.solvent_ae = solvent_ae;
        this.absorb = absorb;
        this.conc = conc;
        this.extinc = extinc;
        this.emis_max = emis_max;
        this.temp_abs = temp_abs;
        this.emis_qy = emis_qy;
        this.temp_ems = temp_ems;
        this.lifetime = lifetime;
        this.temp_cv = temp_cv;
        this.reduc_pot = reduc_pot;
        this.hw_or_pk_rp = hw_or_pk_rp;
        this.oxid_pot = oxid_pot;
        this.hw_or_pk_op = hw_or_pk_op;
        this.solvent_cv = solvent_cv;
        this.electrolyte = electrolyte;
        this.ref_electrd = ref_electrd;
        this.inter_thngs = inter_thngs;
        this.density_20 = density_20;
        this.density_20_source = density_20_source;
        this.default_warn_level = default_warn_level;
        this.n_20 = n_20;
        this.n_20_source = n_20_source;
        this.mp_low = mp_low;
        this.mp_high = mp_high;
        this.mp_source = mp_source;
        this.bp_low = bp_low;
        this.bp_high = bp_high;
        this.bp_press = bp_press;
        this.press_unit = press_unit;
        this.bp_source = bp_source;
        this.safety_r = safety_r;
        this.safety_h = safety_h;
        this.safety_s = safety_s;
        this.safety_p = safety_p;
        this.safety_text = safety_text;
        this.safety_sym = safety_sym;
        this.safety_sym_ghs = safety_sym_ghs;
        this.safety_source = safety_source;
        this.comment_mol = comment_mol;
    }

    public String getId() {
        return id;
    }

    public MoleculeEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getCas_nr() {
        return cas_nr;
    }

    public MoleculeEntity setCas_nr(String cas_nr) {
        this.cas_nr = cas_nr;
        return this;
    }

    public String getSmiles() {
        return smiles;
    }

    public MoleculeEntity setSmiles(String smiles) {
        this.smiles = smiles;
        return this;
    }

    public String getSmiles_stereo() {
        return smiles_stereo;
    }

    public MoleculeEntity setSmiles_stereo(String smiles_stereo) {
        this.smiles_stereo = smiles_stereo;
        return this;
    }

    public String getInchi() {
        return inchi;
    }

    public MoleculeEntity setInchi(String inchi) {
        this.inchi = inchi;
        return this;
    }

    public String getMolfile_blob_source() {
        return molfile_blob_source;
    }

    public MoleculeEntity setMolfile_blob_source(String molfile_blob_source) {
        this.molfile_blob_source = molfile_blob_source;
        return this;
    }

    public String getEmp_formula() {
        return emp_formula;
    }

    public MoleculeEntity setEmp_formula(String emp_formula) {
        this.emp_formula = emp_formula;
        return this;
    }

    public String getEmp_formula_sort() {
        return emp_formula_sort;
    }

    public MoleculeEntity setEmp_formula_sort(String emp_formula_sort) {
        this.emp_formula_sort = emp_formula_sort;
        return this;
    }

    public String getEmp_formula_source() {
        return emp_formula_source;
    }

    public MoleculeEntity setEmp_formula_source(String emp_formula_source) {
        this.emp_formula_source = emp_formula_source;
        return this;
    }

    public Double getMw() {
        return mw;
    }

    public MoleculeEntity setMw(Double mw) {
        this.mw = mw;
        return this;
    }

    public Double getMw_monoiso() {
        return mw_monoiso;
    }

    public MoleculeEntity setMw_monoiso(Double mw_monoiso) {
        this.mw_monoiso = mw_monoiso;
        return this;
    }

    public Double getRdb() {
        return rdb;
    }

    public MoleculeEntity setRdb(Double rdb) {
        this.rdb = rdb;
        return this;
    }

    public String getMw_source() {
        return mw_source;
    }

    public MoleculeEntity setMw_source(String mw_source) {
        this.mw_source = mw_source;
        return this;
    }

    public String getValidated_by() {
        return validated_by;
    }

    public MoleculeEntity setValidated_by(String validated_by) {
        this.validated_by = validated_by;
        return this;
    }

    public String getJournal() {
        return journal;
    }

    public MoleculeEntity setJournal(String journal) {
        this.journal = journal;
        return this;
    }

    public String getAuth_of_intr() {
        return auth_of_intr;
    }

    public MoleculeEntity setAuth_of_intr(String auth_of_intr) {
        this.auth_of_intr = auth_of_intr;
        return this;
    }

    public String getJour_cit() {
        return jour_cit;
    }

    public MoleculeEntity setJour_cit(String jour_cit) {
        this.jour_cit = jour_cit;
        return this;
    }

    public String getYear_publ() {
        return year_publ;
    }

    public MoleculeEntity setYear_publ(String year_publ) {
        this.year_publ = year_publ;
        return this;
    }

    public String getDoi_link() {
        return doi_link;
    }

    public MoleculeEntity setDoi_link(String doi_link) {
        this.doi_link = doi_link;
        return this;
    }

    public String getComp_class() {
        return comp_class;
    }

    public MoleculeEntity setComp_class(String comp_class) {
        this.comp_class = comp_class;
        return this;
    }

    public String getCuniq() {
        return cuniq;
    }

    public MoleculeEntity setCuniq(String cuniq) {
        this.cuniq = cuniq;
        return this;
    }

    public String getCalc_perf() {
        return calc_perf;
    }

    public MoleculeEntity setCalc_perf(String calc_perf) {
        this.calc_perf = calc_perf;
        return this;
    }

    public String getOrg_met() {
        return org_met;
    }

    public MoleculeEntity setOrg_met(String org_met) {
        this.org_met = org_met;
        return this;
    }

    public long getMol_chrg() {
        return mol_chrg;
    }

    public MoleculeEntity setMol_chrg(long mol_chrg) {
        this.mol_chrg = mol_chrg;
        return this;
    }

    public String getState_ofmat() {
        return state_ofmat;
    }

    public MoleculeEntity setState_ofmat(String state_ofmat) {
        this.state_ofmat = state_ofmat;
        return this;
    }

    public String getColor_white() {
        return color_white;
    }

    public MoleculeEntity setColor_white(String color_white) {
        this.color_white = color_white;
        return this;
    }

    public String getColor_uv() {
        return color_uv;
    }

    public MoleculeEntity setColor_uv(String color_uv) {
        this.color_uv = color_uv;
        return this;
    }

    public Double getAbsorb_max() {
        return absorb_max;
    }

    public MoleculeEntity setAbsorb_max(Double absorb_max) {
        this.absorb_max = absorb_max;
        return this;
    }

    public String getSolvent_ae() {
        return solvent_ae;
    }

    public MoleculeEntity setSolvent_ae(String solvent_ae) {
        this.solvent_ae = solvent_ae;
        return this;
    }

    public Double getAbsorb() {
        return absorb;
    }

    public MoleculeEntity setAbsorb(Double absorb) {
        this.absorb = absorb;
        return this;
    }

    public Double getConc() {
        return conc;
    }

    public MoleculeEntity setConc(Double conc) {
        this.conc = conc;
        return this;
    }

    public Double getExtinc() {
        return extinc;
    }

    public MoleculeEntity setExtinc(Double extinc) {
        this.extinc = extinc;
        return this;
    }

    public Double getEmis_max() {
        return emis_max;
    }

    public MoleculeEntity setEmis_max(Double emis_max) {
        this.emis_max = emis_max;
        return this;
    }

    public Double getTemp_abs() {
        return temp_abs;
    }

    public MoleculeEntity setTemp_abs(Double temp_abs) {
        this.temp_abs = temp_abs;
        return this;
    }

    public Double getEmis_qy() {
        return emis_qy;
    }

    public MoleculeEntity setEmis_qy(Double emis_qy) {
        this.emis_qy = emis_qy;
        return this;
    }

    public Double getTemp_ems() {
        return temp_ems;
    }

    public MoleculeEntity setTemp_ems(Double temp_ems) {
        this.temp_ems = temp_ems;
        return this;
    }

    public Double getLifetime() {
        return lifetime;
    }

    public MoleculeEntity setLifetime(Double lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public Double getTemp_cv() {
        return temp_cv;
    }

    public MoleculeEntity setTemp_cv(Double temp_cv) {
        this.temp_cv = temp_cv;
        return this;
    }

    public Double getReduc_pot() {
        return reduc_pot;
    }

    public MoleculeEntity setReduc_pot(Double reduc_pot) {
        this.reduc_pot = reduc_pot;
        return this;
    }

    public String getHw_or_pk_rp() {
        return hw_or_pk_rp;
    }

    public MoleculeEntity setHw_or_pk_rp(String hw_or_pk_rp) {
        this.hw_or_pk_rp = hw_or_pk_rp;
        return this;
    }

    public Double getOxid_pot() {
        return oxid_pot;
    }

    public MoleculeEntity setOxid_pot(Double oxid_pot) {
        this.oxid_pot = oxid_pot;
        return this;
    }

    public String getHw_or_pk_op() {
        return hw_or_pk_op;
    }

    public MoleculeEntity setHw_or_pk_op(String hw_or_pk_op) {
        this.hw_or_pk_op = hw_or_pk_op;
        return this;
    }

    public String getSolvent_cv() {
        return solvent_cv;
    }

    public MoleculeEntity setSolvent_cv(String solvent_cv) {
        this.solvent_cv = solvent_cv;
        return this;
    }

    public String getElectrolyte() {
        return electrolyte;
    }

    public MoleculeEntity setElectrolyte(String electrolyte) {
        this.electrolyte = electrolyte;
        return this;
    }

    public String getRef_electrd() {
        return ref_electrd;
    }

    public MoleculeEntity setRef_electrd(String ref_electrd) {
        this.ref_electrd = ref_electrd;
        return this;
    }

    public String getInter_thngs() {
        return inter_thngs;
    }

    public MoleculeEntity setInter_thngs(String inter_thngs) {
        this.inter_thngs = inter_thngs;
        return this;
    }

    public Double getDensity_20() {
        return density_20;
    }

    public MoleculeEntity setDensity_20(Double density_20) {
        this.density_20 = density_20;
        return this;
    }

    public String getDensity_20_source() {
        return density_20_source;
    }

    public MoleculeEntity setDensity_20_source(String density_20_source) {
        this.density_20_source = density_20_source;
        return this;
    }

    public Double getDefault_warn_level() {
        return default_warn_level;
    }

    public MoleculeEntity setDefault_warn_level(Double default_warn_level) {
        this.default_warn_level = default_warn_level;
        return this;
    }

    public Double getN_20() {
        return n_20;
    }

    public MoleculeEntity setN_20(Double n_20) {
        this.n_20 = n_20;
        return this;
    }

    public String getN_20_source() {
        return n_20_source;
    }

    public MoleculeEntity setN_20_source(String n_20_source) {
        this.n_20_source = n_20_source;
        return this;
    }

    public Double getMp_low() {
        return mp_low;
    }

    public MoleculeEntity setMp_low(Double mp_low) {
        this.mp_low = mp_low;
        return this;
    }

    public Double getMp_high() {
        return mp_high;
    }

    public MoleculeEntity setMp_high(Double mp_high) {
        this.mp_high = mp_high;
        return this;
    }

    public String getMp_source() {
        return mp_source;
    }

    public MoleculeEntity setMp_source(String mp_source) {
        this.mp_source = mp_source;
        return this;
    }

    public Double getBp_low() {
        return bp_low;
    }

    public MoleculeEntity setBp_low(Double bp_low) {
        this.bp_low = bp_low;
        return this;
    }

    public Double getBp_high() {
        return bp_high;
    }

    public MoleculeEntity setBp_high(Double bp_high) {
        this.bp_high = bp_high;
        return this;
    }

    public Double getBp_press() {
        return bp_press;
    }

    public MoleculeEntity setBp_press(Double bp_press) {
        this.bp_press = bp_press;
        return this;
    }

    public String getPress_unit() {
        return press_unit;
    }

    public MoleculeEntity setPress_unit(String press_unit) {
        this.press_unit = press_unit;
        return this;
    }

    public String getBp_source() {
        return bp_source;
    }

    public MoleculeEntity setBp_source(String bp_source) {
        this.bp_source = bp_source;
        return this;
    }

    public String getSafety_r() {
        return safety_r;
    }

    public MoleculeEntity setSafety_r(String safety_r) {
        this.safety_r = safety_r;
        return this;
    }

    public String getSafety_h() {
        return safety_h;
    }

    public MoleculeEntity setSafety_h(String safety_h) {
        this.safety_h = safety_h;
        return this;
    }

    public String getSafety_s() {
        return safety_s;
    }

    public MoleculeEntity setSafety_s(String safety_s) {
        this.safety_s = safety_s;
        return this;
    }

    public String getSafety_p() {
        return safety_p;
    }

    public MoleculeEntity setSafety_p(String safety_p) {
        this.safety_p = safety_p;
        return this;
    }

    public String getSafety_text() {
        return safety_text;
    }

    public MoleculeEntity setSafety_text(String safety_text) {
        this.safety_text = safety_text;
        return this;
    }

    public String getSafety_sym() {
        return safety_sym;
    }

    public MoleculeEntity setSafety_sym(String safety_sym) {
        this.safety_sym = safety_sym;
        return this;
    }

    public String getSafety_sym_ghs() {
        return safety_sym_ghs;
    }

    public MoleculeEntity setSafety_sym_ghs(String safety_sym_ghs) {
        this.safety_sym_ghs = safety_sym_ghs;
        return this;
    }

    public String getSafety_source() {
        return safety_source;
    }

    public MoleculeEntity setSafety_source(String safety_source) {
        this.safety_source = safety_source;
        return this;
    }

    public String getComment_mol() {
        return comment_mol;
    }

    public MoleculeEntity setComment_mol(String comment_mol) {
        this.comment_mol = comment_mol;
        return this;
    }
}
