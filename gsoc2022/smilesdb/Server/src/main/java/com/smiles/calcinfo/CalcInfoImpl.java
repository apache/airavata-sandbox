package com.smiles.calcinfo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.smiles.*;
import com.smiles.CalcInfoServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.bson.Document;

public class CalcInfoImpl extends CalcInfoServiceGrpc.CalcInfoServiceImplBase {

    CalcInfoRepo repo = SpringContext.getBean(CalcInfoRepo.class);

    @Override
    public void getCalcInfo(CalcInfoRequest request, StreamObserver<CalcInfo> responseObserver) {

        System.out.println(request.getAllFields());
        repo.deleteAll();
        CalcInfoEntity c1 = new CalcInfoEntity(1, 1, 1, 1, 1, 1.0, "TEST1");
        repo.save(c1);
        CalcInfoEntity c2 = new CalcInfoEntity(2, 2, 2, 2, 2, 2.0, "TEST2");
        repo.save(c2);
        // Reply the values

        // String query = request.getCalcInfoQuery()

        //CalcInfoEntity calcInfo = repo.findByNalpha(request.getCalcInfoQuery());
        CalcInfoEntity calcInfo = repo.findBySMILES(request.getCalcInfoQuery());
        System.out.println(calcInfo.toString());

        CalcInfo reply = CalcInfo.newBuilder().setNbeta(calcInfo.getNbeta())
                .setNmo(calcInfo.getNmo())
                .setEnergy(calcInfo.getEnergy())
                .setNalpha(calcInfo.getNalpha())
                .setNatom(calcInfo.getNatom())
                .setSMILES(calcInfo.getSMILES()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void createCalcInfo(com.smiles.CalcInfoRequest request,
                               io.grpc.stub.StreamObserver<com.smiles.CalcInfo> responseObserver) {

        CalcInfo calcInfo = request.getCalcInfo();
        System.out.println(request.getAllFields());
        CalcInfoEntity c1001 = new CalcInfoEntity(calcInfo.getNbasis(),
                calcInfo.getNmo(),
                calcInfo.getNalpha(),
                calcInfo.getNbeta(),
                calcInfo.getNatom(),
                calcInfo.getEnergy(),
                calcInfo.getSMILES());
        repo.save(c1001);
        responseObserver.onNext(calcInfo);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCalcInfo(com.smiles.CalcInfoRequest request,
                               io.grpc.stub.StreamObserver<com.smiles.CalcInfo> responseObserver) {
        CalcInfo calcInfo = request.getCalcInfo();
        System.out.println(request.getAllFields());
        CalcInfoEntity c1001 = (CalcInfoEntity) repo.findBySMILES(calcInfo.getSMILES());
        System.out.println(c1001.getId());
        c1001.setNbasis(calcInfo.getNbasis());
        c1001.setNmo(calcInfo.getNmo());
        c1001.setNalpha(calcInfo.getNalpha());
        c1001.setNbeta(calcInfo.getNbeta());
        c1001.setNatom(calcInfo.getNatom());
        c1001.setEnergy(calcInfo.getEnergy());
        c1001.setSMILES(calcInfo.getSMILES());
        repo.save(c1001);
        responseObserver.onNext(calcInfo);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCalcInfo(com.smiles.CalcInfoRequest request,
                               io.grpc.stub.StreamObserver<com.smiles.CalcInfo> responseObserver) {
        CalcInfo calcInfo = request.getCalcInfo();
        CalcInfoEntity c1001 = (CalcInfoEntity) repo.findBySMILES(calcInfo.getSMILES());
        repo.delete(c1001);
        responseObserver.onNext(calcInfo);
        responseObserver.onCompleted();
    }
}
