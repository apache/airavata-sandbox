package com.smiles.calctopology;

import org.springframework.data.annotation.Id;

import java.util.List;

public class CalcTopologyEntity {
    @Id
    private String id;
    private List<String> symbols;
    private List<String> geometry;
    private double mol_charge;
    private long mol_multiplicity;
    private String name;
    private String comment;
    private List<Long> mass_numbers;
    private List<Double> masses;
    private List<Double> atomic_number;
    private List<String> atomic_labels;

    public CalcTopologyEntity(List<String> symbols, List<String> geometry, double mol_charge, long mol_multiplicity, String name, String comment, List<Long> mass_numbers, List<Double> masses, List<Double> atomic_number, List<String> atomic_labels) {
        this.symbols = symbols;
        this.geometry = geometry;
        this.mol_charge = mol_charge;
        this.mol_multiplicity = mol_multiplicity;
        this.name = name;
        this.comment = comment;
        this.mass_numbers = mass_numbers;
        this.masses = masses;
        this.atomic_number = atomic_number;
        this.atomic_labels = atomic_labels;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public CalcTopologyEntity setSymbols(List<String> symbols) {
        this.symbols = symbols;
        return this;
    }

    public List<String> getGeometry() {
        return geometry;
    }

    public CalcTopologyEntity setGeometry(List<String> geometry) {
        this.geometry = geometry;
        return this;
    }

    public double getMol_charge() {
        return mol_charge;
    }

    public CalcTopologyEntity setMol_charge(double mol_charge) {
        this.mol_charge = mol_charge;
        return this;
    }

    public long getMol_multiplicity() {
        return mol_multiplicity;
    }

    public CalcTopologyEntity setMol_multiplicity(long mol_multiplicity) {
        this.mol_multiplicity = mol_multiplicity;
        return this;
    }

    public String getName() {
        return name;
    }

    public CalcTopologyEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public CalcTopologyEntity setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public List<Long> getMass_numbers() {
        return mass_numbers;
    }

    public CalcTopologyEntity setMass_numbers(List<Long> mass_numbers) {
        this.mass_numbers = mass_numbers;
        return this;
    }

    public List<Double> getMasses() {
        return masses;
    }

    public CalcTopologyEntity setMasses(List<Double> masses) {
        this.masses = masses;
        return this;
    }

    public List<Double> getAtomic_number() {
        return atomic_number;
    }

    public CalcTopologyEntity setAtomic_number(List<Double> atomic_number) {
        this.atomic_number = atomic_number;
        return this;
    }

    public List<String> getAtomic_labels() {
        return atomic_labels;
    }

    public CalcTopologyEntity setAtomic_labels(List<String> atomic_labels) {
        this.atomic_labels = atomic_labels;
        return this;
    }
}
