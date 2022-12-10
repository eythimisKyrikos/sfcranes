package com.safeline.safelinecranes.models;

import java.util.List;

public class DetailedResultsSynchro {
    private List<Inspection> inspections;
    private List<Result> results;

    public DetailedResultsSynchro() {
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
