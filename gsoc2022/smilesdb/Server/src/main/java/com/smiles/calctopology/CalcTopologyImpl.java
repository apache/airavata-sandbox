package com.smiles.calctopology;

import com.smiles.CalcTopology;
import com.smiles.CalcTopologyRequest;
import com.smiles.CalcTopologyServiceGrpc;
import com.smiles.SpringContext;
import io.grpc.stub.StreamObserver;

import javax.swing.*;

public class CalcTopologyImpl extends CalcTopologyServiceGrpc.CalcTopologyServiceImplBase {

    CalcTopologyRepo repo = SpringContext.getBean((CalcTopologyRepo.class));
    @Override
    public void getCalcTopology(CalcTopologyRequest request, StreamObserver<CalcTopology> responseObserver) {
//        super.getCalcTopology(request, responseObserver);
        System.out.println(request.getAllFields());

        CalcTopologyEntity calcTopology = repo.findBySymbols(request.getcalcTopologyQuery());
        System.out.println(calcTopology.toString());

        CalcTopology reply = CalcTopology.newBuilder()
                .setSymbols(calcTopology.getSymbols())
                .setGeometry(calcTopology.getGeometry())
                .setMolCharge(calcTopology.getMol_charge())
                .setMolMultiplicity(calcTopology.getMol_multiplicity())
                .setName(calcTopology.getName())
                .setComment(calcTopology.getComment())
                .setMassNumbers(calcTopology.getMass_numbers())
                .setMasses(calcTopology.getMasses())
                .setAtomicNumber(calcTopology.getAtomic_number())
                .setAtomLabels(calcTopology.getAtomic_labels()).build();
    }

    @Override
    public void createCalcTopology(CalcTopologyRequest request, StreamObserver<CalcTopology> responseObserver) {
//        super.createCalcTopology(request, responseObserver);
        CalcTopology calcTopology = request.getCalcTopology();
        System.out.println(request.getAllFields());

    }

    @Override
    public void updateCalcTopology(CalcTopologyRequest request, StreamObserver<CalcTopology> responseObserver) {
        super.updateCalcTopology(request, responseObserver);
    }

    @Override
    public void deleteCalcTopology(CalcTopologyRequest request, StreamObserver<CalcTopology> responseObserver) {
        super.deleteCalcTopology(request, responseObserver);
    }
}
