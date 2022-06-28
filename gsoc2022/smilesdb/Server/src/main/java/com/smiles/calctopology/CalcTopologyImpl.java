package com.smiles.calctopology;

import com.smiles.CalcTopology;
import com.smiles.CalcTopologyRequest;
import com.smiles.CalcTopologyServiceGrpc;
import com.smiles.SpringContext;
import io.grpc.stub.StreamObserver;

import javax.swing.*;
import java.util.ArrayList;

public class CalcTopologyImpl extends CalcTopologyServiceGrpc.CalcTopologyServiceImplBase {

    CalcTopologyRepo repo = SpringContext.getBean((CalcTopologyRepo.class));
    @Override
    public void getCalcTopology(CalcTopologyRequest request, StreamObserver<CalcTopology> responseObserver) {
//        super.getCalcTopology(request, responseObserver);
        System.out.println(request.getAllFields());

        ArrayList<String> input = new ArrayList<String>();
        input.add(request.getCalcTopologyQuery());
        CalcTopologyEntity calcTopology = repo.findBySymbols(input);
        System.out.println(calcTopology.toString());

        CalcTopology reply = CalcTopology.newBuilder()
                .addAllSymbols(calcTopology.getSymbols())
                .addAllGeometry(calcTopology.getGeometry())
                .setMolCharge(calcTopology.getMol_charge())
                .setMolMultiplicity(calcTopology.getMol_multiplicity())
                .setName(calcTopology.getName())
                .setComment(calcTopology.getComment())
                .addAllMassNumbers(calcTopology.getMass_numbers())
                .addAllMasses(calcTopology.getMasses())
                .addAllAtomicNumber(calcTopology.getAtomic_number())
                .addAllAtomLabels(calcTopology.getAtomic_labels()).build();

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
