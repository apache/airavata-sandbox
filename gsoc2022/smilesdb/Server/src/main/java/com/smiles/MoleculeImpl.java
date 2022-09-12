package com.smiles;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import static com.mongodb.client.model.Filters.eq;

public class MoleculeImpl extends MoleculeServiceGrpc.MoleculeServiceImplBase {

    /* >>>>>>>>   Connecting to MongoDB (the BSON document)  >>>>>>>>>>>
     *  MongoDB Hierarchy: Database -> Collection -> Record
     *  Connecting port: localhost:27017 (default port)
     *  Database name: SMILESDataModels
     *  Collection name: InfoDataModel
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    private final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private final MongoDatabase mongoDatabase = mongoClient.getDatabase("smilesDB");
    // private final MongoDatabase mongoDatabase = mongoClient.getDatabase("smilestest1");
    private final MongoCollection<org.bson.Document> mongoCollection = mongoDatabase.getCollection("molecule");
    // private final MongoCollection<org.bson.Document> mongoCollection = mongoDatabase.getCollection("molecule");
    /* <<<<<<<<<<<  End of MongoDB connection parameters  <<<<<<<<<<<<<*/

    /* Writing CRUD operation to retrieve the data from Database
     * Emoji representation: ✅ Built Success, 🔅 On progress/Failed, ❌ Not started.
     * ✅ CREATE/PUT operation.
     * ✅ READ/GET operation, show the data by ID provided.
     * ❌ UPDATE operation.
     * ❌ DELETE operation.
     * ✅ LIST operation, show the available data in collection, iteration.
     * NOTE: Only the LIST method is essential for displaying the data from record.*/


    //    Implementing the CREATE operation method
    //    ✅ Built Success
    @Override
    public void createMolecule(CreateMoleculeRequest request, StreamObserver<CreateMoleculeResponse> responseObserver) {

        System.out.println(" \n\n -----------  INSERT OPERATION ---------- ");
        System.out.println("Received request for indexing a data to MongoDB");
        Molecule molecule = request.getMolecule();

        System.out.println("Running INSERT operation");
        Document document = new Document("mol_id", molecule.getMolId())
                .append("cas_nr", molecule.getCasNr())
                .append("smiles", molecule.getSmiles())
                .append("smiles_stereo", molecule.getSmilesStereo())
                .append("inchi", molecule.getInchi())
                .append("molfile_blob_source", molecule.getMolfileBlobSource())
                .append("emp_formula", molecule.getEmpFormula())
                .append("emp_formula_sort", molecule.getEmpFormulaSort())
                .append("emp_formula_source", molecule.getEmpFormulaSource())
                .append("mw", molecule.getMw())
                .append("mw_monoiso", molecule.getMwMonoiso())
                .append("rdb", molecule.getRdb())
                .append("mw_source", molecule.getMwSource())
                .append("validated_by", molecule.getValidatedBy())
                .append("journal", molecule.getJournal())
                .append("auth_of_intr", molecule.getAuthOfIntr())
                .append("jour_cit", molecule.getJourCit())
                .append("year_publ", molecule.getYearPubl())
                .append("doi_link", molecule.getDoiLink())
                .append("comp_class", molecule.getCompClass())
                .append("cuniq", molecule.getCuniq())
                .append("calc_perf", molecule.getCalcPerf())
                .append("org_met", molecule.getOrgMet())
                .append("mol_chrg", molecule.getMolChrg())
                .append("state_ofmat", molecule.getStateOfmat())
                .append("color_white", molecule.getColorWhite())
                .append("color_uv", molecule.getColorUv())
                .append("absorb_max", molecule.getAbsorbMax())
                .append("solvent_ae", molecule.getSolventAe())
                .append("absorb", molecule.getAbsorb())
                .append("conc", molecule.getConc())
                .append("extinc", molecule.getExtinc())
                .append("emis_max", molecule.getEmisMax())
                .append("temp_abs", molecule.getTempAbs())
                .append("emis_qy", molecule.getEmisQy())
                .append("temp_ems", molecule.getTempEms())
                .append("lifetime", molecule.getLifetime())
                .append("temp_cv", molecule.getTempCv())
                .append("reduc_pot", molecule.getReducPot())
                .append("hw_or_pk_rp", molecule.getHwOrPkRp())
                .append("oxid_pot", molecule.getOxidPot())
                .append("hw_or_pk_op", molecule.getHwOrPkOp())
                .append("solvent_cv", molecule.getSolventCv())
                .append("electrolyte", molecule.getElectrolyte())
                .append("ref_electrd", molecule.getRefElectrd())
                .append("inter_thngs", molecule.getInterThngs())
                .append("density_20", molecule.getDensity20())
                .append("density_20_source", molecule.getDensity20Source())
                .append("default_warn_level", molecule.getDefaultWarnLevel())
                .append("n_20", molecule.getN20())
                .append("n_20_source", molecule.getN20Source())
                .append("mp_low", molecule.getMpLow())
                .append("mp_high", molecule.getMpHigh())
                .append("mp_source", molecule.getMpSource())
                .append("bp_low", molecule.getBpLow())
                .append("bp_high", molecule.getBpHigh())
                .append("bp_press", molecule.getBpPress())
                .append("press_unit", molecule.getPressUnit())
                .append("bp_source", molecule.getBpSource())
                .append("safety_r", molecule.getSafetyR())
                .append("safety_h", molecule.getSafetyH())
                .append("safety_s", molecule.getSafetyS())
                .append("safety_p", molecule.getSafetyP())
                .append("safety_text", molecule.getSafetyText())
                .append("safety_sym", molecule.getSafetySym())
                .append("safety_sym_ghs", molecule.getSafetySymGhs())
                .append("safety_source", molecule.getSafetySource())
                .append("comment_mol", molecule.getCommentMol());

        // Command to insert a document into a database -> collection
        mongoCollection.insertOne(document);
        System.out.println("Inserted an entity to MongoDB record");

        //Get the ID from MongoDB
        String id = document.getObjectId("_id").toString();
        System.out.println("Entity ID:" + id);
        System.out.println("----------------------------------------");

        // Create Mongo Response
        CreateMoleculeResponse moleculeResponse = CreateMoleculeResponse.newBuilder().setMolecule(
                molecule.toBuilder().setMolId(id).build()
        ).build();
        responseObserver.onNext(moleculeResponse);
        responseObserver.onCompleted();
    }

    //    Implementing the READ/GET operation method
    //    ✅ Built Success
    @Override
    public void readMolecule(ReadMoleculeRequest request, StreamObserver<ReadMoleculeResponse> responseObserver) {

//        System.out.println(" \n\n -----------  GET/POST OPERATION ---------- ");
//        System.out.println("Received READ Molecule Request");
//
//        // Get Molecule ID
//        String molId = request.getId();
//
//        Document document = null;
//        try{
//            document = (Document) mongoCollection.find(eq("_id", new ObjectId(molId))).first();
//        }catch (Exception e){
//            responseObserver.onError(Status.NOT_FOUND
//                    .withDescription("Molecule is not found for ID: " + molId)
//                    .augmentDescription(e.getLocalizedMessage())
//                    .asRuntimeException());
//        }
//        System.out.println("Searching for document");
//        if (document == null){
//            System.out.println("Molecule is not found");
//            responseObserver.onError(Status.NOT_FOUND
//                    .withDescription("Molecule is not found for ID: " + molId)
//                    .asRuntimeException());
//        }else {
//            System.out.println("Molecule is found");
//
//            Molecule molecule = documentToMolecule(document);
//            responseObserver.onNext(
//                    ReadMoleculeResponse.newBuilder()
//                            .setMolecule(molecule).build());
//            System.out.println("Sent the Response");
//
//            responseObserver.onCompleted();
//            System.out.println("Server Job Done");
//            System.out.println("----------------------------------------");
//        }

        System.out.println("Received request for Fetching a Blog from MongoDB.");

        Document fetchedDocFromMongo =
                mongoCollection.find(eq("_id", new ObjectId(request.getId()))).first();

        if (fetchedDocFromMongo == null) {
            responseObserver
                    .onError(Status.NOT_FOUND.withDescription("No blog exists with this Id.").asRuntimeException());
        } else {
            System.out.println("Found the Data");
            ReadMoleculeResponse fetchedBlogResponse = ReadMoleculeResponse.newBuilder()
                    .setMolecule(Molecule.newBuilder()
                            .setMolId(String.valueOf(fetchedDocFromMongo.getObjectId("_id")))
                            .setCasNr(fetchedDocFromMongo.getString("cas_nr"))
                            .setSmiles(fetchedDocFromMongo.getString("smiles"))
                            .setSmilesStereo(fetchedDocFromMongo.getString("smiles_stereo"))
                            .setInchi(fetchedDocFromMongo.getString("inchi"))
                            .setMolfileBlobSource(fetchedDocFromMongo.getString("molfile_blob_source"))
                            .setEmpFormula(fetchedDocFromMongo.getString("emp_formula"))
                            .setEmpFormulaSort(fetchedDocFromMongo.getString("emp_formula_sort"))
                            .setEmpFormulaSource(fetchedDocFromMongo.getString("emp_formula_source"))
                            .setMw(fetchedDocFromMongo.getDouble("mw") == null ? -99 : fetchedDocFromMongo.getDouble("mw"))
                            .setMwMonoiso(fetchedDocFromMongo.getDouble("mw_monoiso") == null ? -99 : fetchedDocFromMongo.getDouble("mw_monoiso"))
                            .setRdb(fetchedDocFromMongo.getDouble("rdb") == null ? -99 : fetchedDocFromMongo.getDouble("rdb"))
                            .setRdb(fetchedDocFromMongo.getDouble("rdb") == null ? -99 : fetchedDocFromMongo.getDouble("rdb"))
                            .setMwSource(fetchedDocFromMongo.getString("mw_source"))
                            .setValidatedBy(fetchedDocFromMongo.getString("validated_by"))
                            .setJournal(fetchedDocFromMongo.getString("journal"))
                            .setAuthOfIntr(fetchedDocFromMongo.getString("auth_of_intr"))
                            .setJourCit(fetchedDocFromMongo.getString("jour_cit"))
                            .setYearPubl(fetchedDocFromMongo.getString("year_publ"))
                            .setDoiLink(fetchedDocFromMongo.getString("doi_link"))
                            .setCompClass(fetchedDocFromMongo.getString("comp_class"))
                            .setCuniq(fetchedDocFromMongo.getString("cuniq"))
                            .setCalcPerf(fetchedDocFromMongo.getString("calc_perf"))
                            .setOrgMet(fetchedDocFromMongo.getString("org_met"))
                            .setMolChrg(fetchedDocFromMongo.getLong("mol_chrg") == null ? -99 : fetchedDocFromMongo.getLong("mol_chrg"))
                            .setStateOfmat(fetchedDocFromMongo.getString("state_ofmat"))
                            .setColorWhite(fetchedDocFromMongo.getString("color_white"))
                            .setColorUv(fetchedDocFromMongo.getString("color_uv"))
                            .setAbsorbMax(fetchedDocFromMongo.getDouble("absorb_max") == null ? -99 : fetchedDocFromMongo.getDouble("absorb_max"))
                            .setSolventAe(fetchedDocFromMongo.getString("solvent_ae"))
                            .setAbsorb(fetchedDocFromMongo.getDouble("absorb") == null ? -99 : fetchedDocFromMongo.getDouble("absorb"))
                            .setConc(fetchedDocFromMongo.getDouble("conc") == null ? -99 : fetchedDocFromMongo.getDouble("conc"))
                            .setExtinc(fetchedDocFromMongo.getDouble("extinc") == null ? -99 : fetchedDocFromMongo.getDouble("extinc"))
                            .setEmisMax(fetchedDocFromMongo.getDouble("emis_max") == null ? -99 : fetchedDocFromMongo.getDouble("emis_max"))
                            .setTempAbs(fetchedDocFromMongo.getDouble("temp_abs") == null ? -99 : fetchedDocFromMongo.getDouble("temp_abs"))
                            .setEmisQy(fetchedDocFromMongo.getDouble("emis_qy") == null ? -99 : fetchedDocFromMongo.getDouble("emis_qy"))
                            .setTempEms(fetchedDocFromMongo.getDouble("temp_ems") == null ? -99 : fetchedDocFromMongo.getDouble("temp_ems"))
                            .setLifetime(fetchedDocFromMongo.getDouble("lifetime") == null ? -99 : fetchedDocFromMongo.getDouble("lifetime"))
                            .setTempCv(fetchedDocFromMongo.getDouble("temp_cv") == null ? -99 : fetchedDocFromMongo.getDouble("temp_cv"))
                            .setReducPot(fetchedDocFromMongo.getDouble("reduc_pot") == null ? -99 : fetchedDocFromMongo.getDouble("reduc_pot"))
                            .setHwOrPkRp(fetchedDocFromMongo.getString("hw_or_pk_rp"))
                            .setOxidPot(fetchedDocFromMongo.getDouble("oxid_pot") == null ? -99 : fetchedDocFromMongo.getDouble("oxid_pot"))
                            .setHwOrPkOp(fetchedDocFromMongo.getString("hw_or_pk_op"))
                            .setSolventCv(fetchedDocFromMongo.getString("solvent_cv"))
                            .setElectrolyte(fetchedDocFromMongo.getString("electrolyte"))
                            .setRefElectrd(fetchedDocFromMongo.getString("ref_electrd"))
                            .setInterThngs(fetchedDocFromMongo.getString("inter_thngs"))
                            .setDensity20(fetchedDocFromMongo.getDouble("density_20") == null ? -99 : fetchedDocFromMongo.getDouble("density_20"))
                            .setDensity20Source(fetchedDocFromMongo.getString("density_20_source"))
                            .setDefaultWarnLevel(fetchedDocFromMongo.getDouble("default_warn_level") == null ? -99 : fetchedDocFromMongo.getDouble("default_warn_level"))
                            .setN20(fetchedDocFromMongo.getDouble("n_20") == null ? -99 : fetchedDocFromMongo.getDouble("n_20"))
                            .setN20Source(fetchedDocFromMongo.getString("n_20_source"))
                            .setMpLow(fetchedDocFromMongo.getDouble("mp_low") == null ? -99 : fetchedDocFromMongo.getDouble("mp_low"))
                            .setMpHigh(fetchedDocFromMongo.getDouble("mp_high") == null ? -99 : fetchedDocFromMongo.getDouble("mp_high"))
                            .setMpSource(fetchedDocFromMongo.getString("mp_source"))
                            .setBpLow(fetchedDocFromMongo.getDouble("bp_low") == null ? -99 : fetchedDocFromMongo.getDouble("bp_low"))
                            .setBpHigh(fetchedDocFromMongo.getDouble("bp_high") == null ? -99 : fetchedDocFromMongo.getDouble("bp_high"))
                            .setBpPress(fetchedDocFromMongo.getDouble("bp_press") == null ? -99 : fetchedDocFromMongo.getDouble("bp_press"))
                            .setPressUnit(fetchedDocFromMongo.getString("press_unit"))
                            .setBpSource(fetchedDocFromMongo.getString("bp_source"))
                            .setSafetyR(fetchedDocFromMongo.getString("safety_r"))
                            .setSafetyH(fetchedDocFromMongo.getString("safety_h"))
                            .setSafetyS(fetchedDocFromMongo.getString("safety_s"))
                            .setSafetyP(fetchedDocFromMongo.getString("safety_p"))
                            .setSafetyText(fetchedDocFromMongo.getString("safety_text"))
                            .setSafetySym(fetchedDocFromMongo.getString("safety_sym"))
                            .setSafetySymGhs(fetchedDocFromMongo.getString("safety_sym_ghs"))
                            .setSafetySource(fetchedDocFromMongo.getString("safety_source"))
                            .setCommentMol(fetchedDocFromMongo.getString("comment_mol"))
                            .build())
                    .build();
            responseObserver.onNext(fetchedBlogResponse);
        }
        responseObserver.onCompleted();
    }

    private Molecule documentToMolecule(Document document) {
        return Molecule.newBuilder()
                .setMolId(String.valueOf(document.getObjectId("_id")))
                .setCasNr(document.getString("cas_nr"))
                .setSmiles(document.getString("smiles"))
                .setSmilesStereo(document.getString("smiles_stereo"))
                .setInchi(document.getString("inchi"))
                .setMolfileBlobSource(document.getString("molfile_blob_source"))
                .setEmpFormula(document.getString("emp_formula"))
                .setEmpFormulaSort(document.getString("emp_formula_sort"))
                .setEmpFormulaSource(document.getString("emp_formula_source"))
                .setMw(document.getDouble("mw"))
                .setMwMonoiso(document.getDouble("mw_monoiso"))
                .setRdb(document.getDouble("rdb"))
                .setMwSource(document.getString("mw_source"))
                .setValidatedBy(document.getString("validated_by"))
                .setJournal(document.getString("journal"))
                .setAuthOfIntr(document.getString("auth_of_intr"))
                .setJourCit(document.getString("jour_cit"))
                .setYearPubl(document.getString("year_publ"))
                .setDoiLink(document.getString("doi_link"))
                .setCompClass(document.getString("comp_class"))
                .setCuniq(document.getString("cuniq"))
                .setCalcPerf(document.getString("calc_perf"))
                .setOrgMet(document.getString("org_met"))
//                .setMolChrg(Long.parseLong(document.getString("mol_chrg").equals("")?"9999999":document.getString("mol_chrg")))
                .setStateOfmat(document.getString("state_ofmat"))
                .setColorWhite(document.getString("color_white"))
                .setColorUv(document.getString("color_uv"))
                .setAbsorbMax(document.getDouble("absorb_max"))
                .setSolventAe(document.getString("solvent_ae"))
                .setAbsorb(document.getDouble("absorb"))
                .setConc(document.getDouble("conc"))
                .setExtinc(document.getDouble("extinc"))
                .setEmisMax(document.getDouble("emis_max"))
                .setTempAbs(document.getDouble("temp_abs"))
                .setEmisQy(document.getDouble("emis_qy"))
                .setTempEms(document.getDouble("temp_ems"))
                .setLifetime(document.getDouble("lifetime"))
                .setTempCv(document.getDouble("temp_cv"))
                .setReducPot(document.getDouble("reduc_pot"))
                .setHwOrPkRp(document.getString("hw_or_pk_rp"))
                .setOxidPot(document.getDouble("oxid_pot"))
                .setHwOrPkOp(document.getString("hw_or_pk_op"))
                .setSolventCv(document.getString("solvent_cv"))
                .setElectrolyte(document.getString("electrolyte"))
                .setRefElectrd(document.getString("ref_electrd"))
                .setInterThngs(document.getString("inter_thngs"))
                .setDensity20(document.getDouble("density_20"))
                .setDensity20Source(document.getString("density_20_source"))
                .setDefaultWarnLevel(document.getDouble("default_warn_level"))
                .setN20(document.getDouble("n_20"))
                .setN20Source(document.getString("n_20_source"))
                .setMpLow(document.getDouble("mp_low"))
                .setMpHigh(document.getDouble("mp_high"))
                .setMpSource(document.getString("mp_source"))
                .setBpLow(document.getDouble("bp_low"))
                .setBpHigh(document.getDouble("bp_high"))
                .setBpPress(document.getDouble("bp_press"))
                .setPressUnit(document.getString("press_unit"))
                .setBpSource(document.getString("bp_source"))
                .setSafetyR(document.getString("safety_r"))
                .setSafetyH(document.getString("safety_h"))
                .setSafetyS(document.getString("safety_s"))
                .setSafetyP(document.getString("safety_p"))
                .setSafetyText(document.getString("safety_text"))
                .setSafetySym(document.getString("safety_sym"))
                .setSafetySymGhs(document.getString("safety_sym_ghs"))
                .setSafetySource(document.getString("safety_source"))
                .setCommentMol(document.getString("comment_mol"))
                .build();
    }

    @Override
    public void updateMolecule(UpdateMoleculeRequest request, StreamObserver<UpdateMoleculeResponse> responseObserver) {
        super.updateMolecule(request, responseObserver);
    }

    @Override
    public void deleteMolecule(DeleteMoleculeRequest request, StreamObserver<DeleteMoleculeResponse> responseObserver) {
        super.deleteMolecule(request, responseObserver);
    }

    @Override
    public void listMolecule(ListMoleculeRequest request, StreamObserver<ListMoleculeResponse> responseObserver) {
        System.out.println(" \n\n -----------  LIST OPERATION ---------- ");
        System.out.println("Received a request to LIST Molecule data.");

//        Searching for the data from mongo collection(iteration:document by document)
        mongoCollection.find().iterator().forEachRemaining(document -> responseObserver.onNext(
                ListMoleculeResponse.newBuilder()
                        .setMolecule(documentToMolecule(document)).build()
        ));
        System.out.println("Successfully retrieved the Info Data from Mongo Collection");
        System.out.println("----------------------------------------");
        responseObserver.onCompleted();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
